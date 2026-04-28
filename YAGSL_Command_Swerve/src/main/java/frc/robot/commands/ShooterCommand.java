package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterCommand extends Command {
    private final ShooterSubsystem shooter;
    private final XboxController controller;
    private boolean shooterEnabled = false;
    private boolean shooterStart = false;
  
    public ShooterCommand(ShooterSubsystem shooter, XboxController controller) {
        this.shooter = shooter;
        this.controller = controller;
        addRequirements(shooter);
    }
   
    @Override
    public void execute() {
        if (controller.getAButtonPressed()) {
            shooterEnabled = !shooterEnabled;
        }

        //shoot with close range set speed
        if (controller.getYButton()) { //Emergency set speed of feedforward for close up
            shooter.setShootingDirection(true);
            shooter.setSpeed(0.8);
        }

        //unstuck button
        else if (controller.getLeftBumperButton()) { //Emergency reverse for clearing jams
            shooter.setShootingDirection(false);
            shooter.setSpeed(-1.0);
        }

        //shoot with maximum speed
        else if (controller.getLeftTriggerAxis() > 0.4) {
            shooter.setShootingDirection(true);
            shooter.setSpeed(1);
        }

        //shoot with mid range set speed
        else if (shooterEnabled) {
            shooter.setShootingDirection(true);
            shooter.setSpeed(0.87);
            // shooter.shootAtDistance(shooter.distanceFromHub()); //Simple FF = 0.725
        }

        //shoot with camera
        else if (controller.getStartButtonPressed()) {
            shooterStart = !shooterStart;
        }
        else if (shooterStart) {
            shooter.shootAtDistance(shooter.distanceFromHub());
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
