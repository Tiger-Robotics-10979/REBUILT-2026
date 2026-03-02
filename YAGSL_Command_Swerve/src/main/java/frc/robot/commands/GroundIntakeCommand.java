package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;

public class GroundIntakeCommand extends Command {
    private final ShooterSubsystem shooter;
    private final StorageSubsystem storage;

    public GroundIntakeCommand(ShooterSubsystem shooter, StorageSubsystem storage) {
        this.shooter = shooter;
        this.storage = storage;
        addRequirements(shooter, storage);
    }

    @Override
    public void execute() {
        shooter.runGroundIntake(false);
        storage.intake();
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
        storage.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
