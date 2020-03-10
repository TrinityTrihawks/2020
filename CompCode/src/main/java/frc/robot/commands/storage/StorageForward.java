package frc.robot.commands.storage;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Storage;


public class StorageForward extends CommandBase {
  private final Storage storage;
 
  // Creates a new StorageForward command
  public StorageForward(Storage storage) {
    this.storage = storage;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(storage);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    storage.forward();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    storage.off();
  }

}

