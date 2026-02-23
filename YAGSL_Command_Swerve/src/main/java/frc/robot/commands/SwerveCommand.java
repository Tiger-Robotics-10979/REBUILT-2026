package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;

public class SwerveCommand extends Command {
    private final SwerveSubsystem swerve;
    private final XboxController controller;

    public SwerveCommand(SwerveSubsystem swerve, XboxController controller) {
        this.swerve = swerve;
        this.controller = controller;
        addRequirements(swerve);
    }

    @Override
    public void execute() {
        // Toggle shooting mode with X button
        if (controller.getXButtonPressed()) {
            swerve.toggleShootingMode();
        }

        //Negative controls due to coordinate system
        double xSpeed = -controller.getLeftY();  //Forward/backward (inverted)
        double ySpeed = -controller.getLeftX();  //Left/right (inverted)
        double rotation = -controller.getRightX(); //Rotation (inverted)

        swerve.drive(xSpeed, ySpeed, rotation, true);
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
