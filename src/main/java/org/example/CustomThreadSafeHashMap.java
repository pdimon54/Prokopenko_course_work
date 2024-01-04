package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomThreadSafeHashMap<K,V> {

  private List<Map<K,V>> segments;

  public boolean readyFlag;
  private int segmentsNum;

  public CustomThreadSafeHashMap(){
    this.segmentsNum = 16;
    this.segments = new ArrayList<>(segmentsNum);
    for (int i = 0; i < segmentsNum; i++) {
      segments.add(new HashMap<>());
    }
    readyFlag = false;
  }

  public void setFlag(boolean flag){
    this.readyFlag = flag;
  }

  public int getIndexOfSegment(K key){
    return Math.abs(key.hashCode()% segmentsNum);
  }

  public V get(K key){
    int segment = getIndexOfSegment(key);

    synchronized (segments.get(segment)){
      return segments.get(segment).get(key);
    }

  }

  public boolean isEmpty(){
    for (int i = 0; i < segmentsNum; i++) {
      if(!segments.get(segmentsNum).isEmpty())
        return false;
    }
    return true;
  }

  public boolean isPresent(K key) {
    int segment = getIndexOfSegment(key);

    synchronized (segments.get(segment)){
      return segments.get(segment).containsKey(key);
    }
  }


  public void put(K key, V value){
    int segment = getIndexOfSegment(key);
    synchronized (segments.get(segment)){
      segments.get(segment).put(key,value);
    }
  }


}
