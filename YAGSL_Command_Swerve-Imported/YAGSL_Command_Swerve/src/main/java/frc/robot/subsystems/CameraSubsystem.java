package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CameraSubsystem extends SubsystemBase {
    private double distanceFromTarget = 0.1; // Placeholder value, replace with actual distance measurement logic

    public double getDistanceFromTarget() {
        return distanceFromTarget;
    }
}