package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.Vision;
import frc.robot.subsystems.CameraSubsystem;

public class FaceAprilTagCommand extends Command {
    private final SwerveSubsystem swerveSubsystem;
    private final Vision vision;
    private final XboxController operatorController;
    private final CameraSubsystem camera;

    public FaceAprilTagCommand(SwerveSubsystem swerveSubsystem, Vision vision, XboxController operatorController, CameraSubsystem camera) {
        this.swerveSubsystem = swerveSubsystem;
        this.vision = vision;
        this.operatorController = operatorController;
        this.camera = camera;
        addRequirements(swerveSubsystem);
    }
    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        double targetAngle = vision.getTargetFromId(3, camera).getYaw();
        
        if (operatorController.getRightTriggerAxis() > 0.5) {
            targetAngle = vision.getTargetFromId(3, camera).getYaw();
            if (targetAngle > 1){
                swerveSubsystem.drive(new edu.wpi.first.math.geometry.Translation2d(0, 0), -0.5, false);
            }
            if (targetAngle < -1) {
                swerveSubsystem.drive(new edu.wpi.first.math.geometry.Translation2d(0, 0), 0.5, false);
            } 
            if (targetAngle < 1 && targetAngle > -1) {
                
                swerveSubsystem.drive(new edu.wpi.first.math.geometry.Translation2d(0, 0), 0, false);
                
            }
            
        }
    }

    @Override
    public void end(boolean interrupted) {
        
    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
}
