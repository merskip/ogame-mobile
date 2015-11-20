package pl.merskip.ogamemobile.game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.AuthorizationData;
import pl.merskip.ogamemobile.game.pages.overview.GetOverviewTask;

public class GameActivity extends AppCompatActivity {

    private AuthorizationData auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        auth = (AuthorizationData) getIntent().getSerializableExtra("auth");
        if (auth == null) {
            Log.e("GameActivity", "Parameter 'auth' (AuthorizationData)" +
                    " is required to start GameActivity");
            finish();
        }

        new GetOverviewTask(this, auth).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                new GetOverviewTask(this, auth).execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public AuthorizationData getAuthorizationData() {
        return auth;
    }
}
