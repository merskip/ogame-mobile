package pl.merskip.ogamemobile.adapter.pages;

import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.merskip.ogamemobile.adapter.AuthorizationData;
import pl.merskip.ogamemobile.adapter.ScriptData;
import pl.merskip.ogamemobile.adapter.pages.BuildItem.BuildState;

/**
 * Strona zawierająca pozycje do budowania
 */
public abstract class BuildItemsPage extends AbstractPage<List<BuildItem>> {


    protected BuildItemsPage(AuthorizationData auth, String page) {
        super(auth, page);
    }

    protected void appendFromListLi(List<BuildItem> buildItems, Elements elementsLi) {
        for (Element element : elementsLi) {
            BuildItem buildItem = createFromLi(element);
            buildItems.add(buildItem);
        }
    }

    protected BuildItem createFromLi(Element li) {
        String id = li.select("a[ref]").attr("ref");
        String name = li.select(".textlabel").text().trim();
        String levelWithNameAndDots = li.select(".level").text();
        String level = levelWithNameAndDots.replace(name, "").replace(".", "").trim();

        BuildItem buildItem = new BuildItem(id, name, Integer.parseInt(level));
        buildItem.buildState = getBuildSate(li);

        /* Gdy budynek jest rozbudowywany,
         * nazwa budynku i poziom są napisane w innym miejscu */
        if (buildItem.buildState == BuildState.Upgrading) {
            buildItem.name = li.select(".tooltip").attr("title").replaceAll(" \\(.*?\\)", "");
            String levelWithDots = li.select(".ecke .level").text();
            level = levelWithDots.replace(".", "").trim();
            buildItem.level = Integer.parseInt(level);
        }

        if (buildItem.buildState == BuildState.ReadyToBuild)
            buildItem.buildRequestUrl = getBuildRequestUrl(li);
        if (buildItem.buildState == BuildState.Upgrading)
            buildItem.buildProgress = getBuildingUpgrading(li, scriptData);

        return buildItem;
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
            Log.e("BuildItem", "Unknown building state: " + li.className());
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

    private BuildItem.BuildProgress getBuildingUpgrading(Element li,
                                                      ScriptData scriptData) {

        Element pusherElement = li.select(".pusher").first();
        if (pusherElement == null)
            return null;

        String buildId = pusherElement.attr("id");
        String regex = "new bauCountdown\\(.*?\\('" + buildId + "'\\),(.*?),(.*?),.*?\\);";
        Matcher matcher = Pattern.compile(regex).matcher(scriptData.getContent());

        if (matcher.find()) {
            int remainingSeconds = Integer.parseInt(matcher.group(1));
            BuildItem.BuildProgress buildProgress = new BuildItem.BuildProgress();
            buildProgress.finishTime = getFinishTime(remainingSeconds);
            buildProgress.totalSeconds = Integer.parseInt(matcher.group(2));
            return buildProgress;
        }

        return  null;
    }

    private long getFinishTime(int remainingSeconds) {
        long currentTime = System.currentTimeMillis();
        long remainingMillis = remainingSeconds * 1000;
        long totalDownloadMillis = (long) (timer.getTotalDownloadSecs() * 1000);
        return currentTime + remainingMillis - totalDownloadMillis;
    }
}
