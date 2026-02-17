package frc.robot.subsystems;

// import edu.wpi.first.math.controller.PIDController;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {
    // private final PIDController shooterPID = new PIDController(0, 0, 0);
    public final SparkMax shooterMotor = new SparkMax(12, SparkMax.MotorType.kBrushless); //ID TBD 4

    public final RelativeEncoder shooterEncoder = shooterMotor.getEncoder();
   
    private double shooterRPM = shooterEncoder.getVelocity();
    
    public ShooterSubsystem() {

        final SparkMaxConfig shooterconfig = new SparkMaxConfig();
        
        shooterconfig.voltageCompensation(12);
        shooterconfig.smartCurrentLimit(40);   

        shooterMotor.configure(shooterconfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void runShooter(double speed) { 
        shooterMotor.set(speed);

        System.out.println(shooterRPM);
    }

    public void stopShooter() {
        shooterMotor.set(0); 
    }

}
