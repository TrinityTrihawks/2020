package frc.robot;

/**<p>
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity. (<em>statically</em>, Anthony)
 */
public final class Constants {
    /**
     * Drive Constants 2020
     */
    public static final class DriveConstants {
        public static final int kFrontLeftId = 4;
        public static final int kFrontRightId = 1;
        public static final int kBackLeftId = 7;
        public static final int kBackRightId = 3;
    }

    /**
     * Intake Constants
     */
    public static final class IntakeConstants {
        public static final int kMotorId = 1;
    }

    /**
     * Storage/Ball Lift Constants
     */
    public static final class StorageConstants {
        public static final int kMotorId = 5;
        public static final int encUnitsPer1Rev = 4096; 
        public static final int gearboxRatio = 1 / 36;
        public static final double ballToBeltRatio = 2.5;
    }

    /**
     * Joystick and Scalar Constants
     */
    public static final class JoystickConstants {
        public static final double kSlowValue = 0.16;
        public static final double kRotationScalar = 0.5;
        public static final double kSlowRotationScalar = 0.5;
        public static final double kDeadZoneThreshold = 0.3;
        public static final double kSlowTurnThreshold = 0.1;
        public static final double kQuickRotationScalar = 1.4;

    }

    /**
     * Shooter Constants
     */
    public static final class ShooterConstants {
        public static final int kLeftTalonId = 2;
        public static final int kRightTalonId = 6;
        public static final double kP = 0;
        public static final double kI = 0;
        public static final double kD = 0;
        public static final double kF = 0;

        public static final double encUnitsPer1Rev = 4096;
        public static final double gearboxRatio = 1 / 4;

    }

    /**
     * Climbing Constants
     */
    public static final class ClimbingConstants {
        public static final int TelescopeID = 2;
        public static final int WinchId = 8;
    }

    /**
     * IP Addresses and related info
     */
    public static final class NetworkConstants {
        public static final String kIntakeCameraIPAddress = "10.42.15.12";
        // TODO: set targeting cam (limelight) IP address
        public static final String kLimelightIPAddress = "10.42.15.XX";
    }


    public interface AuxGamepadMap {
        public int intake();        // A
        public int intakeSpit();    // Y
        public int shoot();         // RT
        public int smartShoot();    // X
        public int shootReverse();  // RB
        public int endgame();       // LB
        public int telescope();     // LA
        public int winch();         // B
        public int winchReverse();  // small left button
        public int boost();         // LT
        public int shootAdjust(); // RA
        // Does not include belt because belt is POV
    }

    //The Xbox controller button bindings currently are (afaik)
    // # | XInput | Direct
    //   |        |
    // 1 | A      | X
    // 2 | B      | A
    // 3 | X      | B
    // 4 | Y      | Y
    // 5 | LB     | LB
    // 6 | RB     | RB
    // 7 | back   | LT
    // 8 | start  | RT
    // 9 | LThumb | back
    // 10| RThumb | 

    public static class XboxMap implements AuxGamepadMap {
        //Note: Logitech controller on X mode is the same
        //Buttons
        public int intake() {       return 1;   }
        public int intakeSpit() {   return 4;   }
        public int smartShoot() {   return 3;   }
        public int shootReverse() { return 6;   }
        public int endgame() {      return 5;   }
        public int winch() {        return 2;   }
        public int winchReverse() { return 7;   }
        //Axes
        public int shoot() {        return 3;   }
        public int telescope() {    return 1;   }
        public int boost() {        return 2;   }
        //TODO: confirm right axis id num for Xbox
        public int shootAdjust() {  return 5;   }
    }

    public static class LogitechDualModeMap implements AuxGamepadMap {
        //For Logitech controller on D mode
        // If using X mode, use XboxMap
        //Buttons
        public int intake() {       return 2;   }
        public int intakeSpit() {   return 4;   }
        public int shoot() {        return 8;   }
        public int smartShoot() {   return 1;   }
        public int shootReverse() { return 6;   }
        public int endgame() {      return 5;   }
        public int winch() {        return 3;   }
        public int winchReverse() { return 9;   }
        public int boost() {        return 7;   }
        //Axes
        public int telescope() {    return 1;}
        //TODO: confirm right axis id num for Logitech
        public int shootAdjust() {  return 2;}
    }

    /**
     * Joystick Ports
     */
    public static final class OIConstants {
        public static final int kMainControllerPort = 0;
        public static final int kAuxiliaryControllerPort = 1;
        public static final int kAuxiliaryControllerPort2 = 2;

        public static final int[]  kStorageForwardPOVId = {0, 315, 45};    // POV up, up-left, up-right
        public static final int[] kStorageReversePOVId = {180, 225, 135};  // POV down, down-left, down-right

        public static final int kSlowLeftTurnButtonId = 3;
        public static final int kSlowRightTurnButtonId = 4;
        public static final int kPovId = 0;

        //TODO: decide on cancel commands button on main joystick
        public static final int cancelCommandsButton = 10;

    }

    /**
     * Limelight Constants
     */
    public static final class LimelightConstants {
        // angle above horizantal
        public static final double limelightMountAngle = 0.0;
    }
}