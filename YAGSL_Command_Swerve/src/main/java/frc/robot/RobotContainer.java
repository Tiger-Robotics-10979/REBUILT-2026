package frc.robot;

import frc.robot.commands.ClimberCommand;
import frc.robot.commands.ControllerCommand;
import frc.robot.commands.FollowPath;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.SwerveCommand;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveSubsystem;

import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;

public class RobotContainer {
  // Controllers
  private final XboxController driverController = new XboxController(0);
  private final XboxController operatorController = new XboxController(1);

  // Subsystems
  private final SwerveSubsystem swerveSubsystem = new SwerveSubsystem();
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  private final ClimberSubsystem climberSubsystem = new ClimberSubsystem();

  // Commands
  private final IntakeCommand intakeCommand = new IntakeCommand(intakeSubsystem, driverController);
  private final ShooterCommand shooterCommand = new ShooterCommand(shooterSubsystem, driverController);
  private final ClimberCommand climberCommand = new ClimberCommand(climberSubsystem, operatorController);
  private final Command autoCommand;

  public RobotContainer() {
    configureBindings();

    // Load autonomous path
    PathPlannerPath path;
    try {
      path = PathPlannerPath.fromPathFile("Example Path");
    } catch (Exception e) {
      throw new RuntimeException("Could not load PathPlanner path", e);
    }

    autoCommand = new FollowPath(swerveSubsystem, path);
  }

  private void configureBindings() {
    swerveSubsystem.setDefaultCommand(new SwerveCommand(swerveSubsystem, driverController));
    intakeSubsystem.setDefaultCommand(intakeCommand);
    shooterSubsystem.setDefaultCommand(shooterCommand);
    climberSubsystem.setDefaultCommand(climberCommand);
  }

  public Command getAutonomousCommand() {
    return autoCommand;
  }
}
