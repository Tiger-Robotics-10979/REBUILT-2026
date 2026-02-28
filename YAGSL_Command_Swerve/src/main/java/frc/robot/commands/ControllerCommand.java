package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.StorageSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class ControllerCommand extends Command {
    private final StorageSubsystem intake;
    private final ShooterSubsystem shooter;
    private final CameraSubsystem camera;
    private final XboxController controller;

    public ControllerCommand(StorageSubsystem intake, ShooterSubsystem shooter, CameraSubsystem camera, XboxController controller) {
        this.intake = intake;
        this.shooter = shooter;
        this.camera = camera;
        this.controller = controller;
        addRequirements(intake, shooter);
    }
    
    @Override
    public void execute() {
        if (controller.getRightBumper()) { //Full intake process (storage and roll in)
            shooter.runGroundIntake(false);
            intake.intake();
        } 
        else if (controller.getLeftBumper()) {
            shooter.shootAtDistance(5); // TODO: Replace distance with camera distance
            intake.outtake();
        } 
        else if (controller.getYButton()) {
            intake.outtake(); //Flywheel out
        } 
        else if (controller.getAButton()) {
            intake.intake(); //Flywheel intake
        } 
        else if (controller.getPOV() == 0) {
            shooter.runGroundIntake(true); //Ground intake out
        } 
        else if (controller.getPOV() == 180) {
            shooter.runGroundIntake(false); //Ground intake in
        } 
        else {
            shooter.stop();
            intake.stop();
        }
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
        intake.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
