package pl.merskip.ogamemobile.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Map;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.ServerHost;
import pl.merskip.ogamemobile.adapter.UniversumList;

/**
 * Asynchroniczne pobieranie listy universum
 */
public class GetUniversumListTask extends AsyncTask<Void, Void, Map<String, String>> {

    private LoginActivity activity;
    private ServerHost server;

    private ProgressDialog progressDialog;
    private Exception exception = null;

    public GetUniversumListTask(LoginActivity activity, ServerHost server) {
        this.activity = activity;
        this.server = server;
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
    protected Map<String, String> doInBackground(Void... params) {

        try {
            UniversumList universumList = new UniversumList(server);
            return universumList.downloadUniversumList();
        } catch (IOException e) {
            Log.e("GetUniversumListTask", Log.getStackTraceString(e));
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Map<String, String> universumList) {

        if (exception instanceof IOException) {
            Toast.makeText(activity, R.string.connection_error, Toast.LENGTH_LONG).show();
        }

        if (universumList != null) {
            activity.setUniversumList(universumList);
        }

        progressDialog.dismiss();

        if (universumList == null && activity.isEmptyUniversumList()) {
            showAlertWithTryAgain();
        }
    }

    private void showAlertWithTryAgain() {
        AlertDialog alert =
                new AlertDialog.Builder(activity)
                .setMessage(R.string.get_universum_list_failed)
                .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new GetUniversumListTask(activity, server).execute();
                    }
                })
                .setCancelable(false)
                .create();

        alert.show();
    }
}
