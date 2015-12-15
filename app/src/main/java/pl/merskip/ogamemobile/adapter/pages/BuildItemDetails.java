package pl.merskip.ogamemobile.adapter.pages;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pl.merskip.ogamemobile.adapter.AuthorizationData;

/**
 * Szczegółowe informacje o budowanej pozycji
 */
public class BuildItemDetails extends AbstractPage<BuildItemDetailsData> {

    private BuildItem buildItem;
    private BuildItemDetailsData result;

    public BuildItemDetails(AuthorizationData auth, String page, BuildItem buildItem) {
        super(auth, page);
        this.buildItem = buildItem;
    }

    @Override
    protected Connection createConnection() {
        return super.createConnection()
                .data("ajax", "1")
                .data("type", buildItem.id);
    }

    @Override
    public BuildItemDetailsData createResult(Document document) {
        result = new BuildItemDetailsData();
        result.originBuildItem = buildItem;

        appendBaseAttributes();
        appendCosts();
        appendExtraInfo();
        appendCapacity();
        appendHasAmountBuild();

        return result;
    }

    private void appendBaseAttributes() {
        result.id = document.select("input[name=type]").attr("value");
        result.name = document.select("h2").text();
        String level = document.select(".level").text().replaceAll("[\\D]", "");
        result.level = Integer.parseInt(level);
        result.description = document.select(".description_txt").text();

        result.buildTime = document.select("#buildDuration").text().trim();
    }

    private void appendCosts() {
        result.costMetal = getCostForResource("metal");
        result.costCrystal = getCostForResource("crystal");
        result.costDeuterium = getCostForResource("deuterium");
        result.costEnergy = getCostForResource("energy");
    }

    private int getCostForResource(String resourceName) {
        Elements resourceLi = document.select(".tooltip." + resourceName);
        if (resourceName.equals("energy")) // To jest błąd OGame
            resourceLi = document.select(".tooltip.metal");

        Elements resourceIcon = resourceLi.select(".resourceIcon." + resourceName);
        if (!resourceLi.isEmpty() && !resourceIcon.isEmpty())
            return getCostFromElement(resourceLi);
        return 0;
    }

    private int getCostFromElement(Elements resourceLi) {
        String cost = resourceLi.attr("title").replaceAll("[\\D]", "");
        return Integer.parseInt(cost);
    }

    private void appendExtraInfo() {
        Elements prodInformationsLi = document.select(".production_info li");
        if (prodInformationsLi.size() > 1) {
            Element extraInfo = prodInformationsLi.last();

             // Ignorowanie czasu możliwej budowy od komandora
            if (!extraInfo.select("#possibleInTime").isEmpty())
                return;

            String value = extraInfo.select("span").first().text();
            result.extraInfoLabel = extraInfo.text().replace(value, "").replace(":", "").trim();
            result.extraInfoValue = value;
        }
    }

    private void appendCapacity() {
        Element capacityBar = document.select(".bar_container").first();
        if (capacityBar != null) {
            result.hasCapacity = true;
            result.actualCapacity = Integer.parseInt(capacityBar.attr("data-current-amount"));
            result.storageCapacity = Integer.parseInt(capacityBar.attr("data-capacity"));
        }
    }

    private void appendHasAmountBuild() {
        result.hasAmountBuild = !document.select(".amount_input").isEmpty();
        result.isActiveBuild = !document.select(".build-it").isEmpty();
    }
}
