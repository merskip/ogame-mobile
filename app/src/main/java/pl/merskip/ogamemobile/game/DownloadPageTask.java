package pl.merskip.ogamemobile.game;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.net.UnknownHostException;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.AuthorizationData;
import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.AbstractPage.UnexpectedLogoutException;
import pl.merskip.ogamemobile.login.LoginActivity;

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
        if (exception instanceof IOException)
            showNoInternetConnection();

        if (exception instanceof UnexpectedLogoutException)
            showUnexpectedLogout();

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

    private void showNoInternetConnection() {
        Snackbar snackbar = Utils.createSnackbar(activity, R.string.no_internet_connection);
        snackbar.setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryDownloadPage();
            }
        });
        snackbar.show();
    }

    private void showUnexpectedLogout() {
        Snackbar snackbar = Utils.createSnackbar(activity, R.string.unexpected_logout);
        snackbar.setAction(R.string.login_retry, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrySignIn();
            }
        });
        snackbar.show();
    }

    private void retryDownloadPage() {
        try {
            DownloadPageTask<?> downloadTask = (DownloadPageTask<?>)
                    getClass()
                            .getConstructor(GameActivity.class)
                            .newInstance(activity);
            downloadTask.execute();
        } catch (Exception e) {
            Log.e("DownloadPageTask", "Error create new instance: ", e);
        }
    }

    private void retrySignIn() {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("login-data", auth.loginData);
        intent.putExtra("start-page", pageName);
        activity.startActivity(intent);
        activity.finish();
    }
}
