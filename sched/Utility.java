import java.security.SecureRandom;
import java.util.Vector;

public class Utility {
    public static final String RESULT_FILE_URL = "Summary-Results";
    public static final String PROCESSES_FILE_URL = "Summary-Processes"; //C:\moss\sched\
    public static final String CONFIG_FILE_URL = "scheduling.conf"; //C:\moss\sched\
    public static int defaultUserPriority      = 1;
    public static int defaultUsersCount        = 3;
    public static int defaultProcessCount      = 23;
    public static int defaultMeanDeviation     = 2000;      // unused
    public static int defaultStandardDeviation = 200;   // unused
    public static int defaultIOBlocking        = 75;
    public static int defaultRuntime           = 2000;
    public static int defaultQuantumTime       = 150;
    public static User chooseUser(Vector<User> users){
        int prioritySum = 0;
        int currentSum = 0;
        int randomValue;

        // total count of all priority
        for (User i : users){
            prioritySum += i.getPriority();
        }

        randomValue = random.nextInt(prioritySum);

        for(User i : users){
            currentSum += i.getPriority();
            if(currentSum > randomValue)
                return i;
        }

        // newer rich this point
        return null;
    }
    public static int StringToInt(String s){
        int i = 0;
        try {
            i = Integer.parseInt(s.trim());
        } catch (NumberFormatException nfe) {
            System.out.println("NumberFormatException: " + nfe.getMessage());
        }
        return i;
    }
    public static Vector<User> createUsers(Vector<CustomProcess> processes, int usersCount, Vector<Integer> priorities){
        final Vector<User> users = new Vector<>();

        final int processCount  = processes.size();
        final int oneUserCount  = processCount / (usersCount);

        for(int i = 0; i < usersCount - 1; i++){

            // the amount of priorities can be less than amount of users (commonly not)
            if(priorities.isEmpty())
                users.add(new User(defaultUserPriority, new Vector(processes.subList(i*oneUserCount, (i+1)*oneUserCount))));
            else
                users.add(new User(priorities.remove(0), new Vector(processes.subList(i*oneUserCount, (i+1)*oneUserCount))));
        }

        if(priorities.isEmpty())
            users.add(new User(defaultUserPriority, new Vector(processes.subList((usersCount - 1)*oneUserCount, processCount))));
        else
            users.add(new User(priorities.remove(0), new Vector(processes.subList((usersCount - 1)*oneUserCount, processCount))));

        return users;
    }
    public static int generateDistributedValue(int meanDeviation, int standardDeviation){
        return (int)random.nextGaussian(meanDeviation, standardDeviation);
    }
    private static final SecureRandom random = new SecureRandom();
}
