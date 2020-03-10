package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Shooter;


public class ShootClosedLoop extends SequentialCommandGroup {

    public ShootClosedLoop(Shooter shooter, double vel,
                            double p, double i, double d, double f) {
        addCommands(
            new ShooterSetConstants(shooter, p, i, d, f),
            new ShootClosedLoopVelocityOnly(shooter, vel)
        );
    }
}