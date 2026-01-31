package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase{
    public final SparkMax shooterMotor = new SparkMax(0, SparkMax.MotorType.kBrushless); //ID TBD
    ;
 
    public ShooterSubsystem() {
        final SparkMaxConfig shooterconfig = new SparkMaxConfig();
        
        shooterconfig.voltageCompensation(12);
        shooterconfig.smartCurrentLimit(40);   

        shooterMotor.configure(shooterconfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void runShooter(double speed) { 
        shooterMotor.set(speed); 
    }
    public void stopShooter() {
        shooterMotor.set(0); 
    
    }

}
