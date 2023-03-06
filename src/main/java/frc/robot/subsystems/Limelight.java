package frc.robot.subsystems;

import static frc.robot.Constants.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/*public class Limelight {

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
    }}*/
    
    /*public double GetDistanceToTarget(){
        //if there is noting in view return -1
        if(!isThereValidTarget())
            return -1;
        else{
            //dist calculations
            //every unit of distance increased the total view area increases this much
            //note: to use trig values must be in radians
            double viewAreaUnitIncrease = 4*Math.sin(kLimelightVerticalViewAngle * (Math.PI/180f))*Math.sin(kLimelightHorizontalViewAngle * (Math.PI/180f));
            
            double areaOfObj = m_table.getEntry("ta").getDouble(0);
            //gets the distance in the forward axis (dosen't calc with offset)
            double rawDist = Math.pow(kObjSize,2)/(areaOfObj/1*viewAreaUnitIncrease);
            //we only care about the horizontal offset now vertical since all peices when we are looking will be on the floor
            
            double horizontalPixelSizeofObj = m_table.getEntry("thor").getDouble(0);
            //unit dist per pixel from box reference(units are the objUnits : inches)
            double unitPerPixel = horizontalPixelSizeofObj/kObjSize;
            double horizontalPixelXOffset = m_table.getEntry("tx").getDouble(0);
            double HorizontalOffset = horizontalPixelXOffset*unitPerPixel;
            //combines the 2 distances into a vector
            //Vector2 objPosition = new Vector2(HorizontalOffset, rawDist);
            //returns vector magnitude: true dist
            //return objPosition.magnitude;
            
            //pythagorus approach
            double trueDist = Math.sqrt(Math.pow(HorizontalOffset,2)+ Math.pow(rawDist,2));
            return trueDist;
            //note there is a percent error here that has yet to be calculated
        }
    }
    public boolean isThereValidTarget(){
        //gets the number of targets the limelight is reading
        double validTarget = m_table.getEntry("tv").getDouble(0);
        //if validTarget is greater then zero return true 
        return (validTarget!=0)? true: false;
    }*/