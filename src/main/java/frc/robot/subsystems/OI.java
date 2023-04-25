package frc.robot.subsystems;

import frc.robot.Robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// Operator Inputs
public class OI {
    private static OI m_singleton = null;

    public XboxController m_p1Controller, m_p2Controller;

    private OI() {
        m_p1Controller = new XboxController(0); //Robot.kP1XboxPort
        m_p2Controller = new XboxController(2   ); //Robot.kP2XboxPort
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

    public boolean isP1LeftStickDown() {
        return m_p1Controller.getLeftStickButtonPressed();
    }

    public double getP1RightX() {
        // rounds the returned double to 4 decimal places for smoother movement
        return Math.round(m_p1Controller.getRightX() * 10000.0 + 5) / 10000.0;
    }

    public double getP1RightY() {
        return m_p1Controller.getRightY();
    }

    public boolean isP1RightStickDown() {
        return m_p1Controller.getRightStickButtonPressed();
    }
    
    public double getP1LeftTriggerAxis() {
        return m_p1Controller.getLeftTriggerAxis();
    }

    public double getP1RightTriggerAxis() {
        return m_p1Controller.getRightTriggerAxis();
    }

    public boolean isP1LeftTriggerDown() {
        return m_p1Controller.getLeftTriggerAxis() > 0.1;
    }

    public boolean isP1RightTriggerDown() {
        return m_p1Controller.getRightTriggerAxis() > 0.1;
    }

    public boolean isP1LeftBumperDown() {
        return m_p1Controller.getLeftBumper();
    }

    public boolean isP1RightBumperDown() {
        return m_p1Controller.getRightBumper();
    }

    public boolean isP1ADown() {
        return m_p1Controller.getAButton();
    }

    public boolean isP1BDown() {
        return m_p1Controller.getBButton();
    }

    public boolean isP1XDown() {
        return m_p1Controller.getXButton();
    }

    public boolean isP1YDown() {
        return m_p1Controller.getYButton();
    }

    public boolean isP1DPadUp() {
        int angle = m_p1Controller.getPOV();
        return angle == 0;
    }

    public boolean isP1DPadLeft() {
        int angle = m_p1Controller.getPOV();
        return angle == 270;
    }

    public boolean isP1DPadRight() {
        int angle = m_p1Controller.getPOV();
        return angle == 90;
    }

    public boolean isP1DPadDown() {
        int angle = m_p1Controller.getPOV();
        return angle == 180;
    }

    public boolean isP1StartDown() {
        return m_p1Controller.getStartButton();
    }

    public boolean isP1MenuDown() {
        return m_p1Controller.getBackButton();
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

    public boolean isP2LeftStickDown() {
        return m_p2Controller.getLeftStickButtonPressed();
    }

    public double getP2RightX() {
        // rounds the returned double to 4 decimal places for smoother movement
        return Math.round(m_p2Controller.getRightX() * 10000.0 + 5) / 10000.0;
    }

    public double getP2RightY() {
        return m_p2Controller.getRightY();
    }

    public boolean isP2RightStickDown() {
        return m_p2Controller.getRightStickButtonPressed();
    }
    
    public double getP2LeftTriggerAxis() {
        return m_p2Controller.getLeftTriggerAxis();
    }

    public double getP2RightTriggerAxis() {
        return m_p2Controller.getRightTriggerAxis();
    }

    public boolean isP2LeftTriggerDown() {
        return m_p2Controller.getLeftTriggerAxis() > 0.1;
    }

    public boolean isP2RightTriggerDown() {
        return m_p2Controller.getRightTriggerAxis() > 0.1;
    }

    public boolean isP2LeftBumperDown() {
        return m_p2Controller.getLeftBumper();
    }

    public boolean isP2RightBumperDown() {
        return m_p2Controller.getRightBumper();
    }

    public boolean isP2ADown() {
        return m_p2Controller.getAButton();
    }

    public boolean isP2BDown() {
        return m_p2Controller.getBButton();
    }

    public boolean isP2XDown() {
        return m_p2Controller.getXButton();
    }

    public boolean isP2YDown() {
        return m_p2Controller.getYButton();
    }

    public boolean isP2DPadUp() {
        int angle = m_p2Controller.getPOV();
        return angle == 0;
    }

    public boolean isP2DPadLeft() {
        int angle = m_p2Controller.getPOV();
        return angle == 270;
    }

    public boolean isP2DPadRight() {
        int angle = m_p2Controller.getPOV();
        return angle == 90;
    }

    public boolean isP2DPadDown() {
        int angle = m_p2Controller.getPOV();
        return angle == 180;
    }

    public boolean isP2StartDown() {
        return m_p2Controller.getStartButton();
    }
    
    public boolean isP2MenuDown() {
        return m_p2Controller.getBackButton();
    }
}