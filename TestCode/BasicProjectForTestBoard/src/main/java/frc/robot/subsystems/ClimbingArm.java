

package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class ClimbingArm extends SubsystemBase {

  static ClimbingArm subsystemInst = null;

  /**
   * Use this method to create a drivetrain instance. This method
   * ensures that the drivetrain class is a singleton, aka, that
   * only one drivetrain object ever gets created
   * @return
   */
  public static ClimbingArm getInstance() {
    if (subsystemInst == null) {
      return new ClimbingArm();
    } else {
      return subsystemInst;
    }
  }


  /**
   * Creates a new Drivetrain.
   */
  private ClimbingArm() {


  }

  public void logToNetworkTables(){



  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    logToNetworkTables();

  }


}