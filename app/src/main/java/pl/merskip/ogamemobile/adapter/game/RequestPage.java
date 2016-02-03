package pl.merskip.ogamemobile.adapter.game;

import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import pl.merskip.ogamemobile.adapter.login.AuthorizationData;

/**
 * Pobieranie standardowej strony
 */
public class RequestPage {

    private AuthorizationData auth;
    private String page;
    private String planetId;

    protected Connection connection;
    protected Connection.Response response;

    protected DownloadTimer timer;
    protected Document document;

    public RequestPage(AuthorizationData auth, String page) {
        this(auth, page, "");
    }

    public RequestPage(AuthorizationData auth, String page, String planetId) {
        this.auth = auth;
        this.page = page;
        this.planetId = planetId;
    }

    public AuthorizationData getAuthorizationData() {
        return auth;
    }

    public String getPageName() {
        return page;
    }

    public String getPlanetId() {
        return planetId;
    }

    public Document getDownloadedDocument() {
        return document;
    }

    /**
     * Pobiera dokument, i jeśli nie wystąpił błąd, zwraca go
     */
    public Document download() throws IOException, UnexpectedLogoutException {
        timer = new DownloadTimer();
        timer.started();

        connection = createConnection();
        response = connection.execute();
        timer.downloaded();

        if (!isSuccessResponse(response))
            throw new UnexpectedLogoutException();

        auth.updatePrsess(response);
        document = response.parse();
        timer.parsed();

        return document;
    }

    /**
     * @return Połączenie dla strony z parametrami page i cp (planeta)
     */
    protected Connection createConnection() {
        String requestUrl = getRequestUrl();

        return Jsoup.connect(requestUrl)
                .cookies(auth.toCookiesMap())
                .method(Connection.Method.GET);
    }

    protected String getRequestUrl() {
        String url = getGameIndexUrl();

        if (!page.isEmpty()) {
            url += "?page=" + page;

            if (!planetId.isEmpty())
                url += "&cp=" + planetId;
        }

        return url;
    }

    public String getGameIndexUrl() {
        String uniId = auth.loginData.uniId;
        String host = auth.loginData.host;

        return String.format("http://s%s-%s/game/index.php", uniId, host);
    }

    public DownloadTimer getTimer() {
        return timer;
    }

    public static boolean isSuccessResponse(Connection.Response response) {
        return response.url().getPath().equals("/game/index.php");
    }

    public static class UnexpectedLogoutException extends Exception {}

    /**
     * Dostarcza narzędzi do pomiaru czasu pobierania i parsowania
     * oraz automatycznie generuje logi
     */
    public class DownloadTimer {
        private long startedTime;
        private long downloadedTime;
        private long parsedTime;

        public void started() {
            startedTime = System.currentTimeMillis();
        }

        public void downloaded() {
            downloadedTime = System.currentTimeMillis();

            float downloadSecs = (downloadedTime - startedTime) / 1000.0f;
            Log.w("DownloadTimer", "Downloaded in " + downloadSecs + "s");
        }

        public void parsed() {
            parsedTime = System.currentTimeMillis();

            float totalDownloadSecs = (parsedTime - startedTime) / 1000.0f;
            float parsedSecs = (parsedTime - downloadedTime) / 1000.0f;
            Log.w("DownloadTimer", "Parsed in " + parsedSecs + "s"
                    + ", total downloaded in " + totalDownloadSecs + "s");
        }

        public float getTotalDownloadSecs() {
            return (parsedTime - startedTime) / 1000.0f;
        }
    }
}
