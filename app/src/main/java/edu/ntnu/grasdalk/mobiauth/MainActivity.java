package edu.ntnu.grasdalk.mobiauth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import edu.ntnu.grasdalk.mobiauth.adapters.ApplicationAdapter;
import edu.ntnu.grasdalk.mobiauth.adapters.OrganizationAdapter;
import edu.ntnu.grasdalk.mobiauth.models.Application;
import edu.ntnu.grasdalk.mobiauth.models.Organization;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MobiauthClient mobiauthClient;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_QR_CODE_SCAN = 2;
    final static int PERMISSIONS_CAMERA = 3;

    private ImageView mNavbarImageView;

    private TextView mNavbarFullnameTextView;
    private TextView mNavbarEmailTextView;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.shared_preferences),
                Context.MODE_PRIVATE);

        mobiauthClient = ServiceGenerator.createService(
                MobiauthClient.class,
                getResources().getString(R.string.server_api_path),
                sharedPref.getString(getString(R.string.prompt_username), ""),
                sharedPref.getString(getString(R.string.prompt_password), ""));

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

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
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
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mNavbarImageView.setImageBitmap(imageBitmap);
        }

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                Log.d(result.getContents(), "");
                dispatchTakePictureIntent();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchScanQrCodeIntent() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
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
            //dispatchTakePictureIntent();
            dispatchScanQrCodeIntent();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_authenticate) {
           authenticateUser();
        } else if (id == R.id.nav_organizations) {
            Call<List<Organization>> call = mobiauthClient.getOrganizations();
            call.enqueue(new Callback<List<Organization>>() {
                @Override
                public void onResponse(Call<List<Organization>> call, Response<List<Organization>> response) {
                    if (response.isSuccessful()) {
                        mAdapter = new OrganizationAdapter(response.body());
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        Log.d("Error", response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<Organization>> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast
                            .makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG)
                            .show();
                }
            });
        } else if (id == R.id.nav_applications) {
            Call<List<Application>> call = mobiauthClient.getApplications();
            call.enqueue(new Callback<List<Application>>() {
                @Override
                public void onResponse(Call<List<Application>> call, Response<List<Application>> response) {
                    if (response.isSuccessful()) {
                        mAdapter = new ApplicationAdapter(response.body());
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        Log.d("Error", response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<Application>> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast
                            .makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG)
                            .show();
                }
            });
        } else if (id == R.id.nav_settings) {

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

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
