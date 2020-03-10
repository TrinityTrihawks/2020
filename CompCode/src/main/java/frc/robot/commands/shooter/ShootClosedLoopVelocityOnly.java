package frc.robot.commands.shooter;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

/**
 * Probably want to use ShootClosedLoop instead, which is based on this command
 * Shoot at given velocity using previous feedback constants
 * Also incorporates real-time adjustments to velocity using double supplier between [-1, 1]
 */
public class ShootClosedLoopVelocityOnly extends CommandBase {

  private final Shooter shooter;
  private final double velocity;
  private final DoubleSupplier adjustment;

  private final double adjustmentScalar = 5;
 

  public ShootClosedLoopVelocityOnly(Shooter shooter, DoubleSupplier adjustment, double velocity) {
    this.shooter = shooter;
    this.velocity = velocity;
    this.adjustment = adjustment;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter);
  }

  /**
   * This version of the constructor disables adjustment
   */
  public ShootClosedLoopVelocityOnly(Shooter shooter, double velocity) {
    this(shooter, ()-> 0, velocity);
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // DoubleSupplier should provide num in range [-1, 1]
    // Scaled for sensitivity by adjustmentScalar
    double adjustmentToVel = adjustmentScalar * adjustment.getAsDouble();
    shooter.shootClosedLoop(velocity + adjustmentToVel);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.off();
  }

}
