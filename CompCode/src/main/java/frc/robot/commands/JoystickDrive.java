package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * The default command for driving with joysticks.
 * This command uses two double suppliers (presumably joysticks) to
 * receive forward and rotation values at every time step. The values
 * it receives will translate into percent power values for each side
 * of the drive train.
 */
public class JoystickDrive extends CommandBase {

  private final Drivetrain drivetrain;
 
  private final DoubleSupplier forwardSource;
  private final DoubleSupplier rotationSource;

  // Creates a new JoystickDrive command
  public JoystickDrive(Drivetrain drivetrain, DoubleSupplier forward, DoubleSupplier rotation) {
    this.drivetrain = drivetrain;
    this.forwardSource = forward;
    this.rotationSource = rotation;

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

    // read forward-backward throttle from source
    // and modify sensitivity by squaring value
    double forward = forwardSource.getAsDouble();
    forward = Math.pow(forward, 2) * Math.signum(forward);

    // read rotation from source and scale by throttle
    // so it represents arc-length
    double rotation = rotationSource.getAsDouble();
    rotation = rotation * Math.abs(forward);

    // compute drivetrain values for the left and right sides
    double leftDrive = forward - rotation;
    double rightDrive = forward + rotation;

    // scale down the drivetrain values so that
    // they are within the acceptable range, [-1, 1]
    double maxDrive = Math.max( Math.abs(leftDrive), Math.abs(rightDrive) );
    leftDrive = leftDrive / maxDrive;
    rightDrive = rightDrive / maxDrive;

    // feed the drivetrain values to the drivetrain subsystem
    drivetrain.driveOpenLoop(leftDrive, rightDrive);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.driveOpenLoop(0, 0);
  }

}
