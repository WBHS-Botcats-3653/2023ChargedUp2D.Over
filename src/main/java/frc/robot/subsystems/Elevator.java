package frc.robot.subsystems;

import static frc.robot.Constants.*;
import frc.robot.Robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {
    private static Elevator m_singleton = null;

    private OI m_input;

    private Orientation m_position;

    private WPI_TalonSRX m_elevatorMaster;
    private WPI_VictorSPX  m_elevatorSlave;

    private boolean isExtended = false;
    private boolean isRetracted = true;

    private Elevator() {
        m_input = OI.getInstance();
        m_position = Orientation.getInstance();

        m_elevatorMaster = new WPI_TalonSRX(kLeftElevatorID);   // ID of 19
        m_elevatorSlave = new WPI_VictorSPX(kRightElevatorID);  // ID of 18

        // sets the motors to brake mode
        m_elevatorMaster.setNeutralMode(NeutralMode.Brake);
        m_elevatorSlave.setNeutralMode(NeutralMode.Brake);

        // inverts the right side
        m_elevatorMaster.setInverted(false);
        m_elevatorSlave.setInverted(true);

        // slave setup
        m_elevatorSlave.follow(m_elevatorMaster);
    }

    // returns an instance of Elevator, creating an instance only when one does not already exist
	public static Elevator getInstance() {
		if (m_singleton == null) {
			m_singleton = new Elevator();
		}
		return m_singleton;
	}

    public void elevatorPeriodic() {
        if (m_input.getP2RightTriggerDown()) {
            m_elevatorMaster.set(0.7);
        } else if (m_input.getP2LeftTriggerDown()) {
            m_elevatorMaster.set(0.5);
        } else {
            m_elevatorMaster.set(0);
        }

        SmartDashboard.putNumber("Grabber Supply Current", m_elevatorMaster.getSupplyCurrent());
        SmartDashboard.putNumber("Grabber Stator Current", m_elevatorMaster.getStatorCurrent());
    }

    public boolean isElevatorExtended() {
        return isExtended;
    }

    public boolean isElevatorRetracted() {
        return isRetracted;
    }
}
