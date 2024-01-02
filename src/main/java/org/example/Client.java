package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Client {


  public static void main(String[] args) {
    try {
      Socket socket = new Socket("localhost", 5555);
      System.out.println("Connect to server: " + socket);

      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

//      System.out.print("Write the word: ");
//      String word = reader.readLine();
//
//      System.out.print("Threads count: ");
//      int threadsCount = Integer.parseInt(reader.readLine());

      Scanner scanner = new Scanner(System.in);

      String command = "";
      boolean workFlag = true;
      boolean indexingStatus = IndexThreadsManager.indexMap.readyFlag;
      while (workFlag) {

        PrintWriter write = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println("start: check indexing status");
        System.out.println("find: search a word in files");
        System.out.println("exit: stop work and close connection with server ");
        System.out.print("start/find/exit: ");
        command = scanner.nextLine();
        switch (command){
          case "start":{
            write.println(command);
//            read.readLine();
            if (read.readLine().equals("data has been indexed")){
              indexingStatus = true;
              System.out.println("Data has been indexed, write find command");

            } else {
              System.out.println("Wait, data indexing");
              System.out.println("Write thread number:");
              String threadNum = scanner.nextLine();
              write.println(threadNum);
              String timeMassage = read.readLine();
              System.out.println(timeMassage);
              indexingStatus = true;
            }
            break;
          }
          case "find":{
            write.println(command);
            if(indexingStatus){
              System.out.print("Write searching word:");
              String searchWord = scanner.nextLine();
              write.println(searchWord);
              String returnLine = read.readLine();
              String[] files = returnLine.split("[,]",0);
              for (int i = 0; i < files.length; i++) {
                System.out.println(files[i]);
              }

            }else {
              System.out.println("Wait for indexing. Try to start command");
            }
            break;
          }
          case "hi":{
            write.println(command);
            write.println("hello server");
            break;
          }
          case "exit":{
            write.println(command);
            String exitMassage = read.readLine();
            System.out.println(exitMassage);
            workFlag = false;
            break;
          }
          default:
            System.out.println("Unknown command.Try again");
        }
      }

      socket.close();
      System.out.println("Connection close");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
