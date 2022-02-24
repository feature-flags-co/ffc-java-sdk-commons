package co.featureflags.commons.model;

import co.featureflags.commons.json.JsonHelper;
import com.google.common.base.MoreObjects;
import com.google.common.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * An object returned by the SDK's "variation detail" methods such as boolVariationDetail,
 * combining the result of a flag evaluation with an explanation of how it was calculated.
 *
 * @param <T> - String/Boolean/Numeric Type
 */
public final class EvalDetail<T> implements Serializable {

    private static final int NO_VARIATION = -1;

    private final T variation;

    private final int id;

    private final String reason;

    private final String name;

    private final String keyName;

    private EvalDetail(T variation,
                       int id,
                       String reason,
                       String keyName,
                       String name) {
        this.variation = variation;
        this.id = id;
        this.reason = reason;
        this.keyName = keyName;
        this.name = name;
    }

    /**
     * build method, this method is only for internal use
     *
     * @param variation
     * @param id
     * @param reason
     * @param keyName
     * @param name
     * @param <T> String/Boolean/Numeric Type
     * @return an EvalDetail
     */
    public static <T> EvalDetail<T> of(T variation,
                                       int id,
                                       String reason,
                                       String keyName,
                                       String name) {
        return new EvalDetail(variation, id, reason, keyName, name);
    }

    /**
     * build the method from a json string, this method is only for internal use
     *
     * @param json
     * @param cls
     * @param <T>  String/Boolean/Numeric Type
     * @return an EvalDetail
     */
    public static <T> EvalDetail<T> fromJson(String json, Class<T> cls) {
        Type type = new TypeToken<EvalDetail<T>>() {
        }.getType();
        return JsonHelper.deserialize(json, type);
    }

    /**
     * return a feature flag evaluation value
     *
     * @return the flag value
     */
    public T getVariation() {
        return variation;
    }

    /**
     * The id of the returned value within the flag's list of variations
     * In fact this value is an index, e.g. 0 for the first variation
     * this value is only for internal use
     *
     * @return a integer value
     */
    public int getId() {
        return id;
    }

    /**
     * get the reason that evaluate the flag value.
     *
     * @return a string
     */
    public String getReason() {
        return reason;
    }

    /**
     * name of the flag associated
     *
     * @return a string
     */
    public String getName() {
        return name;
    }

    /**
     * key name of the flag associated
     *
     * @return a string
     */
    public String getKeyName() {
        return keyName;
    }

    /**
     * Returns true if the flag evaluation returned a good value,
     * false if the default value returned
     *
     * @return Returns true if the flag evaluation returned a good value, false if the default value returned
     */
    public boolean isSuccess() {
        return id >= 0;
    }

    /**
     * object converted to json string
     *
     * @return a json string
     */
    public String jsonfy() {
        return JsonHelper.serialize(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvalDetail<?> that = (EvalDetail<?>) o;
        return id == that.id && Objects.equals(variation, that.variation) && Objects.equals(reason, that.reason) && Objects.equals(name, that.name) && Objects.equals(keyName, that.keyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variation, id, reason, name, keyName);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("variation", variation)
                .add("id", id)
                .add("reason", reason)
                .add("name", name)
                .add("keyName", keyName)
                .toString();
    }

    public FlagState toFlagState() {
        return FlagState.of(this);
    }

}
