package frc.robot.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Vision;

public class FaceAprilTagCommand extends Command {
    private final SwerveCommand swerveCommand;
    private final Vision vision;
    private final double kP = 0.10; //the gain (controls how aggresive? the robot rotates)
    private final double TOLERANCE = 0.5; //1 degree of error from the exact goal ("within a degree, stop rotating")
    private List<PhotonTrackedTarget> lastKnownTargets = new ArrayList<>();

    //lists of hub tags for both alliance (makes sure that shooter doesn't activate on enemy alliance side)
    private static final Set<Integer> RED_HUB_TAGS = Set.of(8, 9, 10, 11, 2, 5);
    private static final Set<Integer> BLUE_HUB_TAGS = Set.of(24, 25, 26, 27, 18, 21);

    public FaceAprilTagCommand(SwerveCommand swerveCommand, Vision vision) {
        this.swerveCommand = swerveCommand;
        this.vision = vision;
    }

    @Override
    public void initialize() {
        lastKnownTargets = new ArrayList<>(); //refreshes the list
    }

    @Override
    public void execute() {
        List<PhotonTrackedTarget> visibleHubTargets = getVisibleHubTargets();

        if (visibleHubTargets.isEmpty()) {
            swerveCommand.clearAutoAim();
            return;
        }

        double averageYaw = visibleHubTargets.stream() //calculates the center point of the visible tags by getting the average yaw of each
            .mapToDouble(PhotonTrackedTarget::getYaw)  //ex: one tag is +10 degrees, another at -5 degrees; average is +2.5 degrees (center of both tags)
            .average()
            .orElse(0.0);

        if (Math.abs(averageYaw) <= TOLERANCE) { //if robot has rotated enough, stop rotating
            swerveCommand.setAutoAimRotation(0);
            return;
        }

        double rotationSpeed = Math.max(-4, Math.min(4, -averageYaw * kP)); //4 rads/s (rotation speed)
        swerveCommand.setAutoAimRotation(rotationSpeed);
    }

    //tracks specifically the ids from the current alliance and then adds them to an array the function above will run through and get the average from
    //if there are no current targets, it will get the previous ones so that the bot knows the last location of the targets and is still able to rotate relatively well
    private List<PhotonTrackedTarget> getVisibleHubTargets() {
        boolean isRed = DriverStation.getAlliance().isPresent() && (DriverStation.getAlliance().get() == DriverStation.Alliance.Red);

        Set<Integer> allianceTags = isRed ? RED_HUB_TAGS : BLUE_HUB_TAGS;

        List<PhotonTrackedTarget> visible = new ArrayList<>();
        for (int id : allianceTags) {
            PhotonTrackedTarget target = vision.getTargetFromId(id, Vision.Cameras.CENTER_CAM);
            if (target != null) visible.add(target);
        }

        if (!visible.isEmpty()) lastKnownTargets = visible;
        return lastKnownTargets;
    }

    /**
     * Returns whether the robot is considered aligned with the last known AprilTag targets.
     *
     * <p>Alignment is defined as having at least one tracked target in {@code lastKnownTargets}
     * and the average yaw of those targets being within {@code TOLERANCE} degrees.
     *
     * @return true if aligned (average yaw within {@code TOLERANCE}), false otherwise
     */
    public boolean isAligned() {
        if (lastKnownTargets.isEmpty()) return false;

        double averageYaw = lastKnownTargets.stream()
            .mapToDouble(PhotonTrackedTarget::getYaw)
            .average()
            .orElse(0.0);

        return Math.abs(averageYaw) <= TOLERANCE; //returns true if the averageYaw is within the tolerance
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