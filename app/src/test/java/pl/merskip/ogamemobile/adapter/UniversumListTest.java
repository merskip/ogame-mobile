package pl.merskip.ogamemobile.adapter;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import pl.merskip.ogamemobile.adapter.UniversumList.Universum;

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
        List<Universum> uniList = universumList.downloadUniversumList();

        assertNotEquals(0, uniList.size());

        for (Universum uni : uniList) {
            System.out.println("id=" + uni.id + ", name=" + uni.name);

            assertNotEquals("", uni.id);
            assertNotEquals("", uni.name);

            assertTrue(StringUtils.isNumeric(uni.id));
        }
    }
}
