/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.HashMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class Drivetrain extends SubsystemBase {

  private final HashMap<String, TalonSRX> talons;
  private final PigeonIMU gyro;

  private final NetworkTable subtable;

  private static Drivetrain subsystemInst = null;

  /**
   * Use this method to create a drivetrain instance. This method ensures that the
   * drivetrain class is a singleton, aka, that only one drivetrain object ever
   * gets created
   * 
   * @return
   */
  public static Drivetrain getInstance() {
    if (subsystemInst == null) {
      return new Drivetrain();
    } else {
      return subsystemInst;
    }
  }

  /**
   * Creates a new Drivetrain.
   */
  private Drivetrain() {

    // setup hashmap which contains the names of the talons and
    // their corresponding TalonSRX objects
    talons = new HashMap<>();
    talons.put("frontLeft", new TalonSRX(DriveConstants.kFrontLeftId));
    talons.put("frontRight", new TalonSRX(DriveConstants.kFrontRightId));
    talons.put("backLeft", new TalonSRX(DriveConstants.kBackLeftId));
    talons.put("backRight", new TalonSRX(DriveConstants.kBackRightId));

    // reset all Talon config settings to avoid accidental settings carry-over
    talons.forEach((name, talon) -> talon.configFactoryDefault());

    // coast motors when no voltage applied
    talons.forEach((name, talon) -> talon.setNeutralMode(NeutralMode.Coast));

    // control the drivetrain by setting the front talons, and have
    // the back talons automatically follow
    talons.get("backLeft").follow(talons.get("frontLeft"));
    talons.get("backRight").follow(talons.get("frontRight"));

    // invert right motors
    talons.get("backRight").setInverted(true);
    talons.get("frontRight").setInverted(true);

    //setup gyro
    gyro = new PigeonIMU(talons.get("frontLeft"));
    resetGyro();

    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    subtable = inst.getTable("drivetrain");
    subtable.getEntry("should_log").setBoolean(false);

  }

  public void driveOpenLoop(double leftPercent, double rightPercent) {
    // ensure that the percentages are between -1 and 1
    leftPercent = limitRange(leftPercent);
    rightPercent = limitRange(rightPercent);

    // feed the values to the talons
    talons.get("frontLeft").set(ControlMode.PercentOutput, leftPercent);
    talons.get("frontRight").set(ControlMode.PercentOutput, rightPercent);
  }

  public double getGyroAngle() {
    double[] yawPitchRoll = new double[3];
    gyro.getYawPitchRoll(yawPitchRoll);
    return yawPitchRoll[0];
  }

  public void resetGyro() {
    gyro.setYaw(0);
  }

  public void driveClosedLoop() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    logToNetworkTables();

  }

  private void logToNetworkTables() {
    if(subtable.getEntry("should_log").getBoolean(false)) {
      // Log motor voltages over network tables
      talons.forEach((name, talon) -> subtable.getEntry(name + "_voltage").setDouble(talon.getMotorOutputVoltage()));

      // Log motor currents over network tables
      talons.forEach((name, talon) -> subtable.getEntry(name + "_current").setDouble(talon.getStatorCurrent()));

      // Log gyro angle
      subtable.getEntry("gyro_angle").setDouble(getGyroAngle());

      //Log gyro state
      subtable.getEntry("gyro_state").setString(gyro.getState().toString());
    }
  }

  /**
   * Limit number between -1 and 1
   */
  public double limitRange(double value) {
    return Math.max(-1, Math.min(value, 1));
  }

}
