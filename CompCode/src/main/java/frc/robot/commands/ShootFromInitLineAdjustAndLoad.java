package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.shooter.ShootInitLineAdjust;
import frc.robot.commands.storage.StorageForward;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Storage;

public class ShootFromInitLineAdjustAndLoad extends ParallelCommandGroup {

    public ShootFromInitLineAdjustAndLoad(Shooter shooter, Storage storage, DoubleSupplier adjustment) {
        addCommands(
            new ShootInitLineAdjust(shooter, adjustment),
            new StorageForward(storage)
        );
    }
}