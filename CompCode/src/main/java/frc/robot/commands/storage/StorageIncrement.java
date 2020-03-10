package frc.robot.commands.storage;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.StorageConstants;
import frc.robot.subsystems.Storage;


public class StorageIncrement extends SequentialCommandGroup {

    public StorageIncrement(Storage storage) {
        addCommands(new StorageRotate(storage, StorageConstants.ballToBeltRatio * StorageConstants.encUnitsPer1Rev));
    }
}