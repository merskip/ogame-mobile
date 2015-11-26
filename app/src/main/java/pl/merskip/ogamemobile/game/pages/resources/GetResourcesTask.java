package pl.merskip.ogamemobile.game.pages.resources;

import android.os.Bundle;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.Resources;
import pl.merskip.ogamemobile.game.DownloadPageTask;
import pl.merskip.ogamemobile.game.GameActivity;

/**
 * Pobieranie strony zasobów
 */
public class GetResourcesTask extends DownloadPageTask<List<BuildItem>> {

    public GetResourcesTask(GameActivity activity) {
        super(activity);
    }

    @Override
    protected AbstractPage<List<BuildItem>> createDownloadPage() {
        return new Resources(auth);
    }

    @Override
    protected void afterDownload(List<BuildItem> buildItems) {
        Bundle args = new Bundle();

        ResourcesFragment fragment = new ResourcesFragment();
        fragment.setArguments(args);

        showFragment(fragment);
    }
}
