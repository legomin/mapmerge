package org.legomin.merger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JUnit should be here,
 * but `3) No​ ​ third-parties​ ​ for​ ​ this​ ​ task​ ​ (only​ ​ standard​ ​ library)` ))
 */
public class MapsMergerTest {

  public static void main(String[] args) {
    mergeTestInt();
    mergeTestString();
    mergeListsTests();
  }

  private static void mergeTestInt() {
    final MapsMerger<String, Integer> merger = new MapsMerger<>();

    final Map<String, Integer> map1 = new HashMap<>();
    final Map<String, Integer> map2 = new HashMap<>();
    map1.put("key1", 20);
    map1.put("key2", 30);
    map2.put("key3", 40);
    map2.put("key1", 50);

    Map<String, Integer> resultMap = merger.merge(map1, map2, (key1, key2) -> key1 + key2);
    assert resultMap.get("key1") == 70 : "Unexpected value";
    log(map2 + ", " + map1, resultMap, "a + b", "merge");

    resultMap = merger.merge(map1, map2, (key1, key2) -> key1 * key2);
    assert resultMap.get("key1") == 1000 : "Unexpected value";
    log(map2 + ", " + map1, resultMap, "a * b", "merge");
  }

  private static void mergeTestString() {
    final MapsMerger<String, String> merger = new MapsMerger<>();

    final Map<String, String> map1 = new HashMap<>();
    final Map<String, String> map2 = new HashMap<>();
    map1.put("key1", "one");
    map1.put("key2", "two");
    map2.put("key3", "three");
    map2.put("key1", "four");

    Map<String, String> resultMap = merger.merge(map1, map2, (key1, key2) -> key1 + key2);
    assert "onefour".equals(resultMap.get("key1")) : "Unexpected value";
    log(map1 + ", " + map2, resultMap, "a + b", "merge");

    resultMap = merger.merge(map2, map1, (key1, key2) -> key1 + key2);
    assert "fourone".equals(resultMap.get("key1")) : "Unexpected value";
    log(map2 + ", " + map1, resultMap, "a + b", "merge");
  }

  private static void mergeListsTests() {
    final MapsMerger<String, String> merger = new MapsMerger<>();

    final Map<String, String> map1 = new HashMap<>();
    final Map<String, String> map2 = new HashMap<>();
    final Map<String, String> map3 = new HashMap<>();
    map1.put("key1", "one");
    map1.put("key2", "two");
    map2.put("key3", "three");
    map2.put("key1", "four");
    map3.put("key2", "five");
    map3.put("key4", "six");

    final List<Map<String, String>> list = new ArrayList<>();
    list.add(map1);
    list.add(map2);
    list.add(map3);

    Map<String, String> resultMap = merger.mergeAssociative(list, (key1, key2) -> key1 + key2);
    assert "onefour".equals(resultMap.get("key1")) : "Unexpected value";
    assert "twofive".equals(resultMap.get("key2")) : "Unexpected value";
    log(list, resultMap, "a + b", "mergeAssociative");

    resultMap = merger.mergeNotAssociative(list, (key1, key2) -> key1 + key2);
    assert "onefour".equals(resultMap.get("key1")) : "Unexpected value";
    assert "twofive".equals(resultMap.get("key2")) : "Unexpected value";
    log(list, resultMap, "a + b", "mergeNotAssociative");
  }

  private static void log(Object input, Object output, String behavior, String func) {
    System.out.println("Input: " + input);
    System.out.println("function: " + func + ", behavior: " + behavior);
    System.out.println("Output: " + output + "\n");
  }

}