package frc.robot.subsystems;

import static frc.robot.Constants.swerveDriveConstants.MAX_ANGULAR_VELOCITY;
import static frc.robot.Constants.swerveDriveConstants.MAX_VELOCITY;

import java.io.File;
import java.io.IOException;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathfindingCommand;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;

public class SwerveSubsystem extends SubsystemBase {
    private static final double DEADBAND = 0.05;

    public SwerveDrive swerveDrive;
    private boolean shootingMode = false;

    private CameraSubsystem camera;

    public SwerveSubsystem(CameraSubsystem camera) {
        this.camera = camera;

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

        setupPathPlanner();
    }

    /**
     * Applies deadband to controller input
     * @param value Raw controller value
     * @return Value with deadband applied (0 if below threshold)
     */
    private double applyDeadband(double value) {
        if (Math.abs(value) > DEADBAND) {
            return value;
        }
        return 0.0;
    }
    
    /**
     * Drives the robot using controller input
     * @param xSpeed Forward/backward speed (-1 to 1)
     * @param ySpeed Left/right speed (-1 to 1)
     * @param rotation Rotation speed (-1 to 1)
     * @param fieldRelative True for field-relative control, false for robot-relative
     */
    public void drive(double xSpeed, double ySpeed, double rotation, boolean fieldRelative) {
        xSpeed = applyDeadband(xSpeed);
        ySpeed = applyDeadband(ySpeed);
        rotation = applyDeadband(rotation);

        double xVelocity = xSpeed * MAX_VELOCITY;
        double yVelocity = ySpeed * MAX_VELOCITY;
        double rotationVelocity = rotation * MAX_ANGULAR_VELOCITY;

        Translation2d translation;
        if (shootingMode) {
            //Shooting mode: forward faces shooter direction
            translation = new Translation2d(-xVelocity, -yVelocity);
        } else {
            //Intake mode: forward faces intake direction
            translation = new Translation2d(xVelocity, yVelocity);
        }

        swerveDrive.drive(translation, rotationVelocity, fieldRelative, false);
    }
    
    /**
     * Stops all swerve module movement
     */
    public void stop() {
        swerveDrive.drive(new Translation2d(0, 0), 0, false, false);
    }

    public void setupPathPlanner() {
        RobotConfig config;
        try {
            config = RobotConfig.fromGUISettings();

            final boolean enableFeedforward = true;
            // Configure AutoBuilder last
            AutoBuilder.configure(
                this::getPose,
                // Robot pose supplier
                this::resetOdometry,
                // Method to reset odometry (will be called if your auto has a starting pose)
                this::getRobotVelocity,
                // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
                (speedsRobotRelative, moduleFeedForwards) -> {
                    if (enableFeedforward)
                    {
                    swerveDrive.drive(
                        speedsRobotRelative,
                        swerveDrive.kinematics.toSwerveModuleStates(speedsRobotRelative),
                        moduleFeedForwards.linearForces()
                                    );
                    } else
                    {
                    swerveDrive.setChassisSpeeds(speedsRobotRelative);
                    }
                },
                // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds. Also optionally outputs individual module feedforwards
                new PPHolonomicDriveController(
                    // PPHolonomicController is the built in path following controller for holonomic drive trains
                    new PIDConstants(10.0, 0.25, 0.0),
                    // Translation PID constants
                    new PIDConstants(5.0, 0.0, 0.0)
                    // Rotation PID constants
                ),
                config,
                // The robot configuration
                () -> {
                    // Boolean supplier that controls when the path will be mirrored for the red alliance
                    // This will flip the path being followed to the red side of the field.
                    // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

                    var alliance = DriverStation.getAlliance();
                    if (alliance.isPresent())
                    {
                    return alliance.get() == DriverStation.Alliance.Red;
                    }
                    return false;
                },
                this
                // Reference to this subsystem to set requirements
            );

            } catch (Exception e)
            {
            // Handle exception as needed
            e.printStackTrace();
            }

            //Preload PathPlanner Path finding
            // IF USING CUSTOM PATHFINDER ADD BEFORE THIS LINE
        PathfindingCommand.warmupCommand().schedule();
    }

    /**
     * Gets the current robot pose
     * @return Current pose on the field
     */
    public Pose2d getPose() {
        return swerveDrive.getPose();
    }

    public void addVisionMeasurement(Pose2d visionPose, double timestampSeconds) {
        swerveDrive.addVisionMeasurement(visionPose, timestampSeconds);
    }
 
    /**
     * Resets the odometry to a specific pose
     * @param pose New pose to reset to
     */
    public void resetOdometry(Pose2d pose) {
        swerveDrive.resetOdometry(pose);
    }

    public void updateVision(CameraSubsystem camera) {
        camera.getEstimatedGlobalPose(getPose())
            .ifPresent(estimatedPose -> {
                addVisionMeasurement(
                    estimatedPose.estimatedPose.toPose2d(),
                    estimatedPose.timestampSeconds
                );
            });
    }

    @Override
    public void periodic() {
        if (swerveDrive != null) {
            swerveDrive.updateOdometry();
        }

        camera.getEstimatedGlobalPose(getPose())
            .ifPresent(estimatedPose -> {
                addVisionMeasurement(
                    estimatedPose.estimatedPose.toPose2d(), 
                    estimatedPose.timestampSeconds
                );
            });
    }

    public ChassisSpeeds getRobotVelocity() {
        return swerveDrive.getRobotVelocity();
    }

    public void toggleShootingMode() {
        shootingMode = !shootingMode;
    }
}
