package app;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import redis.clients.jedis.Jedis;
import org.json.JSONArray;

public class ExportDataToRedisTest {

    @Test
    public void testSubdomainsExported() {
        Jedis jedis = new Jedis("localhost");
        JSONArray subdomains = new JSONArray(jedis.get("subdomains"));
        jedis.close();
        assertEquals(37, subdomains.length()); 
    }

    @Test
    public void testCookiesExported() {
        Jedis jedis = new Jedis("localhost");
        String cookieValue = jedis.get("cookie:dlp-avast:amazon");
        jedis.close();
        assertEquals("mmm_amz_dlp_777_ppc_m", cookieValue);
    }
}
