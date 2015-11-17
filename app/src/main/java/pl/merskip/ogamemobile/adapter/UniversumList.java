package pl.merskip.ogamemobile.adapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Pobieranie listy universum
 */
public class UniversumList {

    static public class Universum {
        public String id;
        public String name;
    }

    private ServerHost server;

    public UniversumList(ServerHost server) {
        this.server = server;
    }

    public List<Universum> downloadUniversumList() throws IOException {
        String serverUrl = server.getServerUrl();
        Document document = Jsoup.connect(serverUrl).get();

        List<Universum> universumList = new LinkedList<>();

        Elements universumOptions = document.getElementById("serverLogin")
                .getElementsByTag("option");

        for (Element option : universumOptions) {

            String url = option.attributes().get("value");
            String uniId = url.substring(1, url.indexOf('-')); // s123-pl...

            Universum uni = new Universum();
            uni.id = uniId;
            uni.name = option.text().trim();
            universumList.add(uni);
        }
        return universumList;
    }
}
