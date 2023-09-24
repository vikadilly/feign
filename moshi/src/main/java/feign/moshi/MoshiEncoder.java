package feign.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import feign.RequestTemplate;
import feign.codec.Encoder;
import java.lang.reflect.Type;
import java.util.Collections;

public class MoshiEncoder implements Encoder {

  private final Moshi moshi;
  public MoshiEncoder() {
    this.moshi = new Moshi.Builder().build();
  }

  public MoshiEncoder(Moshi moshi) {
      this.moshi = moshi;
  }

  public MoshiEncoder(Iterable<JsonAdapter<?>> adapters) {
    this(MoshiFactory.create(adapters));
  }

  @Override
  public void encode(Object object, Type bodyType, RequestTemplate template) {
    JsonAdapter<Object> jsonAdapter = moshi.adapter(Object.class).indent("  ");
    template.body(jsonAdapter.toJson(object));
  }
}
