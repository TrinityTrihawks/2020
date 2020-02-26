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

    /**
     * Joystick Ports
     */
    public static final class OIConstants {
        public static final int kMainControllerPort = 0;
        public static final int kAuxiliaryControllerPort = 1;
        public static final int kAuxiliaryControllerPort2 = 2;

        // Normal
        public static final int kShooterAndBeltButtonId = 1; // X
        public static final int kShooterButtonId = 8;        // RT
        public static final int kShooterReverseButtonId = 6; // RB
        public static final int kIntakeRunButtonId = 2;      // A
        public static final int kIntakeReverseButtonId = 4;  // Y
        public static final int kStorageForwardPOVId = 0;    // POV up
        public static final int kStorageReversePOVId = 180;  // POV down

        // Endgame (used when endgame button pressed)
        public static final int kEndgameButtonId = 5;      // LB
        public static final int kWinchButtonId = 3;        // B
        public static final int kWinchReverseButtonId = 9; // back
        public static final int kClimbUpPOVId = 0;         // POV up
        public static final int kClimbDownPOVId = 180;     // POV down

        public static final int kSlowLeftTurnButtonId = 3;
        public static final int kSlowRightTurnButtonId = 4;
        public static final int kPovId = 0;




    }

    /**
     * Limelight Constants
     */
    public static final class LimelightConstants {
        // angle above horizantal
        public static final double limelightMountAngle = 0.0;
    }
}
