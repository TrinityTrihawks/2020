package frc.robot.commands.shooter;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Shooter;


public class ShootInitLineAdjust extends SequentialCommandGroup {

    public ShootInitLineAdjust(Shooter shooter, DoubleSupplier adjustment) {
        addCommands(
            new ShootClosedLoopAdjust(shooter, adjustment, 250, 8, 0, 0, 2)
        );
    }
}