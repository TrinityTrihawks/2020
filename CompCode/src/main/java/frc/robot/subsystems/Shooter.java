package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {

  final TalonSRX left;
  final TalonSRX right;

  final NetworkTable subtable;

  static Shooter subsystemInst = null;

  /**
   * Use this method to create a Shooter instance. This method ensures that the
   * Shooter class is a singleton, aka, that only one Shooter object ever gets
   * created
   * 
   * @return the Shooter instance
   */
  public static Shooter getInstance() {
    if (subsystemInst == null) {
      return subsystemInst = new Shooter(); // assigns AND returns the shooter instance:
                                            // the expression value of the '=' operator is the value assigned
    } else {
      return subsystemInst;
    }
  }

  private Shooter() {
    left = new TalonSRX(ShooterConstants.kLeftTalonId);
    right = new TalonSRX(ShooterConstants.kRightTalonId);

    // Left Talon Config
    left.configFactoryDefault();

    left.setNeutralMode(NeutralMode.Brake);

    left.configNominalOutputForward(0);
    left.configNominalOutputReverse(0);
    left.configPeakOutputForward(1);
    left.configPeakOutputReverse(-1);

    left.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 30);
    left.setSensorPhase(true);

    left.config_kP(0, ShooterConstants.kP);
    left.config_kI(0, ShooterConstants.kI);
    left.config_kD(0, ShooterConstants.kD);
    left.config_kF(0, ShooterConstants.kF);

    // Right Talon config
    right.configFactoryDefault();

    right.setNeutralMode(NeutralMode.Brake);

    right.configNominalOutputForward(0);
    right.configNominalOutputReverse(0);
    right.configPeakOutputForward(1);
    right.configPeakOutputReverse(-1);

    right.config_kP(0, ShooterConstants.kP);
    right.config_kI(0, ShooterConstants.kI);
    right.config_kD(0, ShooterConstants.kD);
    right.config_kF(0, ShooterConstants.kF);

    right.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 30);
    right.setSensorPhase(true);

    right.setInverted(true);

    final NetworkTableInstance inst = NetworkTableInstance.getDefault();
    subtable = inst.getTable("shooter");

  }

  /**
   * runs the shooter at the specified target<br>
   * ***closed feedback loop ONLY***<br>
   * @param target -1, 1
   */
  public void shootClosedLoop(double target) {
    shootLeftClosedLoop(target);
    shootRightClosedLoop(target);
  }

  /**
   * runs the shooter wheels at the specified targets
   * ***closed feedback loop ONLY***
   * 
   * @param left -1, 1
   * @param right -1, 1
   */
  public void shootClosedLoop(double left, double right) {
    shootLeftClosedLoop(left);
    shootRightClosedLoop(right);
  }

  /**
   * runs the shooter at the specified target<br>
   * ***NO feedback loop***<br>
   * @param target -1, 1
   */
  public void shootOpenLoop(double target) {
    shootLeftOpenLoop(target);
    shootRightOpenLoop(target);
  }

  /**
   * runs the shooter wheels at the specified targets
   * ***NO feedback loop***
   * 
   * @param left -1, 1
   * @param right -1, 1
   */
  public void shootOpenLoop(double left, double right) {
    shootLeftOpenLoop(left);
    shootRightOpenLoop(right);
  }

  /**
   * shoots the left wheel w/ feedback loop
   * 
   * @param target -1, 1
   */
  public void shootLeftClosedLoop(double target) {
    target = 1023 * (target > 1.0 ? 1.0 : (target < -1.0 ? -1.0 : target));

    left.set(ControlMode.Velocity, target);
  }

  /**
   * shoots the left wheel w/o feedback loop
   * 
   * @param target -1, 1
   */
  public void shootLeftOpenLoop(double target) {
    target = target > 1.0 ? 1.0 : (target < -1.0 ? -1.0 : target);

    left.set(ControlMode.PercentOutput, target);
  }

  /**
   * shoots the right wheel w/ feedback loop
   * 
   * @param target -1, 1
   */
  public void shootRightClosedLoop(double target) {
    target = 1023 * (target > 1.0 ? 1.0 : (target < -1.0 ? -1.0 : target));

    right.set(ControlMode.Velocity, target);
  }

  /**
   * shoots the right wheel w/o feedback loop
   * 
   * @param target -1, 1
   */
  public void shootRightOpenLoop(double target) {
    target = target > 1.0 ? 1.0 : (target < -1.0 ? -1.0 : target);

    right.set(ControlMode.PercentOutput, target);
  }

  /**
   * stops the shooter <br>
   * works for BOTH closed <br>feedback AND open control
   * 
   * @param closed bool true=closed loop false=open loop 
   */
  public void stopShoot(boolean closed) {
    if(closed)  shootClosedLoop(0); 
    else        shootOpenLoop(0); 
  }

  /**
   * @return int array of {left, right} encoder velocities
   */
  public int[] getEncoderValues() {

    int leftEncVel = left.getSelectedSensorVelocity();
    int rightEncVel = right.getSelectedSensorVelocity();

    return new int[] { leftEncVel, rightEncVel };
  }

  public void updatePIDConstants() {
    ShooterConstants.kP = SmartDashboard.getNumber("kP", 0);
    ShooterConstants.kD = SmartDashboard.getNumber("kD", 0);
    ShooterConstants.kF = SmartDashboard.getNumber("kF", 0);
    ShooterConstants.kI = SmartDashboard.getNumber("kI", 0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    logToNetworkTables();

  }

  /**
   * @return the limited encoder value
   * @deprecated
   */
  public double limitEncoderValue(final double encoderVelocity) {

    return Math.max(-1023, Math.min(encoderVelocity, 1023));
  }


  public void logToNetworkTables(){
    subtable.getEntry("LeftShooterVel").setNumber(getEncoderValues()[0]);
    subtable.getEntry("RightShooterVel").setNumber(getEncoderValues()[1]);
  }
}