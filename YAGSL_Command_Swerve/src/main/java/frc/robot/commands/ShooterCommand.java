package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterCommand extends Command {
    private final ShooterSubsystem shooter;
    private final XboxController controller;
    private boolean shooterEnabled = false;
  
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

        if (controller.getYButton()) { //Emergency set speed of feedforward for close up
            shooter.setSpeed(0.735);
        }
        else if (controller.getLeftBumperButton()) { //Emergency reverse for clearing jams
            shooter.setSpeed(-1.0);

        }
        else if (controller.getLeftTriggerAxis() > 0.4) {
            shooter.setSpeed(1);
        }
        else if (shooterEnabled) {
            //shooter.setSpeed(0.78);
            shooter.shootAtDistance(shooter.distanceFromHub()); //Simple FF = 0.725
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
