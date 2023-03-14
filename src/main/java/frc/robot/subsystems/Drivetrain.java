package frc.robot.subsystems;

import static frc.robot.Constants.*;
import frc.robot.Robot;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain {
    private static Drivetrain m_singleton = null;

    private OI m_input;

    private Position m_position;

    private WPI_TalonSRX m_leftMaster, m_rightMaster;
    private WPI_VictorSPX m_leftSlave, m_rightSlave;

    private DifferentialDrive m_robotDrive;

    private SlewRateLimiter m_throttleFilter;
    private SlewRateLimiter m_rotationFilter;

    private Drivetrain() {
        m_input = OI.getInstance();
        m_position = Position.getInstance();

        m_leftMaster = new WPI_TalonSRX(kFrontRightWheelChannel);
        m_leftSlave = new WPI_VictorSPX(kRearRightWheelChannel);
        m_rightMaster = new WPI_TalonSRX(kFrontLeftWheelChannel);
        m_rightSlave = new WPI_VictorSPX(kRearLeftWheelChannel);

        // sets the motors to brake mode
        m_leftMaster.setNeutralMode(NeutralMode.Brake);
        m_leftSlave.setNeutralMode(NeutralMode.Brake);
        m_rightMaster.setNeutralMode(NeutralMode.Brake);
        m_rightSlave.setNeutralMode(NeutralMode.Brake);

        // sets the motors maximum current limit to 40 amps and enforce it when its exceded for 100 milliseconds
        m_leftMaster.configPeakCurrentLimit(40, 0);
        m_leftMaster.configPeakCurrentDuration(100, 0);
        m_leftMaster.configContinuousCurrentLimit(35);
        m_leftMaster.enableCurrentLimit(true);

        m_rightMaster.configPeakCurrentLimit(40, 0);
        m_rightMaster.configPeakCurrentDuration(100, 0);
        m_rightMaster.configContinuousCurrentLimit(35);
        m_rightMaster.enableCurrentLimit(true);

        // inverts the right side 
        m_leftMaster.setInverted(false);
        m_rightMaster.setInverted(true);

        // slave setups
        m_leftSlave.follow(m_leftMaster);
        m_rightSlave.follow(m_leftMaster);

        m_leftSlave.setInverted(InvertType.FollowMaster);
        m_rightSlave.setInverted(InvertType.FollowMaster);

        // groups the wheel motors
		MotorControllerGroup driveLeft = new MotorControllerGroup(m_leftMaster, m_leftSlave);
		MotorControllerGroup driveRight = new MotorControllerGroup(m_rightMaster, m_rightSlave);

        // creates the robot drive
		m_robotDrive = new DifferentialDrive(driveLeft, driveRight);

        // slew rate limiters create limits on the acceleration for smoother driving
        m_throttleFilter = new SlewRateLimiter(kThrottleAcceleration);
        m_rotationFilter = new SlewRateLimiter(kRotationAcceleartion);
    }

    // returns an instance of DriveTrain, creating an instance only when one does not already exist
	public static Drivetrain getInstance() {
		if (m_singleton == null) {
			m_singleton = new Drivetrain();
		}
		return m_singleton;
	}
    
    public void drivePeriodic() {
        if (m_input.getP1LeftBumperDown() || m_input.getP1RightBumperDown()) {
            m_robotDrive.arcadeDrive(m_input.getP1LeftY() / kSlowDriveCoefficient, -m_input.getP1RightX() / kRotationDampenerCoefficient / kSlowDriveCoefficient);
        } else {
            m_robotDrive.arcadeDrive(m_input.getP1LeftY(), -m_input.getP1RightX() / kRotationDampenerCoefficient);
        }
        
        SmartDashboard.putNumber("Left Drive Supply Current", m_leftMaster.getSupplyCurrent());
        SmartDashboard.putNumber("Left Drive Stator Current", m_leftMaster.getStatorCurrent());
        SmartDashboard.putNumber("Right Drive Supply Current", m_rightMaster.getSupplyCurrent());
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
}