package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.KickerSubsystem;

public class KickerCommand extends Command{
    private final KickerSubsystem kicker;
    private final XboxController controller;

    public KickerCommand(KickerSubsystem kicker, XboxController controller) {
        this.kicker = kicker;
        this.controller = controller;
        addRequirements(kicker);
    }

    @Override
    public void execute() {
        if (controller.getPOV() == 0) {
            kicker.setSpeed(-0.4);
        }
        else {
            kicker.stop();
        }
    }

    @Override
    public void end(boolean interrupted) {
        kicker.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}