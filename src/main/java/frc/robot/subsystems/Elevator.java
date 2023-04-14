package frc.robot.subsystems;

import static frc.robot.Constants.*;
import frc.robot.Robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {
    private static Elevator m_singleton = null;

    private OI m_input;
    private Grabber m_grabber;

    private WPI_TalonSRX m_elevatorWinchMaster, m_elevatorWinchSlave;

    private double output;
    private double error;

    public enum State {
        IDLE,
        EXTENDED,
        EXTENDING,
        RETRACTED,
        RETRACTING
    }

    public enum Target {
        NONE,
        HIGH,
        MID,
        LOW,
        DOUBLE,
        SINGLE
    }

    private State elevatorState = State.IDLE;
    private Target elevatorTarget = Target.NONE;
    private boolean hasInput = false;

    public static double grabStartTime;
    private boolean recordedExtendTime = false;

    private Elevator() {
        m_input = OI.getInstance();
        m_grabber = Grabber.getInstance();

        m_elevatorWinchMaster = new WPI_TalonSRX(kElevatorWinchMasterID);  // ID of 19
        m_elevatorWinchSlave = new WPI_TalonSRX(kElevatorWinchSlaveID);    // ID of 17

        // sets the winch motor to brake mode
        m_elevatorWinchMaster.setNeutralMode(NeutralMode.Brake);
        m_elevatorWinchSlave.setNeutralMode(NeutralMode.Brake);

        // sets the motors maximum current limit to 40 amps and enforce it when its exceded for 20 milliseconds
        m_elevatorWinchMaster.configPeakCurrentLimit(45, 0);
        m_elevatorWinchMaster.configPeakCurrentDuration(20, 0);
        m_elevatorWinchMaster.configContinuousCurrentLimit(40);
        m_elevatorWinchMaster.enableCurrentLimit(true);

        m_elevatorWinchSlave.configPeakCurrentLimit(45, 0);
        m_elevatorWinchSlave.configPeakCurrentDuration(20, 0);
        m_elevatorWinchSlave.configContinuousCurrentLimit(40);
        m_elevatorWinchSlave.enableCurrentLimit(true);

        // inverts the winch motor
        m_elevatorWinchMaster.setInverted(false);
        //m_elevatorWinchSlave.setInverted(true);

        // slave setups
        m_elevatorWinchSlave.follow(m_elevatorWinchMaster);

        m_elevatorWinchSlave.setInverted(InvertType.OpposeMaster);

        // encoder setups
        m_elevatorWinchMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    
        // reverses the positive counting direction for the winch
        m_elevatorWinchMaster.setSensorPhase(true);

        // resets encoders to zero
        m_elevatorWinchMaster.setSelectedSensorPosition(0, 0, 10);

    }

    // returns an instance of Elevator, creating an instance only when one does not already exist
	public static Elevator getInstance() {
		if (m_singleton == null) {
			m_singleton = new Elevator();
		}
		return m_singleton;
	}

    public void extendElevator(Target theTarget) {
        switch (theTarget) {
            case HIGH: 

                if ((m_elevatorWinchMaster.getSelectedSensorPosition() < kHighTargetPoint) && !hasInput) {
                    output = (kHighTargetPoint - m_elevatorWinchMaster.getSelectedSensorPosition()) * kElevatorP;
                
                    elevatorState = State.EXTENDING;
                    elevatorTarget = Target.HIGH;
                
                    m_elevatorWinchMaster.set(output);
                } else if (m_elevatorWinchMaster.getSelectedSensorPosition() >= kHighTargetPoint) {
                    elevatorState = State.EXTENDED;
                    elevatorTarget = Target.NONE;
                
                    m_elevatorWinchMaster.set(0);
                }

                break;
            case MID: 

                if ((m_elevatorWinchMaster.getSelectedSensorPosition() < kMidTargetPoint) && !hasInput) {
                    output = (kMidTargetPoint - m_elevatorWinchMaster.getSelectedSensorPosition()) * kElevatorP;
                
                    elevatorState = State.EXTENDING;
                    elevatorTarget = Target.MID;
                
                    m_elevatorWinchMaster.set(output);
                } else if (m_elevatorWinchMaster.getSelectedSensorPosition() >= kMidTargetPoint) {
                    elevatorState = State.EXTENDED;
                    elevatorTarget = Target.NONE;
                
                    m_elevatorWinchMaster
                    .set(0);
                }

                break;
            case LOW:

                if ((m_elevatorWinchMaster.getSelectedSensorPosition() < kLowTargetPoint) && !hasInput) {
                    output = (kLowTargetPoint - m_elevatorWinchMaster.getSelectedSensorPosition()) * kElevatorP;
                
                    elevatorState = State.EXTENDING;
                    elevatorTarget = Target.LOW;
                
                    m_elevatorWinchMaster.set(output);
                } else if (m_elevatorWinchMaster.getSelectedSensorPosition() >= kLowTargetPoint) {
                    elevatorState = State.EXTENDED;
                    elevatorTarget = Target.NONE;

                    m_elevatorWinchMaster.set(0);
                }

                break;
            case DOUBLE:

                if ((m_elevatorWinchMaster.getSelectedSensorPosition() < kDoubleTargetPoint) && !hasInput) {
                    output = (kDoubleTargetPoint - m_elevatorWinchMaster.getSelectedSensorPosition()) * kElevatorP;
                
                    elevatorState = State.EXTENDING;
                    elevatorTarget = Target.DOUBLE;
                
                    m_elevatorWinchMaster.set(output);
                } else if (m_elevatorWinchMaster.getSelectedSensorPosition() >= kDoubleTargetPoint) {
                    elevatorState = State.EXTENDED;
                    elevatorTarget = Target.NONE;
                
                    m_elevatorWinchMaster.set(0);
                }
            
                break;
            case SINGLE:

               if ((m_elevatorWinchMaster.getSelectedSensorPosition() < kSingleTargetPoint) && !hasInput) {
                    output = (kSingleTargetPoint - m_elevatorWinchMaster.getSelectedSensorPosition()) * kElevatorP;
                
                    elevatorState = State.EXTENDING;
                    elevatorTarget = Target.SINGLE;
                
                    m_elevatorWinchMaster.set(output);
                } else if (m_elevatorWinchMaster.getSelectedSensorPosition() >= kSingleTargetPoint) {
                    elevatorState = State.EXTENDED;
                    elevatorTarget = Target.NONE;
                
                    m_elevatorWinchMaster.set(0);
                }

                break;
            case NONE:
            default:
                elevatorTarget = Target.NONE;
                break;
        }

        this.updateSmartDashboard();
    }

    public void retractElevator() {
        if ((m_elevatorWinchMaster.getSelectedSensorPosition() > kMinExtensionPoint) && !hasInput) {
            output = (kMinExtensionPoint - m_elevatorWinchMaster.getSelectedSensorPosition()) * kElevatorP;
        
            elevatorState = State.RETRACTING;
            elevatorTarget = Target.NONE;
        
            m_elevatorWinchMaster.set(output);
        } else if (m_elevatorWinchMaster.getSelectedSensorPosition() <= kMinExtensionPoint) {
            elevatorState = State.RETRACTED;
        
            m_elevatorWinchMaster.set(0);
        }

        this.updateSmartDashboard();
    }

    public void operateElevatorPeriodic() {
        //if (m_input.isP1LeftTriggerDown() && m_elevatorWinchMaster.getSelectedSensorPosition() > kMinExtensionPoint) {
        if (m_input.isP1LeftTriggerDown()) {
            m_elevatorWinchMaster.set(-m_input.getP1LeftTriggerAxis());
            Robot.userTarget = Target.NONE;

            hasInput = true;
        } else if (m_input.isP1RightTriggerDown() && m_elevatorWinchMaster.getSelectedSensorPosition() < kMaxExtensionPoint) {
        //} else if (m_input.isP1RightTriggerDown()) {
            m_elevatorWinchMaster.set(m_input.getP1RightTriggerAxis());

            Robot.userTarget = Target.NONE;
            hasInput = true;
        } else if (elevatorTarget == Target.NONE) {
            m_elevatorWinchMaster.set(0);

            hasInput = false;
        }

        this.updateSmartDashboard();
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber("Elevator Winch Encoder Ticks", m_elevatorWinchMaster.getSelectedSensorPosition());
        SmartDashboard.putNumber("Elevator Winch Stator Current", m_elevatorWinchMaster.getStatorCurrent());
    }

    public void resetEncoder() {
        // resets encoders to zero
        m_elevatorWinchMaster.setSelectedSensorPosition(0, 0, 10);
    }

    public State getState() {
        return elevatorState;
    }

    public Target getTarget() {
        return elevatorTarget;
    }
}
