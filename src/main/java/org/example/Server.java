package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

public class Server {


  public static void main(String[] args) {

    int threadNumber = 10;
    CustomThreadPool threadPool = new CustomThreadPool(threadNumber);



    try {
      ServerSocket socket
          = new ServerSocket(5555);
      System.out.println("Start server");



      while (true) {
        Socket clientSocket = socket.accept();
        System.out.println("Connection with client: " + clientSocket);
        IndexThreadsManager indexThreadsManager = new IndexThreadsManager();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        threadPool.execute(new ClientThread(indexThreadsManager,clientSocket,bufferedReader,printWriter));
//        Thread t = new ClientThread(clientSocket,bufferedReader,printWriter);
//        t.start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

