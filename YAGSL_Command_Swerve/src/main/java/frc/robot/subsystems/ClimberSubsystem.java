package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberSubsystem extends SubsystemBase {
    public final SparkMax climberMotor = new SparkMax(14, SparkMax.MotorType.kBrushless); //ID TBD 4

    public ClimberSubsystem() {
        final SparkMaxConfig climberConfig = new SparkMaxConfig();
        
        climberConfig.voltageCompensation(12);
        climberConfig.smartCurrentLimit(40);   

        climberMotor.configure(climberConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void raiseClimber() {
        climberMotor.set(0.5);
    }

    public void lowerClimber() {
        climberMotor.set(-0.5);
    }

    public void stopClimber() {
        climberMotor.set(0);
    }
}
