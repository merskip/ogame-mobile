package pl.merskip.ogamemobile.game.pages.buildings;

import android.os.Bundle;

import java.io.Serializable;
import java.util.List;

import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.pages.ViewerPage;

/**
 * Wyświetlanie strony z listą budynków
 */
public class BuildingsListViewer extends ViewerPage {

    public BuildingsListViewer(GameActivity activity) {
        super(activity);
    }

    @Override
    public void show(Object o) {
        List buildItems = (List) o;

        Bundle args = new Bundle();

        BuildingsListFragment fragment = new BuildingsListFragment();
        args.putSerializable("build-items", (Serializable) buildItems);
        fragment.setArguments(args);

        showFragment(fragment);
    }
}
