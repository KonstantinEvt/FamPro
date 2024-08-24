package Joke;

import java.util.*;

public class CollectonOut {
    public static void main(String[] args) {

    }
    public static <K,V> Map<V,Collection<K>> getUniCol(Map<K,V> map){
        Map<V,Collection<K>> revers=new HashMap<>();
        for (K k: map.keySet()){
            Set<K> kss=new HashSet<>();
            kss.add(k);
            revers.merge(map.get(k),kss,(x,y)->{x.add(k);return x;});
        }
        return revers;
    }
    public static <K,V> Map<V,Collection<K>> getReversMap(Map<K,V> map){
        Map<V,Collection<K>> revers=new HashMap<>();
  HashSet<V> val= (HashSet<V>) map.values();
        return revers;
    }

}
