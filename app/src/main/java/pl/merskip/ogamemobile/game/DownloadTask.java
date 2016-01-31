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

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.UnknownHostException;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.Login;
import pl.merskip.ogamemobile.adapter.pages.RequestPage;
import pl.merskip.ogamemobile.adapter.pages.RequestPage.UnexpectedLogoutException;
import pl.merskip.ogamemobile.adapter.pages.ResultPage;
import pl.merskip.ogamemobile.game.pages.ViewerPage;
import pl.merskip.ogamemobile.login.LoginActivity;

/**
 * Asynchroniczne pobieranie i pokazanie strony
 */
public class DownloadTask extends AsyncTask<Void, Void, Object> {

    protected GameActivity activity;

    private RequestPage requestPage;
    private ResultPage resultPage;
    private ViewerPage viewerPage;

    private ProgressDialog progressDialog;
    private Exception exception = null;

    public DownloadTask(GameActivity activity, RequestPage requestPage,
                        ResultPage resultPage, ViewerPage viewerPage) {
        this.activity = activity;
        this.requestPage = requestPage;
        this.resultPage = resultPage;
        this.viewerPage = viewerPage;
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
    protected Object doInBackground(Void... params) {
        try {
            Document document = requestPage.download();
            if (resultPage != null)
                return resultPage.createResult(document, requestPage);
            else
                return null;
        } catch (UnknownHostException e) {
            exception = e;
            return null;
        } catch (Exception e) {
            Log.e("DownloadTask", "Failed download page: ", e);
            exception = e;
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(Object result) {
        if (exception instanceof IOException)
            showNoInternetConnection();

        if (exception instanceof UnexpectedLogoutException)
            showUnexpectedLogout();

        if (result != null) {
            activity.notifyDownloadPage(resultPage);

            viewerPage.setRequestAndResultPage(requestPage, resultPage);
            viewerPage.show(result);
        }

        dismissBuildItemDetails();
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


    private void showNoInternetConnection() {
        Snackbar snackbar = Utils.createSnackbar(activity, R.string.no_internet_connection);

        final DownloadTask copyTask = getCopyTask();
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

    private void retryDownloadPage(DownloadTask downloadTask) {
        try {
            downloadTask.execute();
        } catch (Exception e) {
            Log.e("DownloadTask", "Error while create copy: ", e);
        }
    }

    private void retrySignIn() {
        Login.Data loginData = requestPage.getAuthorizationData().loginData;
        String pageName = requestPage.getPageName();
        String planetId = requestPage.getPlanetId();

        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("login-data", loginData);
        intent.putExtra("start-page", pageName);
        intent.putExtra("planet-id", planetId);
        activity.startActivity(intent);
        activity.finish();
    }

    protected DownloadTask getCopyTask() {
        return new DownloadTask(activity, requestPage, resultPage, viewerPage);
    }

}
