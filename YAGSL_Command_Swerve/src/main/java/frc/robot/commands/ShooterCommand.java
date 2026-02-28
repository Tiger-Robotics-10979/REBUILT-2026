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
        if (driverController.getRightBumperButton()) { //Full ball intake
            shooter.runGroundIntake(false);
            storage.intake();
        }
        else if (driverController.getLeftBumperButton()) { //Ground intake out
            shooter.runGroundIntake(true);
        }

        //operator commands
        if (operatorController.getLeftBumperButtonPressed()) { //Toggle shooter on/off
            enableToggle = !enableToggle;
        }

        if (enableToggle) {
            shooter.shootAtDistance(0); //TODO: Replace with distance from camera
            // shooter.setSpeed(0);
        } 
        else {
            shooter.stop();
        }
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
