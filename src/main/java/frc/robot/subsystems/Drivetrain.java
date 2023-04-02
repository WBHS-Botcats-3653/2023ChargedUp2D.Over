package frc.robot.subsystems;

import static frc.robot.Constants.*;
import frc.robot.Robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.controller.PIDController;

public class Drivetrain {
    private static Drivetrain m_singleton = null;

    private OI m_input;
    private Heading m_heading;

    private WPI_TalonSRX m_leftMaster, m_rightMaster;
    private WPI_VictorSPX m_leftSlave, m_rightSlave;

    private DifferentialDrive m_robotDrive;

    private SlewRateLimiter m_throttleFilter;
    private SlewRateLimiter m_rotationFilter;

    private double feetTraveled;

    public static boolean doneCharging = false;    
    public static boolean doneBackingUp= false;
    public static boolean donePushing= false;
    public static boolean doneMobilizing= false;

    private Drivetrain() {
        m_input = OI.getInstance();
        m_heading = Heading.getInstance();

        m_leftMaster = new WPI_TalonSRX(kFrontLeftWheelID);   // ID of 9
        m_leftSlave = new WPI_VictorSPX(kRearLeftWheelID);    // ID of 6
        m_rightMaster = new WPI_TalonSRX(kFrontRightWheelID); // ID of 7
        m_rightSlave = new WPI_VictorSPX(kRearRightWheelID);  // ID of 8
 
        // sets the motors to brake mode
        m_leftMaster.setNeutralMode(NeutralMode.Brake);
        m_leftSlave.setNeutralMode(NeutralMode.Brake);
        m_rightMaster.setNeutralMode(NeutralMode.Brake);
        m_rightSlave.setNeutralMode(NeutralMode.Brake);

        // sets the motors maximum current limit to 40 amps and enforce it when its exceded for 20 milliseconds
        m_leftMaster.configPeakCurrentLimit(40, 0);
        m_leftMaster.configPeakCurrentDuration(20, 0);
        m_leftMaster.configContinuousCurrentLimit(35);
        m_leftMaster.enableCurrentLimit(true);

        m_rightMaster.configPeakCurrentLimit(40, 0);
        m_rightMaster.configPeakCurrentDuration(20, 0);
        m_rightMaster.configContinuousCurrentLimit(35);
        m_rightMaster.enableCurrentLimit(true);

        // inverts the right side 
        m_leftMaster.setInverted(false);
        m_rightMaster.setInverted(true);

        // slave setups
        m_leftSlave.follow(m_leftMaster);
        m_rightSlave.follow(m_rightMaster);

        m_leftSlave.setInverted(InvertType.FollowMaster);
        m_rightSlave.setInverted(InvertType.FollowMaster);

        // creates the robot drive
		m_robotDrive = new DifferentialDrive(m_leftMaster, m_rightMaster);

        // sets an artificial deadzone of 0.05 for the joystick input
        m_robotDrive.setDeadband(kArtificialStickDeadzone);

        // slew rate limiters create limits on the acceleration for smoother driving
        m_throttleFilter = new SlewRateLimiter(kThrottleAcceleration);
        m_rotationFilter = new SlewRateLimiter(kRotationAcceleartion);


        // encoder setups
        m_leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        m_rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

        // reverses the positive counting direction
        m_leftMaster.setSensorPhase(true);
        m_rightMaster.setSensorPhase(true);

        // resets encoders to zero
        m_leftMaster.setSelectedSensorPosition(0, 0, 10);
        m_rightMaster.setSelectedSensorPosition(0, 0, 10);
    }

    // returns an instance of DriveTrain, creating an instance only when one does not already exist
	public static Drivetrain getInstance() {
		if (m_singleton == null) {
			m_singleton = new Drivetrain();
		}
		return m_singleton;
	}
    
