package com.kararnab.contacts;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.kararnab.contacts.v2.data.database.Contact;

import java.util.Collections;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {

    public interface ContactListener{
            void onItemClicked(Contact contact);
        boolean onItemLongClicked(int position,View view);
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView contactName,contactInitials;
        private final CardView contactCircle;
        private final ViewSwitcher selectSwitch;

        private ContactViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactInitials = itemView.findViewById(R.id.contactInitials);
            contactCircle = itemView.findViewById(R.id.contactCircle);
            selectSwitch = itemView.findViewById(R.id.selectSwitch);
        }

        void bind(Contact contact, final ContactListener listener){
            contactName.setText(contact.getName());
            contactInitials.setText(contact.getName().length()>0? contact.getName().charAt(0)+"":"");
            contactCircle.setCardBackgroundColor(UiUtils.materialColor(contact.getName().length()));
            selectSwitch.setDisplayedChild(contact.isSelected()?1:0);
            itemView.setOnClickListener(v -> listener.onItemClicked(contact));
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return listener.onItemLongClicked(getAdapterPosition(),v);
                }
            });
        }
    }

    private List<Contact> mContacts = Collections.emptyList(); // Cached copy of words
    private final ContactListener listener;

    public ContactListAdapter(ContactListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = mContacts.get(position);
        holder.bind(contact,listener);
    }

    public void setContacts(List<Contact> contacts) {
        mContacts = contacts;
        notifyDataSetChanged();
    }

    public Contact getContact(int position){
        return mContacts.get(position);
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }
}