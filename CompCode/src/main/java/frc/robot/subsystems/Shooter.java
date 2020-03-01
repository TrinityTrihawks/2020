package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.SlotConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

//import org.trinityriverridge.athletics.robotics.mentors.DouglasErickson;
//import org.trinityriverridge.athletics.robotics.students.CharlesNykamp;
//import org.trinityriverridge.athletics.robotics.students.HunterMarble;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {

  private final TalonSRX left;
  private final TalonSRX right;
 
  private final NetworkTable subtable;

  private static Shooter subsystemInst = null;

  /**
   * Use this method to create a Shooter instance. This method ensures that the
   * Shooter class is a singleton, aka, that only one Shooter object ever gets
   * created
   * 
   * @return the Shooter instance
   */
  public static Shooter getInstance() {
    if (subsystemInst == null) {
      subsystemInst = new Shooter();
    }
    return subsystemInst;
  }

  private Shooter() {
    left = new TalonSRX(ShooterConstants.kLeftTalonId);
    right = new TalonSRX(ShooterConstants.kRightTalonId);

    // Left Talon Config
    left.configFactoryDefault();

    left.setNeutralMode(NeutralMode.Coast);

    left.configNominalOutputForward(0);
    left.configNominalOutputReverse(0);
    left.configPeakOutputForward(1);
    left.configPeakOutputReverse(-1);

    left.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 30);
    left.setSensorPhase(false);

    left.setInverted(true);

    // Right Talon config
    right.configFactoryDefault();

    right.setNeutralMode(NeutralMode.Coast);

    right.configNominalOutputForward(0);
    right.configNominalOutputReverse(0);
    right.configPeakOutputForward(1);
    right.configPeakOutputReverse(-1);

    right.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 30);
    right.setSensorPhase(false);

    right.setInverted(false);

    // Initial PID constants
    updatePIDConstants(ShooterConstants.kP, ShooterConstants.kI, ShooterConstants.kD, ShooterConstants.kF);

    // Setup NetworkTables subtable
    final NetworkTableInstance inst = NetworkTableInstance.getDefault();
    subtable = inst.getTable("shooter");
    subtable.getEntry("should_log").setBoolean(false);

  }

  /**
   * Run shooter at specified velocity (raw units / 100ms)
   */
  public void shootClosedLoop(double target) {
    shootClosedLoop(target, target );
  }

  /**
   * Run shooter at specified velocity (raw units / 100ms)
   * Use this one for individual control of the wheels
   */
  public void shootClosedLoop(double left, double right) {

    // left = ShooterConstants.encUnitsPer1Rev * ShooterConstants.gearboxRatio * limitOutputValue(left);
    this.left.set(ControlMode.Velocity, left);

    // right = ShooterConstants.encUnitsPer1Rev * ShooterConstants.gearboxRatio * limitOutputValue(right);
    this.right.set(ControlMode.Velocity, right);
  }

  /**
   * Run shooter at specified voltage percentage. No feedback loop
   * @param target [-1, 1]
   */
  public void shootOpenLoop(double target) {
    shootOpenLoop(target, target);
  }

  /**
   * Run shooter at specified voltage percentage. No feedback loop.
   * Use this one for individual control of the wheels
   * @param left  [-1, 1]
   * @param right [-1, 1]
   */
  public void shootOpenLoop(double left, double right) {
    left = limitOutputValue(left);
    this.left.set(ControlMode.PercentOutput, left);

    right = limitOutputValue(right);
    this.right.set(ControlMode.PercentOutput, right);
  }

  public void reverse() {
    shootOpenLoop(-0.4);
  }

  public void reverseBoost() {
    shootOpenLoop(-0.8);
  }

  /**
   * stops the shooter
   */
  public void off() {
    shootOpenLoop(0);
  }

  /**
   * @return int array of {left, right} encoder velocities
   */
  public int[] getEncoderValues() {

    int leftEncVel = left.getSelectedSensorVelocity();
    int rightEncVel = right.getSelectedSensorVelocity();
    // int leftEncVel = left.getSensorCollection().getPulseWidthVelocity();
    // int rightEncVel = right.getSensorCollection().getPulseWidthVelocity();

    return new int[] { leftEncVel, rightEncVel };
  }

  public void updatePIDConstants(double p, double i, double d, double f) {
    left.config_kP(0, p);
    left.config_kI(0, i);
    left.config_kD(0, d);
    left.config_kF(0, f);

    right.config_kP(0, p);
    right.config_kI(0, i);
    right.config_kD(0, d);
    right.config_kF(0, f);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    logToNetworkTables();


  }

  /**
   * @return the limited encoder value
   * 
   */
  public double limitOutputValue(final double encoderVelocity) {

    return encoderVelocity > 1.0 ? 1.0 : (encoderVelocity < -1.0 ? -1.0 : encoderVelocity);
  }

  public void logToNetworkTables() {
    if(subtable.getEntry("should_log").getBoolean(false)) {
      // Voltage
      subtable.getEntry("left_voltage").setDouble(left.getMotorOutputVoltage());
      subtable.getEntry("right_voltage").setDouble(right.getMotorOutputVoltage());

      // Encoder Velocity
      subtable.getEntry("LeftShooterVel").setNumber(getEncoderValues()[0]);
      subtable.getEntry("RightShooterVel").setNumber(getEncoderValues()[1]);
      
      // Control Mode
      subtable.getEntry("left_controlMode").setString(left.getControlMode().toString());
      subtable.getEntry("right_controlMode").setString(right.getControlMode().toString());
  
      // PID constants
      SlotConfiguration leftSlot = new SlotConfiguration();
      left.getSlotConfigs(leftSlot);
      subtable.getEntry("left_kP").setDouble(leftSlot.kP);
      subtable.getEntry("left_kI").setDouble(leftSlot.kI);
      subtable.getEntry("left_kD").setDouble(leftSlot.kD);
      subtable.getEntry("left_kF").setDouble(leftSlot.kF);

      SlotConfiguration rightSlot = new SlotConfiguration();
      right.getSlotConfigs(rightSlot);
      subtable.getEntry("right_kP").setDouble(rightSlot.kP);
      subtable.getEntry("right_kI").setDouble(rightSlot.kI);
      subtable.getEntry("right_kD").setDouble(rightSlot.kD);
      subtable.getEntry("right_kF").setDouble(rightSlot.kF);

      
      if( left.getControlMode() == ControlMode.Velocity) {
        subtable.getEntry("left_error").setDouble(left.getClosedLoopError());
        subtable.getEntry("right_error").setDouble(right.getClosedLoopError());

          // Target Velocity
        subtable.getEntry("left_targetVel").setDouble(left.getClosedLoopTarget());
        subtable.getEntry("right_targetVel").setDouble(right.getClosedLoopTarget());
      } else {

        subtable.getEntry("left_error").setDouble(0);
        subtable.getEntry("right_error").setDouble(0);
        subtable.getEntry("left_targetVel").setDouble(0);
        subtable.getEntry("right_targetVel").setDouble(0);

      }

    }
    
  }
}