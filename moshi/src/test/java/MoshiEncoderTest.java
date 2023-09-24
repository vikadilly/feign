import com.squareup.moshi.*;
import feign.RequestTemplate;
import feign.moshi.MoshiEncoder;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static feign.assertj.FeignAssertions.assertThat;
import static org.junit.Assert.assertEquals;

public class MoshiEncoderTest {
  @Test
  public void encodesMapObjectNumericalValuesAsInteger() {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("foo", 1);

    RequestTemplate template = new RequestTemplate();
    new MoshiEncoder().encode(map, map.getClass(), template);

    assertThat(template).hasBody("{\n" //
            + "  \"foo\": 1\n" //
            + "}");
  }

  @Test
  public void encodesFormParams() {

    Map<String, Object> form = new LinkedHashMap<>();
    form.put("foo", 1);
    form.put("bar", Arrays.asList(2, 3));

    RequestTemplate template = new RequestTemplate();

    new MoshiEncoder().encode(form, form.getClass(), template);

    assertThat(template).hasBody("{\n" //
            + "  \"foo\": 1,\n" //
            + "  \"bar\": [\n" //
            + "    2,\n" //
            + "    3\n" //
            + "  ]\n" //
            + "}");
  }

  @Test
  public void customEncoder() {
    final UpperZoneJSONAdapter upperZone = new UpperZoneJSONAdapter();

    Moshi testAdapter = new Moshi.Builder().add(upperZone).build();
    MoshiEncoder encoder = new MoshiEncoder(testAdapter);

    List<Zone> zones = new LinkedList<>();
    zones.add(new Zone("denominator.io."));
    zones.add(new Zone("denominator.io.", "abcd"));

    RequestTemplate template = new RequestTemplate();
    encoder.encode(zones, Zone.class, template);

    assertThat(template).hasBody("" //
            + "[\n" //
            + "  {\n" //
            + "    \"name\": \"DENOMINATOR.IO.\"\n" //
            + "  },\n" //
            + "  {\n" //
            + "    \"name\": \"DENOMINATOR.IO.\",\n" //
            + "    \"id\": \"ABCD\"\n" //
            + "  }\n" //
            + "]");
  }

}

