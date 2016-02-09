package pl.merskip.ogamemobile.game.pages.fleet;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.game.pages.fleet.FleetShipsResult.Ship;

public class ShipAdapter extends BaseAdapter {

    private Context context;
    private List<Ship> ships;

    public ShipAdapter(Context context, List<Ship> shipList) {
        this.context = context;
        this.ships = shipList;
    }

    @Override
    public int getCount() {
        return ships.size();
    }

    @Override
    public Object getItem(int position) {
        return ships.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.fleet_ship_item, parent, false);
        }

        TextView shipNameView = (TextView) view.findViewById(R.id.ship_name);
        final EditText amountEdit = (EditText) view.findViewById(R.id.amount);
        final Button maxButton = (Button) view.findViewById(R.id.max_btn);

        final Ship ship = (Ship) getItem(position);

        shipNameView.setText(ship.name);
        amountEdit.setMaxEms(ship.max);
        maxButton.setText(String.valueOf(ship.max));
        maxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maxStr = String.valueOf(ship.max);
                if (!amountEdit.getText().toString().equals(maxStr)) // Jest już maks ustawiony
                    amountEdit.setText(maxStr);
                else
                    amountEdit.setText("");

                amountEdit.requestFocus();
                amountEdit.setSelection(amountEdit.getText().length());
            }
        });

        if (ship.amount > 0)
            amountEdit.setText(String.valueOf(ship.amount));
        else
            amountEdit.setText("");

        amountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    ship.amount = Integer.parseInt(s.toString());
                else
                    ship.amount = 0;
            }
        });

        return view;
    }
}