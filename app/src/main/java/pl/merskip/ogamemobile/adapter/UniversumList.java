package pl.merskip.ogamemobile.adapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Pobieranie listy universum
 *
 * Zwraca wynik w formie mapy, gdzie klucze odpowiadają id universum,
 * a wartości nazwom universum
 */
public class UniversumList {

    private ServerHost server;

    public UniversumList(ServerHost server) {
        this.server = server;
    }

    public Map<String, String> downloadUniversumList() throws IOException {
        String serverUrl = server.getServerUrl();
        Document document = Jsoup.connect(serverUrl).get();

        Map<String, String> universumList = new LinkedHashMap<>();

        Elements universumOptions = document.getElementById("serverLogin")
                .getElementsByTag("option");

        for (Element option : universumOptions) {

            String name = option.text().trim();
            String url = option.attributes().get("value");
            String uniId = url.substring(1, url.indexOf('-')); // s123-pl...

            universumList.put(uniId, name);
        }
        return universumList;
    }
}
