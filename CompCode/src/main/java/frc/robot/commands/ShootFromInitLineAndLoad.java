package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Storage;

public class ShootFromInitLineAndLoad extends ParallelCommandGroup {

    public ShootFromInitLineAndLoad(Shooter shooter, Storage storage) {
        addCommands(
            new ShootFromInitLine(shooter),
            new StorageForward(storage)
        );
    }
}