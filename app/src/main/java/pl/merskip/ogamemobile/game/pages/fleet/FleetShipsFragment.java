package pl.merskip.ogamemobile.game.pages.fleet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.game.pages.fleet.FleetShipsResult;
import pl.merskip.ogamemobile.game.Utils;


public class FleetShipsFragment extends Fragment {

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fleet_ships, container, false);

        Bundle args = getArguments();
        FleetShipsResult.Set set =
                (FleetShipsResult.Set) args.getSerializable("set");

        if (set != null) {
            ShipAdapter adapter = new ShipAdapter(context, set.ships);

            ListView shipListView = (ListView) view.findViewById(R.id.ships_list);
            shipListView.setAdapter(adapter);
            Utils.setupListViewSize(shipListView);
        }

        return view;
    }

}
