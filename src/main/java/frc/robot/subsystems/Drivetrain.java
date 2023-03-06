package frc.robot.subsystems;

import static frc.robot.Constants.*;
import frc.robot.Constants;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class Drivetrain {
    private static Drivetrain m_singleton = null;

    private OI m_input;

    private Position m_position;

    private WPI_TalonSRX m_frontLeftMotor, m_rearLeftMotor, m_frontRightMotor, m_rearRightMotor;

    private DifferentialDrive m_robotDrive;

    private SlewRateLimiter m_throttleFilter;
    private SlewRateLimiter m_rotationFilter;

    private Drivetrain() {
        m_input = OI.getInstance();
        m_position = Position.getInstance();

        m_frontLeftMotor = new WPI_TalonSRX(kFrontRightWheelChannel);
        m_rearLeftMotor = new WPI_TalonSRX(kRearRightWheelChannel);
        m_frontRightMotor = new WPI_TalonSRX(kFrontLeftWheelChannel);
        m_rearRightMotor = new WPI_TalonSRX(kRearLeftWheelChannel);


        // groups the wheel motors
		MotorControllerGroup driveLeft = new MotorControllerGroup(m_frontLeftMotor, m_rearLeftMotor);
		MotorControllerGroup driveRight = new MotorControllerGroup(m_frontRightMotor, m_rearRightMotor);

        // sets the motors to brake mode
        m_frontLeftMotor.setNeutralMode(NeutralMode.Brake);
        m_rearLeftMotor.setNeutralMode(NeutralMode.Brake);
        m_frontRightMotor.setNeutralMode(NeutralMode.Brake);
        m_rearRightMotor.setNeutralMode(NeutralMode.Brake);

        // sets the motors maximum current limit to 40 amps and enforce it when its exceded for 100 milliseconds
        m_frontLeftMotor.configPeakCurrentLimit(40, 0);
        m_frontLeftMotor.configPeakCurrentDuration(100, 0);
        m_frontLeftMotor.configContinuousCurrentLimit(35);
        m_frontLeftMotor.enableCurrentLimit(true);

        m_rearLeftMotor.configPeakCurrentLimit(40, 0);
        m_rearLeftMotor.configPeakCurrentDuration(100, 0);
        m_rearLeftMotor.configContinuousCurrentLimit(35);
        m_rearLeftMotor.enableCurrentLimit(true);

        m_frontRightMotor.configPeakCurrentLimit(40, 0);
        m_frontRightMotor.configPeakCurrentDuration(100, 0);
        m_frontRightMotor.configContinuousCurrentLimit(35);
        m_frontRightMotor.enableCurrentLimit(true);

        m_rearRightMotor.configPeakCurrentLimit(40, 0);
        m_rearRightMotor.configPeakCurrentDuration(100, 0);
        m_rearRightMotor.configContinuousCurrentLimit(35);
        m_rearRightMotor.enableCurrentLimit(true);

        // invert the right side motors
        m_frontRightMotor.setInverted(true);
        m_rearRightMotor.setInverted(true);

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
            m_robotDrive.arcadeDrive(m_input.getP1LeftY() / kSlowDriveCoefficient, -m_input.getP1RightX() / kSlowDriveCoefficient);
        } else {
            m_robotDrive.arcadeDrive(m_input.getP1LeftY(), -m_input.getP1RightX());
        }
    } 

    public void parkPeriodic() {
        m_robotDrive.arcadeDrive(0, 0);
    }

    public void chargePeriodic() {
        if (m_position.getGyroZAngle() > 8) {
            m_robotDrive.arcadeDrive(0, -0.555);
        }
        else if (m_position.getGyroZAngle() < -8) {
            m_robotDrive.arcadeDrive(0, 0.555);
        }

        //if (m_position.getAccelY() > 0.3) {
        //    m_robotDrive.arcadeDrive(-0.4, 0);
        //}
        //else if (m_position.getAccelY() < -0.3) {
        //    m_robotDrive.arcadeDrive(0.4, 0);
        //}
    }
    
    public void mobilizePeriodic() {
        if (Constants.getTime() < 6.8 && Constants.getTime() > 3.8) {
            //if (m_position.getGyroZAngle() > 8) {
            //    m_robotDrive.arcadeDrive(0, -0.555);
            //}
            //else if (m_position.getGyroZAngle() < -8) {
            //    m_robotDrive.arcadeDrive(0, 0.555);
            //} else {
            //    m_robotDrive.arcadeDrive(0.4, 0);
            //}
            //m_frontLeftMotor.set(0.4 * 1.0674157303 * 1.0235294118 * 0.9714285714);
            //m_rearLeftMotor.set(0.4 * 1.0674157303 * 1.0235294118 * 0.9714285714);
            m_robotDrive.arcadeDrive(0.4, 0);
        }
    }   
}