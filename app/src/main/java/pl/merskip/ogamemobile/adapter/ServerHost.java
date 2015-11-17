package pl.merskip.ogamemobile.adapter;

/**
 * Przechowuje host adresu gry
 */
public class ServerHost {

    public String host = "ogame.gameforge.com";
    public String lang;

    public String getGameUrl() {
        return String.format("http://%s.%s",
                lang, host);
    }
}
