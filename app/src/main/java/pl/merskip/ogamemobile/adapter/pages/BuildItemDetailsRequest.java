package pl.merskip.ogamemobile.adapter.pages;

import org.jsoup.Connection;

import pl.merskip.ogamemobile.adapter.AuthorizationData;

/**
 * Żądanie szczegółowych informacji o budynku
 */
public class BuildItemDetailsRequest extends RequestPage {

    protected BuildItem buildItem;

    public BuildItemDetailsRequest(AuthorizationData auth, String page, BuildItem buildItem) {
        super(auth, page);
        this.buildItem = buildItem;
    }

    public BuildItemDetailsRequest(AuthorizationData auth, String page, String planetId, BuildItem buildItem) {
        super(auth, page, planetId);
        this.buildItem = buildItem;
    }

    @Override
    protected Connection createConnection() {
        return super.createConnection()
                .data("ajax", "1")
                .data("type", buildItem.id);
    }
}
