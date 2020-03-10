package frc.robot.commands.storage;

import java.util.ArrayList;
import java.util.List;
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

  private final List<Integer> storageForwardAngles = toIntList(OIConstants.kStorageForwardPOVId);
  private final List<Integer> storageReverseAngles = toIntList(OIConstants.kStorageReversePOVId);


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
    if(storageForwardAngles.contains(povAngle.getAsInt())) {
        storage.forward();
    }
    else if(storageReverseAngles.contains(povAngle.getAsInt())) {
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

  private List<Integer> toIntList(int[] ints) {
    List<Integer> intList = new ArrayList<Integer>(ints.length);
    for (int i : ints)
    {
        intList.add(i);
    }
    return intList;
  }

}
