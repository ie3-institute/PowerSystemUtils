/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A map that also contains an inverse map (e.g. mapping X to Y will map Y to X in the inverse map)
 *
 * @deprecated Is this still used somewhere? If so, please let us know.
 */
@Deprecated(since = "2.0")
public class OneToOneMap<X, Y> extends HashMap<X, Y> {

  private static final long serialVersionUID = 6156332808162504460L;
  private final InverseMap inverse;

  public OneToOneMap() {
    this.inverse = new InverseMap();
  }

  public OneToOneMap(int initialCapacity) {
    super(initialCapacity);
    inverse = new InverseMap(initialCapacity);
  }

  public OneToOneMap(Map<X, Y> existingMap) {
    this(existingMap.size());
    this.putAll(existingMap);
  }

  @Override
  public final Y put(X key, Y value) {
    inverse.put(value, key);
    return super.put(key, value);
  }

  @Override
  public Y remove(Object key) {
    Y val = super.get(key);
    inverse.remove(val);
    return super.remove(key);
  }

  public X removeValue(Y val) {
    X key = inverse.get(val);
    super.remove(key);
    return inverse.remove(val);
  }

  public X getKey(Y value) {
    return inverse.get(value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    OneToOneMap<?, ?> that = (OneToOneMap<?, ?>) o;
    return inverse.equals(that.inverse);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), inverse);
  }

  private final class InverseMap extends HashMap<Y, X> {
    private static final long serialVersionUID = 6156332808162576160L;

    public InverseMap(int initialCapacity, float loadFactor) {
      super(initialCapacity, loadFactor);
    }

    public InverseMap(int initialCapacity) {
      super(initialCapacity);
    }

    public InverseMap() {
      super();
    }

    public InverseMap(Map<? extends Y, ? extends X> m) {
      super(m);
    }
  }
}
