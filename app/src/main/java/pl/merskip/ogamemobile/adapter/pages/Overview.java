package pl.merskip.ogamemobile.adapter.pages;

import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.merskip.ogamemobile.adapter.AuthorizationData;

/**
 * Strona podglądu planety
 */
public class Overview extends AbstractPage<OverviewData> {

    public Overview(AuthorizationData auth) {
        super(auth, "overview");
    }

    @Override
    public OverviewData createResult(Document document) {
        OverviewData data = new OverviewData();
        appendPlanetInfo(data);
        appendPlayerScore(data);
        return data;
    }

    private void appendPlanetInfo(OverviewData data) {
        data.planetDiameter = scriptData.getTextValue("textContent[1]");
        data.planetTemperature = scriptData.getTextValue("textContent[3]");
        data.planetCoordinate = scriptData.getTextValue("textContent[5]");
    }

    private void appendPlayerScore(OverviewData data) {

        String[] scoreAndPosition = getPlayerScoreAndPosition();
        if (scoreAndPosition == null)
            return;

        data.playerScore = scoreAndPosition[0];
        data.playerPosition = scoreAndPosition[1];
        data.playerHonor = scriptData.getTextValue("textContent[9]");
    }

    private String[] getPlayerScoreAndPosition() {
        String rawPoints = scriptData.getTextValue("textContent[7]");

        String regex = "([\\d\\.]*) \\(.*? ([\\d\\.]* .*? [\\d\\.]*)\\)";
        Matcher matcher = Pattern.compile(regex).matcher(rawPoints);

        if (matcher.find()) {
            String score = matcher.group(1);
            String positionScore = matcher.group(2);
            return new String[]{score, positionScore};
        }
        return null;
    }
}
