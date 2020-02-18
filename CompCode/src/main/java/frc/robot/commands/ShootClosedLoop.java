package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;


public class ShootClosedLoop extends CommandBase {

  private final Shooter shooter;
  private final double velocity;
 

  public ShootClosedLoop(Shooter shooter, double velocity) {
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
