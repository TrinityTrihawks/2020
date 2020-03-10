package frc.robot.commands.shooter;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;


public class ShootOpenLoop extends CommandBase {

  private final Shooter shooter;
  private final double power;
  private final DoubleSupplier adjustment;

  private final double adjustmentScalar = 0.1;

  public ShootOpenLoop(Shooter shooter, DoubleSupplier adjustment, double power) {
    this.shooter = shooter;
    this.power = power;
    this.adjustment = adjustment;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter);
  }

  /**
   * This constructor disables real-time adjustment
   */
  public ShootOpenLoop(Shooter shooter, double power) {
    this(shooter, () -> 0, power);
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
    double adjustmentToPower = adjustmentScalar * adjustment.getAsDouble();
    shooter.shootClosedLoop(power + adjustmentToPower);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.off();
  }

}
