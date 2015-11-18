package pl.merskip.ogamemobile.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import pl.merskip.ogamemobile.R;
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

        downloadUniversumList();
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
        String login = loginEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String universumName = universumSpinner.getSelectedItem().toString();
        String uniId = getUniversumIdByName(universumName);

        Log.d("Login", "login=" + login + ", uniId=" + uniId);
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
        for (Map.Entry uni : universumList.entrySet()) {
            if (uni.getValue().equals(name))
                return (String) uni.getKey();
        }
        return null;
    }
}
