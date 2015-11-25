package pl.merskip.ogamemobile.adapter;

import java.text.DecimalFormat;

/**
 * Dane na temat pojedynczego zasobu na planecie
 */
public class ResourceItem {

    public int actual;
    public int storageCapacity;
    public double production;

    public ResourceItem(int actual, int storageCapacity, double production) {
        this.actual = actual;
        this.storageCapacity = storageCapacity;
        this.production = production;
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
