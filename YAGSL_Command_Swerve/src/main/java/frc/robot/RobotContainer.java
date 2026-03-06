package frc.robot;

import frc.robot.commands.ClimberCommand;
import frc.robot.commands.FollowPath;
import frc.robot.commands.GroundIntakeCommand;
import frc.robot.commands.StorageCommand;
import frc.robot.commands.StorageOuttake;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.SwerveCommand;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.StorageSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.SwerveSubsystem;

import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class RobotContainer {
  //controllers
  private final XboxController driverController = new XboxController(0);
  private final XboxController operatorController = new XboxController(1);

  //Subsystems
  public final CameraSubsystem cameraSubsystem = new CameraSubsystem();
  private final SwerveSubsystem swerveSubsystem = new SwerveSubsystem(cameraSubsystem);
  private final StorageSubsystem storageSubsystem = new StorageSubsystem();
  private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  private final ClimberSubsystem climberSubsystem = new ClimberSubsystem();

  PathPlannerPath top;
  PathPlannerPath bottom;
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  public RobotContainer() {
    configureBindings();

    try {
      top = PathPlannerPath.fromPathFile("BlueTop");
      bottom = PathPlannerPath.fromPathFile("BlueBottom");
    } catch (Exception e) {
      throw new RuntimeException("Could not load PathPlanner path", e);
    }

    Command topAuto = new FollowPath(swerveSubsystem, top);
    Command bottomAuto = new FollowPath(swerveSubsystem, bottom);

    autoChooser.setDefaultOption("Blue Top", topAuto);
    autoChooser.addOption("Blue Bottom", bottomAuto);

    SmartDashboard.putData("Auto Chooser", autoChooser);
  }

  private void configureBindings() {
    //driver controls
    swerveSubsystem.setDefaultCommand(new SwerveCommand(swerveSubsystem, driverController)); //Robot movement (Joysticks)

    //operator controls
    shooterSubsystem.setDefaultCommand(new ShooterCommand(shooterSubsystem, operatorController)); //Shooter (Shooter toggle (Left bumper))
    climberSubsystem.setDefaultCommand(new ClimberCommand(climberSubsystem, operatorController)); //Climber (Up (Button Y), Down (Button A))
    storageSubsystem.setDefaultCommand(new StorageCommand(storageSubsystem, operatorController)); //Inside storage intake (Out (POV 0), in (POV 180))

    //driver commands
    new JoystickButton(driverController, XboxController.Button.kRightBumper.value).whileTrue(new GroundIntakeCommand(shooterSubsystem, storageSubsystem));
    new JoystickButton(driverController, XboxController.Button.kLeftBumper.value).whileTrue(new StorageOuttake(storageSubsystem));
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

  public SwerveSubsystem getSwerveSubsystem() {
    return swerveSubsystem;
  }

  public CameraSubsystem getCameraSubsystem() {
    return cameraSubsystem;
  }

  public StorageSubsystem getStorageSubsystem() {
    return storageSubsystem;
  }
}
