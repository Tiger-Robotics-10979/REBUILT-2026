package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.TankDriveSubsystem;


public class TankDriveCommand extends Command {
    private final TankDriveSubsystem tankDriveSubsystem;
    private final XboxController controller;

    public TankDriveCommand(TankDriveSubsystem tankDriveSubsystem, XboxController controller) {
        this.tankDriveSubsystem = tankDriveSubsystem;
        this.controller = controller;
        addRequirements(tankDriveSubsystem);
    }
    
    @Override
    public void execute() {
        if (controller.getYButton()) { // Up
            tankDriveSubsystem.drive(-0.5,0.5);
        }
        else if  (controller.getAButton()) { // Down
            tankDriveSubsystem.drive(0.5,-0.5);
        }
        else if(controller.getBButton()) { // Right
            tankDriveSubsystem.drive(0.5, 0.5);
        }
        else if(controller.getXButton()) { // Left
            tankDriveSubsystem.drive(-0.5,-0.5);
        }
        else if(controller.getRightBumperButton()){
            tankDriveSubsystem.drive(0.,1);
        }
        else if(controller.getLeftBumperButton()){
            tankDriveSubsystem.drive(1.,0);
        }
         else {
            tankDriveSubsystem.stop();
        }
    }

        

    @Override
    public void end(boolean interrupted) {
        tankDriveSubsystem.stop();
    }

    @Override
    public boolean isFinished() {
        return false; // This command should run until interrupted
    }
}
