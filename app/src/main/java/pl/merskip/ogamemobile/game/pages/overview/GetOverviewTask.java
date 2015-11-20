package pl.merskip.ogamemobile.game.pages.overview;

import android.os.Bundle;

import pl.merskip.ogamemobile.adapter.AuthorizationData;
import pl.merskip.ogamemobile.adapter.pages.Overview;
import pl.merskip.ogamemobile.adapter.pages.OverviewData;
import pl.merskip.ogamemobile.game.DownloadPageTask;
import pl.merskip.ogamemobile.game.GameActivity;

/**
 * Pobieranie strony podglądu
 */
public class GetOverviewTask extends DownloadPageTask<OverviewData> {

    public GetOverviewTask(GameActivity activity, AuthorizationData auth) {
        super(activity, auth);
    }

    @Override
    protected OverviewData getResult(AuthorizationData auth) throws Exception {
        return new Overview(auth).download();
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
