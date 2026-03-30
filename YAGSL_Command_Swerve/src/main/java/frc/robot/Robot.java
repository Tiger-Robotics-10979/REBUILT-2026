package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;


public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private RobotContainer m_robotContainer;

  public Robot() {}

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
    CameraServer.startAutomaticCapture();
    DataLogManager.start();
    DriverStation.startDataLog(DataLogManager.getLog());
    DriverStation.silenceJoystickConnectionWarning(true);
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Vision value", 
        m_robotContainer.getVision().getDistanceFromAprilTag(2));
    
    SmartDashboard.putNumberArray("OdometryPose", new double[]{
        m_robotContainer.getSwerveSubsystem().getPose().getX(),
        m_robotContainer.getSwerveSubsystem().getPose().getY(),
        m_robotContainer.getSwerveSubsystem().getPose().getRotation().getDegrees()
    });

    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

 @Override
public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    System.out.println("Auto command: " + m_autonomousCommand);
    if (m_autonomousCommand != null) {
        m_autonomousCommand.schedule();
    }
} 


  @Override
  public void autonomousPeriodic() {}


  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
