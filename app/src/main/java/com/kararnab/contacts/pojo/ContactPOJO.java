package com.kararnab.contacts.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * As of release 1, replaced by {@link com.kararnab.contacts.room.Contact}
 * @deprecated
 */
@Deprecated
public class ContactPOJO implements Parcelable {
    private long contactId;
    private String name;
    private String phoneNumber;
    private String company;
    private String email;
    private String notes;
    private boolean isSelected;
    public ContactPOJO(long contactId, String username, String phoneNumber, String company, String email, String notes) {
        this.contactId = contactId;
        this.name = username;
        this.phoneNumber = phoneNumber;
        this.company = company;
        this.email = email;
        this.notes = notes;
    }

    protected ContactPOJO(Parcel in) {
        contactId = in.readLong();
        name = in.readString();
        phoneNumber = in.readString();
        company = in.readString();
        email = in.readString();
        notes = in.readString();
    }

    public static final Creator<ContactPOJO> CREATOR = new Creator<ContactPOJO>() {
        @Override
        public ContactPOJO createFromParcel(Parcel in) {
            return new ContactPOJO(in);
        }

        @Override
        public ContactPOJO[] newArray(int size) {
            return new ContactPOJO[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getContactId() {
        return contactId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(contactId);
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeString(company);
        dest.writeString(email);
        dest.writeString(notes);
    }
}
