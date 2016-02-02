package pl.merskip.ogamemobile.adapter.game.pages;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import pl.merskip.ogamemobile.adapter.ResultPageFactory;
import pl.merskip.ogamemobile.adapter.game.BuildItem;
import pl.merskip.ogamemobile.adapter.game.PageTest;
import pl.merskip.ogamemobile.adapter.game.RequestPage;
import pl.merskip.ogamemobile.adapter.game.ResultPage;
import pl.merskip.ogamemobile.adapter.login.TestUser;

import static pl.merskip.ogamemobile.adapter.game.BuildItem.BuildState.ReadyToBuild;
import static pl.merskip.ogamemobile.adapter.game.BuildItem.BuildState.Upgrading;

/**
 * Pomocnicza klasa do testowania stron z budynkami
 */
public abstract class BuildItemsPage extends PageTest {

    protected List<BuildItem> buildItems;

    @SuppressWarnings("unchecked")
    @Test
    public void testBuildItems() throws Exception {
        RequestPage request = new RequestPage(auth, getPageName(), TestUser.getPlanetId());
        ResultPage result = createResultPage().createFromRequest(request);
        buildItems = (List<BuildItem>) result.getResult();

        boolean isGoodCount = testItemsCount(buildItems.size());
        Assert.assertTrue(isGoodCount);

        List<String> allowedIds = Arrays.asList(getAllowedIds());
        for (BuildItem buildItem : buildItems) {
            printBuildItem(buildItem);
            testAttributes(buildItem);

            if (!allowedIds.contains(buildItem.id)) {
                Assert.fail("List contains not allowed build item: "
                        + "id=" + buildItem.id + ", name=" + buildItem.name);
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

    private void testAttributes(BuildItem buildItem) {
        Assert.assertNotNull(buildItem.id);
        Assert.assertNotNull(buildItem.name);
        Assert.assertNotNull(buildItem.level);
        Assert.assertNotNull(buildItem.buildState);

        Assert.assertNotEquals("", buildItem.id);
        Assert.assertNotEquals("", buildItem.name);
        Assert.assertNotEquals("", buildItem.level);

        int id = Integer.parseInt(buildItem.id);
        boolean isBuilding = id > 0 && id < 200;

        if (isBuilding && buildItem.buildState == ReadyToBuild )
            Assert.assertNotNull(buildItem.buildRequestUrl);
        else
            Assert.assertNull(buildItem.buildRequestUrl);

        if (buildItem.buildState == Upgrading) {
            Assert.assertNotNull(buildItem.buildProgress);
            Assert.assertNotEquals(0, buildItem.buildProgress.finishTime);
            Assert.assertNotEquals(0, buildItem.buildProgress.totalSeconds);
        } else
            Assert.assertNull(buildItem.buildProgress);
    }

    private void printBuildItem(BuildItem buildItem) {
        System.out.printf("\n * %s (%d)\n", buildItem.name, buildItem.level);
        System.out.printf("\t- id: %s\n", buildItem.id);
        System.out.printf("\t- build state: %s\n", buildItem.buildState);
        System.out.printf("\t- fast build: %s\n", buildItem.buildRequestUrl);
        if (buildItem.buildProgress != null) {
            System.out.printf("\t- build progress: totalSeconds=%d, finishTime=%d\n",
                    buildItem.buildProgress.totalSeconds, buildItem.buildProgress.finishTime);
        } else {
            System.out.printf("\t- build progress: null\n");
        }
    }
}
