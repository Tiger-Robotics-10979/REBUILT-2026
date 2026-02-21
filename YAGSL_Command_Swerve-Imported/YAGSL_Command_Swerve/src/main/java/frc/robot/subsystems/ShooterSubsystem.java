package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {
    private final PIDController shooterPID = new PIDController(0, 0, 0);
    public final SparkMax shooterMotor = new SparkMax(12, SparkMax.MotorType.kBrushless); //ID TBD 4

    private double targetRPM = 0;
    private double currentRPM = shooterMotor.getEncoder().getVelocity();

    public ShooterSubsystem() {
        final SparkMaxConfig shooterconfig = new SparkMaxConfig();
        
        shooterconfig.voltageCompensation(12);
        shooterconfig.smartCurrentLimit(40);   

        shooterMotor.configure(shooterconfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public double calculateTargetRPM(double distance) {
        return distance * (500.0);
        //This function gives the targeted RPM based on the distance from the hub (CAMERA!) and the equation generated from regression of multiple
        //testing points! The equation will replace the 500!
    }

    public void shootWithPID(double distance) { //speed 
        targetRPM = calculateTargetRPM(distance);

        double finalPowerOutput = shooterPID.calculate(currentRPM, targetRPM); //uses current RPM and target RPM to calculate power output

        shooterMotor.set(finalPowerOutput);
    }

    public void groundIntake() { //slower speed, same direction for ball intake
        shooterMotor.set(0.5);
    }

    public void stopShooter() {
        shooterMotor.set(0); 
    }
}
