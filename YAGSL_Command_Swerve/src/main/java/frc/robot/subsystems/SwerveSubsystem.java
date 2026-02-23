package frc.robot.subsystems;

import java.io.File;
import java.io.IOException;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;
import static frc.robot.Constants.swerveDriveConstants.MAX_VELOCITY;
import static frc.robot.Constants.swerveDriveConstants.MAX_ANGULAR_VELOCITY;

public class SwerveSubsystem extends SubsystemBase {
    public SwerveDrive swerveDrive;
    public boolean shooting = false;

    public SwerveSubsystem() {
        File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve");
        try {
            swerveDrive = new SwerveParser(swerveJsonDirectory).createSwerveDrive(MAX_VELOCITY);
        } catch (IOException e) {
            System.err.println("Failed to initialize swerve drive from configuration files");
            e.printStackTrace();
        }

        if (swerveDrive == null) {
            throw new RuntimeException("SwerveDrive failed to initialize");
        }

        if (Robot.isSimulation()) {
            swerveDrive.resetOdometry(new Pose2d(1, 1, new Rotation2d()));
        }

        swerveDrive.zeroGyro();
    }

    public double applyDeadBand(XboxController controller, Integer axis) {
        if (Math.abs(controller.getRawAxis(axis)) > 0.03) {
            return controller.getRawAxis(axis);
        } else {
            return 0.0;
        }
    }
    
    public void drive(XboxController controller, boolean fieldoriented) {
        double leftX = applyDeadBand(controller, 0);
        double leftY = applyDeadBand(controller, 1);
        double rightX = applyDeadBand(controller, 4);

        double LeftXV = leftX * MAX_VELOCITY;
        double LeftYV = leftY * MAX_VELOCITY;
        double RightXV = -rightX * MAX_ANGULAR_VELOCITY;

        // Toggle shooting mode with X button
        if (controller.getXButtonPressed()) {
            shooting = !shooting;
        }

        // Choose forward direction based on shooting mode
        Translation2d strafeVelocities;
        if (shooting) {
            // Forward is to where ball will shoot
            strafeVelocities = new Translation2d(-LeftYV, -LeftXV);
        } else {
            // Forward is towards intake
            strafeVelocities = new Translation2d(LeftYV, LeftXV);
        }

        swerveDrive.drive(strafeVelocities, RightXV, true, false);
    }

    @Override
    public void periodic() {
        if (swerveDrive != null) {
            swerveDrive.updateOdometry();
        }
    }

    public void stop() {
        swerveDrive.drive(new Translation2d(0, 0), 0, false, false);
    }
}
