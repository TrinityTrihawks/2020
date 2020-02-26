
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class ClimbingArm extends SubsystemBase {

  private final VictorSPX telescope;
  private final VictorSPX winch;
  private static ClimbingArm subsystemInst = null;

  private final NetworkTable subtable;

  /**
   * Use this method to create a climbing arm instance. This method
   * ensures that the climbing arm class is a singleton, aka, that
   * only one climbing arm object ever gets created
   * @return
   */
  public static ClimbingArm getInstance() {
    if (subsystemInst == null) {
      subsystemInst = new ClimbingArm();
    }
    return subsystemInst;
  }


  /**
   * Creates a new ClimbingArm.
   */
  private ClimbingArm() {
    telescope = new VictorSPX(Constants.ClimbingConstants.TelescopeID);
    winch = new VictorSPX(Constants.ClimbingConstants.WinchId);

    subtable = NetworkTableInstance.getDefault().getTable("climbing");
  }
 

  public void moveUp()
  {
    telescope.set(ControlMode.PercentOutput, -0.3);
  }

  public void moveDown()
  {
    telescope.set(ControlMode.PercentOutput, 0.3);
    winch.set(ControlMode.PercentOutput, 1);
  } 

  public void winchUnwind() {
    winch.set(ControlMode.PercentOutput, -0.5);
  }

  public void stop()
  {
    telescope.set(ControlMode.PercentOutput, 0);
    winch.set(ControlMode.PercentOutput, 0);
  }

  public void logToNetworkTables(){
    subtable.getEntry("telescope_voltage").setDouble(telescope.getMotorOutputVoltage());
    subtable.getEntry("winch_voltage").setDouble(winch.getMotorOutputVoltage());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    logToNetworkTables();

  }


}