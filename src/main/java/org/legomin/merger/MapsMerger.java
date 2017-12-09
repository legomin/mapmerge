package org.legomin.merger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class MapsMerger<T, U> {

  /**
   * function merges two maps, applying BiFunction behavior to values, those keys are equals
   *
   * The behavior applies as (firstMapValue, secondMapValue) -> mergedValue
   * It matters for not commutative behavior, f.e. concatenation, where  a + b != b + a
   *
   * @param first - first map
   * @param second - second map
   * @param behavior - merging operation
   * @return - merged map
   */
  public Map<T, U> merge(Map<T, U> first, Map<T, U> second, BiFunction<U, U, U> behavior) {
    final Map<T, U> result;
    final Map<T, U> other;
    if (first.size() >= second.size()) {
      result = new HashMap<>(first);
      other = second;
    } else {
      result = new HashMap<>(second);
      other = first;
    }

    for (Map.Entry<T, U> pair : other.entrySet()) {
      if (result.containsKey(pair.getKey())) {
        result.put(pair.getKey(), behavior.apply(first.get(pair.getKey()), second.get(pair.getKey())));
      } else {
        result.put(pair.getKey(), pair.getValue());
      }
    }
    return result;
  }

  /**
   * function merges list maps, applying *associative* BiFunction behavior to values, those keys are equals
   * applies in parallel mode
   *
   * behavior should be associative, i.e. satisfy `apply(apply(a, b). c)  equals to apply(a, apply(b, c))`
   *
   * @param maps - list of maps
   * @param behavior - associative merging operation
   * @return - merged map
   */
  public Map<T, U> mergeAssociative(List<Map<T, U>> maps, BiFunction<U, U, U> behavior) {
    return merge(maps, behavior, true);
  }

  /**
   * function merges list maps, applying *not associative* BiFunction behavior to values, those keys are equals
   * applies in not parallel mode
   *
   * @param maps - list of maps
   * @param behavior - not associative merging operation
   * @return - merged map
   */
  public Map<T, U> mergeNotAssociative(List<Map<T, U>> maps, BiFunction<U, U, U> behavior) {
    return merge(maps, behavior, false);
  }

  private Map<T, U> merge(List<Map<T, U>> maps, BiFunction<U, U, U> behavior, boolean associative) {
    final Optional<Map<T, U>> result;
    if (associative) {
      result = maps.parallelStream().reduce((first, second) -> merge(first, second, behavior));
    } else {
      result = maps.stream().reduce((first, second) -> merge(first, second, behavior));
    }
    if (!result.isPresent()) {
      throw new RuntimeException("This is shouldn't be happen");
    }
    return result.get();
  }


}
