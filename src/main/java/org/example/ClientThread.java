package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientThread extends Thread {

  private IndexThreadsManager indexThreadsManager;
  private PrintWriter printWriter;
  private BufferedReader bufferedReader;
  private Socket socket;

  public ClientThread(IndexThreadsManager indexThreadsManager,Socket socket, BufferedReader bufferedReader, PrintWriter printWriter) {
    this.indexThreadsManager = indexThreadsManager;
    this.socket = socket;
    this.bufferedReader = bufferedReader;
    this.printWriter = printWriter;


  }

  @Override
  public void run() {
    String clientCommand = "";
    while (true) {


      try {
        clientCommand = bufferedReader.readLine();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      switch (clientCommand) {
        case ("start"): {
          if (!indexThreadsManager.indexMap.readyFlag) {
            try {
              printWriter.println("data not indexed");
              int num = Integer.parseInt(bufferedReader.readLine());

              File resources = new File("C:\\Users\\Dmytro\\OneDrive\\Рабочий стол\\IASA CS\\4 курс\\1 семестр\\CW\\CW\\resources");
              System.out.println("Number of threads:" + num);
              List<File> listFiles = FileWorker.buildDocumentsList(resources);
              //System.out.println(listFiles.get(456));
              //System.out.println(listFiles.size());
              long start = System.currentTimeMillis();
              indexThreadsManager.indexing(num, listFiles);
              long time = System.currentTimeMillis() - start;

              printWriter.println("Time build: " + time);
            } catch (IOException e) {
              throw new RuntimeException(e);
            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }
          } else {
            printWriter.println("data has been indexed");
          }
          break;
        }
        case ("find"): {
            try {
              String word = bufferedReader.readLine();
              System.out.println(word);
              printWriter.println(indexThreadsManager.indexMap.get(word));

            } catch (IOException e) {
              throw new RuntimeException(e);
          }
          break;
        }
//        case ("hi"): {
//
//          try {
//            String answer = bufferedReader.readLine();
//            System.out.println(answer);
//
//          } catch (IOException e) {
//            throw new RuntimeException(e);
//          }
//          break;
//        }
        case ("exit"): {
          printWriter.write("thanks");
          try {
            socket.close();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          break;
        }
        default:
          System.out.println("Unknown command");
          System.out.println("start: check indexing status");
          System.out.println("find: search a word in files");
          System.out.println("exit: stop work and close connection with server ");
      }
    }
  }

}
