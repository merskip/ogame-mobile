package pl.merskip.ogamemobile.game.pages.build_items;

import android.os.Bundle;

import java.io.Serializable;
import java.util.List;

import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.pages.ViewerPage;

/**
 * Pobieranie strony z pozycjami budowania
 */
public class BuildItemsViewer extends ViewerPage {

    public BuildItemsViewer(GameActivity activity) {
        super(activity);
    }

    @Override
    public void show(Object o) {
        List buildItems = (List) o;

        Bundle args = new Bundle();

        BuildItemsFragment fragment = new BuildItemsFragment();
        args.putSerializable("build-items", (Serializable) buildItems);
        fragment.setArguments(args);

        showFragment(fragment);
    }
}
