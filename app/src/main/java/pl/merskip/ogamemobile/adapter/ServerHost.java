package pl.merskip.ogamemobile.adapter;

/**
 * Przechowuje host adresu gry
 */
public class ServerHost {

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
