package projects.austin.gymrat;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;

import projects.austin.gymrat.model.Logs.WorkoutLogManager;
import projects.austin.gymrat.model.Workout.WorkoutManager;
import projects.austin.gymrat.providers.WorkoutLogIO;
import projects.austin.gymrat.providers.WorkoutsIO;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        WorkoutSelectionFragment.OnFragmentInteractionListener,
        TemporaryLandingFragment.OnFragmentInteractionListener,
        WorkoutDisplayFragment.OnFragmentInteractionListener,
        WorkoutInstanceFragment.OnFragmentInteractionListener,
        WorkoutLogsDisplayFragment.OnFragmentInteractionListener {
    @Override
    protected void onPause() {
        super.onPause();
        //save my workouts to the file
        JSONArray workoutsAsJSON = WorkoutLogManager.getInstance().toJSONArray();
        WorkoutLogIO.getInstance().writeLogsToFile(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //return to home page
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fragment_layout_container, TemporaryLandingFragment.newInstance(null, null));
                ft.commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });


        //populate our workouts
        if(WorkoutsIO.getInstance().getJSONAndLoad(this, false)){
            System.out.println(WorkoutManager.getInstance().toString());
        };

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_layout_container, new TemporaryLandingFragment());
        ft.commit();
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
        String tagParam = "";

        if (id == R.id.nav_chest) {
            tagParam = "chest";
        } else if (id == R.id.nav_back) {
            tagParam = "back";
        } else if (id == R.id.nav_legs) {
            tagParam = "leg";
        } else if (id == R.id.nav_shoulders) {
            tagParam = "shoulder";
        } else if (id == R.id.nav_cardio) {
            tagParam = "cardio";
        } else if (id == R.id.nav_logs) {
            tagParam = "logs";
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (tagParam.equals("logs")) {
            ft.replace(R.id.fragment_layout_container, WorkoutLogsDisplayFragment.newInstance());
        } else {
            ft.replace(R.id.fragment_layout_container, WorkoutSelectionFragment.newInstance(tagParam));
        }
        ft.addToBackStack(null);
        ft.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
