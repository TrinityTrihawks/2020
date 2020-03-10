package frc.robot.commands.shooter;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Shooter;


public class ShootInitLine extends SequentialCommandGroup {

    public ShootInitLine(Shooter shooter, DoubleSupplier adjustment) {
        addCommands(
            new ShootClosedLoop(shooter, adjustment, 250, 8, 0, 0, 2)
        );
    }

    /**
     * This version of the constructor disables adjustment
     */
    public ShootInitLine(Shooter shooter) {
        this(shooter, ()->0);
    }
}