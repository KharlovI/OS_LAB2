import java.util.Vector;

public class User {
    private final int ID;
    private final int priority; // the probability of choosing this Users' Process
    private final Vector<CustomProcess> ACTIVE_PROCESSES;
    private final Vector<CustomProcess> FINISHED_PROCESSES = new Vector<>();
    private boolean allProcessesAreDone = false;
    private static int totalCount = 0;
    public User(int priority, Vector<CustomProcess> processes){
        this.ID = User.totalCount;
        totalCount++;
        this.priority = priority;
        this.ACTIVE_PROCESSES = processes;
    }
    public void replaceLastProcess(){
        if(ACTIVE_PROCESSES.isEmpty())
            return;
        final int indexOfLastProcess = ACTIVE_PROCESSES.size() - 1;
        FINISHED_PROCESSES.add(ACTIVE_PROCESSES.remove(indexOfLastProcess));
        if(indexOfLastProcess == 0)
            allProcessesAreDone = true;
    }
    public boolean allProcessesAreDone(){
        return allProcessesAreDone;
    }

    // return the first element in vector and set in the end of the vector
    public CustomProcess getCurrentProcess(){
        if(ACTIVE_PROCESSES.isEmpty())
            return null;
        CustomProcess temp = ACTIVE_PROCESSES.remove(0);
        ACTIVE_PROCESSES.add(temp);
        return temp;
    }
    public Vector<CustomProcess> getAllActiveProcesses(){
        return ACTIVE_PROCESSES;
    }
    public Vector<CustomProcess> getAllFinishedProcesses(){
        return FINISHED_PROCESSES;
    }
    public int getPriority(){
        return priority;
    }
    public int getID(){return ID;}
}
