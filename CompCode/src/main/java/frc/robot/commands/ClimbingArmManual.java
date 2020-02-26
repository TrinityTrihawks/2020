package frc.robot.commands;

import java.util.function.IntSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimbingArm;
import frc.robot.Constants.OIConstants;

/**
 * The default command for controlling storage.
 */
public class ClimbingArmManual extends CommandBase {

  private final ClimbingArm climbingArm;
 
  private final IntSupplier povAngle;

  private final int climbUpAngle = OIConstants.kClimbUpPOVId;
  private final int climbDownAngle = OIConstants.kClimbDownPOVId;


  // Creates a new StorageManual command
  public ClimbingArmManual(ClimbingArm climbingArm, IntSupplier povAngle) {
    this.climbingArm = climbingArm;
    this.povAngle = povAngle;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(climbingArm);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(povAngle.getAsInt() == climbUpAngle) {
        climbingArm.moveUp();
    }
    else if(povAngle.getAsInt() == climbDownAngle) {
        climbingArm.moveDown();
    }
    else {
        climbingArm.stop();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climbingArm.stop();
  }

}
