package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Vision;
import org.photonvision.targeting.PhotonTrackedTarget;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class FaceAprilTagCommand extends Command {
    private final SwerveCommand swerveCommand;
    private final Vision vision;
    private final double kP = 0.1;
    private final double TOLERANCE = 1.0;
    private List<PhotonTrackedTarget> lastKnownTargets = new ArrayList<>();

    private static final Set<Integer> RED_HUB_TAGS = Set.of(8, 9, 10, 11, 2, 5);
    private static final Set<Integer> BLUE_HUB_TAGS = Set.of(24, 25, 26, 27, 18, 21);

    public FaceAprilTagCommand(SwerveCommand swerveCommand, Vision vision) {
        this.swerveCommand = swerveCommand;
        this.vision = vision;
    }

    @Override
    public void initialize() {
        lastKnownTargets = new ArrayList<>();
    }

    @Override
    public void execute() {
        List<PhotonTrackedTarget> visibleHubTargets = getVisibleHubTargets();

        if (visibleHubTargets.isEmpty()) {
            swerveCommand.clearAutoAim();
            return;
        }

        double averageYaw = visibleHubTargets.stream()
            .mapToDouble(PhotonTrackedTarget::getYaw)
            .average()
            .orElse(0.0);

        if (Math.abs(averageYaw) <= TOLERANCE) {
            swerveCommand.setAutoAimRotation(0);
            return;
        }

        double rotationSpeed = Math.max(-4, Math.min(4, -averageYaw * kP));
        swerveCommand.setAutoAimRotation(rotationSpeed);
    }

    private List<PhotonTrackedTarget> getVisibleHubTargets() {
        boolean isRed = DriverStation.getAlliance().isPresent() &&
                        DriverStation.getAlliance().get() == DriverStation.Alliance.Red;

        Set<Integer> allianceTags = isRed ? RED_HUB_TAGS : BLUE_HUB_TAGS;

        List<PhotonTrackedTarget> visible = new ArrayList<>();
        for (int id : allianceTags) {
            PhotonTrackedTarget target = vision.getTargetFromId(id, Vision.Cameras.CENTER_CAM);
            if (target != null) visible.add(target);
        }

        if (!visible.isEmpty()) lastKnownTargets = visible;
        return lastKnownTargets;
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