    public void drivePeriodic() {
        feetTraveled = (m_leftMaster.getSelectedSensorPosition() * kDriveTicksToFeet + m_rightMaster.getSelectedSensorPosition() * kDriveTicksToFeet) / 2;
        
        if (m_input.isP2XDown()) {
            m_robotDrive.arcadeDrive(-m_input.getP1LeftY() * kSlowDriveCoefficient, -m_input.getP1RightX() * kSlowDriveCoefficient);
        } else {
            m_robotDrive.arcadeDrive(-m_input.getP1LeftY(), -m_input.getP1RightX() * kRotationDampenerCoefficient);
        }

        // outputs encoders data
        SmartDashboard.putNumber("Left Drive Encoder Ticks", m_leftMaster.getSelectedSensorPosition());
        SmartDashboard.putNumber("Right Drive Encoder Ticks", m_rightMaster.getSelectedSensorPosition());
        SmartDashboard.putNumber("Left Drive Travel Distance", m_leftMaster.getSelectedSensorPosition() * kDriveTicksToFeet);
        SmartDashboard.putNumber("Right Drive Travel Distance", m_rightMaster.getSelectedSensorPosition() * kDriveTicksToFeet);
        SmartDashboard.putNumber("Robot Feet Traveled", feetTraveled);

        // outputs motor currents
        SmartDashboard.putNumber("Left Drive Stator Current", m_leftMaster.getStatorCurrent());
        SmartDashboard.putNumber("Right Drive Stator Current", m_rightMaster.getStatorCurrent());
    } 

    public void parkPeriodic() {
        m_robotDrive.arcadeDrive(0, 0);
    }
    
    // auto that mobilizes 
    public void mobilizePeriodic() {
        feetTraveled = (m_leftMaster.getSelectedSensorPosition() * kDriveTicksToFeet + m_rightMaster.getSelectedSensorPosition() * kDriveTicksToFeet) / 2;

        if (feetTraveled > -12 && !doneMobilizing) {
            m_robotDrive.arcadeDrive(-0.8, 0);
        } else {
            m_robotDrive.arcadeDrive(0, 0);
            doneMobilizing = true;
        }

        // outputs encoders data
        SmartDashboard.putNumber("Left Drive Encoder Ticks", m_leftMaster.getSelectedSensorPosition());
        SmartDashboard.putNumber("Right Drive Encoder Ticks", m_rightMaster.getSelectedSensorPosition());
        SmartDashboard.putNumber("Left Drive Travel Distance", m_leftMaster.getSelectedSensorPosition() * kDriveTicksToFeet);
        SmartDashboard.putNumber("Right Drive Travel Distance", m_rightMaster.getSelectedSensorPosition() * kDriveTicksToFeet);
        SmartDashboard.putNumber("Robot Feet Traveled", feetTraveled);
    }

    // auto that engages with charge station
    public void chargePeriodic() {
        feetTraveled = (m_leftMaster.getSelectedSensorPosition() * kDriveTicksToFeet + m_rightMaster.getSelectedSensorPosition() * kDriveTicksToFeet) / 2;

        if (feetTraveled > -11.40125 && !doneCharging || (m_heading.getAccelY() < 9.7 && m_heading.getAccelY() > 9.9)) {
            m_robotDrive.arcadeDrive(-0.7, 0);
        } else {
            m_robotDrive.arcadeDrive(0, 0);
            doneCharging = true;
        }

        // outputs encoders data
        SmartDashboard.putNumber("Left Drive Encoder Ticks", m_leftMaster.getSelectedSensorPosition());
        SmartDashboard.putNumber("Right Drive Encoder Ticks", m_rightMaster.getSelectedSensorPosition());
        SmartDashboard.putNumber("Left Drive Travel Distance", m_leftMaster.getSelectedSensorPosition() * kDriveTicksToFeet);
        SmartDashboard.putNumber("Right Drive Travel Distance", m_rightMaster.getSelectedSensorPosition() * kDriveTicksToFeet);
        SmartDashboard.putNumber("Robot Feet Traveled", feetTraveled);
    }

    // auto that scores a game piece in hybrid and mobilizes 
    public void yippiePeriodic() {
        feetTraveled = (m_leftMaster.getSelectedSensorPosition() * kDriveTicksToFeet + m_rightMaster.getSelectedSensorPosition() * kDriveTicksToFeet) / 2;

        if (feetTraveled >= -2 && !doneBackingUp) {
            m_robotDrive.arcadeDrive(-0.7, 0);
        } else if (feetTraveled <= -0.1 && !donePushing) {
            doneBackingUp = true;
            m_robotDrive.arcadeDrive(0.5, 0);
        } else if (feetTraveled > -12 && !doneMobilizing) {
            donePushing = true;
            m_robotDrive.arcadeDrive(-0.8, 0);
        } else {
            doneMobilizing = true;
        }

        // outputs encoders data
        SmartDashboard.putNumber("Left Drive Encoder Ticks", m_leftMaster.getSelectedSensorPosition());
        SmartDashboard.putNumber("Right Drive Encoder Ticks", m_rightMaster.getSelectedSensorPosition());
        SmartDashboard.putNumber("Left Drive Travel Distance", m_leftMaster.getSelectedSensorPosition() * kDriveTicksToFeet);
        SmartDashboard.putNumber("Right Drive Travel Distance", m_rightMaster.getSelectedSensorPosition() * kDriveTicksToFeet);
        SmartDashboard.putNumber("Robot Feet Traveled", feetTraveled);
    }

