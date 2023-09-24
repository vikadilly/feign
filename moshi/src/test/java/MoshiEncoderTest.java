import feign.RequestTemplate;
import feign.moshi.MoshiEncoder;
import org.junit.Test;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
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


}
