package frc.robot.commands.autos;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;

public class activateShooter extends Command {
    private final ShooterSubsystem shooter;
    private final StorageSubsystem storage;

    public activateShooter(ShooterSubsystem shooter, StorageSubsystem storage) {
        this.shooter = shooter;
        this.storage = storage;
        addRequirements(shooter, storage);
    }

    @Override
    public void execute() {
        storage.outtake();
        shooter.setSpeed(0.75);
    }

    @Override
    public void end(boolean interrupted) {
        storage.stop();
        shooter.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
