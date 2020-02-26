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

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
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
  private static final String kDefaultAuto = "Default ";
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
   * used for any initializatixon code.
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

    backRight.configNominalOutputForward(0);
    backRight.configNominalOutputReverse(0);
    backRight.configPeakOutputForward(1);
    backRight.configPeakOutputReverse(-1);

    backRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 30);
    backRight.setSensorPhase(true);

    SmartDashboard.putNumber("kF", 0.100);  // 1023.0/7200.0);
    SmartDashboard.putNumber("kP", 0.800);  // 0.25);
    SmartDashboard.putNumber("kI", 0.003);  // 0.001);
    SmartDashboard.putNumber("kD" ,0.000);  // 20);
    SmartDashboard.putNumber("TargetEncVel", 515);


    // all follow backRight
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
    backRight.config_kF(0, SmartDashboard.getNumber("kF", 0.34)); //1023.0/7200.0));
    backRight.config_kP(0, SmartDashboard.getNumber("kP", 0.20)); //0.25));
    backRight.config_kI(0, SmartDashboard.getNumber("kI", 0.00)); //0.001));
    backRight.config_kD(0, SmartDashboard.getNumber("kD", 0.00)); //20));

    SmartDashboard.putNumber("MotorVoltage", backRight.getMotorOutputVoltage());

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
    double targetEncVel           = SmartDashboard.getNumber("TargetEncVel", 0); //joystick.getRawAxis(3) * 1023; //little lever/throttle
    int encodervel                = backRight.getSelectedSensorVelocity();

    backRight.set(ControlMode.Velocity, targetEncVel);
    //backLeft.set(ControlMode.Velocity, -1 * targetEncVel);

    int talonErr = backRight.getClosedLoopError();

    //double error = Math.abs(targetEncVel-encodervel);

    System.out.println(
      "\n    Throttle: "+ throttle +
      "\n    EncVel: "+ encodervel +
      "\n    Target EncVel: " + targetEncVel +
      "\n    Error: "+ talonErr +
      "\n    **********" 
      );

    SmartDashboard.putNumber("EncVel", encodervel);
    SmartDashboard.putNumber("Error", talonErr);
    //SmartDashboard.putNumber("TalonEncVel", talonEncVel);
    //NetworkTableInstance.getDefault().getEntry("EncVel").setDouble(encodervel);
    //NetworkTableInstance.getDefault().getEntry("TargetEncVel").setDouble(targetEncVel);
   

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
