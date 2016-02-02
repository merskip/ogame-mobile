package pl.merskip.ogamemobile.adapter.game;

import org.jsoup.Connection;

import pl.merskip.ogamemobile.adapter.login.AuthorizationData;

/**
 * Żądanie zbudowania floty lub obrony
 */
public class AmountBuildRequest extends RequestPage {

    private Building building;
    private String token;
    private int amount;

    public AmountBuildRequest(AuthorizationData auth, String page, String planetId,
                              Building building, String token, int amount) {
        super(auth, page, planetId);
        this.building = building;
        this.token = token;
        this.amount = amount;
    }

    @Override
    protected Connection createConnection() {
        return super.createConnection()
                .method(Connection.Method.POST)
                .data("token", token)
                .data("modus", "1")
                .data("type", building.id)
                .data("menge", String.valueOf(amount));
    }
}
