package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimberSubsystem;

public class ClimberCommand extends Command {
    private final ClimberSubsystem climber;
    private final XboxController controller;

    public ClimberCommand(ClimberSubsystem climber, XboxController controller) {
        this.climber = climber;
        this.controller = controller;
        addRequirements(climber);
    }

    @Override
    public void execute() {
        if (controller.getXButton()) {
            climber.raise();
        } 
        else if (controller.getBButton()) {
            climber.lower();
        } 
        else {
            climber.stop();
        }
    }

    @Override
    public void end(boolean interrupted) {
        climber.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
