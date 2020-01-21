/*
 * © 2019. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.util;

import java.util.HashMap;
import java.util.Map;

/**
 * A map that also contains an inverse map (e.g. mapping X to Y will map Y to X in the inverse map)
 *
 * @author roemer
 * @version 1
 * @since 22.08.2017
 */
public class OneToOneMap<Type1, Type2> extends HashMap<Type1, Type2> {

  private static final long serialVersionUID = 6156332808162504460L;
  private Map<Type2, Type1> inverse;

  public OneToOneMap(int initialCapacity) {
    super(initialCapacity);
    inverse = new HashMap<>(initialCapacity);
  }

  public OneToOneMap(HashMap<Type1, Type2> existingMap) {
    this(existingMap.size());
    for (Map.Entry<Type1, Type2> entry : existingMap.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public Type2 put(Type1 key, Type2 value) {
    inverse.put(value, key);
    return super.put(key, value);
  }

  @Override
  public Type2 remove(Object key) {
    Type2 val = super.get(key);
    inverse.remove(val);
    return super.remove(key);
  }

  public Type1 removeValue(Object val) {
    Type1 key = inverse.get(val);
    super.remove(key);
    return inverse.remove(val);
  }

  public Type1 getKey(Type2 value) {
    return inverse.get(value);
  }

  public Type1 getInverse(Object value) {
    return inverse.get(value);
  }
}
