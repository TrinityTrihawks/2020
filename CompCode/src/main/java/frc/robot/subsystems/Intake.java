package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {

  private static Intake subsystemInst = null;

  private final NetworkTable subtable;

  private final VictorSPX motor;

  /**
   * Use this method to create a intake instance. This method ensures that the
   * intake class is a singleton, aka, that only one intake object ever gets
   * created
   */
  public static Intake getInstance() {
    if (subsystemInst == null) {
      return new Intake();
    } else {
      return subsystemInst;
    }
  }

  private Intake() {
    motor = new VictorSPX(Constants.IntakeConstants.kMotorId);
    motor.setNeutralMode(NeutralMode.Brake);

    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    subtable = inst.getTable("intake");
    subtable.getEntry("should_log").setBoolean(false);
  }

  public void off() {
    motor.set(ControlMode.PercentOutput, 0);
  }

  public void vacuum() {
    motor.set(ControlMode.PercentOutput, -0.4);
  }

  public void vacuumBoost() {
    motor.set(ControlMode.PercentOutput, -0.8);
  }

  public void intakeByPower(double power) {
    motor.set(ControlMode.PercentOutput, power * -0.7);
  }

  /**
   * Useful for unjamming power cells
   */
  public void spit() {
    motor.set(ControlMode.PercentOutput, 0.8);
  }

  public void spitBoost() {
    motor.set(ControlMode.PercentOutput, 1);
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
    }
  }

}