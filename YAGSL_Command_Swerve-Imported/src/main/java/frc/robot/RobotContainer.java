package frc.robot;

import frc.robot.commands.TankDriveCommand;
import frc.robot.subsystems.TankDriveSubsystem;


import edu.wpi.first.wpilibj.XboxController;

public class RobotContainer {
  public XboxController driverController = new XboxController(0);
  // public XboxController operatorController = new XboxController(1);

  public TankDriveSubsystem tankDriveSubsystem = new TankDriveSubsystem(); 
  

  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    tankDriveSubsystem.setDefaultCommand(new TankDriveCommand(tankDriveSubsystem, driverController));
    
  }

  public TankDriveSubsystem getTankDriveSubsystem() {
    return tankDriveSubsystem;
  }
  

}
