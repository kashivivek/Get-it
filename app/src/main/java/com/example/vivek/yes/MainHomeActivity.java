package com.example.vivek.yes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vivek.utils.SessionManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

public class MainHomeActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyApp_Settings";
    Context context;
    SessionManager session;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Start your hunt here!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
       /* SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String count = saved_values.getString("count", null);
        System.out.println("@@@@@@@@@@iloveanusha"+count+ "@@@@@@@@@@@");
*/
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mViewPager.setTransitionName("profile");
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        logout_dialog();
    }

    public void logout_dialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You are about to log out. Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        session.logoutUser();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //Database Example
    public void floatingthing(View view) {
        Intent intent = new Intent(MainHomeActivity.this, MainHomeActivity.class);
        startActivity(intent);
    }

    //Recent services
    public void openRecentServices(View view) {
        Intent intent = new Intent(MainHomeActivity.this, recentServicesActivity.class);
        startActivity(intent);
    }

    // MainActivity
    public void animateIntent(View view) {

        // Ordinary Intent for launching a new activity
        Intent intent = new Intent(MainHomeActivity.this, TimePass.class);

        // Get the transition name from the string
        String transitionName = "profilepic";

        // Define the view that the animation will start from
        View viewStart = findViewById(R.id.profile_pic);

        ActivityOptionsCompat options =

                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        viewStart,   // Starting view
                        transitionName    // The String
                );
        //Start the Intent
        ActivityCompat.startActivity(this, intent, options.toBundle());

    }

    public void openbox(View view) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {

        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);

    }

    public void openMaps(View view) {

        // Ordinary Intent for launching a new activity
        //Intent intent = new Intent(MainHomeActivity.this, HomePlacesActivity.class);
        Intent intent = new Intent(MainHomeActivity.this, ProductInfo.class);
        startActivity(intent);
    }

    public void openMapsMarker(View view) {

        // Ordinary Intent for launching a new activity
        Intent intent = new Intent(MainHomeActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout_dialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class MainHomeFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public MainHomeFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MainHomeFragment newInstance(int sectionNumber) {
            MainHomeFragment fragment = new MainHomeFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_home, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            /*textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/

            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class HouseholdFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public HouseholdFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static HouseholdFragment newInstance(int sectionNumber) {
            HouseholdFragment fragment = new HouseholdFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_household, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            /*textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
            return rootView;
        }
    }

    public static class GroceriesFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public GroceriesFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static GroceriesFragment newInstance(int sectionNumber) {
            GroceriesFragment fragment = new GroceriesFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_groceries_home, container, false);
            /*TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
            return rootView;
        }
    }

    public static class FoodFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public FoodFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static FoodFragment newInstance(int sectionNumber) {
            FoodFragment fragment = new FoodFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_food_home, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            /*textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    // Main Home Page fragment activity
                    return MainHomeFragment.newInstance(position + 1);
                case 1:
                    // Food fragment activity
                    return FoodFragment.newInstance(position + 1);
                case 2:
                    // Groceries fragment activity
                    return GroceriesFragment.newInstance(position + 1);
                case 3:
                    //House hold Activity
                    return HouseholdFragment.newInstance(position + 1);
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Home Page";
                case 1:
                    return "Food";
                case 2:
                    return "Groceries";
                case 3:
                    return "House Hold Activites";
            }
            return super.getPageTitle(position);
        }
    }
}
