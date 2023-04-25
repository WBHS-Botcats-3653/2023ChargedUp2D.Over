package frc.robot.subsystems;

import static frc.robot.Constants.*;

import javax.naming.ldap.ManageReferralControl;

import frc.robot.Robot;
import frc.robot.subsystems.Elevator;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Grabber {
    private static Grabber m_singleton = null;

    private OI m_input;

    private WPI_TalonSRX m_grabber;

    private boolean hasDropInput = false;
    private boolean hasGrabInput = false;
    private boolean hasManualInput = false;
    private boolean hasGamepiece = true;
    private boolean isSecuringCycle = false;
    private boolean recordedSecuringCycleToggle = false;
    private double securingCycleToggleTime;

    private boolean recordedDropStartTime = false;
    private double dropStartTime;

        
    public static enum Action {
        NONE,
        DROPPING,
        GRABBING
    }

    public Action grabberAction = Action.NONE;

    private Grabber() {
        m_input = OI.getInstance();

        m_grabber = new WPI_TalonSRX(kGrabberID);  // ID of 18

        // sets the motor to break Action
        m_grabber.setNeutralMode(NeutralMode.Brake);

        // makes sure motor is inverted 
        m_grabber.setInverted(true);
    }

    // returns an instance of Grabber, creating an instance only when one does not already exist
    public static Grabber getInstance() {
        if (m_singleton == null) {
            m_singleton = new Grabber();
        }
        return m_singleton;
    }

    public void checkForGamePiece(boolean isDropping) {
        if (isDropping) {
            if (this.hasInput() && 1 < Robot.time - Robot.grabberStartTime){
                hasGamepiece = false;
            } else if (hasGrabInput && m_grabber.getStatorCurrent() > 30) {
                hasGamepiece = true;
            }
        } else {
            if (this.hasInput() && 3 < Robot.time - Robot.grabberStartTime){
                hasGamepiece = false;
            } else if (hasGrabInput && m_grabber.getStatorCurrent() > 30) {
                hasGamepiece = true;
            }
        }

        //this.updateSmartDashboard();
    }

    public void grabGamePiece(double ingestSpeed, double actionTime) {
        if (actionTime > Robot.time - Robot.grabberStartTime) {
            //System.out.println("started grabbing");
            hasGrabInput = true;
            grabberAction = Action.GRABBING;
            m_grabber.set(Math.abs(ingestSpeed));
        } else {
            //System.out.println("stopped grabbing");
            grabberAction = Action.NONE;
            m_grabber.set(0.2);
            Robot.resetCurrentTargetStyle();
        }

        //this.updateSmartDashboard();
    }

    public void grabGamePiece(double ingestSpeed) {
        if (m_input.isP2LeftBumperDown() || m_input.isP2RightBumperDown()) {
            hasGrabInput = true;
            grabberAction = Action.GRABBING;
            m_grabber.set(Math.abs(ingestSpeed));
        } else {
            grabberAction = Action.NONE;
            m_grabber.set(0.2);
            Robot.resetCurrentTargetStyle();
        }

        //this.updateSmartDashboard();
    }

    public void dropGamePiece(double ejectSpeed, double actionTime) {
        if (actionTime > Robot.time - Robot.grabberStartTime) {
            //System.out.println("started dropping");
            hasDropInput = true;
            grabberAction = Action.DROPPING;
            m_grabber.set(-Math.abs(ejectSpeed));
        } else {
            //System.out.println("stopped dropping");
            grabberAction = Action.NONE;
            m_grabber.set(0.2);
            Robot.resetCurrentTargetStyle();
        }

        //this.updateSmartDashboard();
    }

    public void dropGamePiece(double ejectSpeed) {
        if ((m_input.isP1YDown() || m_input.isP1BDown() || m_input.isP1ADown())) {
            dropStartTime = Robot.time;
            //recordedDropStartTime = true;

        } else if (!(m_input.isP1YDown() || m_input.isP1BDown() || m_input.isP1ADown())){

            if (dropStartTime < Robot.time - Robot.grabberStartTime) {
                hasDropInput = true;
                grabberAction = Action.DROPPING;
                m_grabber.set(-Math.abs(ejectSpeed));
            } else {
                grabberAction = Action.NONE;
                m_grabber.set(0.2);
                Robot.resetCurrentTargetStyle();
                recordedDropStartTime = false;
            }
        }

        //this.updateSmartDashboard();
    }

    public void secureGamePiece() {
        if (grabberAction == Action.NONE && !hasManualInput) {
            if (isSecuringCycle && !recordedSecuringCycleToggle) {
                securingCycleToggleTime = Robot.time;
                recordedSecuringCycleToggle = true;
            } else if (isSecuringCycle && recordedSecuringCycleToggle) {
                if (0.5 < Robot.time - securingCycleToggleTime) {
                    m_grabber.set(0.7);
                } else {
                    isSecuringCycle = false;
                    recordedSecuringCycleToggle = false;
                }
            } else if (!isSecuringCycle && !recordedSecuringCycleToggle) {
                securingCycleToggleTime = Robot.time;
                recordedSecuringCycleToggle = true;
            } else if (!isSecuringCycle && recordedSecuringCycleToggle) {
                if (1 < Robot.time - securingCycleToggleTime) {
                    m_grabber.set(0);
                } else {
                    isSecuringCycle = true;
                    recordedSecuringCycleToggle = false;
                }
            }
        }
    }

    public void operateGrabPeriodic() {
        if (this.hasInput() && m_grabber.getStatorCurrent() <= 2.5) {
            hasGamepiece = false;
        } else if (hasGrabInput && m_grabber.getStatorCurrent() > 30) {
            hasGamepiece = true;
        }

        if (m_input.isP1DPadLeft()) {
            hasDropInput = true;
            hasManualInput = true;
            
            //grabberAction = Action.DROPPING;
            m_grabber.set(-0.25);
        } else if (m_input.isP1DPadRight()) {
            hasGrabInput = true;
            hasManualInput = true;

            //grabberAction = Action.GRABBING;
            m_grabber.set(1);
        } else if (grabberAction == Action.NONE) {
            hasDropInput = false;
            hasGrabInput = false;
            hasManualInput = false;

            //grabberAction = Action.NONE;
            m_grabber.set(0.2);
        }

        //this.updateSmartDashboard();
    }

    public void updateSmartDashboard() {
        SmartDashboard.putBoolean("Has Grab Input", hasGrabInput);
        SmartDashboard.putBoolean("Has Drop Input", hasDropInput);
        SmartDashboard.putBoolean("Holding Piece", hasGamepiece);
        SmartDashboard.putNumber("Grabber Stator Current", m_grabber.getStatorCurrent());
    }

    public Action getAction() {
        return grabberAction;
    }

    public boolean hasGrabInput() {
        return hasGrabInput;
    }

    public boolean hasDropInput() {
        return hasDropInput;
    }

    public boolean hasInput() {
        return hasDropInput || hasGrabInput;
    }

    public boolean holdingGamepiece() {
        return hasGamepiece;
    }
}
