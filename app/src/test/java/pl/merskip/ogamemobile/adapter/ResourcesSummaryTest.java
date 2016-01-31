package pl.merskip.ogamemobile.adapter;

import junit.framework.Assert;

import org.junit.Test;

import pl.merskip.ogamemobile.adapter.ResourceItem.FillState;
import pl.merskip.ogamemobile.adapter.pages.RequestPage;

import static pl.merskip.ogamemobile.adapter.ResourceItem.calculateFillState;

/**
 * Test ładnego formatowania liczb
 */
public class ResourcesSummaryTest extends PageTest {

    @Test
    public void testResourcesTest() throws Exception {
        RequestPage<?> examplePage = getExamplePage();
        ScriptData scriptData = examplePage.getScriptData();

        ResourcesSummary resources = ResourcesSummary.fromScriptData(scriptData);

        Assert.assertNotNull(resources);
        Assert.assertNotNull(resources.metal);
        Assert.assertNotNull(resources.crystal);
        Assert.assertNotNull(resources.deuterium);
        Assert.assertNotNull(resources.energy);

        System.out.println("\nResources:");
        printResource(resources.metal, "metal");
        printResource(resources.crystal, "crystal");
        printResource(resources.deuterium, "deuterium");
        printResource(resources.energy, "energy");
    }

    private void printResource(ResourceItem resource, String resourceName) {
        System.out.printf(" * %s: \n\tactual=%d,\n\tstorageCapacity=%d,\n\tproduction=%f\n",
                resourceName, resource.actual, resource.storageCapacity, resource.production);
    }

    @Test
    public void testResourceFillState() {
        int storageCapacity = 100;
        int normal = 10;
        int middle = (int) (ResourceItem.FILL_MIDDLE * 100.0f);
        int overflow = (int) (ResourceItem.FILL_OVERFLOW * 100.0f);
        int belowZero = -10;
        int withoutMax = 100;

        Assert.assertEquals(FillState.Normal, calculateFillState(normal, storageCapacity));
        Assert.assertEquals(FillState.Middle, calculateFillState(middle, storageCapacity));
        Assert.assertEquals(FillState.Overflow, calculateFillState(overflow, storageCapacity));
        Assert.assertEquals(FillState.Overflow, calculateFillState(belowZero, storageCapacity));
        Assert.assertEquals(FillState.Normal, calculateFillState(withoutMax, 0));

    }
}
