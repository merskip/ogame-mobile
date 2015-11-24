package pl.merskip.ogamemobile.game.pages.overview;

import android.os.Bundle;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.Overview;
import pl.merskip.ogamemobile.adapter.pages.OverviewData;
import pl.merskip.ogamemobile.game.DownloadPageTask;
import pl.merskip.ogamemobile.game.GameActivity;

/**
 * Pobieranie strony podglądu
 */
public class GetOverviewTask extends DownloadPageTask<OverviewData> {

    public GetOverviewTask(GameActivity activity) {
        super(activity);
    }

    @Override
    protected AbstractPage<OverviewData> createDownloadPage() {
        return new Overview(auth);
    }

    @Override
    protected void afterDownload(OverviewData data) {
        Bundle args = new Bundle();
        args.putSerializable("data", data);

        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(args);

        showFragment(fragment);
    }
}
