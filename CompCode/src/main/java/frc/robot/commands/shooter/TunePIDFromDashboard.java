package frc.robot.commands.shooter;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.ShooterConstants;
import frc.robot.subsystems.Shooter;


public class TunePIDFromDashboard extends CommandBase {

  private final Shooter shooter;
  private final NetworkTable subtable;

  public TunePIDFromDashboard(Shooter shooter) {
    this.shooter = shooter;
    subtable = NetworkTableInstance.getDefault().getTable("pid_tuning");

    subtable.getEntry("kP").setDefaultDouble(ShooterConstants.kP);
    subtable.getEntry("kI").setDefaultDouble(ShooterConstants.kI);
    subtable.getEntry("kD").setDefaultDouble(ShooterConstants.kD);
    subtable.getEntry("kF").setDefaultDouble(ShooterConstants.kF);
    subtable.getEntry("vel").setDefaultDouble(0);

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    double p = subtable.getEntry("kP").getDouble(ShooterConstants.kP);
    double i = subtable.getEntry("kI").getDouble(ShooterConstants.kI);
    double d = subtable.getEntry("kD").getDouble(ShooterConstants.kD);
    double f = subtable.getEntry("kF").getDouble(ShooterConstants.kF);
    double vel = subtable.getEntry("vel").getDouble(0);

    System.out.println("Tuning pid");

    shooter.updatePIDConstants(p, i, d, f);

    shooter.shootClosedLoop(vel);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.off();
  }

}
