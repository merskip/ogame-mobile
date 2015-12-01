package pl.merskip.ogamemobile.game.buildings;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.BuildItem.BuildState;
import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.Utils;

/**
 * Adapter pozycji budowania
 */
public class BuildItemAdapter extends RecyclerView.Adapter<BuildItemAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context context;
        private BuildItem buildItem;

        public ImageView iconView;
        public TextView nameView;
        public TextView levelView;
        public TextView buildStateView;
        public ImageButton buildButton;

        public BuildProgressTimer timer;
        public ViewGroup buildProgressLayout;

        public ViewHolder(View view) {
            super(view);
            this.context = view.getContext();

            iconView = (ImageView) view.findViewById(R.id.icon);
            nameView = (TextView) view.findViewById(R.id.name);
            levelView = (TextView) view.findViewById(R.id.level);
            buildStateView = (TextView) view.findViewById(R.id.build_state);
            buildButton = (ImageButton) view.findViewById(R.id.build);
            buildProgressLayout = (ViewGroup) view.findViewById(R.id.build_progress);

            buildButton.setOnClickListener(this);
        }

        public void set(final BuildItem buildItem) {
            this.buildItem = buildItem;
            int iconId = getBuildItemIconId(buildItem);
            iconView.setImageResource(iconId);
            setGrayImageIfUnmetRequirements();

            nameView.setText(buildItem.name);
            levelView.setText(Utils.toPrettyText(buildItem.level));

            buildStateView.setText(getBuildStateText());
            buildButton.setVisibility(isCanBuild() ? View.VISIBLE : View.GONE);

            if (timer != null)
                timer.cancel();
            buildProgressLayout.setVisibility(View.GONE);

            if (buildItem.buildProgress != null) {
                buildProgressLayout.setVisibility(View.VISIBLE);

                timer = new BuildProgressTimer(buildItem.buildProgress, buildProgressLayout);
                timer.start();
            }
        }

        private void setGrayImageIfUnmetRequirements() {
            if (buildItem.buildState == BuildState.UnmetRequirements) {
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
            switch (buildItem.buildState) {
                case TooFewResources:
                    return context.getString(R.string.too_few_resources);
                case UnmetRequirements:
                    return context.getString(R.string.unmet_requirements);
                default:
                    return "";
            }
        }

        private boolean isCanBuild() {
            return buildItem.buildState == BuildState.ReadyToBuild
                    && buildItem.buildRequestUrl != null;
        }

        @Override
        public void onClick(View v) {
            if (v == buildButton) {
                build();
            }
        }

        private void build() {
            if (buildItem.buildState == BuildState.ReadyToBuild) {
                GameActivity activity = (GameActivity) context;
                activity.build(buildItem);
            }
        }
    }

    private static Map<String, String> iconsMap;

    private Context context;
    private List<BuildItem> buildItems;

    public BuildItemAdapter(Context context, List<BuildItem> buildItems) {
        this.context = context;
        this.buildItems = buildItems;

        if (iconsMap == null)
            iconsMap = Utils.getHashMapResource(context, R.xml.build_item_icons);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.build_item_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BuildItem buildItem = buildItems.get(position);
        holder.set(buildItem);
    }

    @Override
    public int getItemCount() {
        return buildItems.size();
    }

    public int getBuildItemIconId(BuildItem buildItem) {
        String drawableName = iconsMap.get(buildItem.id);
        if (drawableName == null)
            return R.drawable.metal_mine;
        String packageName = context.getPackageName();
        Resources resources  = context.getResources();
        return resources.getIdentifier(drawableName, "drawable", packageName);
    }

}
