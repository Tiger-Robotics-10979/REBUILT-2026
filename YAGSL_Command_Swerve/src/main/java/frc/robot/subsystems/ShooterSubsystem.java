package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {
    private static final int SHOOTER_MOTOR_ID = 12;
    private static final int CURRENT_LIMIT = 40;
    private static final double VOLTAGE_COMPENSATION = 12.0;

    //Speed constants
    private static final double GROUND_INTAKE_SPEED = 0.6;

    private final SparkMax shooterMotor;
    private final PIDController shooterPID;
    private double targetRPM;

    public ShooterSubsystem() {
        shooterMotor = new SparkMax(SHOOTER_MOTOR_ID, SparkMax.MotorType.kBrushless);
        shooterPID = new PIDController(0.0002, 0.00001, 0.0);

        SparkMaxConfig shooterConfig = new SparkMaxConfig();
        shooterConfig.voltageCompensation(VOLTAGE_COMPENSATION);
        shooterConfig.smartCurrentLimit(CURRENT_LIMIT);

        shooterMotor.configure(shooterConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
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
        double output = shooterPID.calculate(getCurrentRPM(), targetRPM);  //TODO: Use this function to test different RPMs to make regression model (change targetRPM)
        shooterMotor.set(output);
    }

    /**
     * Runs shooter at a manual speed (no PID control)
     * @param speed Speed from -1.0 to 1.0
     */
    public void setSpeed(double speed) {
        shooterMotor.set(speed);
        System.out.println("Shooter RPM: " + getCurrentRPM());
    }

    /**
     * Runs shooter in ground intake mode (slower speed for intake assist)
     * @param reverse True to reverse direction, false for normal
     */
    public void runGroundIntake(boolean invert) {
        if (invert) {
            shooterMotor.set(-GROUND_INTAKE_SPEED);
        } else {
            shooterMotor.set(GROUND_INTAKE_SPEED);
        }
    }

    /**
     * Stops the shooter motor
     */
    public void stop() {
        shooterMotor.set(0);
        targetRPM = 0;
    }

    /**
     * Gets current shooter RPM
     * @return Current RPM
     */
    public double getCurrentRPM() {
        return shooterMotor.getEncoder().getVelocity();
    }
}
