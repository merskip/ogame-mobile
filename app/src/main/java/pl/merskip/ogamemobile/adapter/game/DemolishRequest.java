package pl.merskip.ogamemobile.adapter.game;

import org.jsoup.Connection;

import pl.merskip.ogamemobile.adapter.login.AuthorizationData;

/**
 * Żądanie wyburzenia
 */
public class DemolishRequest extends RequestPage {

    private Building building;
    private String demolishToken;

    public DemolishRequest(AuthorizationData auth, String pageName, String planetId,
                           Building building, String demolishToken) {
        super(auth, pageName, planetId);
        this.building = building;
        this.demolishToken = demolishToken;
    }

    @Override
    protected Connection createConnection() {
        return super.createConnection()
                .data("token", demolishToken)
                .data("modus", "3")
                .data("type", building.id);
    }
}
