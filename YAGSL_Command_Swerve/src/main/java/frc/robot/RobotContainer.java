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
  public XboxController driverController = new XboxController(0);
  public XboxController operatorController = new XboxController(1);
  public SwerveSubsystem swerveSubsystem = new SwerveSubsystem(); 
  public IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  public ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  public ClimberSubsystem climberSubsystem = new ClimberSubsystem();

  // public ControllerCommand controllerCommand = new ControllerCommand(intakeSubsystem, shooterSubsystem, null, driverController);
  public IntakeCommand intakeCommand = new IntakeCommand(intakeSubsystem, driverController);
  public ShooterCommand shooterCommand = new ShooterCommand(shooterSubsystem, driverController);
  public ClimberCommand climberCommand = new ClimberCommand(climberSubsystem, operatorController);
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
    intakeSubsystem.setDefaultCommand(intakeCommand);
    shooterSubsystem.setDefaultCommand(shooterCommand);
    climberSubsystem.setDefaultCommand(climberCommand);
  }

  public Command getAutonomousCommand() {
    return autoCommand;
  }
  

}
