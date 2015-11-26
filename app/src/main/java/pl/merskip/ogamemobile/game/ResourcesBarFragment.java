package pl.merskip.ogamemobile.game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.ResourceItem;
import pl.merskip.ogamemobile.adapter.ResourcesSummary;
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
        ResourcesSummary resources = ResourcesSummary.fromScriptData(scriptData);
        setResources(resources);
    }

    public void setResources(ResourcesSummary resources) {
        String metal = Utils.toPrettyText(resources.metal.actual);
        String crystal = Utils.toPrettyText(resources.crystal.actual);
        String deuterium = Utils.toPrettyText(resources.deuterium.actual);
        String energy = Utils.toPrettyText(resources.energy.actual);

        metalView.setText(metal);
        crystalView.setText(crystal);
        deuteriumView.setText(deuterium);
        energyView.setText(energy);

        metalView.setTextColor(getColorOfResource(resources.metal));
        crystalView.setTextColor(getColorOfResource(resources.crystal));
        deuteriumView.setTextColor(getColorOfResource(resources.deuterium));
        energyView.setTextColor(getColorOfResource(resources.energy));

        Log.d("ResourcesBar", "Set resources: "
                + "metal=" + metal
                + ", crystal=" + crystal
                + ", deuterium=" + deuterium
                + ", energy=" + energy);
    }

    private int getColorOfResource(ResourceItem resource) {
        int colorId = R.color.resource_normal;
        switch (resource.fillState) {
            case Normal: colorId = R.color.resource_normal;
                break;
            case Middle: colorId = R.color.resource_middle;
                break;
            case Overflow: colorId = R.color.resource_overflow;
                break;
        }
        return ContextCompat.getColor(activity, colorId);
    }

}
