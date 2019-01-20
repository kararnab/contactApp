package com.kararnab.contacts;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.kararnab.contacts.room.Contact;
import com.kararnab.contacts.room.ContactViewModel;

public class AddEditContact extends AppCompatActivity {

    EditText edit_name,edit_company,edit_phone,edit_email,edit_notes;
    boolean isEditMode;
    ContactViewModel mContactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        isEditMode = getIntent().getBooleanExtra("editContact",false);
        if(isEditMode){
            getSupportActionBar().setTitle(getString(R.string.edit_contact));
            String phoneNo = getIntent().getStringExtra("phoneNo");
            String name = getIntent().getStringExtra("name");
            String company = getIntent().getStringExtra("company");
            String email = getIntent().getStringExtra("email");
            String notes = getIntent().getStringExtra("notes");
            populateFields(phoneNo,name,company,email,notes);
        }else{
            getSupportActionBar().setTitle(getString(R.string.add_contact));
        }
    }

    void populateFields(String phoneNo,String name,String company,String email,String notes){
        edit_name.setText(name);
        edit_company.setText(company);
        edit_phone.setText(phoneNo);
        edit_email.setText(email);
        edit_notes.setText(notes);
        edit_name.setSelection(name.length());
    }

    void initViews(){
        edit_name = findViewById(R.id.edit_name);
        edit_company = findViewById(R.id.edit_company);
        edit_phone = findViewById(R.id.edit_phone);
        edit_email = findViewById(R.id.edit_email);
        edit_notes = findViewById(R.id.edit_notes);
        mContactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
    }

    void saveContact(Contact contact){
        //todo save to db as new if contact id doesn't exist else edit and save
        mContactViewModel.insert(contact);
        finish();
    }

    public void saveContact(View view){
        Contact contact = new Contact(edit_phone.getText().toString(),edit_name.getText().toString(),edit_company.getText().toString(),edit_email.getText().toString(),edit_notes.getText().toString());
        saveContact(contact);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }

}
