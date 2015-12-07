package pl.merskip.ogamemobile.game.build_items;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.BuildItemDetails;
import pl.merskip.ogamemobile.adapter.pages.BuildItemDetailsData;
import pl.merskip.ogamemobile.game.DownloadPageTask;
import pl.merskip.ogamemobile.game.GameActivity;

/**
 * Pobieranie szczegółowych informacji o budynku
 */
public class GetBuildItemDetailsTask
        extends DownloadPageTask<BuildItemDetailsData> {

    private BuildItem buildItem;

    public GetBuildItemDetailsTask(GameActivity activity, BuildItem buildItem) {
        super(activity);
        this.buildItem = buildItem;
    }

    @Override
    protected AbstractPage<BuildItemDetailsData> createDownloadPage() {
        String currentPage = activity.getCurrentPage();
        return new BuildItemDetails(auth, currentPage, buildItem);
    }

    @Override
    protected DownloadPageTask<?> createCopyTask() throws Exception {
        return new GetBuildItemDetailsTask(activity, buildItem);
    }

    @Override
    protected void afterDownload(BuildItemDetailsData result) {

        Bundle args = new Bundle();
        args.putSerializable("details-data", result);

        DialogFragment alert = new BuildItemDetailsAlert();
        alert.setArguments(args);

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        alert.show(fragmentManager, "build-item-details");
    }
}
