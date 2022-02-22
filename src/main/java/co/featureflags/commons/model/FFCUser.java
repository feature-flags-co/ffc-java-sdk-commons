package co.featureflags.commons.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A collection of attributes that can affect flag evaluation, usually corresponding to a user of your application.
 * The only mandatory property is the key, which must uniquely identify each user;
 * this could be a username or email address for authenticated users, or a ID for anonymous users.
 * All other built-in properties are optional， it's strongly recommended to set userName in order to search your user quickly
 * You may also define custom properties with arbitrary names and values.
 */

public final class FFCUser implements Serializable {

    private final static Function<FFCUser, String> USERNAME = u -> u.userName;
    private final static Function<FFCUser, String> EMAIL = u -> u.email;
    private final static Function<FFCUser, String> KEY = u -> u.key;
    private final static Function<FFCUser, String> COUNTRY = u -> u.country;

    private final static Map<String, Function<FFCUser, String>> BUILTINS = ImmutableMap.of("Name", USERNAME, "KeyId", KEY, "Country", COUNTRY, "Email", EMAIL);


    private final String userName;
    private final String email;
    private final String key;
    private final String country;
    //TODO property for generic type
    private final Map<String, String> custom;


    private FFCUser(Builder builder) {
        String key = builder.key;
        checkArgument(StringUtils.isNotBlank(key), "Key shouldn't be empty");
        this.key = key;
        this.email = builder.email == null ? "" : builder.email;
        this.userName = builder.userName == null ? "" : builder.userName;
        this.country = builder.country == null ? "" : builder.country;
        ImmutableMap.Builder<String, String> map = ImmutableMap.builder();
        for (Map.Entry<String, String> entry : builder.custom.entrySet()) {
            if (!BUILTINS.containsKey(entry.getKey())) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        this.custom = map.build();
    }

    /**
     * returns user's name if presence
     *
     * @return a string or null
     */
    public String getUserName() {
        return userName;
    }

    /**
     * returns user's email if presence
     *
     * @return a string or null
     */
    public String getEmail() {
        return email;
    }

    /**
     * returns user's unique key.
     *
     * @return a string
     */
    public String getKey() {
        return key;
    }

    /**
     * returns user's country if presence
     *
     * @return a string or null
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns a map of all custom attributes set for this user
     *
     * @return a map, note that this map is readonly
     */
    public Map<String, String> getCustom() {
        return custom;
    }

    /**
     * Gets the value of a user attribute, if present.
     * This can be either a built-in attribute or a custom one
     *
     * @param attribute – the attribute to get
     * @return the attribute value or null
     */
    public String getProperty(String attribute) {
        Function<FFCUser, String> f = BUILTINS.get(attribute);
        if (f == null) {
            return custom.get(attribute);
        }
        return f.apply(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FFCUser ffcUser = (FFCUser) o;
        return Objects.equals(userName, ffcUser.userName) && Objects.equals(email, ffcUser.email) && Objects.equals(key, ffcUser.key) && Objects.equals(country, ffcUser.country) && Objects.equals(custom, ffcUser.custom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, email, key, country, custom);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("userName", userName).add("email", email).add("key", key).add("country", country).add("custom", custom).toString();
    }

    /**
     * A builder  that helps construct FFCClient objects. Builder calls can be chained, supporting the following pattern:
     * <pre><code>
     *     FFCClient user = new FFCClient.Builder("key")
     *               .userName("name")
     *               .country("CN")
     *               .build()
     * </code></pre>
     */

    public static class Builder {
        private String userName;

        private String email;

        private String key;

        private String country;

        private final Map<String, String> custom = new HashMap<>();

        /**
         * Creates a builder with the specified key
         *
         * @param key
         */
        public Builder(String key) {
            this.key = key;
        }

        /**
         * Changes the user's key.
         *
         * @param s
         * @return the builder
         */
        public FFCUser.Builder key(String s) {
            this.key = s;
            return this;
        }

        /**
         * set the user's email.
         *
         * @param s
         * @return the builder
         */
        public FFCUser.Builder email(String s) {
            this.email = s;
            return this;
        }

        /**
         * set the user's userName.
         *
         * @param s
         * @return the builder
         */
        public FFCUser.Builder userName(String s) {
            this.userName = s;
            return this;
        }

        /**
         * set the user's country.
         *
         * @param s
         * @return the builder
         */
        public FFCUser.Builder country(String s) {
            this.country = s;
            return this;
        }

        /**
         * Adds a String-valued custom attribute. When set to one of the built-in user attribute keys
         * @param key custom attribute name
         * @param value custom attribute value
         * @return
         */
        public FFCUser.Builder custom(String key, String value) {
            if (StringUtils.isNotBlank(key) && value != null) {
                custom.put(key, value);
            }
            return this;
        }

        /**
         * Builds the configured FFCUser object.
         * Returns the FFCUser configured by this builder
         */
        public FFCUser build() {
            return new FFCUser(this);
        }
    }

}
