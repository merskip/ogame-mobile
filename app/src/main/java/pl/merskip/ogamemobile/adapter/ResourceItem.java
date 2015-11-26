package pl.merskip.ogamemobile.adapter;


/**
 * Dane na temat pojedynczego zasobu na planecie
 */
public class ResourceItem {

    public static final float FILL_MIDDLE = 0.90f;
    public static final float FILL_OVERFLOW = 0.99f;

    public int actual;
    public int storageCapacity;
    public double production;

    public FillState fillState;

    public enum FillState {
        Normal,
        Middle,
        Overflow
    }

    public ResourceItem(int actual, int storageCapacity) {
        this(actual, storageCapacity, 0.0);
    }

    public ResourceItem(int actual, int storageCapacity, double production) {
        this.actual = actual;
        this.storageCapacity = storageCapacity;
        this.production = production;
        this.fillState = calculateFillState(actual, storageCapacity);
    }

    public static FillState calculateFillState(int actual, int storageCapacity) {
        /* Braki energi na planecie */
        if (actual < 0)
            return FillState.Overflow;

        /* Brak ograniczenia dla pojemności */
        if (storageCapacity == 0)
            return FillState.Normal;

        float filled = (float) actual / storageCapacity;
        if (filled >= FILL_OVERFLOW)
            return FillState.Overflow;
        else if (filled >= FILL_MIDDLE)
            return FillState.Middle;
        else
            return FillState.Normal;
    }
}
