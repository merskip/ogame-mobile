package pl.merskip.ogamemobile.adapter.game;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test szczegółowych informacji o budynku
 */
public class BuildingDetailsTest extends PageTest {

    @Test
    public void testDetails() throws Exception {
        testSimpleBuilding();
        testCapacityBuilding();
        testResearch();
        testResearchWithEnergy();
        testShip();
    }

    private void testSimpleBuilding() throws Exception {
        BuildingDetails result = testBuildItem(new Building("1", "", 1));

        Assert.assertNotEquals(0, result.costMetal);
        Assert.assertNotEquals(0, result.costCrystal);
        Assert.assertEquals(0, result.costDeuterium);
        Assert.assertEquals(0, result.costEnergy);
    }

    private void testCapacityBuilding() throws Exception {
        BuildingDetails result = testBuildItem(new Building("22", "", 1));
        Assert.assertTrue(result.hasCapacity);
    }

    private void testResearch() throws Exception {
        BuildingDetails result = testBuildItem(new Building("113", "", 1), "research");

        Assert.assertEquals(0, result.costMetal);
        Assert.assertNotEquals(0, result.costCrystal);
        Assert.assertNotEquals(0, result.costDeuterium);
        Assert.assertEquals(0, result.costEnergy);
    }

    private void testResearchWithEnergy() throws Exception {
        BuildingDetails result = testBuildItem(new Building("199", "", 1));

        Assert.assertEquals(0, result.costMetal);
        Assert.assertEquals(0, result.costCrystal);
        Assert.assertEquals(0, result.costDeuterium);
        Assert.assertNotEquals(0, result.costEnergy);
    }

    private void testShip() throws Exception {
        BuildingDetails result = testBuildItem(new Building("204", "", 1));

        Assert.assertNotEquals(0, result.costMetal);
        Assert.assertNotEquals(0, result.costCrystal);
        Assert.assertEquals(0, result.costDeuterium);
        Assert.assertEquals(0, result.costEnergy);
        Assert.assertTrue(result.hasAmountBuild);
    }

    private BuildingDetails testBuildItem(Building building) throws Exception {
        return testBuildItem(building, "resources");
    }

    private BuildingDetails testBuildItem(Building building, String page) throws Exception {
        RequestPage request = new BuildingDetailsRequest(auth, page, "", building);
        ResultPage result = new BuildingDetailsResult().createFromRequest(request);

        BuildingDetails details = (BuildingDetails) result.getResult();
        boolean hasExtraInfo = hasExtraInfo(details);

        System.out.printf("\n%s\n", details.name);
        System.out.printf(" - id: %s\n", details.id);
        System.out.printf(" - level: %s\n", details.level);
        System.out.printf(" - description: %s...\n", details.description.substring(0, 32));
        System.out.printf(" - buildTime: %s\n", details.buildTime);
        System.out.printf(" - cost: m=%d c=%d d=%d e=%d\n",
                details.costMetal, details.costCrystal, details.costDeuterium, details.costEnergy);
        if (hasExtraInfo) {
            System.out.printf(" - extraInfo: %s: %s\n",
                    details.extraInfoLabel, details.extraInfoValue);
        }
        if (details.hasCapacity) {
            System.out.printf(" - capacity: %s / %d\n",
                    details.actualCapacity, details.storageCapacity);
        }
        if (details.hasAmountBuild) {
            System.out.printf(" - build amount: %s\n",
                    details.isActiveBuild ? "ready" : "disabled");
        }

        if (details.canAbort) {
            System.out.printf(" - can abort, listId=%s\n",
                    details.abortListId);
        }

        Assert.assertNotNull(details.id);
        Assert.assertNotNull(details.name);
        Assert.assertNotNull(details.level);
        Assert.assertNotNull(details.description);
        Assert.assertNotNull(details.buildTime);

        Assert.assertNotEquals("", details.id);
        Assert.assertNotEquals("", details.name);
        Assert.assertNotEquals("", details.level);
        Assert.assertNotEquals("", details.description);
        Assert.assertNotEquals("", details.buildTime);

        if (hasExtraInfo) {
            Assert.assertNotNull(details.extraInfoLabel);
            Assert.assertNotNull(details.extraInfoValue);
        }

        return details;
    }

    private static boolean hasExtraInfo(BuildingDetails result) {
        return result.extraInfoLabel != null || result.extraInfoValue != null;
    }
}
