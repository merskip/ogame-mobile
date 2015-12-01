package pl.merskip.ogamemobile.game.buildings;

import android.os.Bundle;

import java.io.Serializable;
import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.game.DownloadPageTask;
import pl.merskip.ogamemobile.game.GameActivity;

/**
 * Pobieranie strony z pozycjami budowania
 */
public abstract class GetBuildItemsPageTask extends DownloadPageTask<List<BuildItem>> {

    public GetBuildItemsPageTask(GameActivity activity) {
        super(activity);
    }

    @Override
    protected void afterDownload(List<BuildItem> buildItems) {
        Bundle args = new Bundle();

        BuildItemsFragment fragment = new BuildItemsFragment();
        args.putSerializable("build-items", (Serializable) buildItems);
        fragment.setArguments(args);

        showFragment(fragment);
    }
}
