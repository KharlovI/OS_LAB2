public class Results {
  private final String schedulingType;
  private final String schedulingName;
  private final int computationTime;

  public Results (String schedulingType, String schedulingName, int computationTime) {
    this.schedulingType = schedulingType;
    this.schedulingName = schedulingName;
    this.computationTime = computationTime;
  }

  public String getSchedulingType(){
    return schedulingType;
  }
  public String getSchedulingName(){
    return schedulingName;
  }
  public int getComputationTime(){
    return computationTime;
  }
}
