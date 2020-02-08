package frc.robot.commands;

import frc.robot.subsystems.Intake;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * The default command for driving with joysticks.
 * This command uses two double suppliers (presumably joysticks) to
 * receive forward and rotation values at every time step. The values
 * it receives will translate into percent power values for each side
 * of the drive train.
 */
public class JoystickIntake extends CommandBase {

  private final Intake intake;
 
  private final BooleanSupplier shouldIntake;
  private final BooleanSupplier shouldSpit;

  // Creates a new JoystickDrive command
  public JoystickIntake(Intake intake, BooleanSupplier shouldIntake, BooleanSupplier shouldSpit) {
    this.intake = intake;
    this.shouldIntake = shouldIntake;
    this.shouldSpit = shouldSpit;

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

    if(shouldIntake.getAsBoolean()) {
      intake.vacuum();
    } else if(shouldSpit.getAsBoolean()) {
      intake.spit();
    } else {
      intake.off();
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.off();
  }

}
