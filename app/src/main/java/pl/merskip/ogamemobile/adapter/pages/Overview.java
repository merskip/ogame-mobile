package pl.merskip.ogamemobile.adapter.pages;

import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.merskip.ogamemobile.adapter.AuthorizationData;

/**
 * Strona podglądu planety
 */
public class Overview extends AbstractPage<Overview.Data> {

    public class Data {
        public PlanetInfo planetInfo;
        public PlayerScore playerScore;
    }

    public class PlanetInfo {
        public String diameter;
        public String temperature;
        public String coordinate;
    }

    public class PlayerScore {
        public String score;
        public String position;
        public String honor;
    }

    public Overview(AuthorizationData auth) {
        super(auth, "overview");
    }

    @Override
    public Data createResult(Document document) {
        Data data = new Data();
        data.planetInfo = getPlanetInfo();
        data.playerScore = getPlayerScore();
        return data;
    }

    private PlanetInfo getPlanetInfo() {
        PlanetInfo planetInfo = new PlanetInfo();
        planetInfo.diameter = scriptData.getTextValue("textContent[1]");
        planetInfo.temperature = scriptData.getTextValue("textContent[3]");
        planetInfo.coordinate = scriptData.getTextValue("textContent[5]");
        return planetInfo;
    }

    private PlayerScore getPlayerScore() {

        String[] scoreAndPosition = getPlayerScoreAndPosition();
        if (scoreAndPosition == null)
            return null;

        PlayerScore playerScore = new PlayerScore();
        playerScore.score = scoreAndPosition[0];
        playerScore.position = scoreAndPosition[1];
        playerScore.honor = scriptData.getTextValue("textContent[9]");
        return playerScore;
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
