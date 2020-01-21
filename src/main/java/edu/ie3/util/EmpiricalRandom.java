/*
 * © 2019. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.eclipse.collections.impl.UnmodifiableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Returns random objects based on their empirical occurrence.
 *
 * @author Kittl
 * @since 27.03.2019
 */
public class EmpiricalRandom<C> {
  private static final Logger logger = LoggerFactory.getLogger(EmpiricalRandom.class);

  private final UnmodifiableMap<C, Double> empiricalCdf;
  private final Random uniformRandom;

  /**
   * Constructor for the empirical occurrence based random generator
   *
   * @param empiricalExistence Maps the different target objects to their frequence of occurrence
   * @param seed Random Seed
   */
  public EmpiricalRandom(Map<C, Double> empiricalExistence, long seed) {
    if (empiricalExistence == null)
      throw new NullPointerException("The map of empirical existences may not be null!");
    this.uniformRandom = new Random(seed);
    double totalSum = empiricalExistence.values().stream().mapToDouble(value -> value).sum();
    if (totalSum == 0)
      throw new IllegalArgumentException(
          "You may not provide an input map denoting, that the total amount of occurrences is zero.");
    double tempSum = 0;
    Map<C, Double> tempMap = new HashMap<>();
    for (Map.Entry<C, Double> entry : empiricalExistence.entrySet()) {
      tempSum += entry.getValue() / totalSum;
      tempMap.put(entry.getKey(), tempSum);
    }
    this.empiricalCdf = new UnmodifiableMap<>(tempMap);
  }

  /**
   * Constructor for the empirical occurrence based random generator
   *
   * @param empiricalExistence Maps the different target objects to their frequence of occurrence
   */
  public EmpiricalRandom(Map<C, Double> empiricalExistence) {
    this(empiricalExistence, System.nanoTime());
  }

  /**
   * Get the next object
   *
   * @return Get the next object
   */
  public C nextEmpirical() {
    double rand = uniformRandom.nextDouble();
    List<C> candidates =
        this.empiricalCdf.entrySet().stream()
            .filter(entry -> entry.getValue() >= rand)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    if (candidates.isEmpty()) {
      logger.error("There is no candidate, which is not supposed to happen. Take first one");
      return empiricalCdf.keySet().iterator().next();
    } else return candidates.get(candidates.size() - 1);
  }

  public UnmodifiableMap<C, Double> getEmpiricalCdf() {
    return empiricalCdf;
  }
}
