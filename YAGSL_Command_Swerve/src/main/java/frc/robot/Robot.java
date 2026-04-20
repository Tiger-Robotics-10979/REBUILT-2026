package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants.swerveDriveConstants;


public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private RobotContainer m_robotContainer;

  public Robot() {}

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
    CameraServer.startAutomaticCapture();
    // CameraServer.getVideo();
    DataLogManager.start();
    DriverStation.startDataLog(DataLogManager.getLog());
    DriverStation.silenceJoystickConnectionWarning(true);
  }

  @Override
  public void robotPeriodic() {
    // SmartDashboard.putData("SwerveInputStream", m_robotContainer.getSwerveSubsystem().driveFieldOriented(m_robotContainer.getSwerveInputStream()));

    // SmartDashboard.putNumber("Vision value", 
    //     m_robotContainer.getVision().getDistanceFromAprilTag(9));
    
    // SmartDashboard.putNumberArray("OdometryPose", new double[]{
    //     m_robotContainer.getSwerveSubsystem().getPose().getX(),
    //     m_robotContainer.getSwerveSubsystem().getPose().getY(),
    //     m_robotContainer.getSwerveSubsystem().getPose().getRotation().getDegrees()
    // });

    // SmartDashboard.putNumber("currentShooterRPM", m_robotContainer.getShooterSubsystem().getCurrentRPM());
    // SmartDashboard.putNumber("targetShooterRPM", m_robotContainer.getShooterSubsystem().getTargetRPM());

    // SmartDashboard.putNumber("distanceFromHub", m_robotContainer.getShooterSubsystem().distanceFromHub());
    // SmartDashboard.putNumber("distanceFromTag", m_robotContainer.getVision().getDistanceFromAprilTag(9));

    // SmartDashboard.putData("swerveField", m_robotContainer.getSwerveSubsystem().getSwerveDrive().field);

    // SmartDashboard.putNumber("targetRotation", m_robotContainer.getAimAtHub().getTargetRotation());
    // SmartDashboard.putNumber("currentRotation", m_robotContainer.getAimAtHub().getCurrentRotation());
    

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
  public void teleopPeriodic() {
    m_robotContainer.selectRotationController();
  }

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
