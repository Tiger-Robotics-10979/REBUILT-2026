package frc.robot.commands.autos;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.KickerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;

public class activateShooter extends Command {
    private final ShooterSubsystem shooter;
    private final StorageSubsystem storage;
    private final Timer timer = new Timer();
    private final KickerSubsystem kicker;
    public activateShooter(ShooterSubsystem shooter, StorageSubsystem storage, KickerSubsystem kicker) {
        this.shooter = shooter;
        this.storage = storage;
        this.kicker = kicker;
        addRequirements(shooter, storage, kicker);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        shooter.setSpeed(1);

        if (timer.get() > 0.8) {
            storage.outtake();
            kicker.setSpeed(1);
        }
    }

    @Override
    public void end(boolean interrupted) {
        storage.stop();
        shooter.stop();
        kicker.stop();
        timer.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}