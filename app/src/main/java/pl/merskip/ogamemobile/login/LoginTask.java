package pl.merskip.ogamemobile.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
            Toast.makeText(activity, R.string.connection_error, Toast.LENGTH_LONG)
                    .show();
        }

        if (exception instanceof Login.FailedLoginException) {
            Toast.makeText(activity, R.string.login_failed, Toast.LENGTH_SHORT)
                    .show();
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
}
