/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.networktables.NetworkTableInstance;
//import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
//import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
//import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private Joystick joystick;

  private TalonSRX backRight;
  private TalonSRX frontRight;
  private TalonSRX backLeft;
  private TalonSRX frontLeft;


  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // initialize joystick
    joystick = new Joystick(0);

    // initialize talons
    backRight = new TalonSRX(2);
    frontRight = new TalonSRX(9);
    backLeft = new TalonSRX(3);
    frontLeft = new TalonSRX(1);

    backRight.configFactoryDefault();

    backRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 30);

    backRight.config_kF(0, 0);
    backRight.config_kP(0, 0);
    backRight.config_kI(0, 0);
    backRight.config_kD(0, 0);



    // they are in gearboxes
    frontRight.follow(backRight);
    frontLeft.follow(backRight);
    backLeft.follow(backRight);



    // backRight.configAllowableClosedloopError(0, allowableCloseLoopError, timeoutMs)
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
    // ^??
    
    
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    double throttle               = joystick.getY();
    double targetEncVel           = joystick.getRawAxis(3); //little lever/throttle
    int encodervel                = backRight.getSensorCollection().getQuadratureVelocity();

    backRight.set(ControlMode.Velocity, targetEncVel);
    backLeft.set(ControlMode.Velocity, -1 * targetEncVel);


    double error = Math.abs(targetEncVel*2000-encodervel);

    System.out.println(
      "    Throttle: "+ throttle +
      "    EncVel: "+ encodervel +
      "    Target EncVel: " + targetEncVel +
      "    Error: "+ error +
      "**********" 
      );

    SmartDashboard.putNumber("EncVel", encodervel);
    SmartDashboard.putNumber("TargetEncVel", targetEncVel);
    SmartDashboard.putNumber("Error", error);

    // NetworkTableInstance.getDefault().getEntry("Encoder velocity").setDouble(encodervel);
    // NetworkTableInstance.getDefault().getEntry("Target Encoder Velocity").setDouble(targetEncVel);

    

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
