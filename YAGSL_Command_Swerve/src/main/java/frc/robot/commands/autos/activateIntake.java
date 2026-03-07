package frc.robot.commands.autos;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;

public class activateIntake extends Command {
    private final StorageSubsystem storage;
    private final ShooterSubsystem shooter;

    public activateIntake(StorageSubsystem storage, ShooterSubsystem shooter) {
        this.storage = storage;
        this.shooter = shooter;
        addRequirements(storage, shooter);
    }

    @Override
    public void execute() {
        storage.intake();
        shooter.runGroundIntake(false);
    }

    @Override
    public void end(boolean interrupted) {
        storage.stop();
        shooter.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }}
