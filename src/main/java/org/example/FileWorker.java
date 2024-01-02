package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileWorker {

  private static List<File> documentList = new ArrayList<>();

  public static List<File> buildDocumentsList(File path){
    if(path.isDirectory()){
      File[] fileNames = path.listFiles();
      for (int i = 0; i < fileNames.length; i++) {
        buildDocumentsList(fileNames[i]);
      }
    }else{
      documentList.add(path);
    }
    return documentList;
  }
}
