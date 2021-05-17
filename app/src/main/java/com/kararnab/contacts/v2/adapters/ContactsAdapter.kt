package com.kararnab.contacts.v2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kararnab.contacts.databinding.ContactItemBinding
import com.kararnab.contacts.v2.models.Contacts
import com.kararnab.contacts.v2.util.ContactsDiffUtil
import com.kararnab.contacts.v2.models.Result

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {

    private var contacts = emptyList<Result>()

    class MyViewHolder(private val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result){
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

    fun setData(newData: Contacts){
        val recipesDiffUtil =
            ContactsDiffUtil(contacts, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        contacts = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}