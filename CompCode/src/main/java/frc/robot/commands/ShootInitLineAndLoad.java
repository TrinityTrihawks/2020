package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.shooter.ShootInitLine;
import frc.robot.commands.storage.StorageForward;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Storage;

public class ShootInitLineAndLoad extends ParallelCommandGroup {

    public ShootInitLineAndLoad(Shooter shooter, Storage storage, DoubleSupplier adjustment) {
        addCommands(
            new ShootInitLine(shooter, adjustment),
            new StorageForward(storage)
        );
    }

    /**
     * This version of the constructor disables adjustment
     */
    public ShootInitLineAndLoad(Shooter shooter, Storage storage) {
        this(shooter, storage, ()-> 0);
    }
}