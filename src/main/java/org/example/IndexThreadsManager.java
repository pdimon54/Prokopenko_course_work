package org.example;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IndexThreadsManager {

  public static CustomThreadSafeHashMap<String, Set<String>> indexMap = new CustomThreadSafeHashMap<>();



  public static CustomThreadSafeHashMap getMap() {
    return indexMap;
  }



  public static void indexing(int numThreads, List<File> files) throws InterruptedException {
    IndexThread[] threadForIndexers = new IndexThread[numThreads];

    for (int i = 0; i < numThreads; i++) {
      threadForIndexers[i] = new IndexThread((files.size() / numThreads) * i,
          (files.size() / numThreads) * (i + 1), files);
    }

    for (int i = 0; i < numThreads; i++) {
      threadForIndexers[i].start();
    }
    for (int i = 0; i < numThreads; i++) {
      threadForIndexers[i].join();
    }
    indexMap.setFlag(true);


  }


}
