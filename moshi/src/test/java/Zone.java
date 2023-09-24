import java.util.LinkedHashMap;

public class Zone extends LinkedHashMap<String, Object> {

    Zone() {
        // for reflective instantiation.
    }

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

