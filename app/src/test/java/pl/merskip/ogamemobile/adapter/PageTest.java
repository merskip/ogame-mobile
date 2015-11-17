package pl.merskip.ogamemobile.adapter;

import org.junit.Before;

/**
 * Pomocnicza klasa testu strony
 */
public class PageTest {

    private static final String LANG = "pl";
    private static final int UNI_ID = 136; // Japetus
    private static final String LOGIN = "dev_test";
    private static final String PASSWORD = "qwerty1234";

    protected AuthorizationData auth;

    @Before
    public void setUp() throws Exception {
        Login.Data loginData = new Login.Data();
        loginData.lang = LANG;
        loginData.uniId = UNI_ID;
        loginData.login = LOGIN;
        loginData.password = PASSWORD;

        Login login = new Login(loginData);
        auth = login.tryLogin();

        System.out.println("Login with: " + auth.toString());
    }
}
