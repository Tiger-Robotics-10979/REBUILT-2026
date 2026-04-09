package frc.robot.commands.autos;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.StorageSubsystem;

public class indexOut extends Command {
    private final StorageSubsystem storage;

    public indexOut(StorageSubsystem storage) {
        this.storage = storage;
        
        addRequirements(storage);
    }

    @Override
    public void execute() {
        storage.intake();
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
