package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterCommand extends Command {
    ShooterSubsystem shooter;
    XboxController controller;

    boolean enableToggle = false;
    

    public ShooterCommand(ShooterSubsystem shooter, XboxController controller) {
        this.shooter = shooter;
        this.controller = controller;
        addRequirements(shooter);
    }
 
    @Override
    public void execute() {
        if (controller.getAButtonPressed()) {
            if (enableToggle == true) {
                enableToggle = false;
            }
            else { //if enableToggle == false
                enableToggle = true;
            }
        }

       if (enableToggle == true) {
            shooter.runShooter(1); 
        } 
        else {
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
