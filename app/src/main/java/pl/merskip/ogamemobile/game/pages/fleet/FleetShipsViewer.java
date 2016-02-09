package pl.merskip.ogamemobile.game.pages.fleet;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import pl.merskip.ogamemobile.adapter.game.pages.fleet.FleetShipsResult;
import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.pages.ViewerPage;


public class FleetShipsViewer extends ViewerPage<FleetShipsResult.Set> {

    public FleetShipsViewer(GameActivity activity) {
        super(activity);
    }

    @Override
    public void show(FleetShipsResult.Set set) {

        Bundle args = new Bundle();
        args.putSerializable("set", set);

        Fragment fragment = new FleetShipsFragment();
        fragment.setArguments(args);

        showFragment(fragment);
    }
}
