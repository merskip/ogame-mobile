package pl.merskip.ogamemobile.adapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Podsumowanie ilości surowców na planecie, takich jak:
 * metal, kryształ, deuter i energia.
 *
 * Antymateria nie jest uwzględniania.
 */
public class ResourcesSummary {

    public ResourceItem metal;
    public ResourceItem crystal;
    public ResourceItem deuterium;
    public ResourceItem energy;

    public static ResourcesSummary fromScriptData(ScriptData scriptData) {
        String contentFunction = getContentOfFunction(scriptData);

        ResourcesSummary resources = new ResourcesSummary();
        resources.metal = getResource(contentFunction, "metal");
        resources.crystal = getResource(contentFunction, "crystal");
        resources.deuterium = getResource(contentFunction, "deuterium");
        resources.energy = getResource(contentFunction, "energy");
        return resources;
    }

    private static String getContentOfFunction(ScriptData scriptData) {
        String regex = "function initAjaxResourcebox\\(\\)\\{(.*)\\}";
        Matcher matcher = Pattern.compile(regex).matcher(scriptData.content);

        if (matcher.find())
            return matcher.group(1);
        return null;
    }

    private static ResourceItem getResource(String contentFunction,
                                            String resourceName) {
        String regex = "\"" + resourceName + "\""
                + ".*?\"actual\":(.*?),(?:\"max\":(.*?),\"production\":(.*?)|.*?)\\}";
        Matcher matcher = Pattern.compile(regex).matcher(contentFunction);

        if (matcher.find()) {
            int actual = getIntegerOrZero(matcher.group(1));
            int storageCapacity = getIntegerOrZero(matcher.group(2));
            double production = getDoubleOrZero(matcher.group(3));

            return new ResourceItem(actual, storageCapacity, production);
        }
        return null;
    }

    private static int getIntegerOrZero(String value) {
        if (value != null)
            return Integer.parseInt(value);
        return 0;
    }

    private static double getDoubleOrZero(String value) {
        if (value != null)
            return Double.parseDouble(value);
        return 0.0;
    }
}
