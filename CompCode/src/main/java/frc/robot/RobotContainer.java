package frc.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
import static frc.robot.Constants.ShooterConstants;

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
  private final Command reverseStorage;
  private final Command intakeForward;
  private final Command intakeReverse;
  private final Command storageRun;
  private final Command shooterAdjust;
  private final Command shooterAdjustAndStorageUp;
  private final Command shootAndStorageUp;


  // Main drivetrain joystick
  private final Joystick mainController = new Joystick(OIConstants.kMainControllerPort);
  
  // Auxiliary arm joystick
  private final Joystick auxiliaryController = new Joystick(OIConstants.kAuxiliaryControllerPort);
  private final JoystickButton climbUpButton = new JoystickButton(auxiliaryController, OIConstants.kClimbUpButtonId);
  private final JoystickButton climbDownButton = new JoystickButton(auxiliaryController, OIConstants.kClimbDownButtonId);
  private final JoystickButton shooterButton = new JoystickButton(auxiliaryController, OIConstants.kShooterButtonId);
  private final JoystickButton intakeRunButton = new JoystickButton(auxiliaryController, OIConstants.kIntakeRunButtonId);
  private final JoystickButton storageRunButton = new JoystickButton(auxiliaryController, OIConstants.kStorageRunButtonId);
  private final JoystickButton intakeReverseButton = new JoystickButton(auxiliaryController, OIConstants.kIntakeReverseButtonId);
  private final JoystickButton storageReverseButton = new JoystickButton(auxiliaryController, OIConstants.KStorageReverseButtonId);




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

    // intake.setDefaultCommand(new JoystickIntake(
    //   intake,
    //   () -> auxiliaryController.getY()
    // ));

    //intake.setDefaultCommand(new StartEndCommand() {

    //})

    Command driveOffInitLine = new SequentialCommandGroup(
      // Run these commands one after another:
      // 1. Drive backwards
      new InstantCommand(() -> drivetrain.driveOpenLoop(.3, .3), drivetrain),
      // 2. Wait 3 seconds
      new WaitCommand(2),
      // 3. Stop driving
      new InstantCommand(() -> drivetrain.driveOpenLoop(0,0), drivetrain)
    );

    Command pIDSetupCommand = new StartEndCommand(
      () -> {
        SmartDashboard.putNumber("kP", ShooterConstants.kP);
        SmartDashboard.putNumber("kF", ShooterConstants.kF);
        SmartDashboard.putNumber("kI", ShooterConstants.kI);
        SmartDashboard.putNumber("kD", ShooterConstants.kD);
      },

      () -> {}
    );

    Command combinedAuto = new SequentialCommandGroup(
      // FIRST
      pIDSetupCommand,

      // LAST
      driveOffInitLine
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
      // requires subsytem
      climbingArm
    );

    intakeForward = new StartEndCommand(
      // start of command
      () -> intake.vacuum(),
      // end of command
      () -> intake.off(),
      // requires subsystem
      intake
    );

    intakeReverse = new StartEndCommand(
       // start of command
       () -> intake.spit(),
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
    // start of command
      () -> {shooter.shootOpenLoop(.7);
             storage.forwardSlow();
          },
      // end of command
      () -> {shooter.stopShoot(false);
             storage.off();
          },
      // requires subsystem
      storage
    );

    shooterAdjust = new StartEndCommand(
      // TODO: should this be on the XBOX controller?
      () -> shooter.shootOpenLoop(.5 + 1/2 * mainController.getThrottle()), 
      // throttle is [-1, 1]  for BOTH
      () -> shooter.stopShoot(false),
      shooter
    );

    shooterAdjustAndStorageUp = new StartEndCommand(
      // TODO: should this be on the XBOX controller?
      () -> {shooter.shootOpenLoop(.5 + 1/2 * mainController.getThrottle());
             storage.forwardSlow();
      },
      
      () -> {shooter.stopShoot(false);
             storage.off();
      },

      shooter, storage
    );


    

    autoCommand = combinedAuto;

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
    intakeRunButton.whenHeld(intakeForward);
    intakeReverseButton.whenHeld(intakeReverse);
    storageRunButton.whenHeld(storageRun);

    // ***PICK ONE***
    // shooterButton.whenHeld(shootAndStorageUp); 
    // shooterButton.whileHeld(shooterAdjust);
    shooterButton.whileHeld(shooterAdjustAndStorageUp);

    
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
