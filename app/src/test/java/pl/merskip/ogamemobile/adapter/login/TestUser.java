package pl.merskip.ogamemobile.adapter.login;

/**
 * Dane testowego użytkownika
 */
public class TestUser {

    private static final String HOST = "pl.ogame.gameforge.com";
    private static final String UNI_ID = "136"; // Japetus
    private static final String LOGIN = "dev_test";
    private static final String PASSWORD = "qwertyaz";
    // Domyślnie jest pierwsza (matka) planeta gdy id jest pusta
    private static final String DEFAULT_PLANET_ID = "";


    private TestUser() {} // Zablokowanie tworzenia instancji

    public static Login.Data getLoginData() {
        Login.Data loginData = new Login.Data();
        loginData.host = HOST;
        loginData.uniId = UNI_ID;
        loginData.login = LOGIN;
        loginData.password = PASSWORD;
        return loginData;
    }

    public static String getPlanetId() {
        return DEFAULT_PLANET_ID;
    }
}
