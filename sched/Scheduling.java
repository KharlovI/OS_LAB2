// This file contains the main() function for the Scheduling
// simulation.  Init() initializes most of the variables by
// reading from a provided file.  SchedulingAlgorithm.Run() is
// called from main() to run the simulation.  Summary-Results
// is where the summary results are written, and Summary-Processes
// is where the process scheduling summary is written.

// Created by Alexander Reeder, 2001 January 06

import java.io.*;
import java.util.*;

public class Scheduling {
  private static Scheduler scheduler;
  private static void init(String[] args) throws IOException {
    File configFile = new File(Utility.CONFIG_FILE_URL);
    DataInputStream inputStream  = new DataInputStream(new FileInputStream(configFile));
    String currentString = inputStream.readLine();

    int totalCount        = Utility.defaultProcessCount;
    int meanDeviation     = Utility.defaultMeanDeviation;
    int standardDeviation = Utility.defaultStandardDeviation;
    int IOBlocking        = Utility.defaultIOBlocking;
    int runtime           = Utility.defaultRuntime;
    int usersCount        = Utility.defaultUsersCount;
    int quantum           = Utility.defaultQuantumTime;
    Vector<Integer> priorities = new Vector<>();
    Vector<CustomProcess> processes = new Vector<>();

    while (currentString != null) {
      if (currentString.startsWith("numprocess")) {
        StringTokenizer st = new StringTokenizer(currentString);
        st.nextToken();
        totalCount = Utility.StringToInt(st.nextToken());
      }
      else if (currentString.startsWith("quantum")){
        StringTokenizer st = new StringTokenizer(currentString);
        st.nextToken();
        quantum = Utility.StringToInt(st.nextToken());
      }
      else if (currentString.startsWith("numusers")){
        StringTokenizer st = new StringTokenizer(currentString);
        st.nextToken();
        usersCount = Utility.StringToInt(st.nextToken());
      }
      else if (currentString.startsWith("priority")){
        StringTokenizer st = new StringTokenizer(currentString);
        st.nextToken();
        priorities.add(Utility.StringToInt(st.nextToken()));
      }
      else if (currentString.startsWith("meandev")) {
        StringTokenizer st = new StringTokenizer(currentString);
        st.nextToken();
        meanDeviation = Utility.StringToInt(st.nextToken());
      }
      else if (currentString.startsWith("standdev")) {
        StringTokenizer st = new StringTokenizer(currentString);
        st.nextToken();
        standardDeviation = Utility.StringToInt(st.nextToken());
      }
      else if (currentString.startsWith("process")) {
        StringTokenizer st = new StringTokenizer(currentString);
        st.nextToken();
        IOBlocking = Utility.StringToInt(st.nextToken());
        int CPUTime = Utility.generateDistributedValue(meanDeviation, standardDeviation);
        processes.add(new CustomProcess(CPUTime, IOBlocking));
      }
      else if (currentString.startsWith("runtime")) {
        StringTokenizer st = new StringTokenizer(currentString);
        st.nextToken();
        runtime = Utility.StringToInt(st.nextToken());
        scheduler = new Scheduler(Utility.createUsers(processes,usersCount,priorities), runtime, meanDeviation, standardDeviation, quantum);
      }
      currentString = inputStream.readLine();
    }
    inputStream.close();
  }

  public static void main(String[] args){
    System.out.println(args[0]);
    try {
      init(args);
      scheduler.run();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

