package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

import org.photonvision.PhotonUtils;

import com.ctre.phoenix6.swerve.utility.WheelForceCalculator.Feedforwards;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.FeedForwardConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import swervelib.encoders.SparkFlexEncoderSwerve;

public class ShooterSubsystem extends SubsystemBase {
    private static final int LEFT_SHOOTER_MOTOR_ID = 11;
    private static final int RIGHT_SHOOTER_MOTOR_ID = 16; // TODO: Update with actual ID of right shooter motor
    private static final int CURRENT_LIMIT = 40;
    private static final double VOLTAGE_COMPENSATION = 12.0;

    private static final double GROUND_INTAKE_SPEED = 0.6;

    private final SparkMax leftShooterMotor;
    private final SparkMax rightShooterMotor;
    private final SparkFlex flexShooterMotor;
    private final PIDController shooterPID;
    private double targetRPM;
    private double FEED_FORWARD = 0.8;

    public double RPMDifference = 5676 - Math.abs(getCurrentRPM());

    private boolean shootingForward;

    private final SwerveSubsystem swerveSubsystem;

    // private static final Translation2d BLUE_HUB = new Translation2d(11.925, 4.0);
    private static final Translation2d BLUE_HUB = new Translation2d(4.625, 4.0);
    private static final Translation2d RED_HUB = new Translation2d(11.925, 4.0);

    public ShooterSubsystem(SwerveSubsystem swerveSubsystem) {
        this.swerveSubsystem = swerveSubsystem;

        leftShooterMotor = new SparkMax(LEFT_SHOOTER_MOTOR_ID, SparkMax.MotorType.kBrushless); // TODO: Check which
                                                                                               // motors are being
                                                                                               // inverted and invert
                                                                                               // flex respectively
        rightShooterMotor = new SparkMax(RIGHT_SHOOTER_MOTOR_ID, SparkMax.MotorType.kBrushless);
        flexShooterMotor = new SparkFlex(20, SparkFlex.MotorType.kBrushless);
        shooterPID = new PIDController(0.0001, 0.0, 0.0); // 3 - 4.5

        SparkMaxConfig shooterConfig = new SparkMaxConfig();
        shooterConfig.voltageCompensation(VOLTAGE_COMPENSATION);
        shooterConfig.smartCurrentLimit(CURRENT_LIMIT);
        shooterConfig.idleMode(IdleMode.kCoast);

        flexShooterMotor.configure(shooterConfig, com.revrobotics.ResetMode.kResetSafeParameters,
                com.revrobotics.PersistMode.kPersistParameters);

        rightShooterMotor.configure(shooterConfig, com.revrobotics.ResetMode.kResetSafeParameters,
                com.revrobotics.PersistMode.kPersistParameters);

        shooterConfig.inverted(true);

        leftShooterMotor.configure(shooterConfig, com.revrobotics.ResetMode.kResetSafeParameters,
                com.revrobotics.PersistMode.kPersistParameters);
    }

    /**
     * Calculates target RPM based on distance to target
     * 
     * @param distanceMeters Distance to target in meters
     * @return Target RPM for shooter
     */
    public double calculateTargetRPM(double distanceMeters) {
        // double inchesConverted = distanceMeters * 39.3701;

        // return ((0.148362 * (inchesConverted * inchesConverted)) + (12.18062 *
        // inchesConverted) + 2610.44836);
        return (((236.80603 * (distanceMeters * distanceMeters)) + (134.51444 * distanceMeters) + 2793.2) * 0.666);
    }

    /**
     * Runs shooter with PID control based on distance
     * 
     * @param distanceMeters Distance to target in meters
     */
    public void shootAtDistance(double distanceMeters) {
        targetRPM = calculateTargetRPM(distanceMeters);
        double output = shooterPID.calculate(getCurrentRPM(), targetRPM); // TODO: Use this function to test different
                                                                          // RPMs to make regression model (change
                                                                          // targetRPM)
        leftShooterMotor.set(output + FEED_FORWARD);
        rightShooterMotor.set(output + FEED_FORWARD);
        System.out.println("Shooter RPM: " + getCurrentRPM());
        System.out.println("Target RPM: " + targetRPM);
    }

    /**
     * Runs shooter with PID control based on setpoint.
     * ONLY for testing
     */
    public void shootWithPID() {
        double output = shooterPID.calculate(getCurrentRPM(), 2000);
        leftShooterMotor.set(output);
        rightShooterMotor.set(output);
    }

    /**
     * Runs shooter at a manual speed (no PID control)
     * 
     * @param speed Speed from -1.0 to 1.0
     */
    public void setSpeed(double speed) {
        this.shootWithFlex();
        leftShooterMotor.set(speed);
        rightShooterMotor.set(speed);
        System.out.println("Shooter RPM: " + getCurrentRPM());
    }

    /**
     * Runs shooter in ground intake mode (slower speed for intake assist)
     * 
     * @param reverse True to reverse direction, false for normal
     */
    public void runGroundIntake(boolean invert) {
        if (invert) {
            leftShooterMotor.set(-GROUND_INTAKE_SPEED);
            rightShooterMotor.set(-GROUND_INTAKE_SPEED);
        } else {
            leftShooterMotor.set(GROUND_INTAKE_SPEED);
            rightShooterMotor.set(GROUND_INTAKE_SPEED);
        }
    }

    /**
     * Calculates distance from the robot to the nearest hub
     * 
     * @return Distance in meters
     */
    public double distanceFromHub() {
        boolean isRed = DriverStation.getAlliance().isPresent()
                && (DriverStation.getAlliance().get() == DriverStation.Alliance.Red);

        Translation2d hubCenter = isRed ? RED_HUB : BLUE_HUB;

        Pose2d robotPose = swerveSubsystem.getPose();

        double distance = robotPose.getTranslation().getDistance(hubCenter);

        return distance;
    }

    /**
     * Stops the shooter motor
     */
    public void stop() {
        leftShooterMotor.set(0);
        rightShooterMotor.set(0);
    }

    /**
     * Gets current shooter RPM
     * 
     * @return Current RPM
     */
    public double getCurrentRPM() {
        return ((leftShooterMotor.getEncoder().getVelocity() * 0.666));
    }

    public void shootWithFlex() {
        if (shootingForward) {
            if (RPMDifference > 4500) {
                flexShooterMotor.set(1.0);
            } else if (RPMDifference > 3000) {
                flexShooterMotor.set(0.85);
            } else if (RPMDifference > 1500) {
                flexShooterMotor.set(0.75);
            } else if (RPMDifference > 500) {
                flexShooterMotor.set(0.65);
            } else if (RPMDifference > 0) {
                flexShooterMotor.set(0);
            }
        } else {
            if (RPMDifference > 4500) {
                flexShooterMotor.set(-1.0);
            } else if (RPMDifference > 3000) {
                flexShooterMotor.set(-0.85);
            } else if (RPMDifference > 1500) {
                flexShooterMotor.set(-0.75);
            } else if (RPMDifference > 500) {
                flexShooterMotor.set(-0.65);
            } else if (RPMDifference > 0) {
                flexShooterMotor.set(-0);
            }
        }
    }

    public void setShootingDirection(boolean forward) {
        shootingForward = forward;
    }

    /**
     * Gets Flex motor on the shooter RPM
     * 
     * @return Flex RPM
     */
    public double getFlexCurrentRPM() {
        return flexShooterMotor.getEncoder().getVelocity();
    }

    public double getTargetRPM() {
        return targetRPM;
    }

}
