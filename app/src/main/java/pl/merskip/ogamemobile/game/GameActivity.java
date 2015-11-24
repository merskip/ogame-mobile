package pl.merskip.ogamemobile.game;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import pl.merskip.ogamemobile.BuildConfig;
import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.AuthorizationData;
import pl.merskip.ogamemobile.game.pages.overview.GetOverviewTask;

public class GameActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AuthorizationData auth;

    private DrawerLayout drawerLayout;
    private NavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setupToolbarAndNavigationDrawer();

        auth = (AuthorizationData) getIntent().getSerializableExtra("auth");
        if (auth == null) {
            Log.e("GameActivity", "Parameter 'auth' (AuthorizationData)" +
                    " is required to start GameActivity");

            /* Daje to możliwość uruchomienia aktywności w celu przetestowania
             * części interfejsu bez logowania */
            if (BuildConfig.DEBUG)
                return;

            finish();
        }

        new GetOverviewTask(this, auth).execute();
    }

    private void setupToolbarAndNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigation = (NavigationView) findViewById(R.id.navigation);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                        R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(toggle);

        navigation.setNavigationItemSelectedListener(this);
        navigation.setCheckedItem(R.id.overview);

        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawerLayout.closeDrawer(navigation);

        switch (item.getItemId()) {
            case R.id.overview:
                new GetOverviewTask(this, auth).execute();
                return true;
            default:
                Log.w("GameActivity", "Selected unknown menu item: " + item);
                return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                new GetOverviewTask(this, auth).execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public AuthorizationData getAuthorizationData() {
        return auth;
    }
}
