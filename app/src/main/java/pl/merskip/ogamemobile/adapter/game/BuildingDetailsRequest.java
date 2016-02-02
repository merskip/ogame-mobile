package pl.merskip.ogamemobile.adapter.game;

import org.jsoup.Connection;

import pl.merskip.ogamemobile.adapter.login.AuthorizationData;

/**
 * Żądanie szczegółowych informacji o budynku
 */
public class BuildingDetailsRequest extends RequestPage {

    protected Building building;

    public BuildingDetailsRequest(AuthorizationData auth, String page, String planetId,
                                  Building building) {
        super(auth, page, planetId);
        this.building = building;
    }

    @Override
    protected Connection createConnection() {
        return super.createConnection()
                .data("ajax", "1")
                .data("type", building.id);
    }
}
