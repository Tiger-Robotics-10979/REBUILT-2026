package frc.robot.subsystems;

import java.io.File;
import java.io.IOException;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.XboxController;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;

public class SwerveSubsystem {
    SwerveDrive swerveDrive;
    private static final double MAX_VELOCITY = 3.0;

    public void SwerveSubsystem() {
        File swerveJsonDirectory = new File(Filesystem.getDeployDirectory(), "swerve");
        try {
          swerveDrive = new SwerveParser(swerveJsonDirectory).createSwerveDrive(MAX_VELOCITY);
        } catch (IOException e) {
          System.err.println("Failed to initialize swerve drive from configuration files");
          e.printStackTrace();
        }
    }

    public void resetGyro() {
        swerveDrive.zeroGyro();
    }

    public void drive(XboxController controller, boolean fieldoriented) {
     
        double leftX = controller.getLeftX(); 
        double leftY = controller.getLeftY(); 
        double rightX = controller.getRightX();

        double LeftXV = leftX * MAX_VELOCITY; 
        double LeftYV = leftY * MAX_VELOCITY;
        double RightXV = rightX * MAX_VELOCITY;

        Translation2d leftmovement = new Translation2d(LeftXV, LeftYV);

        swerveDrive.drive(leftmovement, RightXV, fieldoriented, false);


    }



}
