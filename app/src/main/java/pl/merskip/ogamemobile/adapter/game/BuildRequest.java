package pl.merskip.ogamemobile.adapter.game;

import pl.merskip.ogamemobile.adapter.login.AuthorizationData;

/**
 * Żądanie wybudowania
 */
public class BuildRequest extends RequestPage {

    private Building building;

    public BuildRequest(AuthorizationData auth, String pageName, Building building) {
        super(auth, pageName);
        this.building = building;
    }

    @Override
    protected String getRequestUrl() {
        return building.buildRequestUrl;
    }
}
