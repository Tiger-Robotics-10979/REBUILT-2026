// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.TankDriveCommand;
import frc.robot.subsystems.CompressorSubsystem;
import frc.robot.subsystems.SolenoidSubsystem;
import frc.robot.subsystems.TankDriveSubsystem;

public class Robot extends TimedRobot {
  private final XboxController m_controller = new XboxController(0);
  private final CompressorSubsystem compressorSubsystem = new CompressorSubsystem();
  private final SolenoidSubsystem solenoidSubsystem = new SolenoidSubsystem();
  private final TankDriveSubsystem tankDriveSubsystem = new TankDriveSubsystem();
  private final TankDriveCommand tankDriveCommand = new TankDriveCommand(tankDriveSubsystem, m_controller);
  public boolean toggle = false;
  public boolean shootToggle =false;
  @Override
  public void teleopPeriodic() {
    //tankDriveCommand.execute();
    // if(m_controller.getAButtonPressed() && !toggle) {
    //   toggle = false;
    //   
    // } else if(m_controller.getAButtonPressed() && toggle) {
    //   toggle = false;
    //   
    // }


    if(m_controller.getAButtonPressed()) {
      if(toggle) {
        toggle = false;
      } else {
        toggle = true;
      }
    } 
    
    tankDriveSubsystem.driveJoystick(m_controller);
    tankDriveSubsystem.compresser(toggle);

    // if(m_controller.getBButtonPressed()) {
    //   compressorSubsystem.shoot();
    // }

    if(m_controller.getBButtonPressed()) {
      if(shootToggle) {
        shootToggle = false;
      } else {
        shootToggle = true;
      }
    } 

    tankDriveSubsystem.shooter(shootToggle);
    
    if (m_controller.getAButtonPressed()) {
      if (compressorSubsystem.getCompressorEnabled()) {
        compressorSubsystem.compressorOff();
      }
      else {
        compressorSubsystem.compressorOn();
      }
    }

    if (m_controller.getBButton()) {
      compressorSubsystem.enableMotor();
    }
    else {
      compressorSubsystem.disableMotor();
    }
    if (m_controller.getYButtonPressed()) {
      solenoidSubsystem.enableMotor();
    }
    if (m_controller.getXButtonPressed()) {
      solenoidSubsystem.disableMotor();
    }
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putBoolean("Compressor", compressorSubsystem.getCompressorEnabled());
  }
}