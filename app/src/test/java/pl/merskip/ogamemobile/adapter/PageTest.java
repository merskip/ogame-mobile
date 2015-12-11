package pl.merskip.ogamemobile.adapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import pl.merskip.ogamemobile.adapter.pages.AbstractPage;
import pl.merskip.ogamemobile.adapter.pages.Overview;

/**
 * Pomocnicza klasa testu strony
 *
 * Przed rozpoczęciem testów przeprowadza logowanie
 * i pobiera przykładową stronę do testów
 */
abstract public class PageTest {

    protected static AuthorizationData auth;

    @BeforeClass
    public static void setUp() throws Exception {
        Login.Data loginData = TestUser.getLoginData();
        Login login = new Login(loginData);
        auth = login.tryLogin();

        System.out.println("Login as userId=" + auth.userId);
        System.out.flush();
    }

    protected AbstractPage<?> getExamplePage() throws Exception {
        AbstractPage<?> downloadPage = new Overview(auth);
        downloadPage.download();
        return downloadPage;
    }

    protected Document getExampleDocumentFromAssets() {
        try {
            String absUrl = "http://s136-pl.ogame.gameforge.com/game/index.php?page=station";
            InputStream in = new FileInputStream("src/test/assets/station.html");
            return Jsoup.parse(in, "UTF-8", absUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
