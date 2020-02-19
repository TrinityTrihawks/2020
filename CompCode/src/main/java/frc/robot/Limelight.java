package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;

public class Limelight {
    private static Limelight inst;

    private final NetworkTable subtable;

    private Limelight() {
        subtable = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public static Limelight getInstance() {
        if (inst == null)
            return inst = new Limelight();
        else
            return inst;
    }

    /**
     * logs limelight data to the shuffleboard
     * @return void, so don't use it in an expression!
     */
    public void logToShuffleboard() {
        NetworkTableEntry tx = getEntries()[0];
        NetworkTableEntry ty = getEntries()[1];
        NetworkTableEntry ta = getEntries()[2];
        NetworkTableEntry tv = getEntries()[3];

        // read values periodically
        double x = tx.getDouble(0.0);
        double y = ty.getDouble(0.0);
        double area = ta.getDouble(0.0);
        boolean target = tv.getFlags() == 1;

        // post to smart dashboard periodically
        SmartDashboard.putNumber("LimelightX", x);
        SmartDashboard.putNumber("LimelightY", y);
        SmartDashboard.putNumber("LimelightArea", area);
        SmartDashboard.putBoolean("LimelightTargetExistence", target);
    }

    /**
     * 
     * @return limelight table
     */
    public NetworkTable getTable() {
        return subtable;
    }

    /**
     * 
     * @return array of table entries [tx, ty, ta, tv]
     */
    public NetworkTableEntry[] getEntries() {
        return new NetworkTableEntry[] {subtable.getEntry("tx"), subtable.getEntry("ty"),
                                        subtable.getEntry("ta"), subtable.getEntry("tv")};
    }

}