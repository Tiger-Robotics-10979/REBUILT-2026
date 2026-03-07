package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterCommand extends Command {
    private final ShooterSubsystem shooter;
    private final XboxController operatorController;
    private boolean shooterEnabled = false;

    public ShooterCommand(ShooterSubsystem shooter, XboxController operatorController) {
        this.shooter = shooter;
        this.operatorController = operatorController;
        addRequirements(shooter);
    }
   
    @Override
    public void execute() {
        if (operatorController.getAButtonPressed()) {
            shooterEnabled = !shooterEnabled;
        }

        if (shooterEnabled) {
            shooter.setSpeed(0.75); //replace distance with camera calculations for distance
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
