package pl.merskip.ogamemobile.game.pages.station;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.Station;
import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.build_items.GetBuildItemsPageTask;

/**
 * Pobieranie strony stacji
 */
public class GetStationTask extends GetBuildItemsPageTask {

    public GetStationTask(GameActivity activity) {
        super(activity);
    }

    @Override
    protected AbstractPage<List<BuildItem>> createDownloadPage() {
        return new Station(auth);
    }
}
