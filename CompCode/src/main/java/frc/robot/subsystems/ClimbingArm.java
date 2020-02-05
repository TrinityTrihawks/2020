
package frc.robot.subsystems;

import java.util.HashMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class ClimbingArm extends SubsystemBase {

  int idk = 1;
  final HashMap<String,TalonSRX> talons;
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
    talons = new HashMap<>();
    talons.put("CMU", new TalonSRX(0));
    talons.put("CMD", new TalonSRX(1));
    talons.put("WU", new TalonSRX(2));
    talons.put("WD", new TalonSRX(3));
  }
 
  public void ClimbingArmCommand(double number)
  {
    if (number == 0)
    {
      talons.get("CMU").set(ControlMode.PercentOutput, idk);
    }
    if (number == 1)
    {
      talons.get("CMU").set(ControlMode.PercentOutput, idk);
    }
    if (number == 2)
    {
      talons.get("CMU").set(ControlMode.PercentOutput, idk);
    }
  }

  public void ClimbingArmMannuallyUp()
  {
    talons.get("CMU").set(ControlMode.PercentOutput, idk);
  }

  public void ClimbingArmMannuallyDown()
  {
    talons.get("CMD").set(ControlMode.PercentOutput, idk);
  }
  
  public void WintchUp()
  {
    ClimbingArmMannuallyUp();
  }

  public void WintchDown()
  {
    ClimbingArmMannuallyDown();
  }

  public void logToNetworkTables(){



  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    logToNetworkTables();

  }


}