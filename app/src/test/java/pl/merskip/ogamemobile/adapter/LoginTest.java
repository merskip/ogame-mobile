package pl.merskip.ogamemobile.adapter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test logowania
 */
public class LoginTest {

    private static final String LANG = "pl";
    private static final int UNI_ID = 136; // Japetus
    private static final String LOGIN = "dev_test";
    private static final String PASSWORD = "qwerty1234";

    private Login login;

    @Before
    public void setUp() {
        Login.Data loginData = new Login.Data();
        loginData.server.lang = LANG;
        loginData.uniId = UNI_ID;
        loginData.login = LOGIN;
        loginData.password = PASSWORD;

        login = new Login(loginData);
    }

    @Test
    public void testUniversumHost() {
        String universumHost = login.getUniversumHost();
        assertEquals("s136-pl.ogame.gameforge.com", universumHost);

        System.out.println("Universum host: " + universumHost);
    }

    @Test
    public void testLogin() throws Exception {
        AuthorizationData auth = login.tryLogin();

        assertNotEquals("", auth.sessionId);
        assertNotEquals("", auth.userId);
        assertNotEquals("", auth.prsess);

        System.out.println("Login success: ");
        System.out.println("\tsessionId=" + auth.sessionId);
        System.out.println("\tuserId=" + auth.userId);
        System.out.println("\tprsess=" + auth.prsess);
    }

    @Test(expected = Login.FailedLoginException.class)
    public void testFailedLogin() throws Exception {
        Login.Data data = new Login.Data();
        data.server.lang = "pl";
        data.uniId = UNI_ID;
        data.login = LOGIN;
        data.password = "!@#INVALID_PASSWOD$%^";

        Login failedLogin = new Login(data);
        failedLogin.tryLogin();

        assertTrue(false); // Powinien zostać wyrzucony wyjątek
    }

}
