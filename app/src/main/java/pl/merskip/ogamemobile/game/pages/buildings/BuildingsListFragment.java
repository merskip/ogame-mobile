package pl.merskip.ogamemobile.game.pages.buildings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.game.Building;

public class BuildingsListFragment extends Fragment {

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buildings_list, container, false);

        Bundle args = getArguments();
        @SuppressWarnings("unchecked")
        List<Building> buildings = (List<Building>) args.getSerializable("build-items");

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        RecyclerView.Adapter adapter = new BuildingAdapter(context, buildings);
        recyclerView.setAdapter(adapter);

        return view;
    }

}

