package pl.merskip.ogamemobile.game.pages.research;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.Research;
import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.buildings.GetBuildItemsPageTask;

/**
 * Pobieranie strony badań
 */
public class GetResearchTask extends GetBuildItemsPageTask {

    public GetResearchTask(GameActivity activity) {
        super(activity);
    }

    @Override
    protected AbstractPage<List<BuildItem>> createDownloadPage() {
        return new Research(auth);
    }
}
