package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.autos.activateShooter;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;

public class AimAndShoot extends SequentialCommandGroup {

    public AimAndShoot(FaceAprilTagCommand faceAprilTagCommand, ShooterSubsystem shooterSubsystem, StorageSubsystem storageSubsystem) {
        addCommands(
            faceAprilTagCommand.withTimeout(3), //3 second limit on facing hub
            new activateShooter(shooterSubsystem, storageSubsystem)
        );
    }
}
