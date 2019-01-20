package com.kararnab.contacts;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import com.kararnab.contacts.callbacks.DebouncedOnClickListener;
import com.kararnab.contacts.room.Contact;
import com.kararnab.contacts.room.ContactViewModel;
import com.kararnab.contacts.widgets.EmptyRecyclerView;

import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton mAddContactFab;
    SwipeRefreshLayout mSwipeRefresh;
    EmptyRecyclerView rvContacts;
    View mAppBar;
    DrawerLayout mDrawerLayout;
    ContactListAdapter mAdapter;
    ContactViewModel mWordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
        //getSupportActionBar().setHomeButtonEnabled(true);

        initViews();
        initListeners();

        mAdapter = new ContactListAdapter(this, new ContactListAdapter.ContactListener() {
            @Override
            public void onItemClicked(int position,View view) {
                navigateToViewContact(mAdapter.getContact(position));
                //navigateToAddContact(mAdapter.getContact(position));
            }

            @Override
            public boolean onItemLongClicked(int position,View view) {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                mAdapter.getContact(position).setSelected(true);
                mAdapter.notifyItemChanged(position);
                return true;
            }
        });
        rvContacts.setAdapter(mAdapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

        // Get a new or existing ViewModel from the ViewModelProvider.
        mWordViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mWordViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(@Nullable final List<Contact> contacts) {
                // Update the cached copy of the contacts in the adapter.
                mAdapter.setContacts(contacts);
            }
        });
    }

    void initViews(){
        mAppBar = findViewById(R.id.appbar);
        mAddContactFab = findViewById(R.id.fab);
        rvContacts = findViewById(R.id.rvContacts);
        rvContacts.setEmptyView(findViewById(R.id.emptyContacts));
        mSwipeRefresh = findViewById(R.id.swipeRefreshContainer);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        // Configure the refreshing colors
        UiUtils.setSwipeRefreshLayoutLoaderColors(mSwipeRefresh);

    }

    void initListeners(){
        mAddContactFab.setOnClickListener(new DebouncedOnClickListener(500) {
            @Override
            public void onDebouncedClick(View view) {

                /*checkCallPermission(new PermissionsCallback() {
                    @Override
                    public void onGranted() {
                        callPhoneNumber("9876543210",true);
                    }
                    @Override
                    public void onRejected() {
                        requestCallPermission();
                    }
                });*/
                Timber.tag("Add ContactPOJO").e("Manufactured by: %s\n User: %s",Build.MANUFACTURER,Build.DEVICE);
                Snackbar.make(view, R.string.work_in_progress, Snackbar.LENGTH_LONG)
                        .setAction(R.string.action, null).show();

                navigateToAddContact(MainActivity.this,null);
            }
        });
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //todo: Add the contact sync logic here
                //initDummyData(mAdapter);
                mSwipeRefresh.setRefreshing(false);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rvContacts.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    mAppBar.setSelected(rvContacts.canScrollVertically(-1));
                }
            });
        }else {
            rvContacts.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    // int scrollY = rvContacts.getScrollY(); // For ScrollView
                    // int scrollX = rvContacts.getScrollX(); // For HorizontalScrollView
                    mAppBar.setSelected(rvContacts.canScrollVertically(-1));
                }
            });
        }
    }

    static void navigateToAddContact(Context context, Contact contact){
        Intent intent = new Intent(context, AddEditContact.class);
        intent.putExtra("editContact", contact!=null);
        if(contact!=null){
            putContactInIntent(intent,contact);
        }
        context.startActivity(intent);

    }
    void navigateToViewContact(Contact contact){
        Intent intent = new Intent(this,ContactViewActivity.class);
        putContactInIntent(intent,contact);
        startActivity(intent);
    }

    static void putContactInIntent(Intent intent,Contact contact){
        intent.putExtra("phoneNo",contact.getPhone());
        intent.putExtra("name",contact.getName());
        intent.putExtra("company",contact.getCompany());
        intent.putExtra("email",contact.getEmail());
        intent.putExtra("notes",contact.getNotes());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            finish();
        }

    }
}
