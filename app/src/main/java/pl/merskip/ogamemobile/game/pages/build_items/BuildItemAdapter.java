package pl.merskip.ogamemobile.game.pages.build_items;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.game.BuildItem;
import pl.merskip.ogamemobile.adapter.game.BuildItem.BuildState;
import pl.merskip.ogamemobile.adapter.game.BuildItemDetailsRequest;
import pl.merskip.ogamemobile.adapter.game.BuildItemDetailsResult;
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
public class BuildItemAdapter extends RecyclerView.Adapter<BuildItemAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private GameActivity activity;
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

        public void set(final BuildItem buildItem) {
            this.buildItem = buildItem;
            Drawable icon = getBuildItemIcon(buildItem);
            iconView.setImageDrawable(icon);
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
            } else {
                showDetails();
            }
        }

        private void build() {
            if (buildItem.buildState == BuildState.ReadyToBuild) {
                GameActivity activity = (GameActivity) context;
                activity.build(buildItem);
            }
        }

        private void showDetails() {
            AuthorizationData auth = activity.getAuth();
            String page = activity.getCurrentPage();
            RequestPage requestPage = new BuildItemDetailsRequest(auth, page, buildItem);

            ResultPage resultPage = new BuildItemDetailsResult();
            ViewerPage viewerPage = new ViewerPage(activity) {
                @Override
                public void show(Object o) {
                    DialogFragment dialog = new BuildItemDetailsDialog();

                    Bundle args = new Bundle();
                    args.putSerializable("details-data", (Serializable) o);
                    dialog.setArguments(args);

                    FragmentManager fm = activity.getSupportFragmentManager();
                    dialog.show(fm, "build-item-details");
                }
            };
            new DownloadTask(activity, requestPage, resultPage, viewerPage).execute();
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

    public Drawable getBuildItemIcon(BuildItem buildItem) {
        String drawableFilename = iconsMap.get(buildItem.id);

        try {
            InputStream stream = context.getAssets().open(drawableFilename);
            return Drawable.createFromStream(stream, null);
        } catch (IOException e) {
            Log.e("BuildItem", "Failed load build item icon: ", e);
            return null;
        }
    }

}
