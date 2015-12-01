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
    public String buildRequestUrl;
    public BuildProgress buildProgress;

    public enum BuildState {
        Upgrading,
        ReadyToBuild,
        TooFewResources,
        UnmetRequirements
    }

    public static class BuildProgress implements Serializable {
        public int totalSeconds;
        public long finishTime;
    }

    public BuildItem(String id, String name, int level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }
}
