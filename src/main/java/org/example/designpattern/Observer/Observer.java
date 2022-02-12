package org.example.designpattern.Observer;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Observer<K, V> {
  private Map<K, Set<V>> observers = Maps.newHashMap();

  void register(K k, V v) {
    Set<V> set = observers.computeIfAbsent(k, key -> Sets.newHashSet());
    set.add(v);
  }

  void unregister(K k, V v) {
    if (!observers.containsKey(k)) {
      return;
    }

    Set<V> set = observers.get(k);

    set.remove(v);
  }

  void callbackObserver(K k, Consumer<V> callback) {
    Set<V> set = observers.computeIfAbsent(k, key -> Sets.newHashSet());
    set.forEach(callback);
  }
}
