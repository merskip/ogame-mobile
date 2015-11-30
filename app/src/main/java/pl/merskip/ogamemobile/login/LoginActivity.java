package pl.merskip.ogamemobile.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.Login;
import pl.merskip.ogamemobile.adapter.ServerHost;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DEFAULT_SERVER_LANG = "pl";

    private ServerHost serverHost;

    private EditText loginEdit;
    private EditText passwordEdit;
    private Spinner universumSpinner;

    private Map<String, String> universumList;

    public LoginActivity() {
        this.serverHost = new ServerHost(DEFAULT_SERVER_LANG);
        this.universumList = new LinkedHashMap<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEdit = (EditText) findViewById(R.id.login);
        passwordEdit = (EditText) findViewById(R.id.password);
        universumSpinner = (Spinner) findViewById(R.id.universum);
        Button signInButton = (Button) findViewById(R.id.sign_in);
        signInButton.setOnClickListener(this);

        if (!loadUniversumList())
            downloadUniversumList();

        restoreLastLoginAndUniversum();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                downloadUniversumList();
                return true;
            case R.id.sign_in:
                singIn();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in:
                singIn();
        }
    }

    public void downloadUniversumList() {
        new GetUniversumListTask(this, serverHost).execute();
    }

    public void singIn() {
        Login.Data loginData = getLoginData();
        singIn(loginData);
    }

    public void singIn(Login.Data loginData) {
        new LoginTask(this, loginData).execute();
        Log.d("Login", "login=" + loginData.login + ", uniId=" + loginData.uniId);

        saveUserLoginAndUniversum();
    }

    private void saveUserLoginAndUniversum() {
        Login.Data loginData = getLoginData();
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(this).edit();

        editor.putString("last_login.login", loginData.login);
        editor.putString("last_login.uniId", loginData.uniId);
        editor.apply();

        Log.d("Login", "Saved as last login: login="
                + loginData.login + ", uniId=" +loginData.uniId);
    }

    private void restoreLastLoginAndUniversum() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String login = preferences.getString("last_login.login", null);
        String uniId = preferences.getString("last_login.uniId", null);
        Log.d("Login", "Restore from last: login="
                + login + ", uniId=" + uniId);

        if (login != null) {
            loginEdit.setText(login);
            if (!login.isEmpty())
                passwordEdit.requestFocus();
        }

        if (uniId != null) {
            int universumIndex = getIndexUniversumById(uniId);
            universumSpinner.setSelection(universumIndex, false);
        }
    }

    private Login.Data getLoginData() {
        Login.Data loginData = new Login.Data();
        loginData.server = serverHost;

        loginData.login = loginEdit.getText().toString();
        loginData.password = passwordEdit.getText().toString();
        String universumName = universumSpinner.getSelectedItem().toString();
        loginData.uniId = getUniversumIdByName(universumName);
        return loginData;
    }

    public boolean saveUniversumList() {
        try {
            FileOutputStream fileOut = openFileOutput("universum_map", Context.MODE_PRIVATE);
            ObjectOutputStream output = new ObjectOutputStream(fileOut);
            output.writeObject(universumList);

            output.close();
            fileOut.close();

            Log.d("UniversumList", "Saved " + universumList.size() + " universum to cache");
            return true;
        } catch (Exception e) {
            Log.e("UniversumList", Log.getStackTraceString(e));
            return false;
        }
    }

    public boolean loadUniversumList() {
        try {
            FileInputStream fileIn = openFileInput("universum_map");
            ObjectInputStream input = new ObjectInputStream(fileIn);
            Object readObject = input.readObject();

            input.close();
            fileIn.close();

            if (readObject instanceof HashMap) {
                @SuppressWarnings("unchecked")
                Map<String, String> universumList = (Map<String, String>) readObject;
                setUniversumList(universumList);

                Log.d("UniversumList", "Loaded " + universumList.size() + " universum from cache");
                return true;
            }
            return false;
        } catch (FileNotFoundException e) {
            Log.w("UniversumList", Log.getStackTraceString(e));
            return false;
        } catch (Exception e) {
            Log.e("UniversumList", Log.getStackTraceString(e));
            return false;
        }
    }

    public void setUniversumList(Map<String, String> universumList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Collection<String> universumNames = universumList.values();
        adapter.addAll(universumNames);

        universumSpinner.setAdapter(adapter);
        this.universumList = universumList;
    }

    public boolean isEmptyUniversumList() {
        return universumList.isEmpty();
    }

    public String getUniversumIdByName(String name) {
        for (Map.Entry<String, String> uni : universumList.entrySet()) {
            if (uni.getValue().equals(name))
                return uni.getKey();
        }
        return null;
    }

    /**
     * Pobiera numer indeksu universum o danym id
     * @return Numer indeksu lub -1 jeśli nie znaleziono id
     */
    public int getIndexUniversumById(String uniId) {
        if (universumList != null) {
            int index = 0;
            for (String key : universumList.keySet()) {
                if (key.equals(uniId))
                    return index;
                index++;
            }
        }
        return -1;
    }

}
