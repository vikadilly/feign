import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import feign.Util;
import feign.moshi.MoshiDecoder;
import feign.moshi.MoshiEncoder;
import org.junit.Test;
import java.util.*;
import static feign.Util.UTF_8;
import static feign.assertj.FeignAssertions.assertThat;
import static org.junit.Assert.assertEquals;

public class MoshiDecoderTest {
  @Test
  public void decodes() throws Exception {

    class Zone extends LinkedHashMap<String, Object> {

      Zone(String name) {
        this(name, null);
      }

      Zone(String name, String id) {
        put("name", name);
        if (id != null) {
          put("id", id);
        }
      }

      private static final long serialVersionUID = 1L;
    }

    List<Zone> zones = new LinkedList<>();
    zones.add(new Zone("denominator.io."));
    zones.add(new Zone("denominator.io.", "ABCD"));

    Response response = Response.builder()
        .status(200)
        .reason("OK")
        .headers(Collections.emptyMap())
        .request(Request.create(Request.HttpMethod.GET, "/api", Collections.emptyMap(), null,
            Util.UTF_8))
        .body(zonesJson, UTF_8)
        .build();

    assertEquals(zones,
        new MoshiDecoder().decode(response, zones.getClass()));
  }

  private String zonesJson = ""//
      + "[\n"//
      + "  {\n"//
      + "    \"name\": \"denominator.io.\"\n"//
      + "  },\n"//
      + "  {\n"//
      + "    \"name\": \"denominator.io.\",\n"//
      + "    \"id\": \"ABCD\"\n"//
      + "  }\n"//
      + "]\n";

}
