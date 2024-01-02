package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IndexThread extends Thread{
  private int start;
  private int end;

  private List<File> files;

  public IndexThread(int start,int end,List<File> files){
    this.start=start;
    this.end=end;
    this.files=files;
  }

  @Override
  public void run(){
    for (int i = start; i < end; i++) {
      byte[] bytesFileData;
      try {
        bytesFileData = Files.readAllBytes(files.get(i).toPath());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      String fileData = new String(bytesFileData, StandardCharsets.UTF_8);
      String[] words = fileData.split("\\W+");
      Arrays.stream(words).forEach(String::toLowerCase);
      System.out.println("flag1");
      List<String> wordsList = Arrays.stream(words).toList();
      System.out.println("flag2");
      for (String word:wordsList) {
        System.out.println("flag3");
        if(IndexThreadsManager.indexMap.isPresent(word)){
          IndexThreadsManager.indexMap.get(word).add(files.get(i).toPath().toString());
          System.out.println("flag4");
        }else {
          Set<String> filesStr = new HashSet<>();
          filesStr.add(files.get(i).toPath().toString());
          IndexThreadsManager.indexMap.put(word,filesStr);
          System.out.println("flag5");
        }
      }

    }
  }



}
