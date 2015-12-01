package pl.merskip.ogamemobile.game;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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



    public static Map<String, String> getHashMapResource(Context c, int hashMapResId) {
        Map<String, String> map = null;
        XmlResourceParser parser = c.getResources().getXml(hashMapResId);

        String key = null, value = null;

        try {
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("map")) {
                        boolean isLinked = parser.getAttributeBooleanValue(null, "linked", false);

                        map = isLinked
                                ? new LinkedHashMap<String, String>()
                                : new HashMap<String, String>();
                    }
                    else if (parser.getName().equals("entry")) {
                        key = parser.getAttributeValue(null, "key");

                        if (null == key) {
                            parser.close();
                            return null;
                        }
                    }
                }
                else if (eventType == XmlPullParser.END_TAG) {
                    if (map == null)
                        throw new IllegalStateException("Map is not initialize");

                    if (parser.getName().equals("entry")) {
                        map.put(key, value);
                        key = null;
                        value = null;
                    }
                }
                else if (eventType == XmlPullParser.TEXT) {
                    if (null != key) {
                        value = parser.getText();
                    }
                }
                eventType = parser.next();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return map;
    }

    public static int SECONDS_IN_MINUTE = 60;
    public static int SECONDS_IN_HOUR = SECONDS_IN_MINUTE * 60;
    public static int SECONDS_IN_DAY = 24 * SECONDS_IN_HOUR;

    public static String timeCountdownConvert(int seconds) {
        int days = (int) TimeUnit.SECONDS.toDays(seconds);
        int hours = (int) (TimeUnit.SECONDS.toHours(seconds) - (days * 24));
        int totalMinutes = (int) TimeUnit.SECONDS.toMinutes(seconds);
        int minutes = (int) (totalMinutes - (TimeUnit.SECONDS.toHours(seconds) * 60));
        int secs = (int) (TimeUnit.SECONDS.toSeconds(seconds) - (totalMinutes * 60));

        StringBuilder time = new StringBuilder();

        if (days > 0) {
            time.append(days)
                    .append("d")
                    .append(" ");
        }

        if (hours > 0 || days > 0) {
            time.append(hours)
                    .append("h")
                    .append(" ");
        }

        if (seconds < SECONDS_IN_DAY - 1 && seconds >= SECONDS_IN_MINUTE) {
            time.append(minutes)
                    .append("m")
                    .append(" ");
        }

        if (seconds < SECONDS_IN_HOUR - 1) {
            if (secs < 10 && seconds >= 10)
                time.append("0");

            time.append(secs)
                    .append("s");
        }

        return time.toString();
    }
}
