package pl.merskip.ogamemobile.adapter;

import org.junit.Before;

/**
 * Pomocnicza klasa testu strony
 */
abstract public class PageTest {

    protected AuthorizationData auth;

    @Before
    public void setUp() throws Exception {
        Login.Data loginData = TestUser.getLoginData();
        Login login = new Login(loginData);
        auth = login.tryLogin();

        System.out.println("Login as userId=" + auth.userId);
    }
}
