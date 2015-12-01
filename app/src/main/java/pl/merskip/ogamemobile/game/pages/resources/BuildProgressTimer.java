package pl.merskip.ogamemobile.game.pages.resources;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.game.Utils;

/**
 * Odmierza czas do końca budowy
 */
public class BuildProgressTimer extends CountDownTimer {

    private BuildItem.BuildProgress buildProgress;

    private TextView remainingLabelView;
    private TextView remainingTimeView;
    private ProgressBar progressBar;

    public BuildProgressTimer(BuildItem.BuildProgress buildProgress, View progressLayout) {
        super(buildProgress.finishTime - System.currentTimeMillis(), 1000);
        this.buildProgress = buildProgress;

        remainingLabelView = (TextView) progressLayout.findViewById(R.id.remaining_label);
        remainingTimeView = (TextView) progressLayout.findViewById(R.id.remaining);
        progressBar = (ProgressBar) progressLayout.findViewById(R.id.progress);

        long remainingMillis = buildProgress.finishTime - System.currentTimeMillis();
        int remainingSeconds = (int) (remainingMillis / 1000);
        remainingTimeView.setText(Utils.timeCountdownConvert(remainingSeconds));
        progressBar.setMax(buildProgress.totalSeconds);
        progressBar.setProgress(buildProgress.totalSeconds - remainingSeconds);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        int remainingSeconds = (int) (millisUntilFinished / 1000);

        remainingTimeView.setText(Utils.timeCountdownConvert(remainingSeconds));
        progressBar.setProgress(buildProgress.totalSeconds - remainingSeconds);
    }

    @Override
    public void onFinish() {
        remainingLabelView.setText(R.string.done);
        remainingTimeView.setText("");
        progressBar.setMax(1);
        progressBar.setProgress(1);
    }
}
