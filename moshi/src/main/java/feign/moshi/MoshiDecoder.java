package feign.moshi;

import com.google.common.io.CharStreams;
import com.squareup.moshi.*;
import feign.Response;
import feign.Util;
import feign.codec.Decoder;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import static feign.Util.UTF_8;
import static feign.Util.ensureClosed;

public class MoshiDecoder implements Decoder {
  private final Moshi moshi;

  public MoshiDecoder(Moshi moshi) {
    this.moshi = moshi;
  }

  public MoshiDecoder() {
    this.moshi = new Moshi.Builder().build();
  }


  @Override
  public Object decode(Response response, Type type) throws IOException {
    JsonAdapter<Object> jsonAdapter = moshi.adapter(Object.class);

    if (response.status() == 404 || response.status() == 204)
      return Util.emptyValueOf(type);
    if (response.body() == null)
      return null;

    Reader reader = response.body().asReader(UTF_8);

    try {
      return parseJson(jsonAdapter, reader);
    } catch (JsonEncodingException e) {
      if (e.getCause() != null && e.getCause() instanceof IOException) {
        throw (IOException) e.getCause();
      }
      throw e;
    } finally {
      // close the reader
      ensureClosed(reader);
    }
  }

  private Object parseJson(JsonAdapter<Object> jsonAdapter, Reader reader) throws IOException {
    String targetString = CharStreams.toString(reader);
    return jsonAdapter.fromJson(targetString);
  }
}

