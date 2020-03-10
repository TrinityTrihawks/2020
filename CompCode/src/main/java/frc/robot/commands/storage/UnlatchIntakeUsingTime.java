package frc.robot.commands.storage;

import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Storage;

/**
 * Reverse storage for a second to unlatch intake
 */
public class UnlatchIntakeUsingTime extends WaitCommand {

  private final Storage storage;

  private static final double duration = 1; //second

  // Creates a new StorageManual command
  public UnlatchIntakeUsingTime(Storage storage) {
    // add duration to wait command
    super(duration);
    this.storage = storage;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(storage);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    storage.reverse();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    storage.off();
  }

}
