package pl.merskip.ogamemobile.adapter;


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
}
