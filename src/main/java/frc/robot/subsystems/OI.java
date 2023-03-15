package frc.robot.subsystems;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.XboxController;

// Operator Inputs
public class OI {
    private static OI m_singleton = null;

    public XboxController m_p1Controller, m_p2Controller;

    private OI() {
        m_p1Controller = new XboxController(Robot.kP1XboxPort);
        m_p2Controller = new XboxController(Robot.kP2XboxPort);
    }

    public static OI getInstance() {
		if (m_singleton == null) {
			m_singleton = new OI();
		}
		return m_singleton;
	}

    /** 
     * player one's inputs
     */
    public double getP1LeftX() {
        // rounds the returned double to 4 decimal places for smoother movement
        return Math.round(m_p1Controller.getLeftX() * 10000.0 + 5) / 10000.0;
    }

    public double getP1LeftY() {
        // rounds the returned double to 4 decimal places for smoother movement
        return Math.round(m_p1Controller.getLeftY() * 10000.0 + 5) / 10000.0;
    }

    public double getP1RightX() {
        // rounds the returned double to 4 decimal places for smoother movement
        return Math.round(m_p1Controller.getRightX() * 10000.0 + 5) / 10000.0;
    }

    public double getP1RightY() {
        return m_p1Controller.getRightY();
    }
    
    public double getP1LeftTriggerAxis() {
        return m_p1Controller.getLeftTriggerAxis();
    }

    public double getP1RightTriggerAxis() {
        return m_p1Controller.getRightTriggerAxis();
    }

    public boolean getP1LeftTriggerDown() {
        return m_p1Controller.getLeftTriggerAxis() > 0.05;
    }

    public boolean getP1RightTriggerDown() {
        return m_p1Controller.getRightTriggerAxis() > 0.05;
    }

    public boolean getP1LeftBumperDown() {
        return m_p1Controller.getLeftBumper();
    }

    public boolean getP1RightBumperDown() {
        return m_p1Controller.getRightBumper();
    }

    public boolean getP1ADown() {
        return m_p1Controller.getAButton();
    }

    public boolean getP1BDown() {
        return m_p1Controller.getBButton();
    }

    public boolean getP1XDown() {
        return m_p1Controller.getXButton();
    }

    public boolean getP1YDown() {
        return m_p1Controller.getYButton();
    }

    /** 
     * player two's inputs
     */
    public double getP2LeftX() {
        // rounds the returned double to 4 decimal places for smoother movement
        return Math.round(m_p2Controller.getLeftX() * 10000.0 + 5) / 10000.0;
    }

    public double getP2LeftY() {
        // rounds the returned double to 4 decimal places for smoother movement
        return Math.round(m_p2Controller.getLeftY() * 10000.0 + 5) / 10000.0;
    }

    public double getP2RightX() {
        // rounds the returned double to 4 decimal places for smoother movement
        return Math.round(m_p2Controller.getRightX() * 10000.0 + 5) / 10000.0;
    }

    public double getP2RightY() {
        return m_p2Controller.getRightY();
    }
    
    public double getP2LeftTriggerAxis() {
        return m_p2Controller.getLeftTriggerAxis();
    }

    public double getP2RightTriggerAxis() {
        return m_p2Controller.getRightTriggerAxis();
    }

    public boolean getP2LeftTriggerDown() {
        return m_p1Controller.getLeftTriggerAxis() > 0.05;
    }

    public boolean getP2RightTriggerDown() {
        return m_p1Controller.getRightTriggerAxis() > 0.05;
    }

    public boolean getP2LeftBumperDown() {
        return m_p2Controller.getLeftBumper();
    }

    public boolean getP2RightBumperDown() {
        return m_p2Controller.getRightBumper();
    }

    public boolean getP2ADown() {
        return m_p1Controller.getAButton();
    }

    public boolean getP2BDown() {
        return m_p1Controller.getBButton();
    }

    public boolean getP2XDown() {
        return m_p1Controller.getXButton();
    }

    public boolean getP2YDown() {
        return m_p1Controller.getYButton();
    }
}