package frc.robot;

import frc.robot.commands.FollowPath;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.SwerveCommand;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveSubsystem;

import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;

public class RobotContainer {
  public XboxController driverController = new XboxController(0);
  public SwerveSubsystem swerveSubsystem = new SwerveSubsystem(); 
  public IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  public ShooterSubsystem shooterSubsystem = new ShooterSubsystem();

  private Command autoCommand;

  private PathPlannerPath path;

  public RobotContainer() {
    configureBindings();

    try {
      path = PathPlannerPath.fromPathFile("Example Path");
    } catch (Exception e) {
      throw new RuntimeException("Could not load PathPlanner path", e);
    }

    autoCommand = new FollowPath(swerveSubsystem, path);
  }

  private void configureBindings() {
    swerveSubsystem.setDefaultCommand(new SwerveCommand(swerveSubsystem, driverController));
    intakeSubsystem.setDefaultCommand(new IntakeCommand(intakeSubsystem, driverController));
    shooterSubsystem.setDefaultCommand(new ShooterCommand(shooterSubsystem, driverController));
  }

  public Command getAutonomousCommand() {
    return autoCommand;
  }
  

}
