package frc.robot;

/** 
 * this file is used to create an easy to use and edit list of constants in the 
 * robot. Most commonly IDs and ports for electronics.
 */
public class Constants {
    /**
     * motor controller IDs
     */
    // drivetrain
    public static final int kFrontLeftWheelID = 9;
    public static final int kRearLeftWheelID = 6;
    public static final int kFrontRightWheelID = 7;
    public static final int kRearRightWheelID = 8;
    // elevator
    public static final int kLeftElevatorID = 19;
    public static final int kRightElevatorID = 18;
    // grabber
    public static final int kGrabberID = 17;

    // drive control accelerations
    public static final double kThrottleAcceleration = 1;
    public static final double kRotationAcceleartion = 1;
    
    // divides operator input by these for a smoother driving experience
    public static final double kRotationDampenerCoefficient = 1.3;
    public static final double kSlowDriveCoefficient = 2;

    // DIO ports


    // xbox controller ports
    //public static int kP1XboxPort = 0;
    //public static int kP2XboxPort = 0;

/* 
    // target objects dimensions in inches
    public static final double kObjSize = 6;
    // the vertical view angle from forward axis
    public static final double kLimelightVerticalViewAngle = 20.5;
    // the horizontal view angle from forward axis
    public static final double kLimelightHorizontalViewAngle = 27;
*/
}
