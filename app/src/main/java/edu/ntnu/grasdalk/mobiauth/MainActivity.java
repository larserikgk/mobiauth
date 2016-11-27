package edu.ntnu.grasdalk.mobiauth;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.ntnu.grasdalk.mobiauth.fragments.ApplicationFragment;
import edu.ntnu.grasdalk.mobiauth.fragments.AuthenticationFragment;
import edu.ntnu.grasdalk.mobiauth.fragments.OrganizationFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final static int PERMISSIONS_CAMERA = 3;

    private ImageView mNavbarImageView;

    private TextView mNavbarFullnameTextView;
    private TextView mNavbarEmailTextView;

    private OrganizationFragment organizationFragment;
    private AuthenticationFragment authenticationFragment;
    private ApplicationFragment applicationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){

                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                        final SharedPreferences sharedPref = getSharedPreferences(
                                getString(R.string.shared_preferences),
                                Context.MODE_PRIVATE);
                        mNavbarFullnameTextView =
                                (TextView)findViewById(R.id.navbar_fullname_textview);
                        mNavbarEmailTextView =
                                (TextView) findViewById(R.id.navbar_email_textview);
                        mNavbarImageView = (ImageView) findViewById(R.id.navbar_image);
                        mNavbarFullnameTextView.setText(
                                sharedPref.getString(getString(R.string.prefs_first_name), ""));
                        mNavbarEmailTextView.setText(
                                sharedPref.getString(getString(R.string.prefs_email), ""));
                    }
                };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        organizationFragment = new OrganizationFragment();
        authenticationFragment = new AuthenticationFragment();
        applicationFragment = new ApplicationFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, authenticationFragment).commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(getFragmentManager().getBackStackEntryCount() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to log out?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            logOut();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            } else {
            super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        final SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.shared_preferences),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().commit();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("source", MainActivity.class.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void authenticateUser() {
        if(!checkCameraHardware(getApplicationContext())) {
            Toast.makeText(
                    getApplicationContext(),
                    getString(R.string.error_no_camera),
                    Toast.LENGTH_LONG).show();
        }

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    PERMISSIONS_CAMERA);
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, authenticationFragment)
                    .addToBackStack("")
                    .commit();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_authenticate) {
            authenticateUser();

        } else if (id == R.id.nav_organizations) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, organizationFragment)
                    .addToBackStack("")
                    .commit();

        } else if (id == R.id.nav_applications) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, applicationFragment)
                    .addToBackStack("")
                    .commit();

        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "Settings are not yet available", Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_support) {
            Intent browserIntent =
                    new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.server_help_url)));
            startActivity(browserIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String permissions[],
            @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    authenticateUser();
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            getString(R.string.error_no_camera),
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
