package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Shooter;

/**
 * Shoot closed-loop for a specified amount of time
 */
public class ShooterTimed extends WaitCommand {

  private final Shooter shooter;
  private double velocity;

  // Creates a new StorageManual command
  public ShooterTimed(Shooter shooter, double velocity, double duration) {
    // add duration to wait command
    super(duration);
    this.shooter = shooter;
    this.velocity = velocity;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shooter.shootClosedLoop(velocity);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.off();
  }

}
