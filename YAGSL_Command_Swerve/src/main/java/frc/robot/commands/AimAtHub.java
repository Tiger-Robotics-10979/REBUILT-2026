package frc.robot.commands;


import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;

public class AimAtHub extends Command {
    private final SwerveSubsystem swerveSubsystem;

    public AimAtHub(SwerveSubsystem swerveSubsystem) {
        this.swerveSubsystem = swerveSubsystem;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        swerveSubsystem.driveCommand(0, 0, swerveSubsystem.getAutonomousCommand(getName()).);
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
