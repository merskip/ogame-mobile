package pl.merskip.ogamemobile.adapter;

import org.junit.BeforeClass;

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
}
