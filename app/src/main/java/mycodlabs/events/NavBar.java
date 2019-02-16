package mycodlabs.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import java.util.HashMap;

import mycodlabs.instapp.PublishActivity;

public class NavBar extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,ListView.OnItemClickListener ,View.OnClickListener {


    private static final String TAG_EVENT_ID = "event_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_TIME = "time";
    private static final String TAG_VENUE = "venue";
    private static final String TAG_DETAILS = "details";

    FloatingActionButton fabCreate;
    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";
    UserSessionManager session;
    View contaner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nav_bar);
        contaner=findViewById(R.id.flotingcontainer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        session = new UserSessionManager(getApplicationContext());
        fabCreate=(FloatingActionButton) contaner.findViewById(R.id.btnCreate);
        fabCreate.setOnClickListener(this);
        if(session.checkLogin())
            finish();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);



        TextView Nameview = (TextView)header.findViewById(R.id.tvName);
        TextView Emailview = (TextView)header.findViewById(R.id.tvEmail);

        HashMap<String, String> user = session.getUserDetails();

        // get name
        String name = user.get(UserSessionManager.KEY_NAME);

        // get email
        String email = user.get(UserSessionManager.KEY_EMAIL);

        Nameview.setText(name);
        Emailview.setText(email);




        displaySelectedScreen(R.id.nav_list_events);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.nav_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, PastEvent.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        HashMap<String, String> user = session.getUserDetails();

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_list_events:
                fragment = new ListEventsTabs();
                break;
            case R.id.nav_bookmarks:
                if(user.get(UserSessionManager.KEY_USERTYPE_ID).equals("2")) {

                    Toast.makeText(getApplicationContext(),"Please Register to acess ",Toast.LENGTH_LONG).show();
                }
                else
            {
                fragment = new BookmarksList();
            }
                break;
            case R.id.nav_organise_event:
                if(user.get(UserSessionManager.KEY_USERTYPE_ID).equals("2")) {

                    Toast.makeText(getApplicationContext(),"Please Register to access ",Toast.LENGTH_LONG).show();
                }
                else
                {
                    onTakePhotoClick();
                }

            break;
            case R.id.nav_manage_organise_event:
                if(user.get(UserSessionManager.KEY_USERTYPE_ID).equals("2")) {

                    Toast.makeText(getApplicationContext(),"Please Register to access ",Toast.LENGTH_LONG).show();
                }
                else
                {
                    fragment = new OrganisedList();
                }

                break;

            case R.id.nav_profile_manage:
                fragment = new UserProfile();
                break;
            case R.id.nav_logout:
                session.logoutUser();
                finish();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }



    public void onTakePhotoClick() {
        int[] startingLocation = new int[2];
        fabCreate.getLocationOnScreen(startingLocation);
        startingLocation[0] += fabCreate.getWidth() / 2;
        ///TakePhotoActivity.startCameraFromLocation(startingLocation, this);
        PublishActivity.openWithPhotoUri(this);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View view) {
        HashMap<String, String> user = session.getUserDetails();
        if(view.getId()==R.id.btnCreate)
        {

            if(user.get(UserSessionManager.KEY_USERTYPE_ID).equals("2")) {

                Toast.makeText(getApplicationContext(),"Please Register to access ",Toast.LENGTH_LONG).show();
            }
            else {
                onTakePhotoClick();
            }
        }
    }
}