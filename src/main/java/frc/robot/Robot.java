// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.cameraserver.CameraServer;

import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Orientation;
//import frc.robot.subsystems.Limelight;  

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kIdleAuto = "Idle Auto";
  private static final String kParkAuto = "Park Auto";
  private static final String kMobilizeAuto = "Mobilize Auto";
  private static final String kChargeAuto = "Charge Station Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_autoChooser = new SendableChooser<>();

  private static final String kOnePlayerMode = "One Player Mode";
  private static final String kTwoPlayerMode = "Two Player Mode";
  private String m_controlModeSelected;
  private final SendableChooser<String> m_controllerChooser = new SendableChooser<>();

  public static int kP1XboxPort;
  public static int kP2XboxPort;

  private Drivetrain m_drivetrain;
  private Orientation m_orientation;
  //private Limelight m_limelight;

  private Timer m_clock = new Timer();
  public static double time;

  
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_drivetrain = Drivetrain.getInstance();
    m_orientation = Orientation.getInstance();
    //m_limelight = Limelight.getInstance();
    // calls a singleton to automatically detect the first connected camera to the roborio
    //CameraServer.startAutomaticCapture();

    m_autoChooser.setDefaultOption("Idle Auto", kIdleAuto);
    m_autoChooser.addOption("Park Auto", kParkAuto);
    m_autoChooser.addOption("Mobilize Auto", kMobilizeAuto);
    m_autoChooser.addOption("Charge Station Auto", kChargeAuto);
    SmartDashboard.putData("Auto choices", m_autoChooser);

    m_controllerChooser.setDefaultOption("One Player Mode", kOnePlayerMode);
    m_controllerChooser.addOption("Two Player Mode", kTwoPlayerMode);
    SmartDashboard.putData("Control Mode", m_controllerChooser);

    m_controlModeSelected = m_controllerChooser.getSelected();
    System.out.println("Control Mode Selected: " + m_controlModeSelected);

    switch (m_controlModeSelected) {
      case kTwoPlayerMode:
        // Put two player mode initializations here
        // controller ports
        kP1XboxPort = 0;
        kP2XboxPort = 1;
        break;
      case kOnePlayerMode:
      default:
        // Put one player mode intializations here
        // controller ports
        kP1XboxPort = 0;
        kP2XboxPort = 0;
        break;
    }
  }


  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    time = m_clock.get();
    m_orientation.IMUPeriodic(); 
    //m_limelight.limelightPeriodic();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_clock.reset();

    System.out.println("Auto selected: " + m_autoSelected);
    m_autoSelected = m_autoChooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    
    m_drivetrain.resetEncoders();
    m_orientation.calibrateIMU();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kParkAuto:
        // Put park auto code here
        m_drivetrain.parkPeriodic();
        break;
      case kMobilizeAuto:
        // Put mobilize auto code here
        m_orientation.IMUPeriodic();
        m_drivetrain.mobilizePeriodic();
        break;
      case kChargeAuto:
        // Put charge station auto code here
        m_orientation.IMUPeriodic();
        m_drivetrain.chargePeriodic();
        break;
      case kIdleAuto:
      default:
        // Put idle auto code here
        m_drivetrain.parkPeriodic();
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    m_clock.reset();
    m_drivetrain.resetEncoders();
    m_orientation.resetIMU();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    m_drivetrain.drivePeriodic();
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    m_clock.stop();
    m_drivetrain.parkPeriodic();
    m_drivetrain.resetEncoders();
    m_orientation.calibrateIMU();
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
    m_clock.reset();
    m_drivetrain.resetEncoders();
    m_orientation.calibrateIMU();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
    m_orientation.IMUPeriodic();
  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {
    m_clock.reset();
    m_drivetrain.resetEncoders();
    m_orientation.calibrateIMU();
  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {
    m_orientation.IMUPeriodic();
  }
}
