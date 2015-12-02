package pl.merskip.ogamemobile.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.net.UnknownHostException;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.AuthorizationData;
import pl.merskip.ogamemobile.adapter.Login;
import pl.merskip.ogamemobile.game.GameActivity;

/**
 * Asynchroniczne logowanie
 */
public class LoginTask extends AsyncTask<Void, Void, AuthorizationData> {

    private Activity activity;
    private Login.Data loginData;

    private ProgressDialog progressDialog;
    private Exception exception = null;

    public LoginTask(Activity activity, Login.Data loginData) {
        this.activity = activity;
        this.loginData = loginData;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getString(R.string.login_in_progress));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected AuthorizationData doInBackground(Void... params) {
        try {
            Login login = new Login(loginData);
            return login.tryLogin();
        } catch (Login.FailedLoginException | UnknownHostException e) {
            exception = e;
        } catch (IOException e) {
            Log.e("LoginTask", "Failed login: ", e);
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(AuthorizationData auth) {

        if (exception instanceof IOException) {
            showNoInternetConnection();
        }

        if (exception instanceof Login.FailedLoginException) {
            showFailedLogin();
        }

        if (auth != null) {
            Intent intent = new Intent(activity, GameActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("auth", auth);
            intent.putExtras(bundle);

            activity.startActivity(intent);
            activity.finish();
        }

        progressDialog.dismiss();
    }

    private void showNoInternetConnection() {
        Snackbar
                .make(activity.findViewById(android.R.id.content),
                        R.string.no_internet_connection,
                        Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new LoginTask(activity, loginData).execute();
                    }
                })
                .show();
    }

    private void showFailedLogin() {
        Snackbar
                .make(activity.findViewById(android.R.id.content),
                        R.string.login_failed,
                        Snackbar.LENGTH_INDEFINITE)
                .show();
    }
}
