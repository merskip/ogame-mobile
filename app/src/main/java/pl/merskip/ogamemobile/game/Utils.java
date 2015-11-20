package pl.merskip.ogamemobile.game;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utils {

    private Utils() {}

    public static void setupListViewSize(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null)
            return;

        int numberOfItems = adapter.getCount();

        // Get total height of all items.
        int totalItemsHeight = 0;
        for (int position = 0; position < numberOfItems; position++){
            View item = adapter.getView(position, null, listView);
            item.measure(0, 0);
            totalItemsHeight += item.getMeasuredHeight();
        }

        // Get total height of all item dividers.
        int totalDividersHeight = listView.getDividerHeight()
                * (numberOfItems - 1);

        // Set list height.
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalItemsHeight + totalDividersHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
