package pl.merskip.ogamemobile.adapter.game.pages;

import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.merskip.ogamemobile.adapter.ScriptData;
import pl.merskip.ogamemobile.adapter.game.Building;
import pl.merskip.ogamemobile.adapter.game.Building.BuildState;
import pl.merskip.ogamemobile.adapter.game.RequestPage;
import pl.merskip.ogamemobile.adapter.game.ResultPage;

/**
 * Strona zawierająca pozycje do budowania
 */
public abstract class BuildingsResult extends ResultPage<List<Building>> {

    protected void appendFromListLi(List<Building> buildings, Elements elementsLi) {
        for (Element element : elementsLi) {
            Building building = createFromLi(element);
            buildings.add(building);
        }
    }

    protected Building createFromLi(Element li) {
        String id = li.select("a[ref]").attr("ref");
        String name = li.select(".textlabel").text().trim();
        String levelWithNameAndDots = li.select(".level").text();
        String level = levelWithNameAndDots.replace(name, "").replace(".", "").trim();

        Building building = new Building(id, name, Integer.parseInt(level));
        building.buildState = getBuildSate(li);

        /* Gdy budynek jest rozbudowywany,
         * nazwa budynku i poziom są napisane w innym miejscu */
        if (building.buildState == BuildState.Upgrading) {
            building.name = li.select(".tooltip").attr("title").replaceAll(" \\(.*?\\)", "");

            String levelWithDots = li.select(".ecke .level").text();
            level = levelWithDots.replace(".", "").trim();
            building.level = Integer.parseInt(level);

            Element nextLevelElement = li.select(".eckeoben span").first();
            String nextLevel = nextLevelElement.text();
            nextLevel = nextLevel.replace(".", "").trim();
            building.nextLevel = Integer.parseInt(nextLevel);
            building.isNextLevelCount = nextLevelElement.hasClass("count");
        }

        if (building.buildState == BuildState.ReadyToBuild)
            building.buildRequestUrl = getBuildRequestUrl(li);
        if (building.buildState == BuildState.Upgrading)
            building.buildProgress = getBuildingUpgrading(li, scriptData);

        return building;
    }

    private static BuildState getBuildSate(Element li) {
        if (!li.select(".construction").isEmpty())
            return BuildState.Upgrading;

        if (li.hasClass("on"))
            return BuildState.ReadyToBuild;
        else if (li.hasClass("disabled"))
            return BuildState.TooFewResources;
        else if (li.hasClass("off"))
            return BuildState.UnmetRequirements;
        else
            Log.e("Building", "Unknown building state: " + li.className());
        return null;
    }

    private static String getBuildRequestUrl(Element li) {
        Element aFastBuild = li.select("a.fastBuild").first();
        if (aFastBuild == null)
            return null;

        String clickAttr = aFastBuild.attr("onclick");

        String regex = "^sendBuildRequest\\('(.*)',.*\\)";
        Matcher matcher = Pattern.compile(regex, Pattern.DOTALL).matcher(clickAttr);

        if (matcher.find())
            return matcher.group(1);
        return null;
    }

    private Building.BuildProgress getBuildingUpgrading(Element li,
                                                      ScriptData scriptData) {

        Element pusherElement = li.select(".pusher").first();
        if (pusherElement == null)
            return null;

        String buildId = pusherElement.attr("id");
        String regex = "new bauCountdown\\(.*?\\('" + buildId + "'\\),(.*?),(.*?),.*?\\);";
        Matcher matcher = Pattern.compile(regex).matcher(scriptData.getContent());

        if (matcher.find()) {
            int remainingSeconds = Integer.parseInt(matcher.group(1));
            Building.BuildProgress buildProgress = new Building.BuildProgress();
            buildProgress.finishTime = getFinishTime(remainingSeconds);
            buildProgress.totalSeconds = Integer.parseInt(matcher.group(2));
            return buildProgress;
        }

        return  null;
    }

    private long getFinishTime(int remainingSeconds) {
        long currentTime = System.currentTimeMillis();
        long remainingMillis = remainingSeconds * 1000;
        long totalDownloadMillis = 0;
        if (request != null) {
            RequestPage.DownloadTimer timer = request.getTimer();
            totalDownloadMillis = (long) (timer.getTotalDownloadSecs() * 1000);
        }

        return currentTime + remainingMillis - totalDownloadMillis;
    }
}
