package frc.robot.subsystems;

import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;

import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CameraSubsystem extends SubsystemBase {
    private PhotonCamera camera;
    private PhotonPoseEstimator photonPoseEstimator;
    private SwerveSubsystem swerve;

    public CameraSubsystem() {
        camera = new PhotonCamera("camera1");

        var fieldLayout = AprilTagFields.kDefaultField.loadAprilTagLayoutField();

        Transform3d robotToCam = new Transform3d(0.3, 0.0, 0.5, new Rotation3d());

        photonPoseEstimator = new PhotonPoseEstimator(fieldLayout, PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, robotToCam);
    }

    public Optional<EstimatedRobotPose> getEstimatedGlobalPose(Pose2d previousPose){ 
        photonPoseEstimator.setReferencePose(previousPose);

        var result = camera.getLatestResult();
        if (!result.hasTargets()) {
            return Optional.empty();
        }

        var target = result.getBestTarget();

        //rejects high ambiguity targets
        if (target.getPoseAmbiguity() > 0.2) {
            return Optional.empty();
        }

        //rejects targets that are too far away
        if (target.getBestCameraToTarget().getTranslation().getNorm() > 4.0) {
            return Optional.empty();
        }

        return photonPoseEstimator.update(result);
    }

    public Optional<Double> getDistanceFromTarget() {
        var result = camera.getLatestResult();
        if (!result.hasTargets()) return Optional.empty();

        var target = result.getBestTarget();
        return Optional.of(
            target.getBestCameraToTarget().getTranslation().getNorm()
        );
    }

    @Override
    public void periodic() {
        var result = camera.getLatestResult();

        if (result.hasTargets()) {
            var target = result.getBestTarget();

            SmartDashboard.putNumber("Tag ID", target.getFiducialId());
            SmartDashboard.putNumber("Tag Yaw", target.getYaw());
            SmartDashboard.putNumber("Tag Pitch", target.getPitch());

            var camToTarget = target.getBestCameraToTarget();

            SmartDashboard.putNumber("CamToTag X", camToTarget.getX());
            SmartDashboard.putNumber("CamToTag Y", camToTarget.getY());
            SmartDashboard.putNumber("CamToTag Z", camToTarget.getZ());

            SmartDashboard.putNumber(
                "Distance To Tag",
                camToTarget.getTranslation().getNorm()
            );

            getEstimatedGlobalPose(swerve.getPose()).ifPresent(pose -> {
                SmartDashboard.putNumber("Robot X", pose.estimatedPose.getX());
                SmartDashboard.putNumber("Robot Y", pose.estimatedPose.getY());
                SmartDashboard.putNumber("Robot Rotation", pose.estimatedPose.getRotation().toRotation2d().getDegrees());
            });
        } else {
            SmartDashboard.putString("Tag Status", "No Targets");
        }
    }


}