import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;

public class Statistic {
    private final String ALGORITHM_TYPE = "Interactive (preemptive)";
    private final String ALGORITHM_NAME = "Fair-Share Scheduling";
    private final Vector<User> USERS_ACTIVE;
    private final Vector<User> USERS_DONE;
    private final int totalComputationTime;
    private final int MEAN_DEVIATION;
    private final int STANDARD_DEVIATION;
    private int quantum;
    public Statistic(Vector<User> usersWithActiveProcess, Vector<User> usersAllProcessDone, int runtime, int meanDeviation, int standardDeviation, int quantum){
        this.USERS_ACTIVE = usersWithActiveProcess;
        this.USERS_DONE   = usersAllProcessDone;
        totalComputationTime = runtime;
        this.MEAN_DEVIATION = meanDeviation;
        this.STANDARD_DEVIATION = standardDeviation;
        this.quantum = quantum;
    }

    private void printProcesses(PrintStream resultsFile, User user, Vector<CustomProcess> userProcesses, String processType, String usersType){
        for(CustomProcess process : userProcesses){
            resultsFile.print(user.getID() + "\t");
            resultsFile.print(usersType + "\t");
            resultsFile.print(process.getID() + "\t");
            resultsFile.print(processType + "\t");
            resultsFile.print(process.getCpuTimeLimit() + "\t\t");
            resultsFile.print(process.getIOBlockingTime() + "\t\t");
            resultsFile.print(process.getProcessExecutionTime() + "\t\t");
            resultsFile.print(process.getTimesCalled() + "\t\t");
            resultsFile.print(process.getBlockingCount());
            resultsFile.println();
        }
    }
    private void printUsers(PrintStream resultsFile, Vector<User> users, String usersType){
        for(User user : users){
            final Vector<CustomProcess> tempActive = user.getAllActiveProcesses();
            final Vector<CustomProcess> tempFinished = user.getAllFinishedProcesses();
            printProcesses(resultsFile, user, tempActive, "Not finished", usersType);
            printProcesses(resultsFile, user, tempFinished, "Finished", usersType);
        }
    }
    public void printStatistic() {
        try {
            PrintStream resultsFile = new PrintStream(new FileOutputStream(Utility.RESULT_FILE_URL));
            resultsFile.println("Scheduling Type: " + ALGORITHM_TYPE);
            resultsFile.println("Scheduling Name: " + ALGORITHM_NAME);
            resultsFile.println("Simulation Run Time: " + totalComputationTime);
            resultsFile.println("Mean: " + MEAN_DEVIATION);
            resultsFile.println("Standard Deviation: " + STANDARD_DEVIATION);
            resultsFile.println("Quantum: " + quantum);

            resultsFile.println("U #\tU St\t\tP #\tP St\t\tCPU Time\tIO B Time\tWork Time \tCall Count\tCPU B Count");
            printUsers(resultsFile, USERS_ACTIVE, "Active"+"\t");
            printUsers(resultsFile, USERS_DONE, "Finished");
            resultsFile.close();
        } catch (FileNotFoundException e) {
            throw new Error( Utility.RESULT_FILE_URL + " does not exist :(" );
        }

    }
}
