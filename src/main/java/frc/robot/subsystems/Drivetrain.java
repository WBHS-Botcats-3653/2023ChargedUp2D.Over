package frc.robot.subsystems;

import static frc.robot.Constants.*;
import frc.robot.Robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain {
    private static Drivetrain m_singleton = null;

    private OI m_input;

    private Orientation m_orientation;

    //private WPI_TalonSRX m_leftMaster, m_rightMaster;
    private WPI_TalonSRX m_rightMaster;
    private WPI_VictorSPX m_leftSlave, m_rightSlave, m_leftMaster;

    private DifferentialDrive m_robotDrive;

    private SlewRateLimiter m_throttleFilter;
    private SlewRateLimiter m_rotationFilter;

    private Drivetrain() {
        m_input = OI.getInstance();
        m_orientation = Orientation.getInstance();

        m_leftMaster = new WPI_VictorSPX(kFrontLeftWheelID);   // ID of 9
        m_leftSlave = new WPI_VictorSPX(kRearLeftWheelID);    // ID of 6
        m_rightMaster = new WPI_TalonSRX(kFrontRightWheelID); // ID of 7
        m_rightSlave = new WPI_VictorSPX(kRearRightWheelID);  // ID of 8
 
        // sets the motors to brake mode
        m_leftMaster.setNeutralMode(NeutralMode.Brake);
        m_leftSlave.setNeutralMode(NeutralMode.Brake);
        m_rightMaster.setNeutralMode(NeutralMode.Brake);
        m_rightSlave.setNeutralMode(NeutralMode.Brake);

        // sets the motors maximum current limit to 40 amps and enforce it when its exceded for 20 milliseconds
        //m_leftMaster.configPeakCurrentLimit(40, 0);
        //m_leftMaster.configPeakCurrentDuration(20, 0);
        //m_leftMaster.configContinuousCurrentLimit(35);
        //m_leftMaster.enableCurrentLimit(true);

        //m_rightMaster.configPeakCurrentLimit(40, 0);
        //m_rightMaster.configPeakCurrentDuration(20, 0);
        //m_rightMaster.configContinuousCurrentLimit(35);
        //m_rightMaster.enableCurrentLimit(true);

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
        //m_leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        m_rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

        // reverses the positive counting direction
        //m_leftMaster.setSensorPhase(false);
        m_rightMaster.setSensorPhase(true);

        // resets encoders to zero
        //m_leftMaster.setSelectedSensorPosition(0, 0, 10);
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
        if (m_input.getP1ADown() || m_input.getP1BDown() || m_input.getP1XDown() || m_input.getP1YDown()) {
            m_robotDrive.arcadeDrive(-m_input.getP1LeftY() * kSlowDriveCoefficient, -m_input.getP1RightX() * kSlowDriveCoefficient);
        } else {
            m_robotDrive.arcadeDrive(-m_input.getP1LeftY(), -m_input.getP1RightX() * kRotationDampenerCoefficient);
        }

        // outputs encoders data
        //SmartDashboard.putNumber("Left Encoder Ticks", m_leftMaster.getSelectedSensorPosition());
        SmartDashboard.putNumber("Right Encoder Ticks", m_rightMaster.getSelectedSensorPosition());
        //SmartDashboard.putNumber("Left Drive Travel Distance", m_leftMaster.getSelectedSensorPosition() * kDriveTicksToFeet);
        SmartDashboard.putNumber("Right Drive Travel Distance", m_rightMaster.getSelectedSensorPosition() * kDriveTicksToFeet);

        // outputs motor currents
        //SmartDashboard.putNumber("Left Drive Stator Current", m_leftMaster.getStatorCurrent());
        SmartDashboard.putNumber("Right Drive Stator Current", m_rightMaster.getStatorCurrent());
    } 

    public void parkPeriodic() {
        m_robotDrive.arcadeDrive(0, 0);
    }

    public void chargePeriodic() {}
    
    public void mobilizePeriodic() {
        if (Robot.time < 6.8 && Robot.time > 3.8) {
            m_robotDrive.arcadeDrive(0.4, 0);
        }
    }
    
    public void resetEncoders() {
        // resets encoders to zero
        //m_leftMaster.setSelectedSensorPosition(0, 0, 10);
        m_rightMaster.setSelectedSensorPosition(0, 0, 10);
    }
}