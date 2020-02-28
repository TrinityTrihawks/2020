package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ClimbingArm;

/**
 * The default command for controlling storage.
 */
public class ClimbingArmManual extends CommandBase {

  private final ClimbingArm climbingArm;
 
  private final DoubleSupplier telescopePower;
  private final BooleanSupplier winchForward, winchUnwind;

  // Creates a new StorageManual command
  public ClimbingArmManual(ClimbingArm climbingArm, DoubleSupplier telescopePower,
                            BooleanSupplier winchForward, BooleanSupplier winchUnwind) {

    this.climbingArm = climbingArm;
    this.telescopePower = telescopePower;
    this.winchForward = winchForward;
    this.winchUnwind = winchUnwind;
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
    climbingArm.telescopeMove(telescopePower.getAsDouble());


    if(winchForward.getAsBoolean()) {
      climbingArm.winchOn();

    } else if(winchUnwind.getAsBoolean()) {
      climbingArm.winchUnwind();
      
    } else {
      climbingArm.winchOff();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climbingArm.stop();
  }

}
