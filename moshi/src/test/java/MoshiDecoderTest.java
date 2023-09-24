import com.squareup.moshi.Moshi;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import feign.Util;
import feign.moshi.MoshiDecoder;
import feign.moshi.MoshiEncoder;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import static feign.Util.UTF_8;
import static feign.assertj.FeignAssertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

  @Test
  public void nullBodyDecodesToNull() throws Exception {
    Response response = Response.builder()
            .status(204)
            .reason("OK")
            .headers(Collections.emptyMap())
            .request(Request.create(Request.HttpMethod.GET, "/api", Collections.emptyMap(), null, Util.UTF_8))
            .build();
    assertNull(new MoshiDecoder().decode(response, String.class));
  }

  @Test
  public void emptyBodyDecodesToNull() throws Exception {
    Response response = Response.builder()
            .status(204)
            .reason("OK")
            .headers(Collections.emptyMap())
            .request(Request.create(Request.HttpMethod.GET, "/api", Collections.emptyMap(), null, Util.UTF_8))
            .body(new byte[0])
            .build();
    assertNull(new MoshiDecoder().decode(response, String.class));
  }


  /** Enabled via {@link feign.Feign.Builder#dismiss404()} */
  @Test
  public void notFoundDecodesToEmpty() throws Exception {
    Response response = Response.builder()
            .status(404)
            .reason("NOT FOUND")
            .headers(Collections.emptyMap())
            .request(Request.create(Request.HttpMethod.GET, "/api", Collections.emptyMap(), null, Util.UTF_8))
            .build();
    assertThat((byte[]) new MoshiDecoder().decode(response, byte[].class)).isEmpty();
  }

  @Test
  public void customDecoder() throws Exception {
    final UpperZoneJSONAdapter upperZone = new UpperZoneJSONAdapter();

    Moshi testAdapter = new Moshi.Builder().add(upperZone).build();
    MoshiDecoder decoder = new MoshiDecoder(testAdapter);

    List<Zone> zones = new LinkedList<>();
    zones.add(new Zone("DENOMINATOR.IO."));
    zones.add(new Zone("DENOMINATOR.IO.", "ABCD"));

    Response response =
            Response.builder()
                    .status(200)
                    .reason("OK")
                    .headers(Collections.emptyMap())
                    .request(
                            Request.create(Request.HttpMethod.GET, "/api", Collections.emptyMap(), null, Util.UTF_8))
                    .body(zonesJson, UTF_8)
                    .build();
    assertEquals(zones, decoder.decode(response, Zone.class));
  }

}
