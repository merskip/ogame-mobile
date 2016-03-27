package pl.merskip.ogamemobile.game.pages.fleet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.game.RequestPage;
import pl.merskip.ogamemobile.adapter.game.ResultPage;
import pl.merskip.ogamemobile.adapter.game.pages.fleet.FleetDetailsResult;
import pl.merskip.ogamemobile.adapter.game.pages.fleet.FleetSendRequest;
import pl.merskip.ogamemobile.adapter.game.pages.fleet.FleetSendResult;
import pl.merskip.ogamemobile.adapter.login.AuthorizationData;
import pl.merskip.ogamemobile.game.DownloadTask;
import pl.merskip.ogamemobile.game.GameActivity;
import pl.merskip.ogamemobile.game.pages.SimpleFragmentViewer;
import pl.merskip.ogamemobile.game.pages.ViewerPage;


public class FleetDetailsFragment extends Fragment implements View.OnClickListener {

    private GameActivity activity;

    private FleetDetailsResult.Set set;

    private EditText galaxyEdit;
    private EditText systemEdit;
    private EditText positionEdit;

    private SeekBar speedBar;

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

        galaxyEdit = (EditText) view.findViewById(R.id.galaxy);
        systemEdit = (EditText) view.findViewById(R.id.system);
        positionEdit = (EditText) view.findViewById(R.id.position);

        galaxyEdit.setText(set.galaxy);
        systemEdit.setText(set.system);
        positionEdit.setText(set.position);

        final TextView speedText = (TextView) view.findViewById(R.id.speedText);
        speedBar = (SeekBar) view.findViewById(R.id.speed);

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
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        int speed = Integer.parseInt(set.speed);
        speedBar.setProgress(speed);

        Button nextBtn = (Button) view.findViewById(R.id.next);
        nextBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                openNextFleetPage();
                break;
        }
    }

    private void openNextFleetPage() {
        updateFromFields();

        AuthorizationData auth = activity.getAuth();
        RequestPage request = new FleetSendRequest(auth, set);
        ResultPage result = new FleetSendResult();
        ViewerPage viewer = new SimpleFragmentViewer(activity, new FleetSendFragment());

        new DownloadTask(activity, request, result, viewer).execute();
    }

    private void updateFromFields() {
        set.galaxy = galaxyEdit.getText().toString();
        set.system = systemEdit.getText().toString();
        set.position = positionEdit.getText().toString();

        set.speed = String.valueOf(speedBar.getProgress());
    }
}
