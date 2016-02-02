package pl.merskip.ogamemobile.adapter.game.pages;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import pl.merskip.ogamemobile.adapter.ResultPageFactory;
import pl.merskip.ogamemobile.adapter.game.Building;
import pl.merskip.ogamemobile.adapter.game.PageTest;
import pl.merskip.ogamemobile.adapter.game.RequestPage;
import pl.merskip.ogamemobile.adapter.game.ResultPage;
import pl.merskip.ogamemobile.adapter.login.TestUser;

import static pl.merskip.ogamemobile.adapter.game.Building.BuildState.ReadyToBuild;
import static pl.merskip.ogamemobile.adapter.game.Building.BuildState.Upgrading;

/**
 * Pomocnicza klasa do testowania stron z budynkami
 */
public abstract class BuildingsPage extends PageTest {

    protected List<Building> buildingsList;

    @SuppressWarnings("unchecked")
    @Test
    public void testBuildItems() throws Exception {
        RequestPage request = new RequestPage(auth, getPageName(), TestUser.getPlanetId());
        ResultPage result = createResultPage().createFromRequest(request);
        buildingsList = (List<Building>) result.getResult();

        boolean isGoodCount = testItemsCount(buildingsList.size());
        Assert.assertTrue(isGoodCount);

        List<String> allowedIds = Arrays.asList(getAllowedIds());
        for (Building building : buildingsList) {
            printBuildItem(building);
            testAttributes(building);

            if (!allowedIds.contains(building.id)) {
                Assert.fail("List contains not allowed build item: "
                        + "id=" + building.id + ", name=" + building.name);
            }
        }
    }

    protected abstract String getPageName();

    protected ResultPage createResultPage() {
        return ResultPageFactory.getResultPage(getPageName());
    }

    /**
     * Metoda sprawdzająca ilość pobranych budynków
     */
    protected abstract boolean testItemsCount(int count);

    protected abstract String[] getAllowedIds();

    private void testAttributes(Building building) {
        Assert.assertNotNull(building.id);
        Assert.assertNotNull(building.name);
        Assert.assertNotNull(building.level);
        Assert.assertNotNull(building.buildState);

        Assert.assertNotEquals("", building.id);
        Assert.assertNotEquals("", building.name);
        Assert.assertNotEquals("", building.level);

        int id = Integer.parseInt(building.id);
        boolean isBuilding = id > 0 && id < 200;

        if (isBuilding && building.buildState == ReadyToBuild )
            Assert.assertNotNull(building.buildRequestUrl);
        else
            Assert.assertNull(building.buildRequestUrl);

        if (building.buildState == Upgrading) {
            Assert.assertNotNull(building.buildProgress);
            Assert.assertNotEquals(0, building.buildProgress.finishTime);
            Assert.assertNotEquals(0, building.buildProgress.totalSeconds);
        } else
            Assert.assertNull(building.buildProgress);
    }

    private void printBuildItem(Building building) {
        System.out.printf("\n * %s (%d)\n", building.name, building.level);
        System.out.printf("\t- id: %s\n", building.id);
        System.out.printf("\t- build state: %s\n", building.buildState);
        System.out.printf("\t- fast build: %s\n", building.buildRequestUrl);
        if (building.buildProgress != null) {
            System.out.printf("\t- build progress: totalSeconds=%d, finishTime=%d\n",
                    building.buildProgress.totalSeconds, building.buildProgress.finishTime);
        } else {
            System.out.printf("\t- build progress: null\n");
        }
    }
}
