package pl.merskip.ogamemobile.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.merskip.ogamemobile.R;
import pl.merskip.ogamemobile.adapter.Planet;

/**
 * Adapter listy planet
 */
public class PlanetListAdapter extends BaseAdapter {

    private Context context;
    private List<Planet> planetList;

    private Map<String, Bitmap> icons;

    public PlanetListAdapter(Context context) {
        this.context = context;
        this.planetList = new LinkedList<>();
        this.icons = new HashMap<>();
    }

    public void setPlanetList(List<Planet> planetList) {
        this.planetList = new LinkedList<>();
        for (Planet planet : planetList) {
            this.planetList.add(planet);
            if (planet.moon != null)
                this.planetList.add(planet.moon);
        }
        notifyDataSetChanged();
    }

    public List<Planet> getPlanetList() {
        return planetList;
    }

    @Override
    public int getCount() {
        return planetList.size();
    }

    @Override
    public Object getItem(int position) {
        return planetList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.planet_item, parent, false);
        }

        TextView nameView = (TextView) view.findViewById(R.id.name);
        TextView coordinateView = (TextView) view.findViewById(R.id.coordinate);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);

        Planet planet = (Planet) getItem(position);

        nameView.setText(planet.name);
        coordinateView.setText(planet.coordinate);

        if (planet instanceof Planet.Moon) {
            nameView.setText(R.string.moon);
            iconView.setImageResource(R.drawable.ic_moon);
        } else {
            loadIcon(iconView, planet);
        }

        return view;
    }

    private void loadIcon(final ImageView iconView, final Planet planet) {
        if (icons.containsKey(planet.iconUrl)) {
            iconView.setImageBitmap(icons.get(planet.iconUrl));
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap icon = downloadIcon(planet);
                icons.put(planet.iconUrl, icon);
                Log.d("PlanetList", "Download icon fot planet: " + planet.name);

                iconView.post(new Runnable() {
                    @Override
                    public void run() {
                        iconView.setImageBitmap(icon);
                    }
                });
            }
        }).start();
    }

    private Bitmap downloadIcon(Planet planet) {
        try {
            URL url = new URL(planet.iconUrl);
            InputStream iconStream = url.openConnection().getInputStream();
            return BitmapFactory.decodeStream(iconStream);
        } catch (IOException e) {
            Log.e("PlanetList", "Error download icon: ", e);
            return null;
        }
    }
}
