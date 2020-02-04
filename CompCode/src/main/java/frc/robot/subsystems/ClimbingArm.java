
package frc.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class ClimbingArm extends SubsystemBase {

  int idk = 1;
  final TalonSRX telescope;
  final TalonSRX winch;
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
   * Creates a new ClimbingArm.
   */
  private ClimbingArm() {
    telescope = new TalonSRX(0);
    winch = new TalonSRX(0);
  }
 

  public void moveUp()
  {
    telescope.set(ControlMode.PercentOutput, idk);
    winch.set(ControlMode.PercentOutput, idk);
  }

  public void moveDown()
  {
    telescope.set(ControlMode.PercentOutput, idk);
    winch.set(ControlMode.PercentOutput, idk);
  }

  public void stop()
  {
    telescope.set(ControlMode.PercentOutput, 0);
    winch.set(ControlMode.PercentOutput, 0);
  }

  public void logToNetworkTables(){



  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    logToNetworkTables();

  }


}