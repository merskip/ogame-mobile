package pl.merskip.ogamemobile.game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.ScriptData;

/**
 * Obsługa pasku z zasobami
 */
public class ResourcesBarFragment
        extends Fragment
        implements DownloadPageNotifier.DownloadPageListener {

    private GameActivity activity;

    private TextView metalView;
    private TextView crystalView;
    private TextView deuteriumView;
    private TextView energyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (GameActivity) getActivity();
        activity.addDownloadPageListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resources_bar, container, false);

        metalView = (TextView) view.findViewById(R.id.metal);
        crystalView = (TextView) view.findViewById(R.id.crystal);
        deuteriumView = (TextView) view.findViewById(R.id.deuterium);
        energyView = (TextView) view.findViewById(R.id.energy);

        return view;
    }

    @Override
    public void onDownloadPage(Document document, ScriptData scriptData) {
        String metal = document.getElementById("resources_metal").text();
        String crystal = document.getElementById("resources_crystal").text();
        String deuterium = document.getElementById("resources_deuterium").text();
        String energy = document.getElementById("resources_energy").text();

        metalView.setText(metal);
        crystalView.setText(crystal);
        deuteriumView.setText(deuterium);
        energyView.setText(energy);

        Log.d("ResourcesBar", "Set resources: "
                + "metal=" + metal
                + ", crystal=" + crystal
                + ", deuterium=" + deuterium
                + ", energy=" + energy);
    }


}
