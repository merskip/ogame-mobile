package pl.merskip.ogamemobile.adapter;

import org.jsoup.Connection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Dane do autoryzacji
 */
public class AuthorizationData implements Serializable {

    public Login.Data loginData;

    public String sessionId;
    public String userId;
    public String prsess;

    public static AuthorizationData fromCookies(Map<String, String> cookies) {
        if (!cookies.containsKey("PHPSESSID"))
            throw new IllegalArgumentException("Cookies do not contains PHPSESSID");

        AuthorizationData auth = new AuthorizationData();
        auth.sessionId = cookies.get("PHPSESSID");
        auth.userId = "";
        auth.prsess = "";

        for (Map.Entry<String, String> cookie : cookies.entrySet()) {
            String key = cookie.getKey();
            String value = cookie.getValue();

            if (key.startsWith("prsess_")) {
                auth.userId = key.substring("prsess_".length());
                auth.prsess = value;
                return auth;
            }
        }

        throw new IllegalArgumentException("Cookies do not contains prsess_%");
    }

    public void updatePrsess(Connection.Response response) {
        if (userId != null) {
            Map<String, String> cookies = response.cookies();
            String newPrsess = cookies.get("prsess_" + userId);

            if (newPrsess != null)
                prsess = newPrsess;
        }
    }

    public Map<String, String> toCookiesMap() {
        Map<String, String> map = new HashMap<>();
        map.put("PHPSESSID", sessionId);
        map.put("prsess_" + userId, prsess);
        return map;
    }
}