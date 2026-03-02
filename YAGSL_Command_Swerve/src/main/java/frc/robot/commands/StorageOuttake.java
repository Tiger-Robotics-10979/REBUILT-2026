package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.StorageSubsystem;

public class StorageOuttake extends Command {
    private final StorageSubsystem storage;

    public StorageOuttake(StorageSubsystem storage) {
        this.storage = storage;
        addRequirements(storage);
    }

    @Override
    public void execute() {
        storage.outtake();
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
