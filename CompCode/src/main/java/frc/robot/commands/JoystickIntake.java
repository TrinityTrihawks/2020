package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

/**
 * The default command for driving with joysticks.
 * This command uses two double suppliers (presumably joysticks) to
 * receive forward and rotation values at every time step. The values
 * it receives will translate into percent power values for each side
 * of the drive train.
 */
public class JoystickIntake extends CommandBase {

  private final Intake intake;
 
  private DoubleSupplier power;

  // Creates a new JoystickDrive command
  public JoystickIntake(Intake intake, DoubleSupplier power) {
    this.intake = intake;
    this.power = power;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intake.intakeByPower(power.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.off();
  }

}
