package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Shooter;


public class ShootInitLine extends SequentialCommandGroup {

    public ShootInitLine(Shooter shooter) {
        addCommands(
            new ShootClosedLoop(shooter, 250, 8, 0, 0, 2)
        );
    }
}