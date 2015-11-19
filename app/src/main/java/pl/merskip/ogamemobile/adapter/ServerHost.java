package pl.merskip.ogamemobile.adapter;

import java.io.Serializable;

/**
 * Przechowuje host adresu gry
 */
public class ServerHost implements Serializable {

    public String host = "ogame.gameforge.com";
    public String lang;

    public ServerHost() {}

    public ServerHost(String lang) {
        this.lang = lang;
    }

    public String getServerUrl() {
        return String.format("http://%s.%s",
                lang, host);
    }
}
