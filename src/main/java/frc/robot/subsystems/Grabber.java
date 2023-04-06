package frc.robot.subsystems;

import static frc.robot.Constants.*;
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
    private boolean hasGamepiece = true;
        
    private static enum State {
        IDLE,
        DROPPING,
        GRABBING
    }

    public State grabberState = State.IDLE;

    private Grabber() {
        m_input = OI.getInstance();

        m_grabber = new WPI_TalonSRX(kGrabberID);  // ID of 18

        // sets the motor to break mode
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

    public void checkForGamePiece() {
        if (this.hasInput() && 1 < Robot.time - Robot.grabStartTime){
            hasGamepiece = false;
        } else if (hasGrabInput && m_grabber.getStatorCurrent() > 14) {
            hasGamepiece = true;
        }

        this.updateSmartDashboard();
    }

    public void grabGamePiece(double ingestSpeed) {
        if (!hasGamepiece) {
            hasGrabInput = true;
            grabberState = State.GRABBING;
            m_grabber.set(ingestSpeed);
        } else {
            grabberState = State.IDLE;
            m_grabber.set(0);
        }

        this.updateSmartDashboard();
    }

    public void dropGamePiece(double ejectSpeed) {
        if (hasGamepiece) {
            hasDropInput = true;
            grabberState = State.DROPPING;
            m_grabber.set(-ejectSpeed);
        } else {
            grabberState = State.IDLE;
            m_grabber.set(0);
        }

        this.updateSmartDashboard();
    }

    public void operateGrabPeriodic() {
        if (this.hasInput() && m_grabber.getStatorCurrent() <= 2.5) {
            hasGamepiece = false;
        } else if (hasGrabInput && m_grabber.getStatorCurrent() > 14) {
            hasGamepiece = true;
        }

        if (m_input.isP1LeftBumperDown()) {
            hasDropInput = true;
            
            grabberState = State.DROPPING;
            m_grabber.set(-0.50);
        } else if (m_input.isP1RightBumperDown()) {
            hasGrabInput = true;

            grabberState = State.GRABBING;
            m_grabber.set(1);
        } else {
            hasDropInput = false;
            hasGrabInput = false;

            grabberState = State.IDLE;
            m_grabber.set(0);
        }

        this.updateSmartDashboard();
    }

    public void updateSmartDashboard() {
        SmartDashboard.putBoolean("Has Grab Input", hasGrabInput);
        SmartDashboard.putBoolean("Has Drop Input", hasDropInput);
        SmartDashboard.putBoolean("Holding Piece", hasGamepiece);
        SmartDashboard.putNumber("Grabber Stator Current", m_grabber.getStatorCurrent());
    }

    public State getState() {
        return grabberState;
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
