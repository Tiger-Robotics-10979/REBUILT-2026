package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.StorageSubsystem;

public class StorageCommand extends Command {
    private final StorageSubsystem storage;
    private final XboxController controller;

    public StorageCommand(StorageSubsystem storage, XboxController controller) {
        this.storage = storage;
        this.controller = controller;
        addRequirements(storage);
    }

    @Override
    public void execute() {
        if (controller.getPOV() == 0) { //top of D-PAD
            storage.outtake();
        } 
        else if (controller.getPOV() == 180) { //bottom of D-PAD
            storage.intake();
        }
        else {
            storage.stop();
        }
    }

    @Override
    public void end(boolean interrupted) {
        storage.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
