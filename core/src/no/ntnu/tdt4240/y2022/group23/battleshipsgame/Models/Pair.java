package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

//Creates a mutable Pair
public class Pair<K,V> {

    private final K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}