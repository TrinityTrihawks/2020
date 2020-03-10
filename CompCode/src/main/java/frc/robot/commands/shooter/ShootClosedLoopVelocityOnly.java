package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

/**
 * Probably want to use ShootClosedLoop instead, which is based on this command
 */
public class ShootClosedLoopVelocityOnly extends CommandBase {

  private final Shooter shooter;
  private final double velocity;
 

  public ShootClosedLoopVelocityOnly(Shooter shooter, double velocity) {
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
