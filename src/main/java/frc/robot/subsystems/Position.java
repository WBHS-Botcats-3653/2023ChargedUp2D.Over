package frc.robot.subsystems;

import static frc.robot.Constants.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.ADIS16470_IMU;

public class Position {
    private static Position m_singleton = null;

    // this is an L3GD20H triple-axis gyro from Adafruit
    private ADIS16470_IMU m_IMU;

    private double gyroX;
    private double gyroY;
    private double gyroZ;

    private double accelX;
    private double accelY;
    private double accelZ;

    private Position() {
        // initializes the IMU
        m_IMU = new ADIS16470_IMU();
    }

    // returns an instance of Position, creating an instance only when one does not already exist
    public static Position getInstance() {
        if (m_singleton == null) {
            m_singleton = new Position();
        }
        return m_singleton;
    }

    public void gyroPeriodic() {
        // stores the gyro information from the IMU
        gyroX = 0;
        gyroY = 0;
        gyroZ = 0;

        accelX = 0;
        accelY = 0;
        accelZ = 0;
        
        SmartDashboard.putNumber("IMU Gyro X", gyroX);    
        SmartDashboard.putNumber("IMU Gyro Y", gyroY);
        SmartDashboard.putNumber("IMU Gyro Z", gyroZ);

        SmartDashboard.putNumber("IMU Accel X", accelX);
        SmartDashboard.putNumber("IMU Accel Y", accelY);
        SmartDashboard.putNumber("IMU Accel Z", accelZ);
    }

    public void calibrateIMU() {
        
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