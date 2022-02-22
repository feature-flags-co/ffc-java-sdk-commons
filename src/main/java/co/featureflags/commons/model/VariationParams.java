package co.featureflags.commons.model;

import co.featureflags.commons.json.JsonHelper;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * a ffc object is used to pass the FFClient and flag key name to Server SDK Wrapped API
 */
@JsonAdapter(VariationParams.VariationParamsTypeAdapter.class)
public class VariationParams implements Serializable {
    private final String featureFlagKeyName;
    private final FFCUser user;

    private final transient boolean needAll;

    private VariationParams(String featureFlagKeyName, FFCUser user) {
        Preconditions.checkNotNull(user, "user should not null");
        this.featureFlagKeyName = featureFlagKeyName;
        this.needAll = StringUtils.isBlank(featureFlagKeyName);
        this.user = user;
    }

    /**
     * build a VariationParams object
     * @param featureFlagKeyName flag key name
     * @param user {@link FFCUser}
     * @return a VariationParams object
     */
    public static VariationParams of(String featureFlagKeyName, FFCUser user) {
        return new VariationParams(featureFlagKeyName, user);
    }

    /**
     * build a VariationParams object from json string
     * @param json json string
     * @return a VariationParams object
     */
    public static VariationParams fromJson(String json) {
        if (StringUtils.isNotBlank(json)) {
            return JsonHelper.deserialize(json, VariationParams.class);
        }
        return null;
    }

    /**
     * serialize a VariationParams to json string
     * @return a json string
     */
    public String jsonfy() {
        return JsonHelper.serialize(this);
    }

    /**
     * return a flag key name
     * @return a string or null
     */
    public String getFeatureFlagKeyName() {
        return featureFlagKeyName;
    }

    /**
     * return a {@link FFCUser}
     * @return a {@link FFCUser}
     */
    public FFCUser getUser() {
        return user;
    }

    /**
     * if need all flags
     * @return true if flag key name is present
     */
    public boolean isNeedAll() {
        return needAll;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariationParams params = (VariationParams) o;
        return needAll == params.needAll && Objects.equals(featureFlagKeyName, params.featureFlagKeyName) && Objects.equals(user, params.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(featureFlagKeyName, user, needAll);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("featureFlagKeyName", featureFlagKeyName)
                .add("user", user)
                .add("needAll", needAll)
                .toString();
    }

    static class VariationParamsTypeAdapter extends TypeAdapter<VariationParams> {
        @Override
        public void write(JsonWriter out, VariationParams variationParams) throws IOException {
            out.beginObject();
            out.name("userKeyId").value(variationParams.user.getKey());
            if (StringUtils.isNotBlank(variationParams.getFeatureFlagKeyName())) {
                out.name("featureFlagKeyName").value(variationParams.getFeatureFlagKeyName());
            }
            if (StringUtils.isNotBlank(variationParams.user.getUserName())) {
                out.name("userName").value(variationParams.user.getUserName());
            }
            if (StringUtils.isNotBlank(variationParams.user.getUserName())) {
                out.name("email").value(variationParams.user.getEmail());
            }
            if (StringUtils.isNotBlank(variationParams.user.getUserName())) {
                out.name("country").value(variationParams.user.getCountry());
            }
            if (!variationParams.user.getCustom().isEmpty()) {
                out.name("userCustomizedProperties").beginArray();
                for (Map.Entry<String, String> entry : variationParams.user.getCustom().entrySet()) {
                    out.beginObject();
                    out.name("name").value(entry.getKey());
                    out.name("value").value(entry.getValue());
                    out.endObject();
                }
                out.endArray();
            }
            out.endObject();
        }

        @Override
        public VariationParams read(JsonReader in) throws IOException {
            String featureFlagKeyName = null;
            FFCUser.Builder builder = new FFCUser.Builder("");
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "featureFlagKeyName":
                        featureFlagKeyName = in.nextString();
                        break;
                    case "userKeyId":
                        builder.key(in.nextString());
                        break;
                    case "userName":
                        builder.userName(in.nextString());
                        break;
                    case "email":
                        builder.email(in.nextString());
                        break;
                    case "country":
                        builder.country(in.nextString());
                        break;
                    case "userCustomizedProperties":
                        in.beginArray();
                        while (in.hasNext()) {
                            String name = null;
                            String value = null;
                            in.beginObject();
                            while (in.hasNext()) {
                                switch (in.nextName()) {
                                    case "name":
                                        name = in.nextString();
                                        break;
                                    case "value":
                                        value = in.nextString();
                                        break;
                                }
                            }
                            in.endObject();
                            builder.custom(name, value);
                        }
                        in.endArray();
                        break;
                }
            }
            in.endObject();
            FFCUser user = builder.build();
            return VariationParams.of(featureFlagKeyName, user);
        }
    }

}
