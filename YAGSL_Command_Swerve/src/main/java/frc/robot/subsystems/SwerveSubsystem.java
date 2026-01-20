package frc.robot.subsystems;

import java.io.File;
import java.io.IOException;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;

public class SwerveSubsystem extends SubsystemBase {
    SwerveDrive swerveDrive;
    private static final double MAX_VELOCITY = 3.0;
    private static final double MAX_ANGULAR_VELOCITY = Math.PI;

    private int invert;

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

        swerveDrive.zeroGyro();
    }

    public double applyDeadBand(XboxController controller, Integer axis) {
        if (Math.abs(controller.getRawAxis(axis)) > 0.1) {
          return controller.getRawAxis(axis);
        } 
        else {
          return 0.0;
        }
      }
    

    public void drive(XboxController controller, boolean fieldoriented) {
        if (Robot.isRed) {
            invert = 1;
        }
        else {
            invert = -1;
        }

        double leftX = applyDeadBand(controller, 0); 
        double leftY = applyDeadBand(controller, 1); 
        double rightX = applyDeadBand(controller, 4);

        double LeftXV = invert * leftX * MAX_VELOCITY; 
        double LeftYV = invert * leftY * MAX_VELOCITY;
        double RightXV = -rightX * MAX_ANGULAR_VELOCITY;

        Translation2d leftMovement = new Translation2d(LeftYV, LeftXV);

        swerveDrive.drive(leftMovement, RightXV, fieldoriented, false);
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
