package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Storage extends SubsystemBase {

  static Storage subsystemInst = null;

  final NetworkTable subtable;
  
  final TalonSRX motor;

  /**
   * Use this method to create a Storage instance. This method
   * ensures that the Storage class is a singleton, aka, that
   * only one Storage object ever gets created
   */
  public static Storage getInstance() {
    if (subsystemInst == null) {
      return new Storage();
    } else {
      return subsystemInst;
    }
  }

  private Storage() {
    motor = new TalonSRX(Constants.StorageConstants.kMotorId);

    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    subtable = inst.getTable("storage");
  }

  public void off() {
    motor.set(ControlMode.PercentOutput, 0);
  }

  public void forward() {
    motor.set(ControlMode.PercentOutput, 0.3);
  }

  public void forwardSlow() {
    motor.set(ControlMode.PercentOutput, 0.15);
  }

  public void reverse() {
    motor.set(ControlMode.PercentOutput, -0.3);
  }

  public void resetPosition() {
    motor.setSelectedSensorPosition(0);
  }

  /**
   * Get position change since last reset in encoder raw units
   */
  public int getPositionChange() {
    return motor.getSelectedSensorPosition();
  }



  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    logToNetworkTables();

  }

  private void logToNetworkTables() {
    // Log motor voltage over network tables
    subtable.getEntry("voltage").setDouble(motor.getMotorOutputVoltage());

    // Log motor current over network tables
    subtable.getEntry("current").setDouble(motor.getStatorCurrent());

    //Log encoder val
    subtable.getEntry("encoderPosition").setDouble(getPositionChange());

  }

}