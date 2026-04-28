package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.events.EventTrigger;
import com.pathplanner.lib.path.EventMarker;

import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.AimAtHub;
import frc.robot.commands.ClimberCommand;
import frc.robot.commands.FaceAprilTagCommand;
import frc.robot.commands.GroundIntakeCommand;
import frc.robot.commands.KickerCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.StorageCommand;
import frc.robot.commands.StorageOuttake;
import frc.robot.commands.SwerveCommand;
import frc.robot.commands.autos.activateIntake;
import frc.robot.commands.autos.activateShooter;
import frc.robot.commands.autos.hubShoot;
import frc.robot.commands.autos.indexOut;
import frc.robot.commands.autos.lowerClimber;
import frc.robot.commands.autos.raiseClimber;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.KickerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.StorageSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.Vision;
import swervelib.SwerveInputStream;

public class RobotContainer {
  //controllers
  public final XboxController driverController = new XboxController(0);
  private final XboxController operatorController = new XboxController(1);

  //Subsystems
  private final SwerveSubsystem swerveSubsystem = new SwerveSubsystem();
  private final StorageSubsystem storageSubsystem = new StorageSubsystem();
  public final ShooterSubsystem shooterSubsystem = new ShooterSubsystem(swerveSubsystem);
  private final ClimberSubsystem climberSubsystem = new ClimberSubsystem();
  private final KickerSubsystem kickerSubsystem = new KickerSubsystem();

  private final SwerveCommand swerveCommand = new SwerveCommand(swerveSubsystem, driverController, operatorController);
  private final AimAtHub aimAtHub = new AimAtHub(swerveCommand, swerveSubsystem);

  private double inverted = 1;
  private XboxController chosenController;

  SwerveInputStream driveAngularVelocity = SwerveInputStream.of(swerveSubsystem.getSwerveDrive(),
                                                                  () -> driverController.getLeftY() * invertControls(), //gets controller left y input
                                                                  () -> driverController.getLeftX() * invertControls()) //gets controller left x input
                                                                  .withControllerRotationAxis(() -> -selectRotationController().getRightX())
                                                                  .deadband(0.1) //applies deadband
                                                                  .scaleTranslation(1)
                                                                  .allianceRelativeControl(true);
                                                                  //depending on which alliance the bot is on is the direction the bot will move by default

  private SendableChooser<Command> autoChooser;

  public RobotContainer() {
    // NamedCommands.registerCommand("Test", new lowerClimber(climberSubsystem).onlyWhile(null));
    NamedCommands.registerCommand("IndexOut", new indexOut(storageSubsystem));
    NamedCommands.registerCommand("LowerClimber", new lowerClimber(climberSubsystem).withTimeout(6));
    NamedCommands.registerCommand("ActivateShooter", new activateShooter(shooterSubsystem, storageSubsystem,kickerSubsystem).withTimeout(4.5));
    NamedCommands.registerCommand("ActivateIntake", new activateIntake(storageSubsystem, shooterSubsystem).withTimeout(5));
    NamedCommands.registerCommand(".5ActivateIntake", new activateIntake(storageSubsystem, shooterSubsystem).withTimeout(1.7));
    NamedCommands.registerCommand("HubShoot", new hubShoot(shooterSubsystem, storageSubsystem).withTimeout(5)); 
    NamedCommands.registerCommand("QHubShoot", new hubShoot(shooterSubsystem, storageSubsystem).withTimeout(2.1)); 

    NamedCommands.registerCommand("RaiseClimber", new raiseClimber(climberSubsystem).withTimeout(6));
    NamedCommands.registerCommand("DeactivateIntake", new StorageCommand(storageSubsystem, operatorController).withTimeout(0.1));
    new EventTrigger("ActivateShooter").onTrue(new activateShooter(shooterSubsystem, storageSubsystem, kickerSubsystem));
    new EventTrigger("ActivateIntake").onTrue(new activateIntake(storageSubsystem, shooterSubsystem));
    new EventTrigger("HubShoot").onTrue(new hubShoot(shooterSubsystem, storageSubsystem));
    configureBindings();
    // registerNamedCommands();

    autoChooser = AutoBuilder.buildAutoChooser();

    SmartDashboard.putData("Auto Chooser", autoChooser); 
  }
  
  private void configureBindings() {
    //driver controls
    swerveSubsystem.setDefaultCommand(swerveSubsystem.driveFieldOriented(driveAngularVelocity)); //Robot movement (Joysticks)

    //operator controls
    shooterSubsystem.setDefaultCommand(new ShooterCommand(shooterSubsystem, operatorController)); //A (toggle)
    climberSubsystem.setDefaultCommand(new ClimberCommand(climberSubsystem, operatorController)); //X (raise) and B (lower)
    storageSubsystem.setDefaultCommand(new StorageCommand(storageSubsystem, operatorController)); //D-Pad Up (out) and D-Pad Down (in)
    kickerSubsystem.setDefaultCommand(new KickerCommand(kickerSubsystem, operatorController)); //dpad up

    //driver commands
    new JoystickButton(operatorController, XboxController.Button.kRightBumper.value).whileTrue(new GroundIntakeCommand(shooterSubsystem, storageSubsystem));
    // new JoystickButton(operatorController, XboxController.Button.kLeftBumper.value).whileTrue(new StorageOuttake(storageSubsystem));
    // new JoystickButton(operatorController, XboxController.Button.kA.value)
    //   .onTrue(new InstantCommand(() -> swerveSubsystem.toggleShootingMode()));
    new Trigger( () -> operatorController.getRightTriggerAxis() > 0.25)
      .whileTrue(new FaceAprilTagCommand(swerveCommand, getVision()));
  }

  public double invertControls() {
    if (driverController.getAButtonPressed()) {
      if (inverted < 0) {
          inverted = 1;
      }
      else {
          inverted = -1;
      }
    }
    return inverted;
  }

  public XboxController selectRotationController() {
    if (Math.abs(operatorController.getRightX()) > 0.1) {
      chosenController = operatorController;
    }
    else {
      chosenController = driverController;
    }
    return chosenController;
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

  public SwerveSubsystem getSwerveSubsystem() {
    return swerveSubsystem;
  }

  public StorageSubsystem getStorageSubsystem() {
    return storageSubsystem;
  }
  
  public ShooterSubsystem getShooterSubsystem() {
    return shooterSubsystem;
  }

  public Vision getVision() { 
    return swerveSubsystem.getVision(); 
  }

  public AimAtHub getAimAtHub() { 
    return aimAtHub; 
  }

  public SwerveCommand getSwerveCommand() { 
    return swerveCommand; 
  }

  public SwerveInputStream getSwerveInputStream() {
    return driveAngularVelocity;
  }
}


