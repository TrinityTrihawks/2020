package frc.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.IntakeForward;
import frc.robot.commands.JoystickDrive;
import frc.robot.commands.ShootClosedLoop;
import frc.robot.commands.ShootOpenLoop;
import frc.robot.commands.StorageIncrement;
import frc.robot.commands.TunePIDFromDashboard;
import frc.robot.subsystems.ClimbingArm;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Storage;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import static frc.robot.Constants.ShooterConstants;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // Subsystems
  private final Drivetrain drivetrain = Drivetrain.getInstance();
  private final ClimbingArm climbingArm = ClimbingArm.getInstance();
  private final Intake intake = Intake.getInstance();
  private final Storage storage = Storage.getInstance();
  private final Shooter shooter = Shooter.getInstance();


  // Commands
  private final Command autoCommand;
  private final Command climbingArmUp;
  private final Command climbingArmDown;
  private final Command intakeReverse;
  private final Command storageForward;
  private final Command storageReverse;
  private final Command intakeAutoStorage;
  private final Command shooterAdjust;
  private final Command closedShooterAdjust;
  private final Command winchUnwind;


  // Main drivetrain joystick
  private final Joystick mainController = new Joystick(OIConstants.kMainControllerPort);

  // Auxiliary arm joystick and buttons
  private final Joystick auxGamepad = new Joystick(OIConstants.kAuxiliaryControllerPort);
 
  private final JoystickButton climbUpButton = new JoystickButton(auxGamepad, OIConstants.kClimbUpButtonId);
  private final JoystickButton climbDownButton = new JoystickButton(auxGamepad, OIConstants.kClimbDownButtonId);
  private final JoystickButton shooterButton = new JoystickButton(auxGamepad, OIConstants.kShooterButtonId);
  private final JoystickButton intakeRunButton = new JoystickButton(auxGamepad, OIConstants.kIntakeRunButtonId);
  private final JoystickButton storageForwardButton = new JoystickButton(auxGamepad, OIConstants.kStorageForwardButtonId);
  private final JoystickButton intakeReverseButton = new JoystickButton(auxGamepad,OIConstants.kIntakeReverseButtonId);
  private final JoystickButton storageReverseButton = new JoystickButton(auxGamepad,OIConstants.kStorageReverseButtonId);
  private final JoystickButton winchUnwindButton = new JoystickButton(auxGamepad,9);


  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    // Drivetrain Command
    drivetrain.setDefaultCommand(new JoystickDrive(
        drivetrain,
        () -> mainController.getY(),
        () -> mainController.getTwist(),
        () -> mainController.getRawButton(3),
        () -> mainController.getRawButton(4),
        () -> mainController.getPOV(0)
    ));

    winchUnwind = new StartEndCommand(
      () -> climbingArm.winchUnwind(),
      () -> climbingArm.stop(),
      climbingArm
    );

    // Intake
    intakeReverse = new StartEndCommand(
      () -> intake.spit(),
      () -> intake.off(),
      intake
    );

    //Storage
    storageForward = new StartEndCommand(
      () -> storage.forward(),
      () -> storage.off(),
      storage
    );

    storageReverse = new StartEndCommand(
      () -> storage.reverse(),
      () -> storage.off(),
      storage
    );

    intakeAutoStorage = new ParallelCommandGroup(
      // Run intake and schedule storageIncrement if intake switch pressed
      new IntakeForward(intake),
      new SequentialCommandGroup(
        new WaitUntilCommand(() -> storage.getIntakeSwitch()),
        new ScheduleCommand(
          new StorageIncrement(storage)
          // TODO: should this iterate so that multiple StorageIncrements can occur if
          // the switch is still pressed?
        )
      )
    );

    // Climbing
    climbingArmUp = new StartEndCommand(
      () -> climbingArm.moveUp(),
      () -> climbingArm.stop(),
      climbingArm
    );

    climbingArmDown = new StartEndCommand(
      () -> climbingArm.moveDown(),
      () -> climbingArm.stop(),
      climbingArm
    );


    // Shooter
    shooterAdjust = new StartEndCommand(
      // TODO: should this be on the XBOX controller?
      () -> shooter.shootOpenLoop(.5 + 1 / 2 * mainController.getThrottle()),
      // throttle is [-1, 1] for BOTH
      () -> shooter.off(),
      shooter
    );


    closedShooterAdjust = new StartEndCommand(
      () -> shooter.shootClosedLoop(.5 + 1 / 2 * mainController.getThrottle()),
      () -> shooter.off()
    );




    // *** Autonomous Commands ***

    Command driveOffInitLine = new SequentialCommandGroup(
      // Drive backwards for 2 seconds
      new InstantCommand(() -> drivetrain.driveOpenLoop(.3, .3), drivetrain),
      new WaitCommand(2),
      new InstantCommand(() -> drivetrain.driveOpenLoop(0, 0), drivetrain)
    );

    Command unlatchIntake = new SequentialCommandGroup(
      // Reverse storage belt for 1 second
      new InstantCommand(() -> storage.reverse(), storage),
      new WaitCommand(1),
      new InstantCommand(() -> storage.off(), storage)
    );

    // Command pIDSetupCommand = new InstantCommand(() -> {
    //   SmartDashboard.putNumber("kP", ShooterConstants.kP);
    //   SmartDashboard.putNumber("kF", ShooterConstants.kF);
    //   SmartDashboard.putNumber("kI", ShooterConstants.kI);
    //   SmartDashboard.putNumber("kD", ShooterConstants.kD);
    // });

    Command combinedAuto = new SequentialCommandGroup(
         unlatchIntake,
        // LAST
        driveOffInitLine);



    autoCommand = combinedAuto;


    shooter.setDefaultCommand(new TunePIDFromDashboard(shooter));


    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    climbUpButton.whenHeld(climbingArmUp);
    climbDownButton.whenHeld(climbingArmDown);
    intakeRunButton.whenHeld(new IntakeForward(intake));
    intakeReverseButton.whenHeld(intakeReverse);
    storageForwardButton.whenHeld(storageForward);
    storageReverseButton.whileHeld(storageReverse);
    winchUnwindButton.whenHeld(winchUnwind);
    shooterButton.whenHeld(new ShootOpenLoop(shooter, 0.45));

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
