package pl.merskip.ogamemobile.game;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

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
    private String planetId;
    private String customUrl;
    private Map<String, String> customRequestData;

    private AbstractPage<Result> downloadPage;

    private ProgressDialog progressDialog;
    private Exception exception = null;

    public DownloadPageTask(GameActivity activity) {
        this(activity, activity.getAuthorizationData());
    }

    public DownloadPageTask(GameActivity activity, AuthorizationData auth) {
        this.activity = activity;
        this.auth = auth;
        this.customRequestData = new HashMap<>();
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public void setPlanetId(String planetId) {
        this.planetId = planetId;
    }

    public void setCustomUrl(String customUrl) {
        this.customUrl = customUrl;
    }

    public void addCustomData(String key, String value) {
        customRequestData.put(key, value);
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
            if (planetId != null)
                downloadPage.setPlanetId(planetId);
            downloadPage.setCustomRequestData(customRequestData);

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
            dismissBuildItemDetails();
            activity.notifyDownloadPage(downloadPage);
            afterDownload(result);
        }

        progressDialog.dismiss();
    }

    private void dismissBuildItemDetails() {
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag("build-item-details");
        if (fragment != null && fragment instanceof DialogFragment) {
            DialogFragment dialog = (DialogFragment) fragment;
            dialog.dismiss();
        }
    }

    protected abstract void afterDownload(Result result);

    protected void showFragment(Fragment fragment) {
        activity.showContentPage(pageName, fragment);
    }

    private void showNoInternetConnection() {
        Snackbar snackbar = Utils.createSnackbar(activity, R.string.no_internet_connection);

        final DownloadPageTask<?> copyTask = getCopyTaskOrNull();
        if (copyTask != null ) {
            snackbar.setAction(R.string.retry, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    retryDownloadPage(copyTask);
                }
            });
        } else {
            snackbar.setDuration(Snackbar.LENGTH_LONG);
        }
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

    private void retryDownloadPage(DownloadPageTask<?> downloadTask) {
        try {
            downloadTask.execute();
        } catch (Exception e) {
            Log.e("DownloadPageTask", "Error while create copy: ", e);
        }
    }

    private void retrySignIn() {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("login-data", auth.loginData);
        intent.putExtra("start-page", pageName);
        intent.putExtra("planet-id", planetId);
        activity.startActivity(intent);
        activity.finish();
    }

    protected DownloadPageTask<?> getCopyTaskOrNull() {
        try {
            return createCopyTask();
        } catch (Exception e) {
            Log.e("DownloadPageTask", "Error while create copy: ", e);
            return null;
        }
    }

    protected DownloadPageTask<?> createCopyTask() throws Exception {
        Constructor<?> defaultConstructor = getClass().getConstructor(GameActivity.class);
        return (DownloadPageTask<?>) defaultConstructor.newInstance(activity);
    }
}
