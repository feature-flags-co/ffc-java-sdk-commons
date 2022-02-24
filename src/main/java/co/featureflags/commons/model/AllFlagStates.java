package co.featureflags.commons.model;

import co.featureflags.commons.json.JsonHelper;
import com.google.common.base.MoreObjects;
import com.google.common.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * The object provides a standard return responding the request of getting all flag values from a client sdk
 *
 * @param <T> String/Boolean/Numeric Type
 */
public final class AllFlagStates<T> extends BasicState implements Serializable {
    private final List<EvalDetail<T>> data;

    private AllFlagStates(boolean success, String message, List<EvalDetail<T>> data) {
        super(success, success ? "OK" : message);
        this.data = data;
    }

    /**
     * build a AllFlagStates without flag value
     *
     * @param message the reason without flag value
     * @return a AllFlagStates
     */
    public static <T> AllFlagStates<T> empty(String message) {
        return new AllFlagStates(false, message, null);
    }

    /**
     * build a AllFlagStates
     *
     * @param success true if the last request is successful
     * @param message the reason
     * @param data    all flag values
     * @param <T>     String/Boolean/Numeric Type
     * @return a AllFlagStates
     */
    public static <T> AllFlagStates<T> of(boolean success, String message, List<EvalDetail<T>> data) {
        return new AllFlagStates(success, success ? "OK" : message, data);
    }

    /**
     * build a AllFlagStates from json
     *
     * @param json a string json
     * @param cls
     * @param <T>  String/Boolean/Numeric Type
     * @return a AllFlagStates
     */
    public static <T> AllFlagStates<T> fromJson(String json, Class<T> cls) {
        return JsonHelper.deserialize(json, new TypeToken<AllFlagStates<T>>() {}.getType());
    }

    /**
     * return details of all the flags
     *
     * @return a list of {@link EvalDetail}
     */
    public List<EvalDetail<T>> getData() {
        return data;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("success", success)
                .add("message", message)
                .add("data", data)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllFlagStates<?> that = (AllFlagStates<?>) o;
        return Objects.equals(data, that.data) && Objects.equals(message, that.message)
                && Objects.equals(success, that.success);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, message, data);
    }
}
