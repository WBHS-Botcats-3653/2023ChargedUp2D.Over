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

    // encoder conversions
    /**
     * ticks * (1 rotation/ticks per rotation) * gear ratio * (wheel diameter * PI/1 rotation) * 
     * (1 foot/12 inches) = feet traveled by the robot
     * which looks like (1.0 / 4096 * 6 * Math.PI / 12 = 0.0003834951969714103074295218973729) 
     */
    public static final double kDriveTicksToFeet = 0.00038349519697141030742952189737299;

    // artificial deadzone for the sticks
    public static  final double kArtificialStickDeadzone = 0.05;

    // drive control accelerations
    public static final double kThrottleAcceleration = 1;
    public static final double kRotationAcceleartion = 1;
    
    // divides operator input by these for a smoother driving experience
    public static final double kRotationDampenerCoefficient = 0.75;
    public static final double kSlowDriveCoefficient = 0.5;

    // DIO ports


    // xbox controller ports
    //public static int kP1XboxPort = 0;
    //public static int kP2XboxPort = 0;
}
