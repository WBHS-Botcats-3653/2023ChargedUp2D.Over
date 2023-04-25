package frc.robot.subsystems;

import static frc.robot.Constants.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.ADIS16470_IMU;

public class Heading {
    private static Heading m_singleton = null;

    // this is an ADIS16470 IMU from Analog Devices
    private ADIS16470_IMU m_IMU;

    private double gyroX;
    private double gyroY;
    private double gyroZ;

    private double accelX;
    private double accelY;
    private double accelZ;

    private Heading() {
        // initializes the IMU
        m_IMU = new ADIS16470_IMU();
    }

    // returns an instance of Position, creating an instance only when one does not already exist
    public static Heading getInstance() {
        if (m_singleton == null) {
            m_singleton = new Heading();
        }
        return m_singleton;
    }

    public void IMUPeriodic() {
        // stores the gyro information from the IMU
        gyroX = m_IMU.getXFilteredAccelAngle();
        gyroY = m_IMU.getYFilteredAccelAngle();
        gyroZ = m_IMU.getAngle();

        accelX = m_IMU.getAccelX();
        accelY = m_IMU.getAccelY();
        accelZ = m_IMU.getAccelZ();
        
        //SmartDashboard.putNumber("IMU Gyro X", gyroX);    
        SmartDashboard.putNumber("IMU Gyro Y", gyroY);
        //SmartDashboard.putNumber("IMU Gyro Z", gyroZ);

        //SmartDashboard.putNumber("IMU Accel X", accelX);
        //SmartDashboard.putNumber("IMU Accel Y", accelY);
        //SmartDashboard.putNumber("IMU Accel Z", accelZ);
    }

    public void resetIMU() {
        m_IMU.reset();
    }

    public void calibrateIMU() {
        m_IMU.calibrate();
    }

    public double getGyroX() {
        return gyroX;
    }
    
    public double getGyroY() {
        return gyroY;
    }

    public double getGyroZ() {
        return gyroZ;
    }

    public double getAccelX() {
        return accelX;
    }
    
    public double getAccelY() {
        return accelY;
    }

    public double getAccelZ() {
        return accelZ;
    }
}