    // auto that scores a game piece in hybrid, mobilizes, and engages with charge station
    public void lolLmaoPeriodic() {
        feetTraveled = (m_leftMaster.getSelectedSensorPosition() * kDriveTicksToFeet + m_rightMaster.getSelectedSensorPosition() * kDriveTicksToFeet) / 2;

        if (feetTraveled >= -2 && !doneBackingUp) {
            m_robotDrive.arcadeDrive(-0.7, 0);
        } else if (feetTraveled <= -0.1 && !donePushing) {
            doneBackingUp = true;
            m_robotDrive.arcadeDrive(0.5, 0);
        } else if (feetTraveled > -13 && !doneMobilizing) {
            donePushing = true;
            m_robotDrive.arcadeDrive(-0.65, 0);
        } else if (feetTraveled <= -12.40125 && !doneCharging || (m_heading.getAccelY() < 9.69)) {
            doneMobilizing = true;
            m_robotDrive.arcadeDrive(0.65, 0);
        } else {
            m_robotDrive.arcadeDrive(0, 0);
            doneCharging = true;
        }

        // outputs encoders data
        SmartDashboard.putNumber("Left Drive Encoder Ticks", m_leftMaster.getSelectedSensorPosition());
        SmartDashboard.putNumber("Right Drive Encoder Ticks", m_rightMaster.getSelectedSensorPosition());
        SmartDashboard.putNumber("Left Drive Travel Distance", m_leftMaster.getSelectedSensorPosition() * kDriveTicksToFeet);
        SmartDashboard.putNumber("Right Drive Travel Distance", m_rightMaster.getSelectedSensorPosition() * kDriveTicksToFeet);
        SmartDashboard.putNumber("Robot Feet Traveled", feetTraveled);
    }

    public void wereSoBackPeriodic() {
        feetTraveled = (m_leftMaster.getSelectedSensorPosition() * kDriveTicksToFeet + m_rightMaster.getSelectedSensorPosition() * kDriveTicksToFeet) / 2;

        if (feetTraveled > -13 && !doneMobilizing) {
            m_robotDrive.arcadeDrive(-0.65, 0);
        } else if (feetTraveled >= -12.40125 && !doneCharging) {
            // || (m_heading.getAccelY() < 9.68)
            doneMobilizing = true;
            m_robotDrive.arcadeDrive(0.65, 0);
        } else {
            m_robotDrive.arcadeDrive(0, 0);
            doneCharging = true;
        }

        // outputs encoders data
        SmartDashboard.putNumber("Left Drive Encoder Ticks", m_leftMaster.getSelectedSensorPosition());
        SmartDashboard.putNumber("Right Drive Encoder Ticks", m_rightMaster.getSelectedSensorPosition());
        SmartDashboard.putNumber("Left Drive Travel Distance", m_leftMaster.getSelectedSensorPosition() * kDriveTicksToFeet);
        SmartDashboard.putNumber("Right Drive Travel Distance", m_rightMaster.getSelectedSensorPosition() * kDriveTicksToFeet);
        SmartDashboard.putNumber("Robot Feet Traveled", feetTraveled);
    }
    
    public void resetEncoders() {
        // resets encoders to zero
        m_leftMaster.setSelectedSensorPosition(0, 0, 10);
        m_rightMaster.setSelectedSensorPosition(0, 0, 10);
    }

    public void setDriveToBrake() {
        m_leftMaster.setNeutralMode(NeutralMode.Brake);
        m_leftSlave.setNeutralMode(NeutralMode.Brake);
        m_rightMaster.setNeutralMode(NeutralMode.Brake);
        m_rightSlave.setNeutralMode(NeutralMode.Brake);
    }

    public void setDriveToCoast(){ 
        m_leftMaster.setNeutralMode(NeutralMode.Coast);
        m_leftSlave.setNeutralMode(NeutralMode.Coast);
        m_rightMaster.setNeutralMode(NeutralMode.Coast);
        m_rightSlave.setNeutralMode(NeutralMode.Coast);
    }
}