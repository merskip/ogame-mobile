package pl.merskip.ogamemobile.game;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

import pl.merskip.ogamemobile.BuildConfig;
import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.AuthorizationData;
import pl.merskip.ogamemobile.adapter.Planet;
import pl.merskip.ogamemobile.adapter.PlanetList;
import pl.merskip.ogamemobile.adapter.ResourcesSummary;
import pl.merskip.ogamemobile.adapter.ScriptData;
import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.game.DownloadPageNotifier.DownloadPageListener;

public class GameActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DownloadPageListener, AdapterView.OnItemSelectedListener {

    private AuthorizationData auth;
    private DownloadPageFactory downloadPageFactory;

    private String currentPage;
    private Planet currentPlanet;

    private Document mainDocument;
    private ScriptData scriptData;

    private DownloadPageNotifier downloadPageNotifier;

    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigation;
    private PlanetListAdapter planetListAdapter;

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

        Intent intent = getIntent();
        String startPage = intent.getStringExtra("start-page");
        openPage(startPage != null ? startPage : "resources");
    }

    private void setupToolbarAndNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigation = (NavigationView) findViewById(R.id.navigation);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                        R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(toggle);
        navigation.setNavigationItemSelectedListener(this);
        toggle.syncState();

        View headerView = navigation.getHeaderView(0);
        Spinner planetSelect = (Spinner) headerView.findViewById(R.id.planet_select);
        planetListAdapter = new PlanetListAdapter(this);
        planetSelect.setAdapter(planetListAdapter);
        planetSelect.setOnItemSelectedListener(this);
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
                return false;
            case R.id.resources:
                openPage("resources");
                return false;
            case R.id.station:
                openPage("station");
                return false;
            case R.id.research:
                openPage("research");
                return false;
            case R.id.shipyard:
                openPage("shipyard");
                return false;
            case R.id.defense:
                openPage("defense");
                return false;
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

    private boolean ignoreFirstPlanetSelect = true;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (ignoreFirstPlanetSelect) {
            ignoreFirstPlanetSelect = false;
            return;
        }

        currentPlanet = (Planet) planetListAdapter.getItem(position);
        refreshPage();
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d("GameActivity", "No planet select");
    }

    public void build(BuildItem buildItem) {
        if (buildItem.buildRequestUrl == null)
            throw new IllegalArgumentException("Build request url is null, " +
                    "maybe is not ready to build or is ship?");

        DownloadPageTask<?> downloadPageTask =
                downloadPageFactory.getDownloadPageTask(currentPage);

        downloadPageTask.setPageName(currentPage);
        downloadPageTask.setCustomUrl(buildItem.buildRequestUrl);
        if (currentPlanet != null)
            downloadPageTask.setPlanetId(currentPlanet.id);

        downloadPageTask.execute();
        Log.d("GameActivity", "Request to build=" + buildItem.id
                + ", page=" + currentPage
                + ", planet=" + (currentPlanet != null ? currentPlanet.name : ""));
    }

    public void refreshPage() {
        openPage(currentPage);
    }

    /**
     * Pobiera i otwiera asynchronicznie stronę o podanej nazwie.
     */
    public void openPage(String pageName) {
        DownloadPageTask<?> downloadPageTask =
                downloadPageFactory.getDownloadPageTask(pageName);

        if (downloadPageTask == null)
            throw new IllegalArgumentException("Unknown page name: " + pageName);

        downloadPageTask.setPageName(pageName);
        if (currentPlanet != null)
            downloadPageTask.setPlanetId(currentPlanet.id);

        downloadPageTask.execute();
        Log.d("GameActivity", "Started opening page=" + pageName
                + ", planet=" + (currentPlanet != null ? currentPlanet.name : ""));
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
    public void onDownloadPage(AbstractPage<?> downloadPage) {
        Document document = downloadPage.getDocument();
        if (document.body().hasClass("ogame")) {
            this.mainDocument = document;
            this.scriptData = downloadPage.getScriptData();
        }

        updateSelectedMenuItem(downloadPage);
        updatePlanetName(document);
        updatePlanetList(document);
        updateCurrentPlanet(document);
    }

    private void updateSelectedMenuItem(AbstractPage<?> downloadPage) {
        String pageName = downloadPage.getPageName();
        int pageMenuId = getMenuIdFromPageName(pageName);
        navigation.setCheckedItem(pageMenuId);
    }

    private int getMenuIdFromPageName(String pageName) {
        String packageName = getPackageName();
        Resources resources  = getResources();
        return resources.getIdentifier(pageName, "id", packageName);
    }

    private void updatePlanetName(Document document) {
        String planetName = getPlanetName(document);
        if (actionBar != null && planetName != null)
            actionBar.setTitle(planetName);
    }

    private static String getPlanetName(Document document) {
        Elements metaPlanet = document.head()
                .getElementsByAttributeValue("name", "ogame-planet-name");

        if (metaPlanet.size() > 0)
            return metaPlanet.attr("content");
        return null;
    }

    private void updatePlanetList(Document document) {
        List<Planet> planetList = PlanetList.fromDocument(document);
        if (planetList != null) {
            planetListAdapter.setPlanetList(planetList);
        }
    }

    private void updateCurrentPlanet(Document document) {
        Elements metaPlanetId = document.head()
                .getElementsByAttributeValue("name", "ogame-planet-id");

        if (metaPlanetId.size() == 0)
            return;

        String currentPlanetId = metaPlanetId.attr("content");
        for (Planet planet : planetListAdapter.getPlanetList()) {
            if (planet.id.equals(currentPlanetId))
                currentPlanet = planet;
        }
    }

    public ResourcesSummary getActualResources() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ResourcesBarFragment resourcesBarFragment = (ResourcesBarFragment)
                fragmentManager.findFragmentById(R.id.resources_bar);

        return resourcesBarFragment.getResourcesSummary();
    }

    public AuthorizationData getAuthorizationData() {
        return auth;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public Document getMainDocument() {
        return mainDocument;
    }

    public ScriptData getScriptData() {
        return scriptData;
    }
}
