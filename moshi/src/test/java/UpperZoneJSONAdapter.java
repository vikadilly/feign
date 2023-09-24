import com.squareup.moshi.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

class UpperZoneJSONAdapter extends JsonAdapter<LinkedList<Zone>> {

    @ToJson
    public void toJson(JsonWriter out, LinkedList<Zone> value) throws IOException {
        out.beginArray();
        for (Zone zone : value) {
            out.beginObject();
            for (Map.Entry<String, Object> entry : zone.entrySet()) {
                out.name(entry.getKey()).value(entry.getValue().toString().toUpperCase());
            }
            out.endObject();
        }
        out.endArray();
    }

    @FromJson
    public LinkedList<Zone> fromJson(JsonReader in) throws IOException {
        LinkedList<Zone> zones = new LinkedList<>();
        in.beginArray();
        while (in.hasNext()) {
            in.beginObject();
            Zone zone = new Zone();
            while (in.hasNext()) {
                zone.put(in.nextName(), in.nextString().toUpperCase());
            }
            in.endObject();
            zones.add(zone);
        }
        in.endArray();
        return zones;
    }
}
