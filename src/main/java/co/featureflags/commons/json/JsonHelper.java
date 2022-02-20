package co.featureflags.commons.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

public abstract class JsonHelper {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();
    private static final String DATA_INVALID_ERROR = "Received Data invalid";

    private JsonHelper() {
        super();
    }


    public static <T> T deserialize(String json, Class<T> objectClass) throws JsonParseException {
        try {
            return gson.fromJson(json, objectClass);
        } catch (Exception e) {
            throw new JsonParseException(DATA_INVALID_ERROR, e);
        }
    }

    public static <T> T deserialize(String json, Type type) throws JsonParseException {
        try {
            return gson.fromJson(json, type);
        } catch (Exception e) {
            throw new JsonParseException(DATA_INVALID_ERROR, e);
        }
    }

    public static <T> T deserialize(Reader reader, Class<T> objectClass) throws JsonParseException {
        try {
            return gson.fromJson(reader, objectClass);
        } catch (Exception e) {
            throw new JsonParseException(DATA_INVALID_ERROR, e);
        }
    }

    public static <T> T deserialize(Reader reader, Type type) throws JsonParseException {
        try {
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            throw new JsonParseException(DATA_INVALID_ERROR, e);
        }
    }

    public static String serialize(Object o) {
        return gson.toJson(o);
    }

    public interface AfterJsonParseDeserializable {
        void afterDeserialization();
    }

    public static final class AfterJsonParseDeserializableTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return new AfterJsonParseDeserializableTypeAdapter(gson.getDelegateAdapter(this, typeToken));
        }
    }

    public static final class AfterJsonParseDeserializableTypeAdapter<T> extends TypeAdapter<T> {
        private final TypeAdapter<T> typeAdapter;

        public AfterJsonParseDeserializableTypeAdapter(TypeAdapter<T> typeAdapter) {
            this.typeAdapter = typeAdapter;
        }

        @Override
        public void write(JsonWriter jsonWriter, T t) throws IOException {
            typeAdapter.write(jsonWriter, t);
        }

        @Override
        public T read(JsonReader jsonReader) throws IOException {
            T res = typeAdapter.read(jsonReader);
            if (res instanceof AfterJsonParseDeserializable) {
                ((AfterJsonParseDeserializable) res).afterDeserialization();
            }
            return res;
        }
    }

}
