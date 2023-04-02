package frc.robot.subsystems;

import static frc.robot.Constants.*;
import frc.robot.Robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {
    private static Elevator m_singleton = null;

    private OI m_input;
    private Grabber m_grabber;

    private WPI_TalonSRX m_elevatorExtendWinch, m_elevatorRetractWinch;

    private double output;
    private double error;

    private String elevatorState = "idle";

    public static double grabStartTime;

    private boolean isScoring = false;
    private boolean isExtended = false;
    private boolean isRetracted = true;
    private boolean hasInput = false;
    private boolean goingHigh = false;
    private boolean goingMiddle = false;
    private boolean goingLow = false;
    private boolean goingDouble = false;
    private boolean goingSingle = false;

    private boolean recordedExtendTime = false;

    private Elevator() {
        m_input = OI.getInstance();
        m_grabber = Grabber.getInstance();

        m_elevatorExtendWinch = new WPI_TalonSRX(kElevatorExtendWinchID);    // ID of 19
        m_elevatorRetractWinch = new WPI_TalonSRX(kElevatorRetractWinchID);  // ID of 17

        // sets the motors to coast mode
        m_elevatorExtendWinch.setNeutralMode(NeutralMode.Brake);
        m_elevatorRetractWinch.setNeutralMode(NeutralMode.Brake);

        // inverts the winch motors
        m_elevatorExtendWinch.setInverted(true);
        m_elevatorRetractWinch.setInverted(true);

        // encoder setups
        m_elevatorExtendWinch.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        m_elevatorRetractWinch.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        
        // reverses the positive counting direction for both winches
        m_elevatorExtendWinch.setSensorPhase(false);
        m_elevatorRetractWinch.setSensorPhase(true);

        // resets encoders to zero
        m_elevatorExtendWinch.setSelectedSensorPosition(0, 0, 10);
        m_elevatorRetractWinch.setSelectedSensorPosition(0, 0, 10);
    }

    // returns an instance of Elevator, creating an instance only when one does not already exist
	public static Elevator getInstance() {
		if (m_singleton == null) {
			m_singleton = new Elevator();
		}
		return m_singleton;
	}

    public void extendElevatorHigh() {
        if ((m_elevatorExtendWinch.getSelectedSensorPosition() < kElevatorHighTargetPoint) && !hasInput) {
            output = (kElevatorHighTargetPoint - m_elevatorExtendWinch.getSelectedSensorPosition()) * kExtendingP;

            elevatorState = "extending";
            m_elevatorExtendWinch.set(output);
            m_elevatorRetractWinch.set(output * kPullyRatio);
        } else if (m_elevatorExtendWinch.getSelectedSensorPosition() >= kElevatorHighTargetPoint) {
            elevatorState = "extended";
            m_elevatorExtendWinch.set(0);
            m_elevatorRetractWinch.set(0);
        }
    }

    public void extendElevatorMiddle() {
        if ((m_elevatorExtendWinch.getSelectedSensorPosition() < kElevatorMiddleTargetPoint) && !hasInput) {
            output = (kElevatorHighTargetPoint - m_elevatorExtendWinch.getSelectedSensorPosition()) * kExtendingP;

            elevatorState = "extending";
            m_elevatorExtendWinch.set(output);
            m_elevatorRetractWinch.set(output * kPullyRatio);
        } else if (m_elevatorExtendWinch.getSelectedSensorPosition() >= kElevatorHighTargetPoint) {
            elevatorState = "extended";
            m_elevatorExtendWinch.set(0);
            m_elevatorRetractWinch.set(0);
        }
    }

    public void extendElevatorLow() {
        if ((m_elevatorExtendWinch.getSelectedSensorPosition() < kElevatorLowTargetPoint) && !hasInput) {
            output = (kElevatorHighTargetPoint - m_elevatorExtendWinch.getSelectedSensorPosition()) * kExtendingP;

            elevatorState = "extending";
            m_elevatorExtendWinch.set(output);
            m_elevatorRetractWinch.set(output * kPullyRatio);
        } else if (m_elevatorExtendWinch.getSelectedSensorPosition() >= kElevatorHighTargetPoint) {
            elevatorState = "extended";
            m_elevatorExtendWinch.set(0);
            m_elevatorRetractWinch.set(0);
        }
    }

    public void retractElevator() {
        if ((m_elevatorExtendWinch.getSelectedSensorPosition() > 10) && (!m_grabber.holdingGamepiece()) && !hasInput){
            output = (10 - m_elevatorExtendWinch.getSelectedSensorPosition()) * kRetractingP;

            elevatorState = "retracting";
            m_elevatorExtendWinch.set(output);
            m_elevatorRetractWinch.set(output * kPullyRatio);

            if (!m_grabber.holdingGamepiece()) recordedExtendTime = false;
        } else if (m_elevatorExtendWinch.getSelectedSensorPosition() <= 10) {
            elevatorState = "idle";
            m_elevatorExtendWinch.set(0);
            m_elevatorRetractWinch.set(0);
        }
    }

    public void elevatorPeriodic() {
        if (m_input.isP2YDown()) {
            isScoring = true;
            goingHigh = true;
        }

        if (isScoring && goingHigh) {

            if ((m_elevatorExtendWinch.getSelectedSensorPosition() < kElevatorHighTargetPoint) && !hasInput) {
                error = kElevatorHighTargetPoint - m_elevatorExtendWinch.getSelectedSensorPosition();
                output = kExtendingP * error;

                isRetracted = false;
                m_elevatorExtendWinch.set(output);
            } else if (m_elevatorExtendWinch.getSelectedSensorPosition() >= kElevatorHighTargetPoint) {
                isExtended = true;
                //isRetracted = false;
                error = 0;
            }

            if (isExtended) {
                m_grabber.dropGamePiece(0.3);
            }

            if (isExtended && (m_elevatorExtendWinch.getSelectedSensorPosition() > 10) && (!m_grabber.holdingGamepiece()) && !hasInput) {
                error = 0 - m_elevatorExtendWinch.getSelectedSensorPosition();
                output = kRetractingP * error;

                m_elevatorExtendWinch.set(output);

                if (!m_grabber.holdingGamepiece()) recordedExtendTime = false;
            } else if (isExtended && (m_elevatorExtendWinch.getSelectedSensorPosition() <= 10)) {
                isRetracted = true;
                isExtended = false;
                isScoring = false;
                goingHigh = false;
                error = 0;
            }
        } else {
            m_elevatorExtendWinch.set(0);
        }

        SmartDashboard.putNumber("Elevator Extend Winch Encoder Ticks", m_elevatorExtendWinch.getSelectedSensorPosition());
        SmartDashboard.putNumber("Elevator Extend Winch Stator Current", m_elevatorExtendWinch.getStatorCurrent());
        SmartDashboard.putNumber("Elevator Retract Winch Encoder Ticks", m_elevatorRetractWinch.getSelectedSensorPosition());
        SmartDashboard.putNumber("Elevator Retract Winch Stator Current", m_elevatorRetractWinch.getStatorCurrent());
    }
    
    public void manualElevatorPeriodic() {
        if (m_input.isP1LeftTriggerDown() && m_elevatorRetractWinch.getSelectedSensorPosition() > 70) {
        //if (m_input.isP1LeftTriggerDown()) {
            m_elevatorRetractWinch.setNeutralMode(NeutralMode.Coast);
            m_elevatorExtendWinch.set(-m_input.getP1LeftTriggerAxis() * 0.666666666666666666666666666666666666666666667);
            m_elevatorRetractWinch.set(-m_input.getP1LeftTriggerAxis());

            hasInput = true;
        } else if (m_input.isP1RightTriggerDown() && m_elevatorExtendWinch.getSelectedSensorPosition() < 90000) {
        //} else if (m_input.isP1RightTriggerDown()) {
            m_elevatorRetractWinch.setNeutralMode(NeutralMode.Coast);
            m_elevatorExtendWinch.set(m_input.getP1RightTriggerAxis());
            m_elevatorRetractWinch.set(-m_input.getP1LeftTriggerAxis() * 0.75);

            hasInput = true;
        } else {
            m_elevatorRetractWinch.setNeutralMode(NeutralMode.Brake);
            m_elevatorExtendWinch.set(0);
            m_elevatorRetractWinch.set(0);

            hasInput = false;
        }
        

        if (m_input.isP1ADown()) {
            m_elevatorRetractWinch.set(0.3);
        } else if (m_input.isP1BDown()) {
            m_elevatorRetractWinch.set(-0.3);
        } 
        //else {
        //    m_elevatorRetractWinch.set(0);
        //}


        //if (m_input.isP1RightTriggerDown()) {
        //    m_elevatorExtendWinch.set(m_input.getP2RightTriggerAxis());
        //    SmartDashboard.putNumber("Right Trigger", m_input.getP2RightTriggerAxis());
        //} else if (m_input.isP1XDown()) {
        //    m_elevatorExtendWinch.set(0.3);
        //} else if (m_input.isP1YDown()) {
        //    m_elevatorExtendWinch.set(-0.3);
        //} else {
        //    m_elevatorExtendWinch.set(0);
        //}

        //if (m_input.isP1LeftTriggerDown()) {
        //    m_elevatorRetractWinch.set(m_input.getP2LeftTriggerAxis());
        //    SmartDashboard.putNumber("Left Trigger", m_input.getP2LeftTriggerAxis());
        //} else if (m_input.isP1ADown()) {
        //    m_elevatorRetractWinch.set(0.3);
        //} else if (m_input.isP1BDown()) {
        //    m_elevatorRetractWinch.set(-0.3);
        //} else {
        //    m_elevatorRetractWinch.set(0);
        //}


        SmartDashboard.putNumber("Elevator Extend Winch Encoder Ticks", m_elevatorExtendWinch.getSelectedSensorPosition());
        SmartDashboard.putNumber("Elevator Extend Winch Stator Current", m_elevatorExtendWinch.getStatorCurrent());
        SmartDashboard.putNumber("Elevator Retract Winch Encoder Ticks", m_elevatorRetractWinch.getSelectedSensorPosition());
        SmartDashboard.putNumber("Elevator Retract Winch Stator Current", m_elevatorRetractWinch.getStatorCurrent());
    }

    public void resetEncoders() {
        // resets encoders to zero
        m_elevatorExtendWinch.setSelectedSensorPosition(0, 0, 10);
        m_elevatorRetractWinch.setSelectedSensorPosition(0, 0, 10);
    }

    public String getElevatorState() {
        return elevatorState;
    }

    public boolean isRobotScoring() {
        return isScoring;
    }

    public boolean isElevatorExtended() {
        return isExtended;
    }

    public boolean isElevatorRetracted() {
        return isRetracted;
    }
}
