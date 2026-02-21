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
        addRequirements(swerve);
    }

    @Override
    public void execute() {
        swerve.drive(controller, true); 
    }

    @Override
    public void end(boolean interrupted) {
        swerve.stop(); 
    }

    @Override
    public boolean isFinished() {
        return false; 
    }
    
}
