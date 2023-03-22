package frc.robot.subsystems;

import static frc.robot.Constants.*;
import frc.robot.Robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Grabber {
    private static Grabber m_singleton = null;

    private OI m_input;

    private Orientation m_position;

    private WPI_TalonSRX m_grabber;

    private boolean hasInput = false;
    private boolean hasGamepiece = true;

    private Grabber() {
        m_input = OI.getInstance();
        m_position = Orientation.getInstance();

        m_grabber = new WPI_TalonSRX(kGrabberID);  // ID of 17

        // sets the motor to break mode
        m_grabber.setNeutralMode(NeutralMode.Brake);

        // makes sure motor is not inverted 
        m_grabber.setInverted(false);
    }

    // returns an instance of Grabber, creating an instance only when one does not already exist
    public static Grabber getInstance() {
        if (m_singleton == null) {
            m_singleton = new Grabber();
        }
        return m_singleton;
    }

    public void grabPeriodic() {
        if (m_input.getP2LeftBumperDown()) {
            hasInput = true;
            m_grabber.set(0.6);
        } else if (m_input.getP2RightBumperDown()) {
            hasInput = true;
            m_grabber.set(-0.2);
        } else {
            hasInput = false;
            m_grabber.set(0);
        }

        if (!hasInput && m_grabber.getSupplyCurrent() > 10) {
            hasGamepiece = true;
        } else if (!hasInput && m_grabber.getSupplyCurrent() <= 10) {
            hasGamepiece = false;
        }

        if (hasGamepiece && !hasInput) m_grabber.set(0.1);

        SmartDashboard.putNumber("Grabber Supply Current", m_grabber.getSupplyCurrent());
        SmartDashboard.putNumber("Grabber Stator Current", m_grabber.getStatorCurrent());

    }

    public boolean grabberHasInput() {
        return hasInput;
    }

    public boolean holdingGamepiece() {
        return hasGamepiece;
    }
}
