package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;


public class ShootOpenLoop extends CommandBase {

  private final Shooter shooter;
  private final double power;
 

  public ShootOpenLoop(Shooter shooter, double power) {
    this.shooter = shooter;
    this.power = power;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shooter.shootOpenLoop(power);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stopShoot(false);
  }

}
