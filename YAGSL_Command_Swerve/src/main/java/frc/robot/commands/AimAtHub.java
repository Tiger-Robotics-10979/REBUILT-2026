package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;

public class AimAtHub extends Command {
    private final SwerveCommand swerveCommand;
    private final SwerveSubsystem swerveSubsystem;

    //translations created from pathplanner's hub centers
    private static final Translation2d RED_HUB = new Translation2d(11.925, 4.0); 
    private static final Translation2d BLUE_HUB = new Translation2d(4.625, 4.0);

    private final double TOLERANCE = 1;
    private final double kP = 10.0;

    public AimAtHub(SwerveCommand swerveCommand, SwerveSubsystem swerveSubsystem) {
        this.swerveCommand = swerveCommand;
        this.swerveSubsystem = swerveSubsystem;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        boolean isRed = DriverStation.getAlliance().isPresent() && (DriverStation.getAlliance().get() == DriverStation.Alliance.Red);

        Translation2d hubCenter = isRed ? RED_HUB : BLUE_HUB;

        Pose2d robotPose = swerveSubsystem.getPose();

        double dx = hubCenter.getX() - robotPose.getX();
        double dy = hubCenter.getY() - robotPose.getY();
        
        double targetAngleRadians = Math.atan2(dy, dx);

        double currentAngleRadians = robotPose.getRotation().getRadians();
        double errorRadians = targetAngleRadians - currentAngleRadians;


        while (errorRadians > Math.PI) errorRadians -= 2 * Math.PI;
        while (errorRadians < -Math.PI) errorRadians += 2 * Math.PI;

        if (Math.abs(Math.toDegrees(errorRadians)) <= TOLERANCE) {
            swerveCommand.setAutoAimRotation(0);
            return;
        }

        double rotationSpeed = Math.max(-4, Math.min(4, errorRadians * kP));
        swerveCommand.setAutoAimRotation(rotationSpeed);
    }

    @Override
    public void end(boolean interrupted) {
        swerveCommand.clearAutoAim();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
