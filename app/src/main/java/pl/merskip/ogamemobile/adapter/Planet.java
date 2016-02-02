package pl.merskip.ogamemobile.adapter;

import java.io.Serializable;

/**
 * Ogólne dane planety
 */
public class Planet implements Serializable {

    public String id;
    public String name;
    /** format [00:000:00] */
    public String coordinate;

    public boolean isBuild = false;
    public boolean isAlert = false;

    public String iconUrl;

    public Moon moon = null;

    public class Moon extends Planet {

        public Moon(String id, String name) {
            // Koordynaty księżyca są takie same jak planety macierzystej
            super(id, name, Planet.this.coordinate);
        }
    }

    public Planet(String id, String name, String coordinate) {
        this.id = id;
        this.name = name;
        this.coordinate = coordinate;
    }
}
