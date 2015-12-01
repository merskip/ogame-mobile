package pl.merskip.ogamemobile.adapter;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import pl.merskip.ogamemobile.adapter.pages.BuildItem;
import pl.merskip.ogamemobile.adapter.pages.Resources;

/**
 * Test strony zasobów
 */
public class ResourcesTest extends PageTest {

    public static final int BUILDINGS_IN_RESOURCES = 9;

    @Test
    public void testPage() throws Exception {
        Resources resources = new Resources(auth);
        List<BuildItem> buildItems = resources.download();

        Assert.assertEquals(BUILDINGS_IN_RESOURCES, buildItems.size());

        System.out.println("\nBuildings:");

        for (BuildItem buildItem : buildItems) {

            Assert.assertNotEquals("", buildItem.id);
            Assert.assertNotEquals("", buildItem.name);
            Assert.assertNotNull(buildItem.buildState);
            if (buildItem.buildState != BuildItem.BuildState.ReadyToBuild)
                Assert.assertNull(buildItem.buildRequestUrl);
            if (buildItem.buildState == BuildItem.BuildState.Upgrading)
                Assert.assertNotNull(buildItem.buildProgress);

            System.out.printf("\n * %s (%d)\n", buildItem.name, buildItem.level);
            System.out.printf("\t- id: %s\n", buildItem.id);
            System.out.printf("\t- build state: %s\n", buildItem.buildState);
            System.out.printf("\t- fast build: %s\n", buildItem.buildRequestUrl);
            if (buildItem.buildProgress != null)
                System.out.printf("\t- build progress: totalSeconds=%d, finishTime=%d\n",
                        buildItem.buildProgress.totalSeconds, buildItem.buildProgress.finishTime);
        }
    }
}
