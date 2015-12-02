package pl.merskip.ogamemobile.game.pages.defence;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.Defense;
import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.build_items.GetBuildItemsPageTask;

/**
 * Pobieranie strony obrony
 */
public class GetDefenseTask extends GetBuildItemsPageTask {

    public GetDefenseTask(GameActivity activity) {
        super(activity);
    }

    @Override
    protected AbstractPage<List<BuildItem>> createDownloadPage() {
        return new Defense(auth);
    }
}
