package pl.merskip.ogamemobile.game.pages.fleet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.game.pages.fleet.FleetDetailsResult;
import pl.merskip.ogamemobile.adapter.game.pages.fleet.FleetSendResult;
import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.Utils;


public class FleetSendFragment extends Fragment implements TextWatcher {

    private GameActivity activity;

    private FleetSendResult.Set set;

    private EditText metalEdit;
    private EditText crystalEdit;
    private EditText deuteriumEdit;

    private TextView capacityText;
    private ProgressBar capacityBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (GameActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fleet_send, container, false);

        Bundle args = getArguments();
        set = (FleetSendResult.Set) args.getSerializable("data");
        if (set == null)
            return null;

        metalEdit = (EditText) view.findViewById(R.id.metal);
        crystalEdit = (EditText) view.findViewById(R.id.crystal);
        deuteriumEdit = (EditText) view.findViewById(R.id.deuterium);

        metalEdit.addTextChangedListener(this);
        crystalEdit.addTextChangedListener(this);
        deuteriumEdit.addTextChangedListener(this);

        capacityText = (TextView) view.findViewById(R.id.capacityText);
        capacityBar = (ProgressBar) view.findViewById(R.id.capacity);
        capacityBar.setMax(Integer.parseInt(set.capacity));

        return view;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateCapacity();
    }

    private void updateCapacity() {
        int metal = Integer.parseInt(metalEdit.getText().toString());
        int crystal = Integer.parseInt(crystalEdit.getText().toString());
        int deuterium = Integer.parseInt(deuteriumEdit.getText().toString());

        int usedCapacity = metal + crystal + deuterium;
        capacityBar.setProgress(usedCapacity);

        int maxCapacity = Integer.parseInt(set.capacity);
        String text = Utils.toPrettyText(usedCapacity)
                + " / " + Utils.toPrettyText(maxCapacity);
        capacityText.setText(text);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void afterTextChanged(Editable s) { }
}
