package pl.merskip.ogamemobile.adapter.login;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test logowania
 */
public class LoginTest {

    private Login login;

    @Before
    public void setUp() {
        Login.Data loginData = TestUser.getLoginData();
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
        Login.Data loginData = TestUser.getLoginData();
        loginData.password += "InvalidPassword";

        Login failedLogin = new Login(loginData);
        failedLogin.tryLogin();

        assertTrue(false); // Powinien zostać wyrzucony wyjątek
    }

}
