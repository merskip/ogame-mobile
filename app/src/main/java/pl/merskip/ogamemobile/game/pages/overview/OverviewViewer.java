package pl.merskip.ogamemobile.game.pages.overview;

import android.os.Bundle;

import pl.merskip.ogamemobile.adapter.game.pages.OverviewData;
import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.pages.ViewerPage;

/**
 * Pobieranie strony podglądu
 */
public class OverviewViewer extends ViewerPage {

    public OverviewViewer(GameActivity activity) {
        super(activity);
    }

    @Override
    public void show(Object o) {
        OverviewData data = (OverviewData) o;

        Bundle args = new Bundle();
        args.putSerializable("data", data);

        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments(args);

        showFragment(fragment);
    }
}
