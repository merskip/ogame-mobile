package pl.merskip.ogamemobile.adapter.pages;

import java.io.Serializable;

/**
 * Pozycja budowania, takie jak budynki i badania
 */
public class BuildItem implements Serializable {

    public String id;
    public String name;
    public int level;

    public BuildState buildState;

    public enum BuildState {
        Upgrading,
        ReadyToBuild,
        TooFewResources,
        UnmetRequirements
    }

    public BuildItem(String id, String name, int level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }
}
