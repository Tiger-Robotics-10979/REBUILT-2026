package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {
    private final PIDController shooterPID = new PIDController(0, 0, 0);
    public final SparkMax shooterMotor = new SparkMax(12, SparkMax.MotorType.kBrushless);
    private double goalRPM;

    public ShooterSubsystem() {
        final SparkMaxConfig shooterconfig = new SparkMaxConfig();

        shooterconfig.voltageCompensation(12);
        shooterconfig.smartCurrentLimit(40);

        shooterMotor.configure(shooterconfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public double calculateTargetRPM(double distance) {
        // Targeted RPM based on camera distance and exponential regression
        return distance * 500.0;
    }

    public void shootWithPID(double distance) {
        goalRPM = calculateTargetRPM(distance);
        double currentRPM = shooterMotor.getEncoder().getVelocity();
        double finalPowerOutput = shooterPID.calculate(currentRPM, goalRPM);
        shooterMotor.set(finalPowerOutput);
    }

    public void groundIntake(boolean out) {
        if (out) {
            shooterMotor.set(-0.5);
        } else {
            shooterMotor.set(0.5);
        }
    }

    public void testingRegressionSpeed() {
        shooterMotor.set(0.5);
        System.out.println(shooterMotor.getEncoder().getVelocity());
    }

    public void stopShooter() {
        shooterMotor.set(0);
    }
}
