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

    @Test
    public void testPrettyText() {
        System.out.println("\nTest format text:");
        testValue("1",                1);
        testValue("12",              12);
        testValue("123",            123);
        testValue("1 234",         1234);
        testValue("12 345",       12345);
        testValue("123,4k",      123411);
        testValue("1 234k",     1234111);
        testValue("12 345k",   12345111);
        testValue("123,4M",   123411111);
        testValue("1 234M",  1234111111);
    }

    private void testValue(String excepted, int value) {
        // Zamiana twardej spacji na zwykłą
        String text = ResourceItem.toPrettyText(value).replaceAll("\\u00a0", " ");

        Assert.assertEquals(excepted, text);
        System.out.printf("%10d: %7s\n", value, text);
    }
}
