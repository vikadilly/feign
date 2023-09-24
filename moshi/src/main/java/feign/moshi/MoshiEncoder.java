package feign.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import feign.RequestTemplate;
import feign.codec.Encoder;
import java.lang.reflect.Type;

public class MoshiEncoder implements Encoder {
  Moshi moshi = new Moshi.Builder().build();

  @Override
  public void encode(Object object, Type bodyType, RequestTemplate template) {
    JsonAdapter<Object> jsonAdapter = moshi.adapter(Object.class).indent("  ");
    template.body(jsonAdapter.toJson(object));
  }
}
