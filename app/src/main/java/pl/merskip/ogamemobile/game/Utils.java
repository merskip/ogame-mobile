package pl.merskip.ogamemobile.game;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.DecimalFormat;

public class Utils {

    private Utils() {}

    public static void setupListViewSize(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null)
            return;

        int screenWidth = getScreenWidthInPixels(listView.getContext());

        int numberOfItems = adapter.getCount();
        int totalHeight = 0;
        for (int i = 0; i < numberOfItems; i++) {
            View listItem = adapter.getView(i, null, listView);

            int listViewWidth = screenWidth - listItem.getPaddingLeft() - listItem.getPaddingRight();
            int widthSpec = View.MeasureSpec.makeMeasureSpec(listViewWidth, View.MeasureSpec.AT_MOST);
            listItem.measure(widthSpec, 0);

            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private static int getScreenWidthInPixels(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static String toPrettyText(int value) {
        float number = value;
        String pattern = "###,###.#";
        DecimalFormat f = new DecimalFormat(pattern);

        String suffix = "";
        if (number >= 100000) {
            number /= 1000.0f;
            suffix = "k";
        }
        if (number >= 100000) {
            number /= 1000.0f;
            suffix = "M";
        }

        if (!suffix.isEmpty() && number < 1000) {
            f.setDecimalSeparatorAlwaysShown(true);
        } else {
            number = Math.round(number);
        }

        return f.format(number) + suffix;
    }
}
