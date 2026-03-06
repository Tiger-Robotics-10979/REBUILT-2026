package frc.robot.commands;

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
        double xSpeed = -controller.getLeftX();  //Forward/backward (inverted)
        double ySpeed = -controller.getLeftY();  //Left/right (inverted)
        double rotation = r; //Rotation (inverted)

        
        if  (controller.getRightX() != 0) {
            r = -controller.getRightX();

        }
        else {
            r = -operatorController.getRightX();
        }
        
        //Drive with field-relative control
        swerve.drive(ySpeed, xSpeed, rotation, true);
    }
    @Override
    public void end(boolean interrupted) {
        swerve.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
