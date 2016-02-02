package pl.merskip.ogamemobile.adapter.login;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import pl.merskip.ogamemobile.adapter.ServerHost;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test pobierania listy universum
 */
public class UniversumListTest {

    @Test
    public void testDownloadUniversumList() throws IOException {
        ServerHost server = TestUser.getLoginData().server;

        UniversumList universumList = new UniversumList(server);
        Map<String, String> uniList = universumList.downloadUniversumList();

        assertNotEquals(0, uniList.size());

        for (Map.Entry uni : uniList.entrySet()) {
            String uniId = (String) uni.getKey();
            String name = (String) uni.getValue();

            System.out.println("id=" + uniId + ", name=" + name);

            assertNotEquals("", uniId);
            assertNotEquals("", name);

            assertTrue(StringUtils.isNumeric(uniId));
        }
    }
}
