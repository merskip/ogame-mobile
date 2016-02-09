package pl.merskip.ogamemobile.game.pages.fleet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.game.pages.fleet.FleetShipsResult;
import pl.merskip.ogamemobile.game.Utils;


public class FleetShipsFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private FleetShipsResult.Set set;
    private ShipAdapter adapter;

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
        set = (FleetShipsResult.Set) args.getSerializable("set");

        if (set == null)
            return null;

        adapter = new ShipAdapter(context, set.ships);

        ListView shipListView = (ListView) view.findViewById(R.id.ships_list);
        shipListView.setAdapter(adapter);
        Utils.setupListViewSize(shipListView);

        Button nextBtn = (Button) view.findViewById(R.id.next);
        nextBtn.setOnClickListener(this);

        Button selectAllBtn = (Button) view.findViewById(R.id.select_all);
        selectAllBtn.setOnClickListener(this);

        Button clearBtn = (Button) view.findViewById(R.id.clear);
        clearBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_all:
                selectAllShips();
                break;
            case R.id.clear:
                clearSelectedShips();
                break;
            case R.id.next:
                openNextFleetPage();
                break;
        }
    }

    private void selectAllShips() {
        for (FleetShipsResult.Ship ship : set.ships)
            ship.amount = ship.max;
        adapter.notifyDataSetChanged();
    }

    private void clearSelectedShips() {
        for (FleetShipsResult.Ship ship : set.ships)
            ship.amount = 0;
        adapter.notifyDataSetChanged();
    }

    private void openNextFleetPage() {
        logDebugShips();
    }

    private void logDebugShips() {
        String msg = "";
        for (FleetShipsResult.Ship ship : set.ships) {
            if (ship.amount > 0)
                msg += "\nam" + ship.id
                        + "=" + ship.amount
                        + " (" + ship.name + ")";
        }

        Log.d("FleetShips", "Send ships:" + msg);
    }
}
