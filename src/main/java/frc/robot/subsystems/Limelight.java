package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {

    private static Limelight m_singleton = null;
    private NetworkTable m_table;

    private double x = 0;     
    private double y = 0;
    private double z = 0;
    private double area = 0;   
    private double skew = 0;

    public Limelight() {
        m_table = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public static Limelight getInstance() {
		if (m_singleton == null) {
			m_singleton = new Limelight();
		}
		return m_singleton;
	}

    public void limelightPeriodic() {
        // pulls the data from the limelight
        x = m_table.getEntry("tx").getDouble(0);
        y = m_table.getEntry("ty").getDouble(0);
        area = m_table.getEntry("ta").getDouble(0);
        skew = m_table.getEntry("ts").getDouble(0);
        
        //z = this.GetDistanceToTarget();

        // saves the data into the smart dashboard display
        SmartDashboard.putNumber("Limelight X", x);
        SmartDashboard.putNumber("Limelight Y", y);
        SmartDashboard.putNumber("Limelight Z", z);
        SmartDashboard.putNumber("Limelight Area", area);
        SmartDashboard.putNumber("Limelight Skew", skew);
    }
}