package pl.merskip.ogamemobile.adapter.game;

import java.io.Serializable;

/**
 * Dane szczegółowych informacji o budynku
 */
public class BuildItemDetailsData implements Serializable {

    public BuildItem originBuildItem;

    public String id;
    public String name;
    public int level;
    public String description;


    public String buildTime;
    public int costMetal;
    public int costCrystal;
    public int costDeuterium;
    public int costEnergy;

    public String extraInfoLabel;
    public String extraInfoValue;

    public boolean hasCapacity = false;
    public int actualCapacity;
    public int storageCapacity;

    public boolean hasAmountBuild = false;
    public boolean isActiveBuild = false;

    public boolean canAbort = false;
    public String abortListId;

}
