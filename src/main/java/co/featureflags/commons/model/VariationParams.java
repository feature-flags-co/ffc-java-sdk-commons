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

@JsonAdapter(VariationParams.VariationParamsTypeAdapter.class)
public class VariationParams implements Serializable {
    private final String featureFlagKeyName;

    private final FFCUser user;

    private VariationParams(String featureFlagKeyName, FFCUser user) {
        Preconditions.checkNotNull(user, "user should not null");
        Preconditions.checkArgument(StringUtils.isNotBlank(featureFlagKeyName), "featureFlagKeyName should not blank");
        this.featureFlagKeyName = featureFlagKeyName;
        this.user = user;
    }

    public static VariationParams of(String featureFlagKeyName, FFCUser user) {
        return new VariationParams(featureFlagKeyName, user);
    }

    public static VariationParams fromJson(String json) {
        if (StringUtils.isNotBlank(json)) {
            return JsonHelper.deserialize(json, VariationParams.class);
        }
        return null;
    }

    public String jsonfy() {
        return JsonHelper.serialize(this);
    }

    public String getFeatureFlagKeyName() {
        return featureFlagKeyName;
    }

    public FFCUser getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariationParams that = (VariationParams) o;
        return Objects.equals(featureFlagKeyName, that.featureFlagKeyName) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(featureFlagKeyName, user);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("featureFlagKeyName", featureFlagKeyName).add("user", user).toString();
    }


    static class VariationParamsTypeAdapter extends TypeAdapter<VariationParams> {
        @Override
        public void write(JsonWriter out, VariationParams variationParams) throws IOException {
            out.beginObject();
            out.name("featureFlagKeyName").value(variationParams.featureFlagKeyName);
            out.name("userKeyId").value(variationParams.user.getKey());
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
