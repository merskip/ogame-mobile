package pl.merskip.ogamemobile.game;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.jsoup.nodes.Document;

import java.util.List;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.Planet;
import pl.merskip.ogamemobile.adapter.PlanetList;
import pl.merskip.ogamemobile.adapter.game.ResultPage;

/**
 * Nawigacja
 */
public class AppNavigation
        extends NavigationView
        implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemSelectedListener,
DownloadPageNotifier.DownloadPageListener {

    private GameActivity activity;

    private DrawerLayout drawerLayout;
    private PlanetListAdapter planetListAdapter;

    public AppNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup(GameActivity activity) {
        this.activity = activity;

        activity.getDownloadPageNotifier().addListener(this);

        setupDrawer();
        setupPlanetList();
    }

    private void setupDrawer() {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(activity, drawerLayout, toolbar,
                        R.string.drawer_open, R.string.drawer_close);

        drawerLayout.setDrawerListener(toggle);
        setNavigationItemSelectedListener(this);
        toggle.syncState();
    }

    private void setupPlanetList() {
        View headerView = getHeaderView(0);
        Spinner planetSelect = (Spinner) headerView.findViewById(R.id.planet_select);
        planetListAdapter = new PlanetListAdapter(activity);
        planetSelect.setAdapter(planetListAdapter);
        planetSelect.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawerLayout.closeDrawer(this);

        switch (item.getItemId()) {
            case R.id.overview:
                activity.openPage("overview");
                return false;
            case R.id.resources:
                activity.openPage("resources");
                return false;
            case R.id.station:
                activity.openPage("station");
                return false;
            case R.id.research:
                activity.openPage("research");
                return false;
            case R.id.shipyard:
                activity.openPage("shipyard");
                return false;
            case R.id.defense:
                activity.openPage("defense");
                return false;
            case R.id.fleet:
                activity.openPage("fleet");
                return false;
            default:
                Log.w("GameActivity", "Selected unknown menu item: " + item);
                return false;
        }
    }

    private boolean ignoreFirstPlanetSelect = true;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (ignoreFirstPlanetSelect) {
            ignoreFirstPlanetSelect = false;
            return;
        }

        String currentPage = activity.getCurrentPage();
        Planet selectedPlanet = (Planet) planetListAdapter.getItem(position);
        activity.openPage(currentPage, selectedPlanet);

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d("GameActivity", "No planet select");
    }

    public Planet getPlanetById(String planetId) {
        List<Planet> planetList = planetListAdapter.getPlanetList();
        for (Planet planet : planetList) {
            if (planet.id.equals(planetId))
                return planet;
        }
        return null;
    }

    @Override
    public void onDownloadPage(ResultPage resultPage) {
        Document document = resultPage.getDocument();

        updateSelectedMenuItem(resultPage);
        updatePlanetList(document);
    }

    private void updateSelectedMenuItem(ResultPage resultPage) {
        String pageName = resultPage.getRequest().getPageName();
        int pageMenuId = getMenuIdFromPageName(pageName);
        setCheckedItem(pageMenuId);
    }

    private int getMenuIdFromPageName(String pageName) {
        String packageName = activity.getPackageName();
        Resources resources  = getResources();
        return resources.getIdentifier(pageName, "id", packageName);
    }

    private void updatePlanetList(Document document) {
        List<Planet> planetList = PlanetList.fromDocument(document);

        if (planetList != null) {
            planetListAdapter.setPlanetList(planetList);
        }

    }


}
