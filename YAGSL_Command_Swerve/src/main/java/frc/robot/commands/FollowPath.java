package frc.robot.commands;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.trajectory.PathPlannerTrajectory;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;

public class FollowPath extends Command {
    private final SwerveSubsystem swerve;
    private final PathPlannerPath path;
    private PathPlannerTrajectory trajectory;

    private final PIDController pidX = new PIDController(10, 0.25, 0);
    private final PIDController pidY = new PIDController(10, 0.25, 0);
    private final PIDController pidRotation = new PIDController(2, 0, 0);
    
    private double startTime;

    public FollowPath(SwerveSubsystem swerve, PathPlannerPath path) {
        this.swerve = swerve;
        this.path = path;
        addRequirements(swerve);
    }

    @Override
    public void initialize() {
        try {
            trajectory = path.generateTrajectory(new ChassisSpeeds(), new Rotation2d(), RobotConfig.fromGUISettings());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        
        startTime = Timer.getFPGATimestamp();

        swerve.swerveDrive.resetOdometry(trajectory.getInitialPose());
    }

    @Override
    public void execute() {
        double elapsedTime = Timer.getFPGATimestamp() - startTime;
        var goalState = trajectory.sample(elapsedTime);

        double desiredVx = goalState.fieldSpeeds.vxMetersPerSecond;
        double desiredVy = goalState.fieldSpeeds.vyMetersPerSecond;

        Translation2d trajectoryVelocity = new Translation2d(desiredVx, desiredVy);
        double desiredRotation = goalState.fieldSpeeds.omegaRadiansPerSecond;


        double xError = pidX.calculate(swerve.swerveDrive.getPose().getX(), goalState.pose.getX());
        double yError = pidY.calculate(swerve.swerveDrive.getPose().getY(), goalState.pose.getY());
        double rotationError = pidRotation.calculate(swerve.swerveDrive.getPose().getRotation().minus(goalState.pose.getRotation()).getRadians(), 0);

        Translation2d correction = new Translation2d(xError, yError);

        Translation2d finalVelocity = trajectoryVelocity.plus(correction);

        swerve.swerveDrive.drive(finalVelocity, desiredRotation + rotationError, true, false);
    }

    @Override
    public boolean isFinished() {
      return Timer.getFPGATimestamp() - startTime >= trajectory.getTotalTimeSeconds();
    }
  
    @Override
    public void end(boolean interrupted) {
      swerve.swerveDrive.drive(new Translation2d(), 0, false, false);
    }
}
