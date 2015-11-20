package pl.merskip.ogamemobile.game.pages.overview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map;

import pl.merskip.ogamemobile.R;

/**
 * Adapter dla listy informacji
 */
public class InformationsAdapter extends BaseAdapter {

    private Context context;
    private Map<String, String> data;

    public InformationsAdapter(Context context) {
        this.context = context;
        this.data = new LinkedHashMap<>();
    }

    public void addInformation(String key, String value) {
        data.put(key, value);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        int i = 0;
        for (Map.Entry e : data.entrySet()) {
            if (i++ == position)
                return e;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.information_item, parent, false);
        }

        TextView keyView = (TextView) view.findViewById(R.id.key);
        TextView valueView = (TextView) view.findViewById(R.id.value);

        Map.Entry item = (Map.Entry) getItem(position);
        String key = (String) item.getKey();
        String value = (String) item.getValue();

        keyView.setText(key);
        valueView.setText(value);

        return view;
    }
}
