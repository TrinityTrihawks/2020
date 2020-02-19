package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.StorageConstants;

public class Storage extends SubsystemBase {

  private static Storage subsystemInst = null;

  private final NetworkTable subtable;

  private final TalonSRX motor;

  //DigitalInput intakeSwitch;

  /**
   * Use this method to create a Storage instance. This method ensures that the
   * Storage class is a singleton, aka, that only one Storage object ever gets
   * created
   */
  public static Storage getInstance() {
    if (subsystemInst == null) {
      subsystemInst = new Storage();
    }
    return subsystemInst;
  }

  private Storage() {
    motor = new TalonSRX(StorageConstants.kMotorId);

    motor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 30);
    motor.setSensorPhase(true);

    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    subtable = inst.getTable("storage");

    //intakeSwitch = new DigitalInput(0);
  }

  public void off() {
    motor.set(ControlMode.PercentOutput, 0);
  }

  public void forward() {
    motor.set(ControlMode.PercentOutput, -0.3);
  }

  public void forwardSlow() {
    motor.set(ControlMode.PercentOutput, -0.15);
  }

  public void reverse() {
    motor.set(ControlMode.PercentOutput, 0.3);
  }

  public void resetPosition() {
    motor.setSelectedSensorPosition(0);
  }

  /**
   * Get position change since last reset in encoder raw units
   */
  public int getPosition() {
    return motor.getSelectedSensorPosition();
  }

  public boolean getIntakeSwitch() {
    return false;

    // // Invert because of how our limit switch is currently wired. Pressed should yeild true
    // return !intakeSwitch.get();
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

    // Log encoder val
    subtable.getEntry("encoderPosition").setDouble(getPosition());

  }

}