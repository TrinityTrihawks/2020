package frc.robot.commands;

import java.util.function.IntSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Storage;
import frc.robot.Constants.OIConstants;

/**
 * The default command for controlling storage.
 */
public class StorageManual extends CommandBase {

  private final Storage storage;
 
  private final IntSupplier povAngle;

  private final int storageForwardAngle = OIConstants.kStorageForwardPOVId;
  private final int storageReverseAngle = OIConstants.kStorageReversePOVId;


  // Creates a new StorageManual command
  public StorageManual(Storage storage, IntSupplier povAngle) {
    this.storage = storage;
    this.povAngle = povAngle;
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
    if(povAngle.getAsInt() == storageForwardAngle) {
        storage.forward();
    }
    else if(povAngle.getAsInt() == storageReverseAngle) {
        storage.reverse();
    }
    else {
        storage.off();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    storage.off();
  }

}
