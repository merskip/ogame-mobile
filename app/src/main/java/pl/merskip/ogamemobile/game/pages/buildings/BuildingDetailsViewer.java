package pl.merskip.ogamemobile.game.pages.buildings;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import java.io.Serializable;

import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.pages.ViewerPage;

/**
 * Wyświetlanie szczegółów o budynku
 */
public class BuildingDetailsViewer extends ViewerPage {

    public BuildingDetailsViewer(GameActivity activity) {
        super(activity);
    }

    @Override
    public void show(Object o) {
        DialogFragment dialog = new BuildingDetailsDialog();

        Bundle args = new Bundle();
        args.putSerializable("details-data", (Serializable) o);
        dialog.setArguments(args);

        FragmentManager fm = activity.getSupportFragmentManager();
        dialog.show(fm, "build-item-details");
    }
}
