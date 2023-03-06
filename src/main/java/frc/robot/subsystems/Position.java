package frc.robot.subsystems;

import static frc.robot.Constants.*;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Position {
    private static Position m_singleton = null;

    // this is the built-in accelerometer from the RoboRIO
    private BuiltInAccelerometer m_accel;

    private double accelX;
    private double accelY;
    private double accelZ;

    // this is an L3GD20H triple-axis gyro from Adafruit
    private I2C m_gyro;

    private int gyroX;
    private int gyroY;
    private int gyroZ;

    private double gyroXAngle = 0;
    private double gyroYAngle = 0;
    private double gyroZAngle = 0;

    private double gyroXAngleRate;
    private double gyroYAngleRate;
    private double gyroZAngleRate;

    private Position() {
        // initializes the accel
        m_accel = new BuiltInAccelerometer();

        // initializes the gyro
        m_gyro = new I2C(I2C.Port.kOnboard, kGyroAddress);

        m_gyro.write(kRegCtrl1, 0x0f); // enable all axes and set the output data rate to 95 Hz
        m_gyro.write(kRegCtrl4, 0x10); // set the full-scale range to 2000 dps
    }

    // returns an instance of Position, creating an instance only when one does not already exist
    public static Position getInstance() {
        if (m_singleton == null) {
            m_singleton = new Position();
        }
        return m_singleton;
    }

    public void gyroPeriodic() {
        // storing the accelerometers data
        accelX = m_accel.getX();
        accelY = m_accel.getY();
        accelZ = m_accel.getZ();
        
        SmartDashboard.putNumber("Accel X", accelX);    
        SmartDashboard.putNumber("Accel Y", accelY);
        SmartDashboard.putNumber("Accel Z", accelZ);

        // for storing the data coming from the gyro for conversions
        byte[] gyroData = new byte[6];

        // read the X-axis data from the gyro
        m_gyro.read(kRegOutX | 0x80, 2, gyroData);
        // convert the raw gyroData to a signed 16-bit integer
        int gyroX = ((gyroData[1] << 8) | gyroData[0] & 0xff) / 2400;
        gyroXAngle += gyroX;

        // read the Y-axis data from the gyro
        m_gyro.read(kRegOutY | 0x80, 2, gyroData);
        // convert the raw gyroData to a signed 16-bit integer
        int gyroY = ((gyroData[1] << 8) | gyroData[0] & 0xff) / 2400;
        gyroYAngle += gyroY;

        // read the Z-axis data from the gyro
        m_gyro.read(kRegOutZ | 0x80, 2, gyroData);
        // convert the raw gyroData to a signed 16-bit integer
        int gyroZ = ((gyroData[1] << 8) | gyroData[0] & 0xff) / 2400;
        gyroZAngle += gyroZ;

        // convert the data to degrees per second
        double L3Sensitivity = 0.00875; // sensitivity in degrees per second per digit
        gyroXAngleRate = gyroX * L3Sensitivity;
        gyroYAngleRate = gyroY * L3Sensitivity;
        gyroZAngleRate = gyroZ * L3Sensitivity;

        SmartDashboard.putNumber("L3 X", gyroX);    
        SmartDashboard.putNumber("L3 Y", gyroY);
        SmartDashboard.putNumber("L3 Z", gyroZ);

        SmartDashboard.putNumber("L3 X-Angle", gyroXAngle);    
        SmartDashboard.putNumber("L3 Y-Angle", gyroYAngle);
        SmartDashboard.putNumber("L3 Z-Angle", gyroZAngle);

        SmartDashboard.putNumber("L3 X-Angle Rate", gyroXAngleRate);
        SmartDashboard.putNumber("L3 Y-Angle Rate", gyroYAngleRate);
        SmartDashboard.putNumber("L3 Z-Angle Rate", gyroZAngleRate);
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
    
    public int getGyroX() {
        return gyroX;
    }
    
    public int getGyroY() {
        return gyroY;
    }

    public int getGyroZ() {
        return gyroZ;
    }

    public double getGyroXRate() {
        return gyroXAngleRate;
    }
    
    public double getGyroYRate() {
        return gyroYAngleRate;
    }

    public double getGyroZRate() {
        return gyroZAngleRate;
    }

    public double getGyroXAngle() {
        return gyroXAngle;
    }
    
    public double getGyroYAngle() {
        return gyroYAngle;
    }

    public double getGyroZAngle() {
        return gyroZAngle;
    }
    public void calibrateGyro() {
        gyroXAngle = 0;
        gyroYAngle = 0;
        gyroZAngle = 0;
    }
}