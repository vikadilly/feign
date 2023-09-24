package feign.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.lang.reflect.Type;
import java.util.Map;

import static feign.Util.resolveLastTypeParameter;

public class MoshiFactory {
    private MoshiFactory() {}

    /**
     * Registers type adapters by implicit type. Adds one to read numbers in a {@code Map<String,
     * Object>} as Integers.
     */
    static Moshi create(Iterable<JsonAdapter<?>> adapters) {
        Moshi.Builder builder = new Moshi.Builder();

        for (JsonAdapter<?> adapter : adapters) {
            Type type = resolveLastTypeParameter(adapter.getClass(), JsonAdapter.class);
            builder.add(type, adapter);
        }
        return builder.build();
    }
}
