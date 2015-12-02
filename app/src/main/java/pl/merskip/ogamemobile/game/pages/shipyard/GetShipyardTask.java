package pl.merskip.ogamemobile.game.pages.shipyard;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.Shipyard;
import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.build_items.GetBuildItemsPageTask;

/**
 * Pobieranie strony stoczni
 */
public class GetShipyardTask extends GetBuildItemsPageTask {

    public GetShipyardTask(GameActivity activity) {
        super(activity);
    }

    @Override
    protected AbstractPage<List<BuildItem>> createDownloadPage() {
        return new Shipyard(auth);
    }
}
