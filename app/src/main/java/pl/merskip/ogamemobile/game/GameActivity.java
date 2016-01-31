package pl.merskip.ogamemobile.game;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

import pl.merskip.ogamemobile.BuildConfig;
import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.AuthorizationData;
import pl.merskip.ogamemobile.adapter.Planet;
import pl.merskip.ogamemobile.adapter.PlanetList;
import pl.merskip.ogamemobile.adapter.ResourcesSummary;
import pl.merskip.ogamemobile.adapter.ResultPageFactory;
import pl.merskip.ogamemobile.adapter.ScriptData;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.RequestPage;
import pl.merskip.ogamemobile.adapter.pages.ResultPage;
import pl.merskip.ogamemobile.game.DownloadPageNotifier.DownloadPageListener;
import pl.merskip.ogamemobile.game.pages.ViewerPage;
import pl.merskip.ogamemobile.game.pages.ViewerPageFactory;

public class GameActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DownloadPageListener, AdapterView.OnItemSelectedListener {

    private AuthorizationData auth;

    private String currentPage;
    private Planet currentPlanet = new Planet("", "", "");

    private DownloadPageNotifier downloadPageNotifier;
    private ResultPage lastResultPage;
    private Document lastMainDocument;
    private ScriptData lastScriptData;

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
                refreshCurrentPage();
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
        openPage(currentPage, currentPlanet);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d("GameActivity", "No planet select");
    }

    public AuthorizationData getAuth() {
        return auth;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public Planet getCurrentPlanet() {
        return currentPlanet;
    }

    public void abortBuild(final BuildItem buildItem, final String listId) {
        String pageName = currentPage;
        final String abortToken = lastScriptData.getAbortToken();
        RequestPage requestPage = new RequestPage(auth, pageName, currentPlanet.id) {
            @Override
            protected Connection createConnection() {
                return super.createConnection()
                        .data("token", abortToken)
                        .data("modus", "2")
                        .data("techid", buildItem.id)
                        .data("listid", listId);
            }
        };

        ResultPage resultPage = ResultPageFactory.getResultPage(pageName);
        ViewerPage viewerPage = ViewerPageFactory.getViewerPage(this, pageName);

        new DownloadTask(this, requestPage, resultPage, viewerPage).execute();

        Log.d("GameActivity", "Request abort name=" + buildItem.name
                + ", page=" + pageName
                + ", planet=" + currentPlanet.name);
    }

    public void build(final BuildItem buildItem) {
        String pageName = currentPage;
        RequestPage requestPage = new RequestPage(auth, pageName, currentPlanet.id) {
            @Override
            protected String getRequestUrl() {
                return buildItem.buildRequestUrl;
            }
        };
        ResultPage resultPage = ResultPageFactory.getResultPage(pageName);
        ViewerPage viewerPage = ViewerPageFactory.getViewerPage(this, pageName);

        new DownloadTask(this, requestPage, resultPage, viewerPage).execute();

        Log.d("GameActivity", "Request build name=" + buildItem.name
                + ", page=" + pageName
                + ", planet=" + currentPlanet.name);
    }

    public void buildAmount(final BuildItem buildItem, final int amount) {
        String pageName = currentPage;
        final String token = lastMainDocument.select("form[name=form] input[name=token]").attr("value");
        RequestPage requestPage = new RequestPage(auth, pageName, currentPlanet.id) {
            @Override
            protected Connection createConnection() {
                return super.createConnection()
                        .method(Connection.Method.POST)
                        .data("token", token)
                        .data("modus", "1")
                        .data("type", buildItem.id)
                        .data("menge", String.valueOf(amount));
            }
        };

        new DownloadTask(this, requestPage, null, null).execute();

        Log.d("GameActivity", "Request amount build name=" + buildItem.name
                + ", amount=" + amount
                + ", page=" + pageName
                + ", planet=" + currentPlanet.name);
    }

    public void refreshCurrentPage() {
        openPage(currentPage, currentPlanet);
    }

    public void openPage(String pageName) {
        openPage(pageName, currentPlanet);
    }

    public void openPage(String pageName, Planet planet) {
        RequestPage requestPage = new RequestPage(auth, pageName, planet.id);
        ResultPage resultPage = ResultPageFactory.getResultPage(pageName);
        ViewerPage viewerPage = ViewerPageFactory.getViewerPage(this, pageName);

        new DownloadTask(this, requestPage, resultPage, viewerPage).execute();

        Log.d("GameActivity", "Started opening page=" + pageName
                + ", planet=" + planet.name);
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

    public void notifyDownloadPage(ResultPage resultPage) {
        downloadPageNotifier.notifyListeners(resultPage);
    }

    @Override
    public void onDownloadPage(ResultPage resultPage) {
        lastResultPage = resultPage;
        Document document = resultPage.getDocument();

        if (isMainDocument(document)) {
            this.lastMainDocument = document;
            this.lastScriptData = resultPage.getScriptData();
        }

        updateSelectedMenuItem(resultPage);
        updatePlanetName(document);
        updatePlanetList(document);
        updateCurrentPlanet(document);
    }

    private boolean isMainDocument(Document document) {
        return document.body().hasClass("ogame");
    }

    private void updateSelectedMenuItem(ResultPage resultPage) {
        String pageName = resultPage.getRequest().getPageName();
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
}
