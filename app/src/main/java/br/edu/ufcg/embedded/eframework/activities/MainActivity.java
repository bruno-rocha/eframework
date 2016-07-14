package br.edu.ufcg.embedded.eframework.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import br.edu.ufcg.embedded.eframework.R;
import br.edu.ufcg.embedded.eframework.fragments.MapFragment;
import br.edu.ufcg.embedded.eframework.utils.CircleTransform;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    public static final String MAP_TAG = "MAP_TAG";

    private SharedPreferences sharedPreferences;
    private GoogleApiClient mGoogleApiClient;
    private TextView nameUsr;
    private TextView emailUsr;
    private ImageView imgUsr;
    private MapFragment mapFragment;
    private FragmentManager fragmentManager;
    private int lastFragment;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(SplashActivity.PREFERENCE_NAME, MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);

        nameUsr = (TextView) headerLayout.findViewById(R.id.nameUsr);
        emailUsr = (TextView) headerLayout.findViewById(R.id.emailUsr);
        imgUsr = (ImageView) headerLayout.findViewById(R.id.imgUsr);

        setUpViewsDrawer();
        setUpFragments();
    }

    private void setUpFragments(){
        mapFragment = new MapFragment();

        currentFragment = mapFragment;

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mapFragment, MAP_TAG);
        fragmentTransaction.commit();
    }

    private void setUpViewsDrawer(){
        String nome = sharedPreferences.getString(SplashActivity.USER_NOME, "");
        String email = sharedPreferences.getString(SplashActivity.USER_EMAIL, "");
        String urlImage = sharedPreferences.getString(SplashActivity.USER_URL_PHOTO, "");

        nameUsr.setText(nome);
        emailUsr.setText(email);
        nameUsr.setVisibility(View.VISIBLE);

        if(!urlImage.equals("")){
            Picasso.with(this).load(urlImage).transform(new CircleTransform()).into(imgUsr);
        } else {
            Picasso.with(this).load(R.drawable.default_user).transform(new CircleTransform()).into(imgUsr);
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (id == 0) {
            id = lastFragment;
        }

        switch (id) {
            case R.id.map:
                getSupportActionBar().setTitle(getString(R.string.app_name));
                if (fragmentManager.findFragmentByTag(MAP_TAG) == null) {
                    fragmentTransaction.hide(currentFragment);
                    fragmentTransaction.add(R.id.fragment_container, mapFragment, MAP_TAG);
                    fragmentTransaction.show(mapFragment).commit();
                } else if (!fragmentManager.findFragmentByTag(MAP_TAG).isVisible()) {
                    fragmentTransaction.hide(currentFragment).show(mapFragment).commit();
                }
                currentFragment = mapFragment;
                lastFragment = R.id.map;
                break;
            case R.id.logout:
                signOut();

                Intent mainIntent = new Intent(MainActivity.this,SplashActivity.class);
                startActivity(mainIntent);
                finish();
            default:
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        sharedPreferences = getSharedPreferences(SplashActivity.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String login = sharedPreferences.getString(SplashActivity.USER_LOGIN, "");

        if (login.equals(SplashActivity.FACEBOOK_LOGIN)) {
            LoginManager.getInstance().logOut();
        } else if (login.equals(SplashActivity.GOOGLE_LOGIN)){
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {}
                    });
        }

        editor.putString(SplashActivity.USER_NOME, "");
        editor.putString(SplashActivity.USER_URL_PHOTO, "");
        editor.putString(SplashActivity.USER_EMAIL, "");
        editor.putString(SplashActivity.USER_ID, "0");
        editor.putBoolean(SplashActivity.USER_STATUS, false);
        editor.putString(SplashActivity.USER_LOGIN, SplashActivity.NO_SOCIAL_LOGIN);

        editor.apply();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == SIGN_IN_CODE) {
//            isConsentScreenOpened = false;
//
//            if (resultCode != RESULT_OK) {
//                isSignInButtonClicked = false;
//            }
//
//            if (!googleApiClient.isConnecting()) {
//                googleApiClient.connect();
//            }
//        }
//    }
}
