package pl.merskip.ogamemobile.adapter.game;

import org.jsoup.Connection;

import pl.merskip.ogamemobile.adapter.login.AuthorizationData;

/**
 * Żądanie przerwania budowania
 */
public class AbortBuildRequest extends RequestPage {

    private Building building;
    private String abortToken;
    private String abortListId;

    public AbortBuildRequest(AuthorizationData auth, String page, String planetId,
                             Building building, String abortToken, String abortListId) {
        super(auth, page, planetId);
        this.building = building;
        this.abortToken = abortToken;
        this.abortListId = abortListId;
    }

    @Override
    protected Connection createConnection() {
        return super.createConnection()
                .data("token", abortToken)
                .data("modus", "2")
                .data("techid", building.id)
                .data("listid", abortListId);
    }
}
