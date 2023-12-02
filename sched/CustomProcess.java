public class CustomProcess {
    private static int totalCount = 0;
    private final int PROCESS_ID;
    private final int CPU_TIME_LIMIT;
    private final int INPUT_OUTPUT_BLOCKING_TIME;
    private int blockingCount = 0;
    private int processExecutionTime = 0;
    private int timesCalled = 0;
    public CustomProcess(int cpuTimeLimit, int blockingLimit){
        this.PROCESS_ID = totalCount;
        CustomProcess.totalCount++;
        if(cpuTimeLimit < 0)
            cpuTimeLimit = 0;
        if(blockingLimit < 0)
            blockingLimit = 0;
        this.CPU_TIME_LIMIT = cpuTimeLimit;
        this.INPUT_OUTPUT_BLOCKING_TIME = blockingLimit;
    }
    public void makeWork(int time){
        if(time <= 0)
            return;
        this.timesCalled++;
        this.processExecutionTime += time;
    }
    public void blockProcess(){
        this.blockingCount++;
    }
    public int getTimesCalled(){
        return this.timesCalled;
    }
    public int getBlockingCount(){
        return this.blockingCount;
    }
    public int getIOBlockingTime(){
        return this.INPUT_OUTPUT_BLOCKING_TIME;
    }
    public int getCpuTimeLimit(){
        return this.CPU_TIME_LIMIT;
    }
    public int getProcessExecutionTime(){
        return this.processExecutionTime;
    }
    public int getID(){
        return this.PROCESS_ID;
    }
}
