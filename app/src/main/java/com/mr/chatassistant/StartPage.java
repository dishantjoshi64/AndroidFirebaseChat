package com.mr.chatassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mr.chatassistant.core.logout.LogoutContract;
import com.mr.chatassistant.core.logout.LogoutPresenter;
import com.mr.chatassistant.news.NewsActivity;
import com.mr.chatassistant.to_do_list.ToDoListActivity;
import com.mr.chatassistant.ui.activities.LoginActivity;
import com.mr.chatassistant.ui.activities.UserListingActivity;

/**
 * Created by mr on 3/27/2018.
 */

public class StartPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LogoutContract.View {

    String username;
    private ImageView ndprofileImageView;
    private LogoutPresenter mLogoutPresenter;
    TextView tv;
    //private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        /*session = new Session(this);
        if(!session.loggedin()){
            logout();
        }*/

        /*username = getIntent().getStringExtra("Username");
        tv = (TextView)findViewById(R.id.TVusername);
        tv.setText(username);*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(StartPage.this, Display.class);
                startActivity(i);

            }
        });*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();




        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mLogoutPresenter = new LogoutPresenter(this);

        /*tv = (TextView) navigationView.getHeaderView(0).findViewById(R.id.TVusername);
        username = getIntent().getStringExtra("Username");
        tv.setText(username);*/

        /*ndprofileImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.nd_profile_image);*/

    }

    /*private void logout(){
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(StartPage.this,LoginActivity.class));
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen( GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id)
        {
            case R.id.nav_profile:
            {
                Intent intent = new Intent(StartPage.this,UserListingActivity.class);
                startActivity(intent);
                break;
            }
            /*case R.id.nav_weather:
            {
                Intent intent = new Intent(StartPage.this,WeatherActivity.class);
                startActivity(intent);
                break;
            }*/
            case R.id.nav_to_do_list:
            {
                Intent intent = new Intent(StartPage.this,ToDoListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_news:
            {
                Intent intent = new Intent(StartPage.this,NewsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.nav_chatBot:
            {
                Intent intent = new Intent(StartPage.this,ChatBotActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.logout:
            {
                logout();
                break;
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mLogoutPresenter.logout();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onLogoutSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        LoginActivity.startIntent(this,
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void onLogoutFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }
}
