package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {
    private static final int LEFT_SHOOTER_MOTOR_ID = 12;
    private static final int RIGHT_SHOOTER_MOTOR_ID = 25; //TODO: Update with actual ID of right shooter motor
    private static final int CURRENT_LIMIT = 40;
    private static final double VOLTAGE_COMPENSATION = 12.0;

    private static final double GROUND_INTAKE_SPEED = 0.6;

    private final SparkMax leftShooterMotor;
    private final SparkMax rightShooterMotor;
    private final PIDController shooterPID;
    private double targetRPM;

    public ShooterSubsystem() {
        leftShooterMotor = new SparkMax(LEFT_SHOOTER_MOTOR_ID, SparkMax.MotorType.kBrushless);
        rightShooterMotor = new SparkMax(RIGHT_SHOOTER_MOTOR_ID, SparkMax.MotorType.kBrushless);
        shooterPID = new PIDController(0.0003, 0.00001, 0.0);

        SparkMaxConfig shooterConfig = new SparkMaxConfig();
        shooterConfig.voltageCompensation(VOLTAGE_COMPENSATION);
        shooterConfig.smartCurrentLimit(CURRENT_LIMIT);

        leftShooterMotor.configure(shooterConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        rightShooterMotor.configure(shooterConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        leftShooterMotor.setInverted(true);
    }

    /**
     * Calculates target RPM based on distance to target
     * @param distanceMeters Distance to target in meters
     * @return Target RPM for shooter
     */
    public double calculateTargetRPM(double distanceMeters) {
        return ((14.18581 * (distanceMeters * distanceMeters)) + (417.28272 * distanceMeters) + 1796.55345);
    }

    /**
     * Runs shooter with PID control based on distance
     * @param distanceMeters Distance to target in meters
     */
    public void shootAtDistance(double distanceMeters) {
        targetRPM = calculateTargetRPM(distanceMeters);
        double output = shooterPID.calculate(getCurrentRPM(), 1250);  //TODO: Use this function to test different RPMs to make regression model (change targetRPM)
        leftShooterMotor.set(output + 0.1); //+0.25 feedforward value
        rightShooterMotor.set(output + 0.1);
        System.out.println("Shooter RPM: " + getCurrentRPM());

    }

    /**
     * Runs shooter at a manual speed (no PID control)
     * @param speed Speed from -1.0 to 1.0
     */
    public void setSpeed(double speed) {
        leftShooterMotor.set(speed);
        rightShooterMotor.set(speed);
        System.out.println("Shooter RPM: " + getCurrentRPM());
    }
  
    /**
     * Runs shooter in ground intake mode (slower speed for intake assist)
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
     * Stops the shooter motor
     */
    public void stop() {
        leftShooterMotor.set(0);
        rightShooterMotor.set(0);
        targetRPM = 0;
    }

    /**
     * Gets current shooter RPM
     * @return Current RPM
     */
    public double getCurrentRPM() {
        return (leftShooterMotor.getEncoder().getVelocity() + rightShooterMotor.getEncoder().getVelocity()) / 2.0;
    }
    
    public double getTargetRPM() {
        return targetRPM;
    }
}
