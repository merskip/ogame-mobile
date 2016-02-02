package pl.merskip.ogamemobile.adapter;

import org.junit.Assert;
import org.junit.Test;

import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.BuildItemDetailsData;
import pl.merskip.ogamemobile.adapter.pages.BuildItemDetailsRequest;
import pl.merskip.ogamemobile.adapter.pages.BuildItemDetailsResult;
import pl.merskip.ogamemobile.adapter.pages.RequestPage;
import pl.merskip.ogamemobile.adapter.pages.ResultPage;

/**
 * Test szczegółowych informacji o budynku
 */
public class BuildItemDetailsTest extends PageTest {

    @Test
    public void testDetails() throws Exception {
        testSimpleBuilding();
        testCapacityBuilding();
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

    private void testCapacityBuilding() throws Exception {
        BuildItemDetailsData result = testBuildItem(new BuildItem("22", "", 1));
        Assert.assertTrue(result.hasCapacity);
    }

    private void testResearch() throws Exception {
        BuildItemDetailsData result = testBuildItem(new BuildItem("113", "", 1), "research");

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
        Assert.assertTrue(result.hasAmountBuild);
    }

    private BuildItemDetailsData testBuildItem(BuildItem buildItem) throws Exception {
        return testBuildItem(buildItem, "resources");
    }

    private BuildItemDetailsData testBuildItem(BuildItem buildItem, String page) throws Exception {
        RequestPage request = new BuildItemDetailsRequest(auth, page, buildItem);
        ResultPage result = new BuildItemDetailsResult().createFromRequest(request);

        BuildItemDetailsData details = (BuildItemDetailsData) result.getResult();
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

    private static boolean hasExtraInfo(BuildItemDetailsData result) {
        return result.extraInfoLabel != null || result.extraInfoValue != null;
    }
}
