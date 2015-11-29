package pl.merskip.ogamemobile.game.pages.resources;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.game.Utils;

/**
 * Adapter pozycji budowania
 */
public class BuildItemAdapter extends RecyclerView.Adapter<BuildItemAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iconView;
        public TextView nameView;
        public TextView levelView;

        public ViewHolder(View view) {
            super(view);

            iconView = (ImageView) view.findViewById(R.id.icon);
            nameView = (TextView) view.findViewById(R.id.name);
            levelView = (TextView) view.findViewById(R.id.level);
        }

        public void set(BuildItem buildItem) {
            int iconId = getBuildItemIconId(buildItem);
            iconView.setImageResource(iconId);
            nameView.setText(buildItem.name);
            levelView.setText(Utils.toPrettyText(buildItem.level));
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
        String packageName = context.getPackageName();
        Resources resources  = context.getResources();
        return resources.getIdentifier(drawableName, "drawable", packageName);
    }

}
