package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

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
   * @return
   */
  public static Shooter getInstance() {
    if (subsystemInst == null) {
      return new Shooter();
    } else {
      return subsystemInst;
    }
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
    left.setSensorPhase(true);
    
    left.config_kP(0, ShooterConstants.kP);
    left.config_kI(0, ShooterConstants.kI);
    left.config_kD(0, ShooterConstants.kD);
    left.config_kF(0, ShooterConstants.kF);

    // Right Talon config
    right.configFactoryDefault();

    right.setNeutralMode(NeutralMode.Coast);
    
    right.configNominalOutputForward(0);
    right.configNominalOutputReverse(0);
    right.configPeakOutputForward(1);
    right.configPeakOutputReverse(-1);
    
    right.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 30);
    right.setSensorPhase(true);
    
    right.config_kP(0, ShooterConstants.kP);
    right.config_kI(0, ShooterConstants.kI);
    right.config_kD(0, ShooterConstants.kD);
    right.config_kF(0, ShooterConstants.kF);

    right.setInverted(true);
     

    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    subtable = inst.getTable("shooter");

  }

  public boolean shoot(double targetEncoderVelocity) {

    targetEncoderVelocity = limitEncoderValue(targetEncoderVelocity);

    left.set(ControlMode.Velocity, targetEncoderVelocity);
    right.set(ControlMode.Velocity, -targetEncoderVelocity);

    return true;
  }
  public double limitEncoderValue(double encoderVelocity) {
    return Math.max(-1023, Math.min(encoderVelocity, 1023));
  }
}