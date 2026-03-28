package frc.robot.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;

public class SwerveCommand extends Command {
    private final SwerveSubsystem swerve;
    private final XboxController controller;
    private final XboxController operatorController;
    private double rotation = 0;
    public SwerveCommand(SwerveSubsystem swerve, XboxController controller, XboxController operatorController) {
        this.swerve = swerve;
        this.controller = controller;
        this.operatorController = operatorController;
        addRequirements(swerve);
    }

    @Override
    public void execute() {
        double maxSpeed = swerve.getSwerveDrive().getMaximumChassisVelocity();
        double maxRotationSpeed = swerve.getSwerveDrive().getMaximumChassisAngularVelocity();


        rotation = -applyDeadBand(controller, 4) * maxRotationSpeed;
        if  (applyDeadBand(operatorController, 4) != 0) {
            rotation = -applyDeadBand(operatorController, 4) * maxRotationSpeed;
        }

        double xSpeed = applyDeadBand(controller, 0);  //Forward/backward (inverted)
        double ySpeed = applyDeadBand(controller, 1);  //Left/right (inverted)

        boolean isRedAlliance = DriverStation.getAlliance().isPresent() && 
                                DriverStation.getAlliance().get() == DriverStation.Alliance.Red;
        
        double adjustedX = (isRedAlliance ? ySpeed : -ySpeed) * maxSpeed;
        double adjustedY = (isRedAlliance ? xSpeed : -xSpeed) * maxSpeed;

        Translation2d translation = new Translation2d(adjustedX, adjustedY);
        swerve.drive(translation, rotation, true);
    }

    public double applyDeadBand(XboxController controller, Integer axis) {
        if (Math.abs(controller.getRawAxis(axis)) > 0.1) {
          return controller.getRawAxis(axis);
        } 
        else {
          return 0.0;
        }
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
