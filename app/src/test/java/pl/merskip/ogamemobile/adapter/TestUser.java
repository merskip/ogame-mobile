package pl.merskip.ogamemobile.adapter;

/**
 * Dane testowego użytkownika
 */
public class TestUser {

    private static final String LANG = "pl";
    private static final String UNI_ID = "136"; // Japetus
    private static final String LOGIN = "dev_test";
    private static final String PASSWORD = "qwertyaz";

    private TestUser() {} // Zablokowanie tworzenia instancji

    public static Login.Data getLoginData() {
        Login.Data loginData = new Login.Data();
        loginData.server.lang = LANG;
        loginData.uniId = UNI_ID;
        loginData.login = LOGIN;
        loginData.password = PASSWORD;
        return loginData;
    }
}
