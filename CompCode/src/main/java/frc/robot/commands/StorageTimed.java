package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Storage;

/**
 * Storage forward for a specified amount of time
 */
public class StorageTimed extends WaitCommand {

  private final Storage storage;

  // Creates a new StorageManual command
  public StorageTimed(Storage storage, double duration) {
    // add duration to wait command
    super(duration);
    this.storage = storage;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(storage);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    storage.forward();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    storage.off();
  }

}
