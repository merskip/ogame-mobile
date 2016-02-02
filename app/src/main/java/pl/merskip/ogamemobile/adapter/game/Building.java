package pl.merskip.ogamemobile.adapter.game;

import java.io.Serializable;

/**
 * Budynek, badanie, flota
 */
public class Building implements Serializable {

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

    public Building(String id, String name, int level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }
}
