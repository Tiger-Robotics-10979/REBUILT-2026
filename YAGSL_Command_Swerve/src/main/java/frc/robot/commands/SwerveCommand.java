package frc.robot.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;

public class SwerveCommand extends Command {
    private final SwerveSubsystem swerve;
    private final XboxController controller;
    private final XboxController operatorController;
    private double r = 0;
    public SwerveCommand(SwerveSubsystem swerve, XboxController controller, XboxController operatorController) {
        this.swerve = swerve;
        this.controller = controller;
        this.operatorController = operatorController;
        addRequirements(swerve);
    }

    @Override
    public void execute() {
        //Negative due to coordinate plane
        double xSpeed = controller.getLeftX();  //Forward/backward (inverted)
        double ySpeed = controller.getLeftY();  //Left/right (inverted)
        double rotation = r; //Rotation (inverted)
        
        if  (controller.getRightX() != 0) {
            r = -controller.getRightX();

        }
        if (operatorController.getRightX() != 0) {
            
             r = -operatorController.getRightX();
        }

        Translation2d translation = new Translation2d(-ySpeed, -xSpeed);
        
        //Drive with field-relative control
        swerve.drive(translation, rotation, true);
    }
    @Override
    public void end(boolean interrupted) {
        swerve.drive(new Translation2d(0,0),0,false);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
