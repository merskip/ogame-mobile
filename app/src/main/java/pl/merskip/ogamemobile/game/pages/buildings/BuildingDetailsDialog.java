package pl.merskip.ogamemobile.game.pages.buildings;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.ResourceItem;
import pl.merskip.ogamemobile.adapter.ResourcesSummary;
import pl.merskip.ogamemobile.adapter.game.BuildingDetails;
import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.Utils;

/**
 * Dialog ze szczegółami budynku
 */
public class BuildingDetailsDialog extends DialogFragment implements View.OnClickListener {

    private View view;
    private GameActivity activity;
    private ResourcesSummary actualResources;

    private List<String> moreOptions = new ArrayList<>(2);

    private BuildingDetails details;

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        activity = (GameActivity) getActivity();
        actualResources = activity.getActualResources();
        view = activity.getLayoutInflater().inflate(R.layout.build_item_details, null);

        Bundle args = getArguments();
        details = (BuildingDetails) args.getSerializable("details-data");
        setupUI();

        if (details.canAbort || details.canDemolish)
            builder.setNeutralButton(R.string.more, null);

        builder.setTitle(details.name);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        Button moreButton = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        if (moreButton != null) {
            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMoreOptionsDialog();
                }
            });
        }
    }

    private void showMoreOptionsDialog() {
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1);

        final String abortBuild = activity.getString(R.string.abort_build);
        final String demolish = activity.getString(R.string.demolish);

        if (details.canAbort)
            adapter.add(abortBuild);
        if (details.canDemolish)
            adapter.add(demolish);

        if (adapter.isEmpty())
            return;

        new AlertDialog.Builder(activity)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String option = adapter.getItem(which);
                        if (option.equals(abortBuild)) {
                            activity.abortBuild(details.originBuilding, details.abortListId);
                        } else if (option.equals(demolish)) {
                            activity.demolish(details.originBuilding);
                        } else {
                            Log.e("BuildingDetails", "Chosen unknown option: " + option);
                        }
                    }
                }).show();
    }

    private void setupUI(){
        TextView levelView = (TextView) view.findViewById(R.id.level);
        TextView extraInfoView = (TextView) view.findViewById(R.id.extra_info);

        String levelText = activity.getString(R.string.level,
                "<font color='#ffffff'>" + details.level + "</font>");
        levelView.setText(Html.fromHtml(levelText));

        if (details.extraInfoLabel != null) {
            String extraInfo = String.format("%s: <font color='#ffffff'>%s</font>",
                    details.extraInfoLabel, details.extraInfoValue);
            extraInfoView.setText(Html.fromHtml(extraInfo));
            extraInfoView.setVisibility(View.VISIBLE);
        }

        setResourceCost(R.id.cost_metal, details.costMetal, actualResources.metal);
        setResourceCost(R.id.cost_crystal, details.costCrystal, actualResources.crystal);
        setResourceCost(R.id.cost_deuterium, details.costDeuterium, actualResources.deuterium);
        setResourceCost(R.id.cost_energy, details.costEnergy, actualResources.energy);

        setupCapacity();
        setupAmountBuild();
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
        if (details.hasCapacity) {
            View capacityLayout = view.findViewById(R.id.capacity_layout);
            capacityLayout.setVisibility(View.VISIBLE);

            TextView actualView = (TextView) capacityLayout.findViewById(R.id.capacity);
            ProgressBar capacityBar = (ProgressBar) capacityLayout.findViewById(R.id.capacity_bar);
            TextView fillTimeView = (TextView) capacityLayout.findViewById(R.id.fill_time);

            String capacityText = toPrettyText(details.actualCapacity)
                    + " / " + toPrettyText(details.storageCapacity);
            actualView.setText(capacityText);
            capacityBar.setMax(details.storageCapacity);
            capacityBar.setProgress(details.actualCapacity);

            ResourceItem resource = getResourceForCapacity();
            if (resource != null) {
                int secondsToFull = getSecondsToEnough(resource, details.storageCapacity);
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
        switch (details.id) {
            case "22": return resources.metal;
            case "23": return resources.crystal;
            case "24": return resources.deuterium;
            default: return null;
        }
    }

    private void setupAmountBuild() {
        View amountBuildLayout = view.findViewById(R.id.amount_build_layout);
        amountBuildLayout.setVisibility(details.hasAmountBuild ? View.VISIBLE : View.GONE);
        if (details.hasAmountBuild) {
            EditText amountEdit = (EditText) view.findViewById(R.id.amount);
            int maxAmount = getMaxAmountBuild();
            amountEdit.setHint(Utils.toPrettyText(maxAmount));

            ImageButton buildButton = (ImageButton) view.findViewById(R.id.build);
            buildButton.setEnabled(details.isActiveBuild);
            amountEdit.setEnabled(details.isActiveBuild);

            Drawable buildIcon = buildButton.getDrawable();
            if (details.isActiveBuild)
                buildIcon.clearColorFilter();
            else
                buildIcon.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            buildButton.setImageDrawable(buildIcon);

            buildButton.setOnClickListener(this);
        }
    }

    private int getMaxAmountBuild() {
        List<Integer> maxAmount = new ArrayList<>(3);

        if (details.costMetal != 0)
            maxAmount.add(getMaxAmountForResource(actualResources.metal, details.costMetal));

        if (details.costCrystal != 0)
            maxAmount.add(getMaxAmountForResource(actualResources.crystal, details.costCrystal));

        if (details.costDeuterium != 0)
            maxAmount.add(getMaxAmountForResource(actualResources.deuterium, details.costDeuterium));

        if (details.costEnergy != 0)
            maxAmount.add(getMaxAmountForResource(actualResources.energy, details.costEnergy));

        return Collections.min(maxAmount);
    }

    private int getMaxAmountForResource(ResourceItem resource, int cost) {
        return (int) Math.floor(resource.actual / cost);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.build) {
            buildAmount();
        }
    }

    private void buildAmount() {
        EditText amountEdit = (EditText) view.findViewById(R.id.amount);
        String userAmount = amountEdit.getText().toString();
        if (userAmount.isEmpty())
            userAmount = amountEdit.getHint().toString();
        int amount = Integer.parseInt(userAmount);

        activity.buildAmount(details.originBuilding, amount);
    }

}
