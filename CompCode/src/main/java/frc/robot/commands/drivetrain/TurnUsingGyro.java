package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;


public class TurnUsingGyro extends CommandBase {

  private final Drivetrain drivetrain;
 
  private double angle;
  private double speed;

  // Creates a new TurnUsingGyro command
  public TurnUsingGyro(Drivetrain drivetrain, double angle, double speed) {
    this.drivetrain = drivetrain;
    this.angle = angle;
    this.speed = speed;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    double leftPercent, rightPercent;
    if(angle > 0) {
      // Turn right
      leftPercent = speed;
      rightPercent = -1 * speed;
    } else {
      // Turn left
      leftPercent = -1 * speed;
      rightPercent = speed;
    }

    drivetrain.resetGyro();
    drivetrain.driveOpenLoop(leftPercent, rightPercent);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  @Override
  public boolean isFinished() {
    if(Math.abs(drivetrain.getGyroAngle()) >= Math.abs(angle)) {
      return true;
    } else {
      return false;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.driveOpenLoop(0,0);
  }

}
