package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Shooter;

/**
 * Shoot at given velocity using given feedback constants
 * Also incorporates real-time adjustments to velocity using double supplier between [-1, 1]
 */
public class ShootClosedLoopAdjust extends SequentialCommandGroup {

    public ShootClosedLoopAdjust(Shooter shooter, double vel, DoubleSupplier adjustment,
                            double p, double i, double d, double f) {
        addCommands(
            new ShooterSetConstants(shooter, p, i, d, f),
            new ShootClosedLoopAdjustVelocityOnly(shooter, vel, adjustment)
        );
    }
}