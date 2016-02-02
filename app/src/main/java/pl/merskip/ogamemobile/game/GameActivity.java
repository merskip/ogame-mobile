package pl.merskip.ogamemobile.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import pl.merskip.ogamemobile.BuildConfig;
import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.Planet;
import pl.merskip.ogamemobile.adapter.ResourcesSummary;
import pl.merskip.ogamemobile.adapter.ResultPageFactory;
import pl.merskip.ogamemobile.adapter.ScriptData;
import pl.merskip.ogamemobile.adapter.game.AbortBuildRequest;
import pl.merskip.ogamemobile.adapter.game.AmountBuildRequest;
import pl.merskip.ogamemobile.adapter.game.BuildRequest;
import pl.merskip.ogamemobile.adapter.game.Building;
import pl.merskip.ogamemobile.adapter.game.DemolishRequest;
import pl.merskip.ogamemobile.adapter.game.RequestPage;
import pl.merskip.ogamemobile.adapter.game.ResultPage;
import pl.merskip.ogamemobile.adapter.login.AuthorizationData;
import pl.merskip.ogamemobile.game.DownloadPageNotifier.DownloadPageListener;
import pl.merskip.ogamemobile.game.pages.ViewerPage;
import pl.merskip.ogamemobile.game.pages.ViewerPageFactory;

public class GameActivity
        extends AppCompatActivity
        implements DownloadPageListener {

    private AuthorizationData auth;

    private String currentPage;
    private Planet currentPlanet = new Planet("", "", "");

    private DownloadPageNotifier downloadPageNotifier = new DownloadPageNotifier();
    private Document lastMainDocument;
    private ScriptData lastScriptData;

    private ActionBar actionBar;
    private AppNavigation navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setupActionBar();
        navigation = (AppNavigation) findViewById(R.id.navigation);
        navigation.setup(this);

        downloadPageNotifier.addListener(this);

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

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
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

    public AuthorizationData getAuth() {
        return auth;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public Planet getCurrentPlanet() {
        return currentPlanet;
    }

    public void abortBuild(Building building, String listId) {
        String pageName = currentPage;
        String planetId = currentPlanet.id;
        String abortToken = lastScriptData.getAbortToken();
        RequestPage requestPage =
                new AbortBuildRequest(auth, pageName, planetId, building, abortToken, listId);

        ResultPage resultPage = ResultPageFactory.getResultPage(pageName);
        ViewerPage viewerPage = ViewerPageFactory.getViewerPage(this, pageName);

        new DownloadTask(this, requestPage, resultPage, viewerPage).execute();

        Log.d("GameActivity", "Request abort name=" + building.name
                + ", page=" + pageName
                + ", planet=" + currentPlanet.name);
    }

    public void demolish(Building building) {
        String pageName = currentPage;
        String planetId = currentPlanet.id;
        String demolishToken = lastScriptData.getDemolishToken();
        RequestPage requestPage =
                new DemolishRequest(auth, pageName, planetId, building, demolishToken);

        ResultPage resultPage = ResultPageFactory.getResultPage(pageName);
        ViewerPage viewerPage = ViewerPageFactory.getViewerPage(this, pageName);

        new DownloadTask(this, requestPage, resultPage, viewerPage).execute();

        Log.d("GameActivity", "Request demolish name=" + building.name
                + ", page=" + pageName
                + ", planet=" + planetId);
    }

    public void build(Building building) {
        String pageName = currentPage;
        RequestPage requestPage = new BuildRequest(auth, pageName, building);
        ResultPage resultPage = ResultPageFactory.getResultPage(pageName);
        ViewerPage viewerPage = ViewerPageFactory.getViewerPage(this, pageName);

        new DownloadTask(this, requestPage, resultPage, viewerPage).execute();

        Log.d("GameActivity", "Request build name=" + building.name
                + ", page=" + pageName
                + ", planet=" + currentPlanet.name);
    }

    public void buildAmount(Building building, int amount) {
        String pageName = currentPage;
        String planetId = currentPlanet.id;
        String token = getBuildTokenFromDocument();
        RequestPage requestPage =
                new AmountBuildRequest(auth, pageName, planetId, building, token, amount);

        new DownloadTask(this, requestPage, null, null).execute();

        Log.d("GameActivity", "Request amount build name=" + building.name
                + ", amount=" + amount
                + ", page=" + pageName
                + ", planet=" + currentPlanet.name);
    }

    private String getBuildTokenFromDocument() {
        return lastMainDocument.select("form[name=form] input[name=token]").attr("value");
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
        if (pageName == null || pageName.isEmpty())
            Log.e("GameActivity", "showContentPage: pageName is null or empty!");

        this.currentPage = pageName;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment)
                .commitAllowingStateLoss();

        Log.d("GameActivity", "Shown content of page: " + pageName);
    }

    public DownloadPageNotifier getDownloadPageNotifier() {
        return downloadPageNotifier;
    }

    @Override
    public void onDownloadPage(ResultPage resultPage) {
        Document document = resultPage.getDocument();

        if (isMainDocument(document)) {
            this.lastMainDocument = document;
            this.lastScriptData = resultPage.getScriptData();
        }

        updateCurrentPlanet(document);
    }

    private boolean isMainDocument(Document document) {
        return document.body().hasClass("ogame");
    }

    private void updateCurrentPlanet(Document document) {
        Elements metaPlanetId =
                document.head()
                        .getElementsByAttributeValue("name", "ogame-planet-id");

        if (!metaPlanetId.isEmpty()) {
            String currentPlanetId = metaPlanetId.attr("content");
            currentPlanet = navigation.getPlanetById(currentPlanetId);

            if (actionBar != null)
                actionBar.setTitle(currentPlanet.name);
        }
    }

    public ResourcesSummary getActualResources() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ResourcesBarFragment resourcesBarFragment = (ResourcesBarFragment)
                fragmentManager.findFragmentById(R.id.resources_bar);

        return resourcesBarFragment.getResourcesSummary();
    }
}
