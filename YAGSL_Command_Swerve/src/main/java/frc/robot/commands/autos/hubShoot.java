package frc.robot.commands.autos;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;

public class hubShoot extends Command {
    private final ShooterSubsystem shooter;
    private final StorageSubsystem storage;
    private final Timer timer = new Timer();

    public hubShoot(ShooterSubsystem shooter, StorageSubsystem storage) {
        this.shooter = shooter;
        this.storage = storage;
        addRequirements(shooter, storage);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        shooter.setSpeed(0.84);

        if (timer.get() > 0.8) {
            storage.outtake();
        }
    }

    @Override
    public void end(boolean interrupted) {
        storage.stop();
        shooter.stop();
        timer.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}