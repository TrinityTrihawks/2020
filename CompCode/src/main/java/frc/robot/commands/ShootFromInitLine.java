package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Shooter;


public class ShootFromInitLine extends SequentialCommandGroup {

    public ShootFromInitLine(Shooter shooter) {
        addCommands(
            new ShooterSetConstants(shooter, 8, 0, 0, 2),
            new ShootClosedLoop(shooter, 250)
        );
    }
}