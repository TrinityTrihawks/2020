package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

/**
 * The default command for driving with joysticks.
 * This command uses two double suppliers (presumably joysticks) to
 * receive forward and rotation values at every time step. The values
 * it receives will translate into percent power values for each side
 * of the drive train.
 */
public class DrivetrainRotate extends CommandBase {

  private final Drivetrain drivetrain;
 
  private Double rightPercent;
  private Double leftPercent;

  // Creates a new JoystickDrive command
  public DrivetrainRotate(Drivetrain drivetrain, Double rightPercent, Double leftPercent) {
    this.drivetrain = drivetrain;
    this.rightPercent = rightPercent;
    this.leftPercent = leftPercent;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drivetrain.driveOpenLoop(leftPercent, rightPercent);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.driveOpenLoop(0,0);
  }

}
