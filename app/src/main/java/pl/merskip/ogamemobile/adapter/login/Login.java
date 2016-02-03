package pl.merskip.ogamemobile.adapter.login;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import pl.merskip.ogamemobile.adapter.game.RequestPage;

/**
 * Logowanie
 */
public class Login {

    /**
     * Przechowuje dane niezbędne do logowania
     */
    public static class Data implements Serializable {
        public String host;
        public String uniId;
        public String login;
        public String password;
    }

    private Data loginData;

    public Login(Data loginData) {
        this.loginData = loginData;
    }

    public AuthorizationData tryLogin() throws IOException, FailedLoginException {
        String url = getLoginUrl();
        String universumHost = getUniversumHost();
        Connection connection =
                Jsoup.connect(url)
                        .method(Connection.Method.POST)
                        .data("kid", "")
                        .data("uni", universumHost)
                        .data("login", loginData.login)
                        .data("pass", loginData.password);

        Connection.Response response = connection.execute();

        if (RequestPage.isSuccessResponse(response)) {
            Map<String, String> cookies = response.cookies();
            AuthorizationData auth = AuthorizationData.fromCookies(cookies);
            auth.loginData = loginData;
            return auth;
        } else {
            throw new FailedLoginException();
        }
    }

    public String getLoginUrl() {
        return "http://" + loginData.host + "/main/login";
    }

    public String getUniversumHost() {
        return String.format("s%s-%s", loginData.uniId, loginData.host);
    }

    /**
     * Błąd podczas logowania
     */
    public static class FailedLoginException extends Exception {}
}
