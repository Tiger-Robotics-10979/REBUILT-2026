package frc.robot;

import frc.robot.commands.ClimberCommand;
import frc.robot.commands.FollowPath;
import frc.robot.commands.StorageCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.SwerveCommand;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.StorageSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveSubsystem;

import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;

public class RobotContainer {
  //controllers
  private final XboxController driverController = new XboxController(0);
  private final XboxController operatorController = new XboxController(1);

  //Subsystems
  private final SwerveSubsystem swerveSubsystem = new SwerveSubsystem();
  private final StorageSubsystem storageSubsystem = new StorageSubsystem();
  private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  private final ClimberSubsystem climberSubsystem = new ClimberSubsystem();

  //Commands
  private final Command autoCommand;

  public RobotContainer() {
    configureBindings();

    // Load autonomous path
    PathPlannerPath path;
    try {
      path = PathPlannerPath.fromPathFile("RightCorner");
    } catch (Exception e) {
      throw new RuntimeException("Could not load PathPlanner path", e);
    }

    autoCommand = new FollowPath(swerveSubsystem, path);
  }

  private void configureBindings() {
    //driver controls
    swerveSubsystem.setDefaultCommand(new SwerveCommand(swerveSubsystem, driverController)); //Robot movement (Joysticks)

    //operator controls
    climberSubsystem.setDefaultCommand(new ClimberCommand(climberSubsystem, operatorController)); //Climber (Button Y, Button A)
    storageSubsystem.setDefaultCommand(new StorageCommand(storageSubsystem, operatorController)); //Inside storage intake (POV 0, POV 180)

    //Shooter (DRIVER: Ground intake (Right bumper, Left bumper), OPERATOR: Shooter toggle (Left bumper))
    shooterSubsystem.setDefaultCommand(new ShooterCommand(shooterSubsystem, storageSubsystem, driverController, operatorController));
  }

  public Command getAutonomousCommand() {
    return autoCommand;
  }
}
