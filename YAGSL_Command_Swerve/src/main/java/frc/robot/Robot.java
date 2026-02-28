package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.StorageSubsystem;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private final RobotContainer m_robotContainer;
  private final XboxController operatorController;

  public Robot() {
    m_robotContainer = new RobotContainer();
    operatorController = new XboxController(0);
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();

    // var swerve = m_robotContainer.getSwerveSubsystem();
    // var camera = m_robotContainer.getCameraSubsystem();

    // camera.getEstimatedGlobalPose(swerve.getPose())
    //   .ifPresent(estimatedPose -> {
    //       swerve.addVisionMeasurement(
    //           estimatedPose.estimatedPose.toPose2d(),
    //           estimatedPose.timestampSeconds
    //       );
    //   });
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

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
    if (operatorController.getPOV() == 180) {
      m_robotContainer.getStorageSubsystem().outtake();
    }
    else if (operatorController.getPOV() == 0){ 
      m_robotContainer.getStorageSubsystem().intake();
    }
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
