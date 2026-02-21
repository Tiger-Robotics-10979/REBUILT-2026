package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class ControllerCommand extends Command {
    IntakeSubsystem intake;
    ShooterSubsystem shooter;
    CameraSubsystem camera;
    XboxController controller;

    public ControllerCommand(IntakeSubsystem intake, ShooterSubsystem shooter, CameraSubsystem camera, XboxController controller) {
        this.intake = intake;
        this.shooter = shooter;
        this.camera = camera;
        this.controller = controller;
        addRequirements(intake, shooter);
    }

    @Override
    public void execute() {
        if (controller.getRightBumperButton()) { //full intake process (storage and roll in)
            shooter.groundIntake(); //run ground intake inward at half speed
            intake.runIntake(1); //run flywheel inward for storage
        }
        else if (controller.getLeftBumperButton()) {
            shooter.shootWithPID(0); //replace distance with camera distance
            intake.runIntake(-1); //run flywheel outward to shoot
        }
        else if (controller.getYButton()) {
            intake.runIntake(-1); //run flywheel outwards to shoot
        }
        else if (controller.getAButton()) {
            intake.runIntake(1); //run flywheel inwards for storage
        }
        else {
            shooter.stopShooter();
            intake.stopIntake();
        }
    }
}
