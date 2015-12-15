package pl.merskip.ogamemobile.adapter.pages;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;

import pl.merskip.ogamemobile.adapter.AuthorizationData;

/**
 * Budowanie statków/obrony
 */
public class BuildAmount extends AbstractPage<Boolean> {

    private BuildItem buildItem;
    private int amount;
    private String token;

    public BuildAmount(AuthorizationData auth, String page) {
        super(auth, page);
    }

    public void setBuildItem(BuildItem buildItem) {
        this.buildItem = buildItem;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    protected Connection createConnection() {
        return super.createConnection()
                .method(Method.POST)
                .data("token", token)
                .data("modus", "1")
                .data("type", buildItem.id)
                .data("menge", String.valueOf(amount))
                .followRedirects(false);
    }

    @Override
    public Boolean createResult(Document document) {
        // OGame wysyła odpowiedź 302 Found jeśli udało się zaakceptować żądanie
        return response.statusCode() == 302;
    }
}
