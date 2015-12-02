package pl.merskip.ogamemobile.adapter;

import org.junit.Assert;
import org.junit.Test;

import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.BuildItemDetails;
import pl.merskip.ogamemobile.adapter.pages.BuildItemDetailsData;

/**
 * Test szczegółowych informacji o budynku
 */
public class BuildItemDetailsTest extends PageTest {

    @Test
    public void testDetails() throws Exception {
        testSimpleBuilding();
        testBuildingThisCapacity();
        testResearch();
        testResearchWithEnergy();
        testShip();
    }

    private void testSimpleBuilding() throws Exception {
        BuildItemDetailsData result = testBuildItem(new BuildItem("1", "", 1));

        Assert.assertNotEquals(0, result.costMetal);
        Assert.assertNotEquals(0, result.costCrystal);
        Assert.assertEquals(0, result.costDeuterium);
        Assert.assertEquals(0, result.costEnergy);
    }

    private void testBuildingThisCapacity() throws Exception {
        BuildItemDetailsData result = testBuildItem(new BuildItem("22", "", 1));
        Assert.assertTrue(result.hasCapacity);
    }

    private void testResearch() throws Exception {
        BuildItemDetailsData result = testBuildItem(new BuildItem("113", "", 1));

        Assert.assertEquals(0, result.costMetal);
        Assert.assertNotEquals(0, result.costCrystal);
        Assert.assertNotEquals(0, result.costDeuterium);
        Assert.assertEquals(0, result.costEnergy);
    }

    private void testResearchWithEnergy() throws Exception {
        BuildItemDetailsData result = testBuildItem(new BuildItem("199", "", 1));

        Assert.assertEquals(0, result.costMetal);
        Assert.assertEquals(0, result.costCrystal);
        Assert.assertEquals(0, result.costDeuterium);
        Assert.assertNotEquals(0, result.costEnergy);
    }

    private void testShip() throws Exception {
        BuildItemDetailsData result = testBuildItem(new BuildItem("204", "", 1));

        Assert.assertNotEquals(0, result.costMetal);
        Assert.assertNotEquals(0, result.costCrystal);
        Assert.assertEquals(0, result.costDeuterium);
        Assert.assertEquals(0, result.costEnergy);
    }

    private BuildItemDetailsData testBuildItem(BuildItem buildItem) throws Exception {
        BuildItemDetails buildItemDetails =
                new BuildItemDetails(auth, "resources", buildItem);

        BuildItemDetailsData result = buildItemDetails.download();
        boolean hasExtraInfo = hasExtraInfo(result);

        System.out.printf("\n%s\n", result.name);
        System.out.printf(" - id: %s\n", result.id);
        System.out.printf(" - level: %s\n", result.level);
        System.out.printf(" - description: %s...\n", result.description.substring(0, 32));
        System.out.printf(" - buildTime: %s\n", result.buildTime);
        System.out.printf(" - cost: m=%d c=%d d=%d e=%d\n",
                result.costMetal, result.costCrystal, result.costDeuterium, result.costEnergy);
        if (hasExtraInfo) {
            System.out.printf(" - extraInfo: %s: %s\n",
                    result.extraInfoLabel, result.extraInfoValue);
        }
        if (result.hasCapacity) {
            System.out.printf(" - capacity: %s / %d\n",
                    result.actualCapacity, result.storageCapacity);
        }


        Assert.assertNotNull(result.id);
        Assert.assertNotNull(result.name);
        Assert.assertNotNull(result.level);
        Assert.assertNotNull(result.description);
        Assert.assertNotNull(result.buildTime);

        Assert.assertNotEquals("", result.id);
        Assert.assertNotEquals("", result.name);
        Assert.assertNotEquals("", result.level);
        Assert.assertNotEquals("", result.description);
        Assert.assertNotEquals("", result.buildTime);

        if (hasExtraInfo) {
            Assert.assertNotNull(result.extraInfoLabel);
            Assert.assertNotNull(result.extraInfoValue);
        }

        return result;
    }

    private static boolean hasExtraInfo(BuildItemDetailsData result) {
        return result.extraInfoLabel != null || result.extraInfoValue != null;
    }
}
