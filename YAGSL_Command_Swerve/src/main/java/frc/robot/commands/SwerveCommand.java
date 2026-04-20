package frc.robot.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;
import swervelib.SwerveInputStream;

public class SwerveCommand extends Command {
    private final SwerveSubsystem swerve;
    private final XboxController controller;
    private final XboxController operatorController;
    private double inverted = 1;
    private double rotation = 0;
    private double autoAimRotation = 0;
    private boolean autoAimActive = false;

    private double xSpeed;
    private double ySpeed;

    public String whatever;

    public SwerveCommand(SwerveSubsystem swerve, XboxController controller, XboxController operatorController) {
        this.swerve = swerve;
        this.controller = controller;
        this.operatorController = operatorController;
        addRequirements(swerve);
    }

    public void setAutoAimRotation(double rotation) {
        this.autoAimRotation = rotation;
        this.autoAimActive = true;
    }

    public void clearAutoAim() {
        this.autoAimActive = false;
        this.autoAimRotation = 0;
    }

    @Override
    public void execute() {
        
        // if (controller.getAButtonPressed()) {
        //     if (inverted < 0) {
        //         inverted = 1;
        //     }
        //     else {
        //         inverted = -1;
        //     } 
        // }

        // double maxRotationSpeed = swerve.getSwerveDrive().getMaximumChassisAngularVelocity();

        // rotation = -applyDeadBand(controller, 4) * maxRotationSpeed;
        // if (Math.abs(applyDeadBand(operatorController, 4)) > 0) {
        //     rotation = -applyDeadBand(operatorController, 4) * maxRotationSpeed;
        // }

        // xSpeed = applyDeadBand(controller, 0) * inverted * 1.5;
        // ySpeed = applyDeadBand(controller, 1) * inverted * 1.5;
         

        // boolean isRedAlliance = DriverStation.getAlliance().isPresent() &&
        //                         DriverStation.getAlliance().get() == DriverStation.Alliance.Red;

        // double adjustedX = isRedAlliance ? -ySpeed : ySpeed;
        // double adjustedY = isRedAlliance ? -xSpeed : xSpeed;

        // double maxSpeed = swerve.getSwerveDrive().getMaximumChassisVelocity();

        // //Use auto aim rotation if active, otherwise use joystick
        // double finalRotation = autoAimActive ? autoAimRotation : rotation;

        //calculates speed for robot depending on left joystick inputs and rotation values while scaling it down and applying alliance relativity
        SwerveInputStream driveAngularVelocity = SwerveInputStream.of(swerve.getSwerveDrive(),
                                                                        () -> controller.getLeftY(), //gets controller left y input
                                                                        () -> controller.getLeftX()) //gets controller left x input
                                                                        .withControllerRotationAxis(() -> controller.getRightX() * -1)
                                                                        .deadband(0.1) //applies deadband
                                                                        .scaleTranslation(0.8)
                                                                        .allianceRelativeControl(true);
                                                                        //depending on which alliance the bot is on is the direction the bot will move by default
                                                                   

        swerve.driveFieldOriented(driveAngularVelocity);
     

        // swerve.drive(
        //     new Translation2d(adjustedX * maxSpeed, adjustedY * maxSpeed),
        //     finalRotation,
        //     true
        // );
    }

    public double applyDeadBand(XboxController controller, Integer axis) {
        if (Math.abs(controller.getRawAxis(axis)) > 0.1) {
          return controller.getRawAxis(axis);
        } 
        else {
          return 0.0;
        }
    }
    
    @Override
    public void end(boolean interrupted) {
        swerve.drive(new Translation2d(0,0),0,false);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
