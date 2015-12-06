package pl.merskip.ogamemobile.game.build_items;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.ResourceItem;
import pl.merskip.ogamemobile.adapter.ResourcesSummary;
import pl.merskip.ogamemobile.adapter.pages.BuildItemDetailsData;
import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.Utils;

/**
 * Alert ze szczegółami budynku
 */
public class BuildItemDetailsAlert extends DialogFragment {

    private View view;
    private GameActivity activity;
    private ResourcesSummary actualResources;

    private BuildItemDetailsData data;

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        activity = (GameActivity) getActivity();
        actualResources = activity.getActualResources();
        view = activity.getLayoutInflater().inflate(R.layout.build_item_details, null);

        Bundle args = getArguments();
        data = (BuildItemDetailsData) args.getSerializable("details-data");
        setupUI();

        builder.setTitle(data.name);
        builder.setView(view);
        return builder.create();
    }

    private void setupUI(){
        TextView levelView = (TextView) view.findViewById(R.id.level);
        TextView extraInfoView = (TextView) view.findViewById(R.id.extra_info);

        String levelText = activity.getString(R.string.level,
                "<font color='#ffffff'>" + data.level + "</font>");
        levelView.setText(Html.fromHtml(levelText));

        if (data.extraInfoLabel != null) {
            String extraInfo = String.format("%s: <font color='#ffffff'>%s</font>",
                    data.extraInfoLabel, data.extraInfoValue);
            extraInfoView.setText(Html.fromHtml(extraInfo));
            extraInfoView.setVisibility(View.VISIBLE);
        }

        setResourceCost(R.id.cost_metal, data.costMetal, actualResources.metal);
        setResourceCost(R.id.cost_crystal, data.costCrystal, actualResources.crystal);
        setResourceCost(R.id.cost_deuterium, data.costDeuterium, actualResources.deuterium);
        setResourceCost(R.id.cost_energy, data.costEnergy, actualResources.energy);

        setupCapacity();
    }

    private void setResourceCost(int viewId, int cost, ResourceItem resource) {
        TextView costView = (TextView) view.findViewById(viewId);

        costView.setVisibility(cost != 0 ? View.VISIBLE : View.GONE);
        costView.setText(Utils.toPrettyText(cost));

        int difference = resource.actual - cost;
        int differenceColorId = difference >= 0
                ? R.color.cost_enough
                : R.color.cost_not_enough;
        char diffSign = difference >= 0 ? '+' : '-';

        int costColorId = cost <= resource.storageCapacity
                ? R.color.resource_normal
                : R.color.resource_overflow;

        int secondsEnough = getSecondsToEnough(resource, cost);

        String costFormat = (secondsEnough <= 0)
                ? "<font color='%s'>%s</font> (<font color='%s'>%c%s</font>)"
                : "<font color='%s'>%s</font> (<font color='%s'>%c%s</font>, %s)";

        String costHtml =
                String.format(costFormat,
                        colorIdAsHex(costColorId), toPrettyText(cost),
                        colorIdAsHex(differenceColorId), diffSign, toPrettyText(difference),
                        Utils.timeCountdownConvert(secondsEnough));

        costView.setText(Html.fromHtml(costHtml));
    }

    private void setupCapacity() {
        if (data.hasCapacity) {
            View capacityLayout = view.findViewById(R.id.capacity_layout);
            capacityLayout.setVisibility(View.VISIBLE);

            TextView actualView = (TextView) capacityLayout.findViewById(R.id.capacity);
            ProgressBar capacityBar = (ProgressBar) capacityLayout.findViewById(R.id.capacity_bar);
            TextView fillTimeView = (TextView) capacityLayout.findViewById(R.id.fill_time);

            String capacityText = toPrettyText(data.actualCapacity)
                    + " / " + toPrettyText(data.storageCapacity);
            actualView.setText(capacityText);
            capacityBar.setMax(data.storageCapacity);
            capacityBar.setProgress(data.actualCapacity);

            ResourceItem resource = getResourceForCapacity();
            if (resource != null) {
                int secondsToFull = getSecondsToEnough(resource, data.storageCapacity);
                fillTimeView.setText(Utils.timeCountdownConvert(secondsToFull));
            }
        }
    }

    private String colorIdAsHex(int colorId) {
        int color = ContextCompat.getColor(activity, colorId);
        return String.format("#%06x", 0xFFFFFF & color);
    }

    private static String toPrettyText(int value) {
        return new DecimalFormat("###,###").format(value);
    }

    private int getSecondsToEnough(ResourceItem resource, int value) {
        if (resource.production == 0)
            return 0;
        int difference = value - resource.actual;
        if (difference > 0)
            return (int) (difference / resource.production);
        return 0;
    }

    private ResourceItem getResourceForCapacity() {
        ResourcesSummary resources = activity.getActualResources();
        switch (data.id) {
            case "22": return resources.metal;
            case "23": return resources.crystal;
            case "24": return resources.deuterium;
            default: return null;
        }
    }
}
