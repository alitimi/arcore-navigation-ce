package com.example.arcore_navigation_ce;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;

    public NavigationView navigationView = null;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private View navHeader;
    public static int navItemIndex = 0;
    private String[] activityTitles;
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private static final String TAG_HOME = "home";
    private static final String TAG_Questions = "questions";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_CallUs = "call_us";
    private static final String TAG_Share = "share";
    private static final String TAG_Exit = "log_out";
    public static String CURRENT_TAG = TAG_HOME;
    static int number;
    ImageView header;
    TextView sarvar;
    TextView name;
    ImageButton ar;
    ImageButton test;
    ImageButton contact;
    ImageButton map;
    String line;
    int id;
    String first;
    String last;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.lightBlue));
        Bundle extras = getIntent().getExtras();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ar = findViewById(R.id.ar_navigation);
        test = findViewById(R.id.ar1_navigation);
        name = findViewById(R.id.policyholderDisplayName);
        sarvar = findViewById(R.id.textView3);
//        contact = findViewById(R.id.button_contact);
//        map = findViewById(R.id.button_map);
        header = findViewById(R.id.image_hero);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        number = sharedPreferences.getInt("id", 0);
        first = sharedPreferences.getString("firstName", null);
        last = sharedPreferences.getString("lastName", null);
        test.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GetLocation.class);
            startActivity(intent);
        });
        if (first != null) {
            CharacterIterator it = new StringCharacterIterator(first);
            CharacterIterator it1 = new StringCharacterIterator(last);
            for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
                if (ch == 'ي') {
                    first = first.replace("ي", "ی");
                }
            }
            for (char ch = it1.first(); ch != CharacterIterator.DONE; ch = it1.next()) {
                if (ch == 'ي') {
                    last = last.replace("ي", "ی");
                }
            }
        }
        name.setText(first + " " + last);
        name.setTextSize(15);
        name.setTypeface(ResourcesCompat.getFont(this, R.font.iransansmobile));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//            toolbar.setNavigationIcon(R.drawable.ic_settings_black_24dp);
        mHandler = new Handler();
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        // load nav menu header data
        // initializing navigation menu
        setUpNavigationView();
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
//                    startActivity(intent);
            }
        });

        ar.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Destination.class);
            startActivity(intent);
        });


//        contact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
//                    startActivity(intent);
            }
//        });
//        map.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    Intent intent = new Intent(HomeActivity.this, MapActivity.class);
//                    startActivity(intent);
//            }
//        });
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();
        // set toolbar title
        setToolbarTitle();
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            return;
        }
        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                int num = getHome(navItemIndex);
                if (num == 1) {
//                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//                    startActivity(intent);
                } else if (num == 2) {
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                        String shareMessage = "\nLet me recommend you this application\n\n";
                        shareMessage = "aliteymourian80.at@gmail.com";
//                        + BuildConfig.APPLICATION_ID +"\n\n"
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch (Exception e) {
                        //e.toString();
                    }
                } else if (num == 3) {
//                    Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
//                    startActivity(intent);
                } else if (num == 4) {
                    Intent intent = new Intent(MainActivity.this, QuestionsActivity.class);
                    startActivity(intent);
                } else if (num == 5) {
                    finish();
                    System.exit(0);
                }
                // update the main content by replacing fragments
//                Fragment fragment = getHomeFragment(navItemIndex);
//                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                        android.R.anim.fade_out);
//                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
//                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        //    If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        //Closing drawer on item click
        drawer.closeDrawers();
        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private int getHome(int navItemIndex) {
        switch (navItemIndex) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            default:
                return 0;
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.nav_share:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_Share;
                        break;
                    case R.id.nav_contactus:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_CallUs;
                        break;
                    case R.id.nav_questions:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_Questions;
                        break;
                    case R.id.nav_logout:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_Exit;
                        break;
                    default:
                        navItemIndex = 0;
                }
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                loadHomeFragment();
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                drawer.closeDrawer(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawers();
            return;
        }
        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            } else {
                drawer.openDrawer(Gravity.RIGHT);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}