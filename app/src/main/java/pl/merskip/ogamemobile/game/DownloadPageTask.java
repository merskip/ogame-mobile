package pl.merskip.ogamemobile.game;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.UnknownHostException;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.AuthorizationData;

/**
 * Asynchroniczne pobieranie i pokazanie strony
 */
abstract public class DownloadPageTask<Result> extends AsyncTask<Void, Void, Result> {

    protected GameActivity activity;
    private AuthorizationData auth;

    private ProgressDialog progressDialog;
    private Exception exception = null;

    public DownloadPageTask(GameActivity activity) {
        this(activity, activity.getAuthorizationData());
    }

    public DownloadPageTask(GameActivity activity, AuthorizationData auth) {
        this.activity = activity;
        this.auth = auth;
    }

    @Override
    protected void onPreExecute() {
        createAndShowProgressDialog();
    }

    private void createAndShowProgressDialog() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getString(R.string.downloading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Result doInBackground(Void... params) {
        try {
            return getResult(auth);
        } catch (UnknownHostException e) {
            exception = e;
            return null;
        } catch (Exception e) {
            Log.e("DownloadPageTask", "Failed download page: ", e);
            exception = e;
            return null;
        }
    }

    protected abstract Result getResult(AuthorizationData auth) throws Exception;

    @Override
    protected void onPostExecute(Result result) {
        if (exception instanceof IOException) {
            Toast.makeText(activity, R.string.connection_error, Toast.LENGTH_LONG)
                    .show();
        }

        if (result != null) {
            afterDownload(result);
        }

        progressDialog.dismiss();
    }

    protected abstract void afterDownload(Result result);

    protected void showFragment(Fragment fragment) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
    }
}
