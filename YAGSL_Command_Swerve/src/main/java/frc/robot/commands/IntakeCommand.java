package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeCommand extends Command {
    IntakeSubsystem intake; 
    XboxController controller;

    public IntakeCommand(IntakeSubsystem intake, XboxController controller) {
        this.intake = intake; 
        this.controller = controller; 
        addRequirements(intake);

    }

    @Override 
    public void execute() {
        if (controller.getRightBumper()) {
            intake.runIntake(-1.0); 
        } else if (controller.getLeftBumper()) {
            intake.runIntake(1.0); 
        } else {
            intake.stopIntake(); 
        }
    }

    @Override
    public void end(boolean interrupted) {
        intake.stopIntake(); 
    }

    @Override
    public boolean isFinished() {
        return false; 
    }
    
    
}
