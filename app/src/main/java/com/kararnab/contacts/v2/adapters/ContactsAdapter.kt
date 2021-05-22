package com.kararnab.contacts.v2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kararnab.contacts.databinding.ContactItemBinding
import com.kararnab.contacts.v2.data.database.Contact
import com.kararnab.contacts.v2.util.ContactsDiffUtil

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {

    private var contacts = emptyList<Contact>()

    class MyViewHolder(private val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Contact){
            binding.contact = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ContactItemBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentContact = contacts[position]
        holder.bind(currentContact)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    fun setData(newData: List<Contact>){
        val recipesDiffUtil =
            ContactsDiffUtil(contacts, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        contacts = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}