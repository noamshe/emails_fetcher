package noam;

/**
 * Created by noam on 18/10/14.
 */
public class Report {
  String directoryName;
  boolean success;
  String failureMsg;

  public Report(String directoryName, boolean success, String failureMsg) {
    this.directoryName = directoryName;
    this.success = success;
    this.failureMsg = failureMsg;
  }
}
