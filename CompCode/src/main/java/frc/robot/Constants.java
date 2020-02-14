package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final class DriveConstants {
        public static final int kFrontLeftId = 1;
        public static final int kFrontRightId = 9;
        public static final int kBackLeftId = 3;
        public static final int kBackRightId = 2;
    }

    public static final class IntakeConstants {
        public static final int kMotorId = 2;
    }

    public static final class StorageConstants {
        public static final int kMotorId = 3;
    }

    public static final class JoystickConstants {
        public static final double kSlowValue = 0.16;
        public static final double kRotationScalar = 0.5;
        public static final double kSlowRotationScalar = 0.3;
        public static final double kDeadZoneThreshold = 0.3;
        public static final double kSlowTurnThreshold = 0.1;
        public static final double kQuickRotationScalar = 1.4;

    }


    public static final class ShooterConstants {
        public static final int kShooterTalonId = 0;

    }

    public static final class ClimbingConstants {
        public static final int TelescopeID = 0;
        public static final int WinchId = 0;
    }

    public static final class NetworkConstants {
        public static final String kCameraIPAddress = "10.42.15.11";
        public static final String KLimelightIPAddress = "10.42.15.XX";
    }

    public static final class OIConstants {
        public static final int kMainControllerPort = 0;
        public static final int kAuxiliaryControllerPort = 1;

        public static final int kClimbUpButtonId = 1;

    }
}
