package pl.merskip.ogamemobile.adapter.pages;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import pl.merskip.ogamemobile.adapter.AuthorizationData;
import pl.merskip.ogamemobile.adapter.ScriptData;

/**
 * Abstrakcyjna strona
 *
 * Implementuje pobieranie strony
 */
public abstract class AbstractPage<Result> {

    private AuthorizationData auth;
    private String page;
    private String planetId;

    protected Connection connection;
    protected Connection.Response response;

    protected Document document;
    protected ScriptData scriptData;

    protected AbstractPage(AuthorizationData auth, String page) {
        this(auth, page, "");
    }

    protected AbstractPage(AuthorizationData auth, String page, String planetId) {
        this.auth = auth;
        this.page = page;
        this.planetId = planetId;
    }

    public void setPlanetId(String planetId) {
        this.planetId = planetId;
    }

    /**
     * Pobiera dokument, i jeśli nie wystąpił błąd, zwraca przetworzone dane
     * @return Przetworzone dane
     */
    public Result download() throws IOException, UnexpectedLogoutException {
        connection = createConnection();
        response = connection.execute();

        if (!isSuccessResponse(response))
            throw new UnexpectedLogoutException();

        document = response.parse();
        scriptData = new ScriptData(document);
        return createResult(document);
    }

    /**
     * Miejsce na przetworzenie strony i zwrócenie wyników
     * @param document Odebrany dokument
     * @return Przetworzone dane ze strony
     */
    abstract public Result createResult(Document document);

    /**
     * @return Połączenie dla strony z parametrami page i cp (planeta)
     */
    protected Connection createConnection() {
        String gameUrl = getGameIndexUrl();
        Connection connection =
                Jsoup.connect(gameUrl)
                        .cookies(auth.toCookiesMap())
                        .method(Connection.Method.GET);

        if (!page.isEmpty())
            connection.data("page", page);

        if (!planetId.isEmpty())
            connection.data("cp", planetId);

        return connection;
    }

    public String getGameIndexUrl() {
        String uniId = auth.loginData.uniId;
        String lang = auth.loginData.server.lang;
        String host = auth.loginData.server.host;

        return String.format("http://s%s-%s.%s/game/index.php",
                uniId, lang, host);
    }

    public static boolean isSuccessResponse(Connection.Response response) {
        return response.url().getPath().equals("/game/index.php");
    }

    public static class UnexpectedLogoutException extends Exception {}
}
