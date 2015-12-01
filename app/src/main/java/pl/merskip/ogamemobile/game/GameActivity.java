package pl.merskip.ogamemobile.game;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.jsoup.nodes.Document;

import pl.merskip.ogamemobile.BuildConfig;
import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.AuthorizationData;
import pl.merskip.ogamemobile.adapter.ScriptData;
import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.game.DownloadPageNotifier.DownloadPageListener;

public class GameActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DownloadPageListener {

    private AuthorizationData auth;
    private DownloadPageFactory downloadPageFactory;
    private String currentPage;

    private DownloadPageNotifier downloadPageNotifier;

    private DrawerLayout drawerLayout;
    private NavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        downloadPageNotifier = new DownloadPageNotifier();
        downloadPageNotifier.addListener(this);

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

        downloadPageFactory = new DownloadPageFactory(this);
        openPage("overview");
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
                openPage("overview");
                return true;
            case R.id.resources:
                openPage("resources");
                return true;
            case R.id.station:
                openPage("station");
                return true;
            case R.id.research:
                openPage("research");
                return true;
            case R.id.shipyard:
                openPage("shipyard");
                return true;
            case R.id.defense:
                openPage("defense");
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
                refreshPage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void build(BuildItem buildItem) {
        if (buildItem.buildRequestUrl == null)
            throw new IllegalArgumentException("Build request url is null, " +
                    "maybe is not ready to build or is ship?");

        DownloadPageTask<?> downloadPageTask =
                downloadPageFactory.getDownloadPageTask(currentPage);

        downloadPageTask.setPageName(currentPage);
        downloadPageTask.setCustomUrl(buildItem.buildRequestUrl);
        downloadPageTask.execute();
        Log.d("GameActivity", "Request to build " + buildItem.id + " on page: " + currentPage);
    }

    public void refreshPage() {
        openPage(currentPage);
    }

    /**
     * Pobiera i otwiera asynchronicznie stronę o podanej nazwie.
     */
    public void openPage(String pageName){
        DownloadPageTask<?> downloadPageTask =
                downloadPageFactory.getDownloadPageTask(pageName);

        if (downloadPageTask == null)
            throw new IllegalArgumentException("Unknown page name: " + pageName);

        downloadPageTask.setPageName(pageName);
        downloadPageTask.execute();
        Log.d("GameActivity", "Started opening page: " + pageName);
    }

    /**
     * Wyświetla treść strony i ustawia ją na aktualnie otwartą
     */
    public void showContentPage(String pageName, Fragment fragment) {
        if (pageName == null)
            Log.e("GameActivity", "showContentPage: pageName is null!");

        this.currentPage = pageName;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment)
                .commitAllowingStateLoss();

        Log.d("GameActivity", "Shown content of page: " + pageName);
    }

    public void addDownloadPageListener(DownloadPageListener listener) {
        downloadPageNotifier.addListener(listener);
    }

    public void notifyDownloadPage(AbstractPage<?> downloadPage) {
        downloadPageNotifier.notifyListeners(downloadPage);
    }

    @Override
    public void onDownloadPage(Document document, ScriptData scriptData) {
        updatePlanetName(document);
    }

    private void updatePlanetName(Document document) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            String planetName = getPlanetName(document);
            actionBar.setTitle(planetName);
        }
    }

    private static String getPlanetName(Document document) {
        return document.head()
                .getElementsByAttributeValue("name", "ogame-planet-name")
                .attr("content");
    }

    public AuthorizationData getAuthorizationData() {
        return auth;
    }
}
