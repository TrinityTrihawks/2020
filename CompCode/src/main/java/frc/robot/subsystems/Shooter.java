package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;


public class Shooter extends SubsystemBase {
 
  final TalonSRX shooterTalon;

  final NetworkTable subtable;

  static Shooter subsystemInst = null;

  /**
   * Use this method to create a Shooter instance. This method
   * ensures that the Shooter class is a singleton, aka, that
   * only one Shooter object ever gets created
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
    shooterTalon = new TalonSRX(ShooterConstants.kShooterTalonId);
    shooterTalon.configFactoryDefault();
    shooterTalon.setNeutralMode(NeutralMode.Coast);

    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    subtable = inst.getTable("shooter");
    

  }

}