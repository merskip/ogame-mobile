package pl.merskip.ogamemobile.game.pages.resources;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.Resources;
import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.build_items.GetBuildItemsPageTask;

/**
 * Pobieranie strony zasobów
 */
public class GetResourcesTask extends GetBuildItemsPageTask {

    public GetResourcesTask(GameActivity activity) {
        super(activity);
    }

    @Override
    protected AbstractPage<List<BuildItem>> createDownloadPage() {
        return new Resources(auth);
    }
}
