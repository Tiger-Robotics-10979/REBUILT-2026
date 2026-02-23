package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeCommand extends Command {
    private final IntakeSubsystem intake;
    private final XboxController controller;

    public IntakeCommand(IntakeSubsystem intake, XboxController controller) {
        this.intake = intake;
        this.controller = controller;
        addRequirements(intake);
    }

    @Override
    public void execute() {
        if (controller.getRightBumper()) {
            intake.outtake();
        } 
        else if (controller.getLeftBumper()) {
            intake.intake();
        } 
        else {
            intake.stop();
        }
    }

    @Override
    public void end(boolean interrupted) {
        intake.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
