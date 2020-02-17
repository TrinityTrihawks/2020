package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Storage;

/**
 * The default command for rotating storage.
 * This command uses two double suppliers (presumably joysticks) to
 * receive forward and rotation values at every time step. The values
 * it receives will translate into percent power values for each side
 * of the drive train.
 */
public class StorageRotate extends CommandBase {

  private final Storage storage;
 
  private Double amount;

  // Creates a new StorageRotate command
  public StorageRotate(Storage storage, Double amount) {
    this.storage = storage;
    this.amount = amount;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(storage);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Move towards desired position
    if(amount > 0.0) {
        storage.forward();
    }
    else {
        storage.reverse();
    }
  }

  @Override
  public boolean isFinished() {
    double absolutePosition = Math.abs(storage.getPositionChange());
    double absoluteTarget = Math.abs(amount);

    // Set margin to positive to end before the position reaches target
    double margin = 0.0;

    return absoluteTarget - absolutePosition < margin;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    storage.off();
  }

}
