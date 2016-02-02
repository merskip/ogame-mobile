package pl.merskip.ogamemobile.game.pages.buildings;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.game.Building;
import pl.merskip.ogamemobile.adapter.game.Building.BuildState;
import pl.merskip.ogamemobile.adapter.game.BuildingDetailsRequest;
import pl.merskip.ogamemobile.adapter.game.BuildingDetailsResult;
import pl.merskip.ogamemobile.adapter.game.RequestPage;
import pl.merskip.ogamemobile.adapter.game.ResultPage;
import pl.merskip.ogamemobile.adapter.login.AuthorizationData;
import pl.merskip.ogamemobile.game.DownloadTask;
import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.Utils;
import pl.merskip.ogamemobile.game.pages.ViewerPage;

/**
 * Adapter pozycji budowania
 */
public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private GameActivity activity;
        private Building building;

        public ImageView iconView;
        public TextView nameView;
        public TextView levelView;
        public TextView buildStateView;
        public ImageButton buildButton;

        public BuildProgressTimer timer;
        public ViewGroup buildProgressLayout;

        public ViewHolder(View view) {
            super(view);
            this.activity = (GameActivity) view.getContext();

            iconView = (ImageView) view.findViewById(R.id.icon);
            nameView = (TextView) view.findViewById(R.id.name);
            levelView = (TextView) view.findViewById(R.id.level);
            buildStateView = (TextView) view.findViewById(R.id.build_state);
            buildButton = (ImageButton) view.findViewById(R.id.build);
            buildProgressLayout = (ViewGroup) view.findViewById(R.id.build_progress);

            buildButton.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        public void set(final Building building) {
            this.building = building;
            Drawable icon = getBuildItemIcon(building);
            iconView.setImageDrawable(icon);
            setGrayImageIfUnmetRequirements();

            nameView.setText(building.name);
            levelView.setText(Utils.toPrettyText(building.level));
            if (building.buildState == BuildState.Upgrading) {
                int textId;
                if (building.isNextLevelCount)
                    textId = R.string.level_build_count;
                else if (building.nextLevel > building.level)
                    textId = R.string.level_upgrade;
                else if (building.nextLevel < building.level)
                    textId = R.string.level_downgrade;
                else
                    textId = 0;

                if (textId != 0) {
                    String level = Utils.toPrettyText(building.level);
                    String nextLevel = Utils.toPrettyText(building.nextLevel);
                    String text = activity.getString(textId, level, nextLevel);
                    levelView.setText(Html.fromHtml(text));
                }
            }

            buildStateView.setText(getBuildStateText());
            buildButton.setVisibility(isCanBuild() ? View.VISIBLE : View.GONE);

            if (timer != null)
                timer.cancel();
            buildProgressLayout.setVisibility(View.GONE);

            if (building.buildProgress != null) {
                buildProgressLayout.setVisibility(View.VISIBLE);

                timer = new BuildProgressTimer(building.buildProgress, buildProgressLayout);
                timer.start();
            }
        }

        private void setGrayImageIfUnmetRequirements() {
            if (building.buildState == BuildState.UnmetRequirements) {
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                iconView.setColorFilter(new ColorMatrixColorFilter(matrix));
                iconView.setAlpha(0.5f);
                levelView.setAlpha(0.5f);
            } else {
                iconView.clearColorFilter();
                iconView.setAlpha(1.0f);
                levelView.setAlpha(1.0f);
            }
        }

        private String getBuildStateText() {
            switch (building.buildState) {
                case TooFewResources:
                    return context.getString(R.string.too_few_resources);
                case UnmetRequirements:
                    return context.getString(R.string.unmet_requirements);
                default:
                    return "";
            }
        }

        private boolean isCanBuild() {
            return building.buildState == BuildState.ReadyToBuild
                    && building.buildRequestUrl != null;
        }

        @Override
        public void onClick(View v) {
            if (v == buildButton) {
                build();
            } else {
                showDetails();
            }
        }

        private void build() {
            if (building.buildState == BuildState.ReadyToBuild) {
                GameActivity activity = (GameActivity) context;
                activity.build(building);
            }
        }

        private void showDetails() {
            AuthorizationData auth = activity.getAuth();
            String page = activity.getCurrentPage();
            String planetId = activity.getCurrentPlanet().id;

            RequestPage requestPage = new BuildingDetailsRequest(auth, page, planetId, building);
            ResultPage resultPage = new BuildingDetailsResult();
            ViewerPage viewerPage = new BuildingDetailsViewer(activity);
            new DownloadTask(activity, requestPage, resultPage, viewerPage).execute();
        }
    }

    private static Map<String, String> iconsMap;

    private Context context;
    private List<Building> buildings;

    public BuildingAdapter(Context context, List<Building> buildings) {
        this.context = context;
        this.buildings = buildings;

        if (iconsMap == null)
            iconsMap = Utils.getHashMapResource(context, R.xml.buildings_icons);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.buildings_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Building building = buildings.get(position);
        holder.set(building);
    }

    @Override
    public int getItemCount() {
        return buildings.size();
    }

    public Drawable getBuildItemIcon(Building building) {
        String drawableFilename = iconsMap.get(building.id);

        try {
            InputStream stream = context.getAssets().open(drawableFilename);
            return Drawable.createFromStream(stream, null);
        } catch (IOException e) {
            Log.e("Building", "Failed load build item icon: ", e);
            return null;
        }
    }

}
