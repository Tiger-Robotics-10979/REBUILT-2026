package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;

public class SwerveCommand extends Command {
    SwerveSubsystem swerve; 
    XboxController controller;

    public SwerveCommand(SwerveSubsystem swerve, XboxController controller) {
        this.swerve = swerve; 
        this.controller = controller; 
    }

    @Override
    public void initialize() {
        // Optional: Add any setup logic here
    }

    @Override
    public void execute() {
        swerve.drive(controller, true); // Drive the swerve subsystem
    }

    @Override
    public void end(boolean interrupted) {
        // Optional: Add cleanup logic here
        swerve.stop(); // Stop the swerve drive when the command ends
    }

    @Override
    public boolean isFinished() {
        return false; // Command runs indefinitely unless interrupted
    }
    
}
