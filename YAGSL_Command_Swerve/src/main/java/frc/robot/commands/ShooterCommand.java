package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterCommand extends Command {
    private final ShooterSubsystem shooter;
    private final XboxController controller;
    private boolean enableToggle = false;

    public ShooterCommand(ShooterSubsystem shooter, XboxController controller) {
        this.shooter = shooter;
        this.controller = controller;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        // Toggle shooter on/off with A button
        if (controller.getAButtonPressed()) {
            enableToggle = !enableToggle;
        }

        if (enableToggle) {
            shooter.testingRegressionSpeed(); // TODO: Replace with distance from camera
        } else {
            shooter.stopShooter();
        }
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stopShooter();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
