package pl.merskip.ogamemobile.adapter.pages;

import org.jsoup.Connection;

import pl.merskip.ogamemobile.game.GameActivity;

/**
 * Żądanie szczegółowych informacji o budynku
 */
public class BuildItemDetailsRequest extends RequestPage {

    protected BuildItem buildItem;

    public BuildItemDetailsRequest(GameActivity activity, BuildItem buildItem) {
        super(activity);
        this.buildItem = buildItem;
    }

    @Override
    protected Connection createConnection() {
        return super.createConnection()
                .data("ajax", "1")
                .data("type", buildItem.id);
    }
}
