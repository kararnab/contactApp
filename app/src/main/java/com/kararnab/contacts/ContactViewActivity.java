package com.kararnab.contacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kararnab.contacts.callbacks.DebouncedOnClickListener;
import com.kararnab.contacts.callbacks.PermissionsCallback;
import com.kararnab.contacts.room.Contact;

public class ContactViewActivity extends AppCompatActivity {

    TextView mEmailId, mPhoneNumber, tvCompany;
    CollapsingToolbarLayout mCollapsingToolbar;
    AppBarLayout mAppBar;
    View mFABEdit,mActionCall,mActionEmail;
    Menu mCollapsedMenu;
    private static final int REQUEST_CALL = 0;
    private boolean appBarExpanded = true;

    String id, phoneNo, name, company, email, notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        id = getIntent().getStringExtra("id");
        phoneNo = getIntent().getStringExtra("phoneNo");
        name = getIntent().getStringExtra("name");
        company = getIntent().getStringExtra("company");
        email = getIntent().getStringExtra("email");
        notes = getIntent().getStringExtra("notes");

        populateFields(phoneNo,name,company,email,notes);

        mFABEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editContact(id, phoneNo,name,company,email,notes);
            }
        });

        mActionCall.setOnClickListener(new DebouncedOnClickListener(500) {
            @Override
            public void onDebouncedClick(View v) {
                checkCallPermission(new PermissionsCallback() {
                    @Override
                    public void onGranted() {
                        callPhoneNumber(phoneNo,true);
                    }
                    @Override
                    public void onRejected() {
                        requestCallPermission();
                    }
                });
            }
        });

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener(){

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) > 200) {
                    appBarExpanded = false;
                    invalidateOptionsMenu();
                } else {
                    appBarExpanded = true;
                    invalidateOptionsMenu();
                }
            }
        });
    }

    void initViews(){
        mAppBar = findViewById(R.id.appbar);
        mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mEmailId = findViewById(R.id.emailId);
        mFABEdit = findViewById(R.id.fabEdit);
        mActionCall = findViewById(R.id.action_call);
        mActionEmail = findViewById(R.id.action_mail);
        tvCompany = findViewById(R.id.tvCompany);
    }

    void populateFields(String phoneNo,String name,String company,String email,String notes){
        mCollapsingToolbar.setTitle(name);
        mPhoneNumber.setText(formattedPhoneNo(phoneNo));
        mEmailId.setText(email);
        tvCompany.setText(company);
    }

    void editContact(String id, String phoneNo,String name,String company,String email,String notes){
        Contact contact = new Contact(id, phoneNo,name,company,email,notes);
        MainActivity.navigateToAddContact(ContactViewActivity.this,contact);
    }

    String formattedPhoneNo(String phoneNo){
        return phoneNo.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
    }

    void callPhoneNumber(String phoneNo,boolean isAdminPriviledge){
        if(isAdminPriviledge){
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+phoneNo));
            startActivity(intent); //TODO: Android M Runtime Permissions
        }else{
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+phoneNo));
            startActivity(intent);
        }
    }

    void checkCallPermission(PermissionsCallback callback){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.
            callback.onRejected();
        } else {
            // Permission is already available, now you can call.
            callback.onGranted();
        }
    }

    private void requestCallPermission() {
        // https://github.com/tbruyelle/RxPermissions, we can simplify it later using rxPermissions
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {
            // Provide an additional rationale to the user if the permission was not granted,
            // user has previously denied the permission.
            Toast.makeText(this,R.string.manual_call_perm_request,Toast.LENGTH_LONG).show();
        } else {
            // Call permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mCollapsedMenu != null && !appBarExpanded) {
            //collapsed
            mCollapsedMenu.add(Menu.NONE,1,Menu.NONE,"Edit")
                    .setIcon(R.drawable.ic_edit_24dp)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return super.onPrepareOptionsMenu(mCollapsedMenu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empty, menu);
        mCollapsedMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==1){
            editContact(id,phoneNo,name,company,email,notes);
            return true;
        }else if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
