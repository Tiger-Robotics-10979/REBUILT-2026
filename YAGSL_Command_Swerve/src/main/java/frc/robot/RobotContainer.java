package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.ClimberCommand;
import frc.robot.commands.GroundIntakeCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.StorageCommand;
import frc.robot.commands.StorageOuttake;
import frc.robot.commands.SwerveCommand;
import frc.robot.commands.autos.activateIntake;
import frc.robot.commands.autos.activateShooter;
import frc.robot.commands.autos.lowerClimber;
import frc.robot.commands.autos.raiseClimber;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.Vision;



public class RobotContainer {
  //controllers
  public final XboxController driverController = new XboxController(0);
  private final XboxController operatorController = new XboxController(1);

  //Subsystems
  public final CameraSubsystem cameraSubsystem = new CameraSubsystem();
  private final SwerveSubsystem swerveSubsystem = new SwerveSubsystem();
  private final StorageSubsystem storageSubsystem = new StorageSubsystem();
  public final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  private final ClimberSubsystem climberSubsystem = new ClimberSubsystem();
  
  private SendableChooser<Command> autoChooser;

  public RobotContainer() {
    configureBindings();

    registerNamedCommands();

    autoChooser = AutoBuilder.buildAutoChooser();

    SmartDashboard.putData("Auto Chooser", autoChooser);
  }
  
  private void configureBindings() {
    //driver controls
    swerveSubsystem.setDefaultCommand(new SwerveCommand(swerveSubsystem, driverController, operatorController)); //Robot movement (Joysticks)

    //operator controls
    shooterSubsystem.setDefaultCommand(new ShooterCommand(shooterSubsystem, operatorController)); //Shooter (Shooter toggle (Left bumper))
    climberSubsystem.setDefaultCommand(new ClimberCommand(climberSubsystem, operatorController)); //Climber (Up (Button Y), Down (Button A))
    storageSubsystem.setDefaultCommand(new StorageCommand(storageSubsystem, operatorController)); //Inside storage intake (Out (POV 0), in (POV 180))

    //driver commands
    new JoystickButton(operatorController, XboxController.Button.kRightBumper.value).whileTrue(new GroundIntakeCommand(shooterSubsystem, storageSubsystem));
    new JoystickButton(operatorController, XboxController.Button.kLeftBumper.value).whileTrue(new StorageOuttake(storageSubsystem));
    // new JoystickButton(operatorController, XboxController.Button.kA.value)
    //   .onTrue(new InstantCommand(() -> swerveSubsystem.toggleShootingMode()));
  }

  private void registerNamedCommands() {
    NamedCommands.registerCommand(
        "RaiseClimber",
        new raiseClimber(climberSubsystem).withTimeout(6)
    );
    NamedCommands.registerCommand(
        "LowerClimber",
        new lowerClimber(climberSubsystem).withTimeout(6)
    );
    NamedCommands.registerCommand(
        "ActivateShooter",
        new activateShooter(shooterSubsystem, storageSubsystem).withTimeout(7)
    );
    NamedCommands.registerCommand(
        "ActivateIntake", 
        new activateIntake(storageSubsystem, shooterSubsystem)) ;
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

  public Vision getVision() { 
    return swerveSubsystem.getVision(); 
  }
}
