package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimberSubsystem;

public class ClimberCommand extends Command {
    ClimberSubsystem climber; 
    XboxController controller;

    public ClimberCommand(ClimberSubsystem climber, XboxController controller) {
        this.climber = climber; 
        this.controller = controller; 
        addRequirements(climber);
    }

    @Override 
    public void execute() {
        if (controller.getYButton()) {
            climber.raiseClimber();
        }
        else if (controller.getAButton()) {
            climber.lowerClimber();
        }
        else {
            climber.stopClimber();
        }
    }

    @Override
    public void end(boolean interrupted) {
        climber.stopClimber(); 
    }

    @Override
    public boolean isFinished() {
        return false; 
    }
}
