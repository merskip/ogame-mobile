package pl.merskip.ogamemobile.game.pages.fleet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.game.pages.fleet.FleetDetailsResult;
import pl.merskip.ogamemobile.game.GameActivity;


public class FleetDetailsFragment extends Fragment {

    private GameActivity activity;

    private FleetDetailsResult.Set set;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (GameActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fleet_details, container, false);

        Bundle args = getArguments();
        set = (FleetDetailsResult.Set) args.getSerializable("data");
        if (set == null)
            return null;

        EditText galaxyEdit = (EditText) view.findViewById(R.id.galaxy);
        EditText systemEdit = (EditText) view.findViewById(R.id.system);
        EditText positionEdit = (EditText) view.findViewById(R.id.position);

        galaxyEdit.setText(set.galaxy);
        systemEdit.setText(set.system);
        positionEdit.setText(set.position);

        final TextView speedText = (TextView) view.findViewById(R.id.speedText);
        SeekBar speedBar = (SeekBar) view.findViewById(R.id.speed);

        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = progress * 10 + "%";
                if (progress >= 1)
                    speedText.setText(text);
                else
                    seekBar.setProgress(1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        int speed = Integer.parseInt(set.speed);
        speedBar.setProgress(speed);

        return view;
    }

}
