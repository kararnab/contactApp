package com.kararnab.contacts.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * A basic class representing an entity that is a row in a one-column database table.
 *
 * @ Entity - You must annotate the class as an entity and supply a table name if not class name.
 * @ PrimaryKey - You must identify the primary key.
 * @ ColumnInfo - You must supply the column name if it is different from the variable name.
 *
 * See the documentation for the full rich set of annotations.
 * https://developer.android.com/topic/libraries/architecture/room.html
 */

@Entity(tableName = "contact_table")
public class Contact {

    @ColumnInfo(name = "name")
    private String mName;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "phone")
    private String mPhone;

    @ColumnInfo(name = "company")
    private String mCompany;

    @ColumnInfo(name = "email")
    private String mEmail;

    @ColumnInfo(name = "notes")
    private String mNotes;

    @ColumnInfo(name = "selected")
    private boolean selected;

    public Contact(@NonNull String mPhone,String mName,String mCompany,String mEmail,String mNotes) {
        this.mPhone = mPhone;
        this.mName = mName;
        this.mCompany = mCompany;
        this.mEmail = mEmail;
        this.mNotes = mNotes;
    }

    @NonNull
    public String getName() {
        return this.mName;
    }

    @NonNull
    public String getPhone() {
        return mPhone;
    }

    public String getCompany() {
        return this.mCompany;
    }

    public String getEmail() {
        return this.mEmail;
    }

    public String getNotes() {
        return this.mNotes;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean isSelected) {
        this.selected = isSelected;
    }
}
