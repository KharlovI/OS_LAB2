import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;

public class Scheduler {
    private final Vector<User> USERS_WITH_ACTIVE_PROCESSES;
    private final Vector<User> USERS_DONE = new Vector<>();
    private final int RUN_TIME_LIMIT;
    private final int QUANTUM;
    private int runTime = 0;
    private int meanDeviation;
    private int standardDeviation;


    // Use for replacing the user, who's all processes are done
    private void ReplaceUser(User user){
        if(user == null)
            return;
        USERS_WITH_ACTIVE_PROCESSES.remove(user);
        USERS_DONE.add(user);
    }
    public Scheduler(Vector<User> users, int runTimeLimit, int meanDeviation, int standardDeviation, int quantum){
        this.USERS_WITH_ACTIVE_PROCESSES = users;
        this.RUN_TIME_LIMIT = runTimeLimit;
        this.meanDeviation = meanDeviation;
        this.standardDeviation = standardDeviation;
        this.QUANTUM = quantum;
    }
    public void run(){
        FairShareScheduling method = new FairShareScheduling();
        Statistic results = method.scheduling();
        results.printStatistic();
    }
    private class FairShareScheduling{
        public Statistic scheduling(){
            try {
                PrintStream processesFile = new PrintStream(new FileOutputStream(Utility.PROCESSES_FILE_URL));
                while(runTime < RUN_TIME_LIMIT) {
                    User currentUser = Utility.chooseUser(USERS_WITH_ACTIVE_PROCESSES);
                    CustomProcess currentProcess = currentUser.getCurrentProcess();
                    assert currentProcess != null;

                    processesFile.println("User " + currentUser.getID() + " process " + currentProcess.getID() + " registered...");
                    final int timeToProcessEnd = currentProcess.getCpuTimeLimit() - currentProcess.getProcessExecutionTime();
                    final int timeToProgramEnd = RUN_TIME_LIMIT - runTime;
                    int episodeTime;

                    // Program runtime is ended
                    if(timeToProgramEnd <= timeToProcessEnd && timeToProgramEnd <= currentProcess.getIOBlockingTime() && timeToProgramEnd <= QUANTUM){
                        episodeTime = timeToProgramEnd;
                        currentProcess.makeWork(episodeTime);
                        runTime += episodeTime;
                        processesFile.println("Out of time (" + runTime + ")");
                        return new Statistic(USERS_WITH_ACTIVE_PROCESSES, USERS_DONE, runTime, meanDeviation, standardDeviation, QUANTUM);
                    }

                    // Process finished his task
                    if(timeToProcessEnd <= QUANTUM && timeToProcessEnd <= currentProcess.getIOBlockingTime()){
                        episodeTime = timeToProcessEnd;
                        currentProcess.makeWork(episodeTime);
                        String currentProcessParams = currentProcess.getCpuTimeLimit() + " " + currentProcess.getProcessExecutionTime() + " " + currentProcess.getIOBlockingTime() + " " + currentProcess.getBlockingCount();
                        processesFile.println("User " + currentUser.getID() + " process " + currentProcess.getID() + " completed... ("  + currentProcessParams + ")");
                        currentUser.replaceLastProcess();
                        runTime += episodeTime;
                        if(currentUser.allProcessesAreDone()){
                            processesFile.println("User " + currentUser.getID() +  " finished all processes");
                            ReplaceUser(currentUser);
                            if(USERS_WITH_ACTIVE_PROCESSES.isEmpty()) {
                                processesFile.println("All users are done. Program has been finished ");
                                return new Statistic(USERS_WITH_ACTIVE_PROCESSES, USERS_DONE, runTime, meanDeviation, standardDeviation, QUANTUM);
                            }
                        }
                    }
                    // Process blocked (increment blocked count)
                    else if(QUANTUM > currentProcess.getIOBlockingTime()){
                        episodeTime = currentProcess.getIOBlockingTime();
                        runTime += episodeTime;
                        currentProcess.blockProcess();
                        currentProcess.makeWork(episodeTime);
                        String currentProcessParams = currentProcess.getCpuTimeLimit() + " " + currentProcess.getProcessExecutionTime() + " " + currentProcess.getIOBlockingTime() + " " + currentProcess.getBlockingCount();
                        processesFile.println("User " + currentUser.getID() + " process " + currentProcess.getID() + " I/O blocked...("+ currentProcessParams + ")");
                    }

                    // Quantum time has gone
                    else{
                        episodeTime = QUANTUM;
                        currentProcess.makeWork(episodeTime);
                        String currentProcessParams = currentProcess.getCpuTimeLimit() + " " + currentProcess.getProcessExecutionTime() + " " + currentProcess.getIOBlockingTime() + " " + currentProcess.getBlockingCount();
                        processesFile.println("User " + currentUser.getID() + " process " + currentProcess.getID() + " quantum limit ("  + currentProcessParams + ")");
                        runTime += episodeTime;
                    }
                }

                // should not be achieved
                System.out.println("You achieve 45 string in scheduling method (FairShareScheduling) :(");
                return new Statistic(USERS_WITH_ACTIVE_PROCESSES, USERS_DONE, runTime, meanDeviation, standardDeviation, QUANTUM);
            } catch (FileNotFoundException e) {
                throw new Error(Utility.RESULT_FILE_URL + " file not found :(");
            }
        }
    }
}
