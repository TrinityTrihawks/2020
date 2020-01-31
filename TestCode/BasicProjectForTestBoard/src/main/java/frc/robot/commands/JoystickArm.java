package frc.robot.commands;

import frc.robot.subsystems.ClimbingArm;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * The default command for driving with joysticks.
 * This command uses two double suppliers (presumably joysticks) to
 * receive forward and rotation values at every time step. The values
 * it receives will translate into percent power values for each side
 * of the drive train.
 */
public class JoystickArm extends CommandBase {


  // Creates a new JoystickDrive command
  public JoystickArm() {
   

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(ClimbingArm);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {


  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.driveOpenLoop(0, 0);
  }

}
