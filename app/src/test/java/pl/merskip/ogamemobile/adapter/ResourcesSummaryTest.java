package pl.merskip.ogamemobile.adapter;

import junit.framework.Assert;

import org.junit.Test;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;

/**
 * Test ładnego formatowania liczb
 */
public class ResourcesSummaryTest extends PageTest {

    @Test
    public void testResourcesTest() throws Exception {
        AbstractPage<?> examplePage = getExamplePage();
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
}
