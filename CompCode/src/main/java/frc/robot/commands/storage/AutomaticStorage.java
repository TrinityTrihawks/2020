package frc.robot.commands.storage;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Storage;


public class AutomaticStorage extends CommandBase {

  private final Storage storage;
 

  public AutomaticStorage(Storage storage) {
    this.storage = storage;
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
    if( storage.lowerTriggered() && !storage.highTriggered()) {
      storage.setPowerByNetworkTables();
    } else {
      storage.off();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    storage.off();
  }

}
