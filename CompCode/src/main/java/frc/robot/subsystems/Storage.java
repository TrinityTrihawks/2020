package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
//import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.StorageConstants;

public class Storage extends SubsystemBase {

  private static Storage subsystemInst = null;

  private final NetworkTable subtable;

  private final TalonSRX motor;

  DigitalInput lowLimitSwitch;
  DigitalInput midLimitSwitch;
  DigitalInput highLimitSwitch;

  public static enum SwitchSelector {
    low, mid, high
  }

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
    subtable.getEntry("should_log").setBoolean(false);

    subtable.getEntry("beltSpeed").setDouble(-0.3);

    lowLimitSwitch = new DigitalInput(0);
    midLimitSwitch = new DigitalInput(1);
    highLimitSwitch = new DigitalInput(2);

  }

  public void autoForward() {
    motor.set(ControlMode.PercentOutput, -0.7);
  }

  public void off() {
    motor.set(ControlMode.PercentOutput, 0);
  }

  public void forward() {
    motor.set(ControlMode.PercentOutput, -0.3);
  }

  public void forwardBoost() {
    motor.set(ControlMode.PercentOutput, -0.6);
  }

  public void reverse() {
    motor.set(ControlMode.PercentOutput, 0.3);
  }

  public void reverseBoost() {
    motor.set(ControlMode.PercentOutput, 0.6);
  }

  // public boolean lowerTriggered() {
  // return !lowerLimitSwitch.get();
  // }

  // public boolean middleTriggered() {
  // return !midLimitSwitch.get();
  // }

  // public boolean highTriggered() {
  // return !higherLimitSwitch.get();
  // }

  public boolean getBeamTrigger(SwitchSelector selector) {
    switch (selector) {
    case low:
      return !lowLimitSwitch.get();
    case mid:
      return !midLimitSwitch.get();
    case high:
      return !highLimitSwitch.get();
    default:
      throw new IllegalArgumentException();
    }
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

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    logToNetworkTables();

  }

  private void logToNetworkTables() {
    if (subtable.getEntry("should_log").getBoolean(false)) {

      // Log motor voltage over network tables
      subtable.getEntry("voltage").setDouble(motor.getMotorOutputVoltage());
      // Log motor current over network tables
      subtable.getEntry("current").setDouble(motor.getStatorCurrent());
      // Log encoder val
      subtable.getEntry("encoderPosition").setDouble(getPosition());

      subtable.getEntry("IRBeamLow").setBoolean(getBeamTrigger(SwitchSelector.low));
      subtable.getEntry("IRBeamMid").setBoolean(getBeamTrigger(SwitchSelector.mid));
      subtable.getEntry("IRBeamHigh").setBoolean(getBeamTrigger(SwitchSelector.high));

    }

  }

}