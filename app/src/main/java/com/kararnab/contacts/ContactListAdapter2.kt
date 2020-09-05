package com.kararnab.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.annotation.Nullable
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.kararnab.contacts.room.Contact

//Usage: ContactListAdapter2({ selectedUser: User -> onListItemClicked(selectedUser) })
class ContactListAdapter2(private val clickListener: (Contact) -> Unit): Adapter<MyViewMolder>() {

    private var userList: MutableList<Contact> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewMolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItem = inflater.inflate(R.layout.contact_item, parent, false)
        return MyViewMolder(listItem)
    }

    override fun onBindViewHolder(holder: MyViewMolder, position: Int) {
        holder.bind(userList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setContacts(newUserList: List<Contact>) {
        val diffCallback = MyDiffCallback(userList, newUserList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        userList.clear()
        userList.addAll(newUserList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getContact(position: Int): Contact {
        return userList[position]
    }

}

class MyViewMolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
    private var contactName: TextView? = null
    private var contactInitials: TextView? = null
    private var contactCircle: CardView? = null
    private var selectSwitch: ViewSwitcher? = null


    init {
        contactName = itemView.findViewById(R.id.contactName)
        contactInitials = itemView.findViewById(R.id.contactInitials)
        contactCircle = itemView.findViewById(R.id.contactCircle)
        selectSwitch = itemView.findViewById(R.id.selectSwitch)
    }

    fun bind(contact: Contact, clickListener: (Contact) -> Unit) {
        contactName?.text = contact.name
        contactInitials?.text = if (contact.name.length > 0) contact.name[0].toString() + "" else ""
        contactCircle?.setCardBackgroundColor(UiUtils.materialColor(contact.name.length))
        selectSwitch?.displayedChild = if (contact.isSelected) 1 else 0
        itemView.setOnClickListener {  clickListener(contact) }
    }
}

class MyDiffCallback(private val oldList: List<Contact>, private val newList: List<Contact>) : DiffUtil.Callback() {

    /**
     * It returns the size of the old list.
     */
    override fun getOldListSize(): Int = oldList.size

    /**
     * Returns the size of the new list
     */
    override fun getNewListSize(): Int = newList.size

    /**
     * Called by the DiffUtil to decide whether two object represent the same Item in the old and new list.
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    /**
     * Checks whether two items have the same data.You can change its behavior depending on your UI.
     * This method is called by DiffUtil only if areItemsTheSame returns true. In our example,
     * we are comparing the name and value for the specific item.
     */
    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        val (_, name, email) = oldList[oldPosition]
        val (_, name1, email1) = newList[newPosition]

        return name == name1 && email == email1
    }

    /**
     *  If areItemTheSame return true and areContentsTheSame returns false DiffUtil calls this method to get a payload about the change.
     */
    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldPosition, newPosition)
    }
}