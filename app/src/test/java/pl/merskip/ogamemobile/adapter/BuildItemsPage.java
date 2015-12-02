package pl.merskip.ogamemobile.adapter;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.BuildItem;

import static pl.merskip.ogamemobile.adapter.pages.BuildItem.BuildState.ReadyToBuild;
import static pl.merskip.ogamemobile.adapter.pages.BuildItem.BuildState.Upgrading;

/**
 * Pomocnicza klasa do testowania stron z budynkami
 */
public abstract class BuildItemsPage extends PageTest {

    protected List<BuildItem> buildItems;

    @Test
    public void testBuildItems() throws Exception {
        AbstractPage<List<BuildItem>> downloadPage = createDownloadPage();
        buildItems = downloadPage.download();

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

    protected abstract AbstractPage<List<BuildItem>> createDownloadPage();

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
