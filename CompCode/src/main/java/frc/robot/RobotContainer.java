package frc.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.JoystickDrive;
import frc.robot.subsystems.ClimbingArm;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Storage;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final Drivetrain drivetrain = Drivetrain.getInstance();
  private final ClimbingArm climbingArm = ClimbingArm.getInstance();
  private final Shooter shooter = Shooter.getInstance();
  private final Intake intake = Intake.getInstance();
  private final Storage storage = Storage.getInstance();
  private final Command autoCommand;
  private final Command climbingArmUp;
  private final Command climbingArmDown;
  private final Command shootAndStorageUp;
  private final Command intakeRun;
  private final Command storageRun;

  // Main drivetrain joystick
  private final Joystick mainController = new Joystick(OIConstants.kMainControllerPort);
  
  // Auxiliary arm joystick
  private final Joystick auxiliaryController = new Joystick(OIConstants.kAuxiliaryControllerPort);
  private final JoystickButton climbUpButton = new JoystickButton(auxiliaryController, OIConstants.kClimbUpButtonId);
  private final JoystickButton climbDownButton = new JoystickButton(auxiliaryController, OIConstants.kClimbDownButtonId);
  private final JoystickButton shootAndStorageUpButton = new JoystickButton(auxiliaryController, OIConstants.kShootAndStorageUpButtonId);
  private final JoystickButton intakeRunButton = new JoystickButton(auxiliaryController, OIConstants.kIntakeRunButtonId);
  private final JoystickButton storageRunButton = new JoystickButton(auxiliaryController, OIConstants.kStorageRunButtonId);




  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    drivetrain.setDefaultCommand(new JoystickDrive(
      drivetrain,
      () -> mainController.getY(),
      () -> mainController.getTwist(),
      () -> mainController.getRawButton(3),
      () -> mainController.getRawButton(4),
      () -> mainController.getPOV(0)
    ));

    Command driveOffInitLine = new SequentialCommandGroup(
      // Run these commands one after another:
      // 1. Drive backwards
      new InstantCommand(() -> drivetrain.driveOpenLoop(-.3, -.3), drivetrain),
      // 2. Wait 3 seconds
      new WaitCommand(3),
      // 3. Stop driving
      new InstantCommand(() -> drivetrain.driveOpenLoop(0,0), drivetrain)
    );

    climbingArmUp = new StartEndCommand(
      // start of command
      () -> climbingArm.moveUp(),
      // end of command
      () -> climbingArm.stop(),
      // requires subsystem
      climbingArm
    );

    climbingArmDown = new StartEndCommand(
      // start of command
      () -> climbingArm.moveDown(),
      // end of command
      () -> climbingArm.stop(),
      // requires subsystem
      climbingArm
    );

    intakeRun = new StartEndCommand(
      // start of command
      () -> intake.vacuum(),
      // end of command
      () -> intake.off(),
      // requires subsystem
      intake
    );

    storageRun = new StartEndCommand(
      // start of command
      () -> storage.forward(),
      // end of command
      () -> storage.off(),
      // requires subsystem
      storage
    );

    shootAndStorageUp = new StartEndCommand(
    // TODO : validate anon function scope  
    // start of command
      () -> {shooter.shoot(1);
            storage.forwardSlow();
          },
      // end of command
      () -> {shooter.stopShoot();
            storage.off();
          },
      // requires subsystem
      shooter,storage
    );


    autoCommand = driveOffInitLine;

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    climbUpButton.whenHeld(climbingArmUp);
    climbDownButton.whenHeld(climbingArmDown);
    shootAndStorageUpButton.whenHeld(shootAndStorageUp);
    intakeRunButton.whenHeld(intakeRun);
    storageRunButton.whenHeld(storageRun);

    
  }

  public void logData() {
    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    inst.getEntry("RobotContainer/MainController/Throttle").setDouble(mainController.getThrottle());
    inst.getEntry("RobotContainer/MainController/Twist").setDouble(mainController.getTwist());
    inst.getEntry("RobotContainer/MainController/Y").setDouble(mainController.getY());


  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return autoCommand;
  }
}
