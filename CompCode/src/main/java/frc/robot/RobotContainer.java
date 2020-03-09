package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.AuxGamepadMap;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.ClimbingArmManual;
import frc.robot.commands.IntakeForward;
import frc.robot.commands.JoystickDrive;
import frc.robot.commands.ShootClosedLoop;
import frc.robot.commands.ShootClosedLoopWithConstants;
import frc.robot.commands.ShootFromInitLine;
import frc.robot.commands.ShootOpenLoop;
import frc.robot.commands.StorageForward;
import frc.robot.commands.StorageTimed;
import frc.robot.commands.TunePIDFromDashboard;
import frc.robot.commands.UnlatchIntakeUsingTime;
import frc.robot.subsystems.ClimbingArm;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Storage;

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
  private final Command intakeForwardBoost, intakeReverse, intakeReverseBoost;
  // private final Command intakeAutoStorage;
  private final Command shootReverse, shootReverseBoost,
                        shooterAdjust, closedShooterAdjust;
  private final Command smartShoot;
  private final Command endgameCommand;
  private final Command storageForwardBoost, storageReverse, storageReverseBoost;


  // Main drivetrain joystick and  auxiliary arm joystick/buttons
  private final Joystick mainController = new Joystick(OIConstants.kMainControllerPort);
  private final Joystick auxGamepad = new Joystick(OIConstants.kAuxiliaryControllerPort);
  private final AuxGamepadMap auxMap;

  final NetworkTable subtable;


  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    subtable  = NetworkTableInstance.getDefault().getTable("RobotContainer");

    // Set the scheduler to log Shuffleboard events for command initialize, interrupt, finish
    CommandScheduler.getInstance().onCommandInitialize(command -> Shuffleboard.addEventMarker(
        "Command initialized", command.getName(), EventImportance.kNormal));
    CommandScheduler.getInstance().onCommandInterrupt(command -> Shuffleboard.addEventMarker(
        "Command interrupted", command.getName(), EventImportance.kNormal));
    CommandScheduler.getInstance().onCommandFinish(command -> Shuffleboard.addEventMarker(
        "Command finished", command.getName(), EventImportance.kNormal));

    // Button mappings for auxiliary gamepad
    auxMap = new Constants.XboxMap();


    /////////////////////////////////////////////
    // Single-subsystem commands ////////////////
    ////////////////////////////////////////////

    // Intake (intake forward is its own file)
    intakeForwardBoost = new StartEndCommand(
      () -> intake.vacuumBoost(),
      () -> intake.off(),
      intake
    );

    intakeReverse = new StartEndCommand(
      () -> intake.spit(),
      () -> intake.off(),
      intake
    );

    intakeReverseBoost = new StartEndCommand(
      () -> intake.spitBoost(),
      () -> intake.off(),
      intake
    );

    //Storage (storage forward is its own file)
    storageForwardBoost = new StartEndCommand(
      () -> storage.forwardBoost(),
      () -> storage.off(),
      storage
    );

    storageReverse = new StartEndCommand(
      () -> storage.reverse(),
      () -> storage.off(),
      storage
    );

    storageReverseBoost = new StartEndCommand(
      () -> storage.reverseBoost(),
      () -> storage.off(),
      storage
    );

    // Shooter
    shootReverse = new StartEndCommand(
      () -> shooter.reverse(),
      () -> shooter.off(),
      shooter
    );

    shootReverseBoost = new StartEndCommand(
      () -> shooter.reverseBoost(),
      () -> shooter.off(),
      shooter
    );

    shooterAdjust = new StartEndCommand(
      //unused should this be on the XBOX controller?
      () -> shooter.shootOpenLoop(.5 + 1 / 2 * mainController.getThrottle()),
      // throttle is [-1, 1] for BOTH
      () -> shooter.off(),
      shooter
    );

    closedShooterAdjust = new StartEndCommand(
      () -> shooter.shootClosedLoop(.5 + 1 / 2 * mainController.getThrottle()),
      () -> shooter.off()
    );

    // Endgame (Climbing & Winch)
    endgameCommand = new ClimbingArmManual(
      climbingArm,
      () -> auxGamepad.getRawAxis(auxMap.telescope()),
      () -> auxGamepad.getRawButton(auxMap.winch()),
      () -> auxGamepad.getRawButton(auxMap.winchReverse())
    );



    //////////////////////////////////////////////
    // Multi-subystem commands /////////////////////////
    /////////////////////////////////////////////

    // intakeAutoStorage = new ParallelCommandGroup(
    //   // Run intake and schedule storageIncrement if intake switch pressed
    //   new IntakeForward(intake),
    //   new SequentialCommandGroup(
    //     new WaitUntilCommand(() -> storage.getIntakeSwitch()),
    //     new ScheduleCommand(
    //       new StorageIncrement(storage)
    //       // TODO: should this iterate so that multiple StorageIncrements can occur if
    //       // the switch is still pressed?
    //     )
    //   )
    // );

    smartShoot = new ParallelCommandGroup(
      new ShootFromInitLine(shooter),
      new StorageForward(storage)
    );



    ////////////////////////////////////////////////////
    // Autonomous-specific Commands ////////////////////
    ////////////////////////////////////////////////////

    final Command driveOffInitLine = new SequentialCommandGroup(
      // Drive backwards for 2 seconds
      new InstantCommand(() -> drivetrain.driveOpenLoop(.3, .3), drivetrain),
      new WaitCommand(2),
      new InstantCommand(() -> drivetrain.driveOpenLoop(0, 0), drivetrain)
    );


    final Command threeBallAutoFromInit = new SequentialCommandGroup(
      new UnlatchIntakeUsingTime(storage),
      new ParallelRaceGroup(
        new ShootFromInitLine(shooter),
        new StorageForward(storage)
      ).withTimeout(8),
      driveOffInitLine
    );

    autoCommand = threeBallAutoFromInit;

 
    configureDefaultCommands();

    // Configure the button bindings
    configureButtonBindings();

  }


  private void configureDefaultCommands() {
    // Drivetrain default
    drivetrain.setDefaultCommand(new JoystickDrive(
      drivetrain,
      () -> mainController.getY(),
      () -> mainController.getTwist(),
      () -> mainController.getRawButton(OIConstants.kSlowLeftTurnButtonId),
      () -> mainController.getRawButton(OIConstants.kSlowRightTurnButtonId),
      () -> mainController.getPOV(OIConstants.kPovId)
    ));

    // Shooter default
    // shooter.setDefaultCommand(new TunePIDFromDashboard(shooter));

    // Storage default    
    // storage.setDefaultCommand(new StorageManual(
    //   storage,
    //   () -> auxGamepad.getPOV()
    // ));


  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    final JoystickButton boost = new JoystickButton(auxGamepad, auxMap.boost());

    // Intake
    final JoystickButton intakeForwardButton = new JoystickButton(auxGamepad, auxMap.intake());
    final JoystickButton intakeReverseButton = new JoystickButton(auxGamepad, auxMap.intakeSpit());
    
    intakeForwardButton.and(boost)
      .whileActiveOnce(intakeForwardBoost);

    intakeForwardButton.and(boost.negate())
      .whileActiveOnce(new IntakeForward(intake));

    intakeReverseButton.and(boost)
      .whileActiveOnce(intakeReverseBoost);

    intakeReverseButton.and(boost.negate())
      .whileActiveOnce(intakeReverse);


    // Endgame
    new JoystickButton(auxGamepad, auxMap.endgame())
      .whenHeld(endgameCommand);

    // Shooter
    final Trigger shootButton = new Trigger( () -> auxGamepad.getRawAxis(auxMap.shoot()) > 0.9 );
    final JoystickButton smartShootButton = new JoystickButton(auxGamepad, auxMap.smartShoot());
    final JoystickButton shootReverseButton = new JoystickButton(auxGamepad, auxMap.shootReverse());

    shootButton.and(boost)
      .whileActiveOnce(new ShootOpenLoop(shooter, 0.7));
    
    shootButton.and(boost.negate())
      .whileActiveOnce(new ShootOpenLoop(shooter, 0.45));

    shootReverseButton.and(boost)
      .whileActiveOnce(shootReverseBoost);

    shootReverseButton.and(boost.negate())
      .whileActiveOnce(shootReverse);

    smartShootButton.whileActiveOnce(new TunePIDFromDashboard(shooter));

    // Storage Belt
    final PovUpTrigger storageUpTrigger = new PovUpTrigger(auxGamepad);
    final PovDownTrigger storageDownTrigger = new PovDownTrigger(auxGamepad);

    storageUpTrigger.and(boost)
    .whileActiveOnce(storageForwardBoost);

    storageUpTrigger.and(boost.negate())
      .whileActiveOnce(new StorageForward(storage));

    storageDownTrigger.and(boost)
      .whileActiveOnce(storageReverseBoost);
    
    storageDownTrigger.and(boost.negate())
      .whileActiveOnce(storageReverse);

  }

  public void logData() {
    subtable.getEntry("MainController/Throttle").setDouble(mainController.getThrottle());
    subtable.getEntry("MainController/Twist").setDouble(mainController.getTwist());
    subtable.getEntry("MainController/Y").setDouble(mainController.getY());

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
