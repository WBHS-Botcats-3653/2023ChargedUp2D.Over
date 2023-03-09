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
    public static final int kFrontLeftWheelChannel = 11;
    public static final int kRearLeftWheelChannel = 12;
    public static final int kFrontRightWheelChannel = 13;
    public static final int kRearRightWheelChannel = 14;

    // drive control accelerations
    public static final double kThrottleAcceleration = 1;
    public static final double kRotationAcceleartion = 1;
    
    // divides operator input by this number in slow driving mode
    public static final double kSlowDriveCoefficient = 2;

    // controller ports
    public static final int kP1XboxPort = 0;
    public static final int kP2XboxPort = 0;
    
    // ADIS16470 IMU SPI Address
    //public static final int kIMUSPIAddress = 0;

    // DIO ports


    // this variable is actually not a constant 
    // it is being used to roughly tell how long its been in each phase of the game 
    public static double timer = 0;

    public static void count() {
        timer += 0.02;
    }

    public static double getTime() {    
        return timer;
    }

    public static void resetTimer() {
        timer = 0;
    }

/* 
    // target objects dimensions in inches
    public static final double kObjSize = 6;
    // the vertical view angle from forward axis
    public static final double kLimelightVerticalViewAngle = 20.5;
    // the horizontal view angle from forward axis
    public static final double kLimelightHorizontalViewAngle = 27;
*/
}
