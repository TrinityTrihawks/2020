package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class Intake extends SubsystemBase {

  static Intake subsystemInst = null;

  final NetworkTable subtable;
  
  final TalonSRX motor;

  /**
   * Use this method to create a intake instance. This method
   * ensures that the intake class is a singleton, aka, that
   * only one intake object ever gets created
   */
  public static Intake getInstance() {
    if (subsystemInst == null) {
      return new Intake();
    } else {
      return subsystemInst;
    }
  }

  private Intake() {
    motor = new TalonSRX(Constants.IntakeConstants.kMotorId);

    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    subtable = inst.getTable("intake");
  }

  public void off() {
    motor.set(ControlMode.PercentOutput, 0);
  }

  public void vacuum() {
    motor.set(ControlMode.PercentOutput, 0.5);
  }

  // perhaps useful for un-jamming power cells
  public void spit() {
    motor.set(ControlMode.PercentOutput, -0.5);
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

  }

}