package org.example;

public class CustomThreadPool{

  private final int threadNumber;
  private final CustomClientsQueue<Runnable> taskQueue;
  private final ThreadPoolWorker[] threadList;

  public CustomThreadPool(int threadNumber){
    this.threadNumber = threadNumber;
    this.taskQueue = new CustomClientsQueue<>(threadNumber);
    this.threadList = new ThreadPoolWorker[threadNumber];

    for (int i = 0; i < threadNumber; i++) {
      threadList[i]=new ThreadPoolWorker();
      threadList[i].start();
    }
  }

  public void execute(Runnable task){
    synchronized (taskQueue){
      taskQueue.add(task);
      taskQueue.notify();
    }
  }

  public void shutdown() {
    for (ThreadPoolWorker thread : threadList) {
      thread.runFlag=false;
      thread.interrupt();
    }
  }


  private class ThreadPoolWorker extends Thread{

    private volatile boolean runFlag = true;
    @Override
    public void run(){
      Runnable task;

      while (runFlag) {
        synchronized (taskQueue) {
          while (taskQueue.isEmpty()) {
            try {
              taskQueue.wait();
            } catch (InterruptedException e) {
              System.out.println("Waiting error: " + e.getMessage());
            }
          }
          try {
            task = taskQueue.remove();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }

        try {
          task.run();
        } catch (RuntimeException e) {
          System.out.println("Thread pool is interrupted: " + e.getMessage());
        }
      }

    }
  }
}
