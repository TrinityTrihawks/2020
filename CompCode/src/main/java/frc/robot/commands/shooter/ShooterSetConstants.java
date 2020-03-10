package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Shooter;

/**
 * This command sets the shooter's feedback loop constants and then quits
 */
public class ShooterSetConstants extends InstantCommand {
  private final Shooter shooter;
  private final double p, i, d, f;

  // Creates a new ShooterSetConstants command
  public ShooterSetConstants(Shooter shooter, double p, double i, double d, double f) {
    this.shooter = shooter;
    this.p = p;
    this.i = i;
    this.d = d;
    this.f = f;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shooter.updatePIDConstants(p, i, d, f);
  }


}