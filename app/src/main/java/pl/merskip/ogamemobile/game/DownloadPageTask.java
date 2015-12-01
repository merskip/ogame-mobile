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
import pl.merskip.ogamemobile.adapter.pages.AbstractPage;

/**
 * Asynchroniczne pobieranie i pokazanie strony
 */
abstract public class DownloadPageTask<Result> extends AsyncTask<Void, Void, Result> {

    protected GameActivity activity;
    protected AuthorizationData auth;
    private String pageName;
    private String customUrl;

    private AbstractPage<Result> downloadPage;

    private ProgressDialog progressDialog;
    private Exception exception = null;

    public DownloadPageTask(GameActivity activity) {
        this(activity, activity.getAuthorizationData());
    }

    public DownloadPageTask(GameActivity activity, AuthorizationData auth) {
        this.activity = activity;
        this.auth = auth;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
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
            downloadPage = createDownloadPage();
            if (customUrl != null)
                downloadPage.setCustomUrl(customUrl);

            return downloadPage.download();
        } catch (UnknownHostException e) {
            exception = e;
            return null;
        } catch (Exception e) {
            Log.e("DownloadPageTask", "Failed download page: ", e);
            exception = e;
            return null;
        }
    }

    protected abstract AbstractPage<Result> createDownloadPage();

    @Override
    protected void onPostExecute(Result result) {
        if (exception instanceof IOException) {
            Toast.makeText(activity, R.string.connection_error, Toast.LENGTH_LONG)
                    .show();
        }

        if (result != null) {
            activity.notifyDownloadPage(downloadPage);
            afterDownload(result);
        }

        progressDialog.dismiss();
    }

    protected abstract void afterDownload(Result result);

    protected void showFragment(Fragment fragment) {
        activity.showContentPage(pageName, fragment);
    }
}
