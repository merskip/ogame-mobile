package pl.merskip.ogamemobile.game.pages.overview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.pages.OverviewData;
import pl.merskip.ogamemobile.game.Utils;


public class OverviewFragment extends Fragment {

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        Bundle args = getArguments();
        OverviewData data = (OverviewData) args.getSerializable("data");

        InformationsAdapter adapter = new InformationsAdapter(context);

        if (data != null) {
            adapter.addInformation("Średnica:", data.planetDiameter);
            adapter.addInformation("Temperatura:", data.planetTemperature);
            adapter.addInformation("Koordynaty:", data.planetCoordinate);

            adapter.addInformation("Punkty:", data.playerScore);
            adapter.addInformation("Miejsce:", data.playerPosition);
            adapter.addInformation("Punkty honoru:", data.playerHonor);
        }

        ListView informationsList = (ListView) view.findViewById(R.id.informations);
        informationsList.setAdapter(adapter);
        Utils.setupListViewSize(informationsList);

        return view;
    }

}
