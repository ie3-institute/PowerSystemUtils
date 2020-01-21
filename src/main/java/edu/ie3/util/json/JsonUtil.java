/*
 * © 2019. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.util.json;

import com.google.gson.Gson;
import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Static wrapper class to perform JSON operations with {@link Gson}
 *
 * @author hiry
 * @version 0.1
 * @since 2019-01-10
 */
public final class JsonUtil {

  private static final Gson gson = new Gson();

  private JsonUtil() {}

  public static String serializeToJson(Serializable o) {
    return gson.toJson(o);
  }

  public static <T> T deserializeToObject(String jsonString, Class<T> classOfObject) {
    return gson.fromJson(jsonString, classOfObject);
  }

  public static <T> T deserializeToObjectList(String jsonString, Type t) {
    return gson.fromJson(jsonString, t);
  }
}
