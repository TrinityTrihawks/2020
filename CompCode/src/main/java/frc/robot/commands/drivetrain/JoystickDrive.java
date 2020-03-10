package frc.robot.commands.drivetrain;

import frc.robot.subsystems.Drivetrain;
import static frc.robot.Constants.JoystickConstants;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * The default command for driving with joysticks. This command uses two double
 * suppliers (presumably joysticks) to receive forward and rotation values at
 * every time step. The values it receives will translate into percent power
 * values for each side of the drive train.
 */
public class JoystickDrive extends CommandBase {

  private final Drivetrain drivetrain;

  private final DoubleSupplier forwardSource;
  private final DoubleSupplier rotationSource;
  private final BooleanSupplier leftSlowTurn;
  private final BooleanSupplier rightSlowTurn;
  private final IntSupplier povAngle;

  // Creates a new JoystickDrive command
  public JoystickDrive(Drivetrain drivetrain, DoubleSupplier forward, DoubleSupplier rotation,
      BooleanSupplier leftSlowTurn, BooleanSupplier rightSlowTurn, IntSupplier povAngle) {
    this.drivetrain = drivetrain;
    this.forwardSource = forward;
    this.rotationSource = rotation;
    this.leftSlowTurn = leftSlowTurn;
    this.rightSlowTurn = rightSlowTurn;
    this.povAngle = povAngle;

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

    System.out
        .println("Forward: " + Math.abs(forward) + "\nRotation: " + rotation + "\nPOV Angle: " + povAngle.getAsInt()
            + "\nRS Btn: " + rightSlowTurn.getAsBoolean() + "\nLS Btn: " + leftSlowTurn.getAsBoolean());

    SmartDashboard.putNumber("Throttle", forward);
    SmartDashboard.putNumber("Rotation", rotation);
    SmartDashboard.putNumber("POV Angle", povAngle.getAsInt());
    SmartDashboard.putBoolean("Right Slow Turn", rightSlowTurn.getAsBoolean());
    SmartDashboard.putBoolean("Left Slow Turn", leftSlowTurn.getAsBoolean());

    double leftDrive = 0;
    double rightDrive = 0;

    if (Math.abs(forward) < JoystickConstants.kSlowTurnThreshold) {
      // Not much throttle: deadzone
      if (Math.abs(rotation) > JoystickConstants.kDeadZoneThreshold) {
        // Rotate in place
        leftDrive = rotation * JoystickConstants.kSlowRotationScalar;
        rightDrive = -rotation * JoystickConstants.kSlowRotationScalar;
      }
      if (povAngle.getAsInt() != -1) {
        // Thumb little joystick thing

        switch (povAngle.getAsInt()) {
        case 0: // Forward
          leftDrive = JoystickConstants.kSlowValue;
          rightDrive = JoystickConstants.kSlowValue;
          break;
        case 90: // Right
          leftDrive = JoystickConstants.kSlowValue * JoystickConstants.kQuickRotationScalar;
          rightDrive = JoystickConstants.kSlowValue;
          break;
        case 180: // Back
          leftDrive = -JoystickConstants.kSlowValue;
          rightDrive = -JoystickConstants.kSlowValue;
          break;
        case 270: // Left
          leftDrive = JoystickConstants.kSlowValue;
          rightDrive = JoystickConstants.kSlowValue * JoystickConstants.kQuickRotationScalar;
          break;
        }
      } else if (leftSlowTurn.getAsBoolean() == true) {
        // Slow left turn in place
        leftDrive = -(JoystickConstants.kSlowValue);
        rightDrive = JoystickConstants.kSlowValue;

      } else if (rightSlowTurn.getAsBoolean() == true) {
        // Slow right turn in place
        leftDrive = JoystickConstants.kSlowValue;
        rightDrive = -(JoystickConstants.kSlowValue);

      }

    } else {
      // Throttling forward
      rotation = rotation * Math.abs(forward) * 0.2;

      // compute drivetrain values for the left and right sides
      leftDrive = forward + rotation;
      rightDrive = forward - rotation;

      // scale down the drivetrain values so that
      // they are within the acceptable range, [-1, 1]
      double maxDrive = Math.max(Math.abs(leftDrive), Math.abs(rightDrive));
      if (maxDrive >= 1) {
        leftDrive = leftDrive / maxDrive;
        rightDrive = rightDrive / maxDrive;
      } else {
        leftDrive = leftDrive * 1;
        rightDrive = rightDrive * 1;
      }
    }

    System.out.println("LeftD: " + leftDrive + "\nRightD: " + rightDrive);

    // feed the drivetrain values to the drivetrain subsystem
    drivetrain.driveOpenLoop(leftDrive, rightDrive);

    //

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.driveOpenLoop(0, 0);
  }

}
