package org.example;

import java.util.LinkedList;
import java.util.Queue;

public class CustomClientsQueue<T> {

  private int queueSize;
  private Queue<T> queue = new LinkedList<>();

  public CustomClientsQueue(int queueSize){
    this.queueSize=queueSize;
  }

  public synchronized boolean add(T task){
    if(queue.size()==queueSize)
      return false;
    queue.offer(task);
    return true;

  }

  public synchronized T remove() throws InterruptedException {
    if (queue.isEmpty())
      wait();
    T removeElem = queue.poll();
    notifyAll();
    return removeElem;
  }
  public synchronized boolean isEmpty()  {
    if (queue.isEmpty())
      return true;
    return false;
  }
}
