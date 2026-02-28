package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;

public class ShooterCommand extends Command {
    private final ShooterSubsystem shooter;
    private final StorageSubsystem storage;
    private final XboxController driverController;
    private final XboxController operatorController;
    private boolean enableToggle = false;
    private boolean driverToggle = false; 

    public ShooterCommand(ShooterSubsystem shooter, StorageSubsystem storage, XboxController driverController, XboxController operatorController) {
        this.shooter = shooter;
        this.storage = storage;
        this.driverController = driverController;
        this.operatorController = operatorController;
        addRequirements(shooter, storage);
    }

    @Override
    public void execute() {
        //driver commands
        if (driverController.getRightBumperPressed()) {
            driverToggle = !driverToggle;
        }

        if (driverToggle) {
            shooter.runGroundIntake(false);
            storage.intake();
        }
        else {
            shooter.stop();
            storage.stop();
        }

        if (driverController.getLeftBumperButton()) {
            storage.outtake();
        }

        // //operator commands
        // if (operatorController.getLeftBumperButtonPressed()) { //Toggle shooter on/off
        //     enableToggle = !enableToggle;
        // }

        // if (enableToggle) {
        //     shooter.setSpeed(0.6); //TODO: Replace with distance from camera
        //     // shooter.setSpeed(0);
        // } 
        // else {
        //     shooter.stop();
        // }
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
