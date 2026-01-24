package frc.robot.subsystems;


import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase{
    public final SparkMax intakeMotor = new SparkMax(0, SparkMax.MotorType.kBrushless); //ID TBD
 

    public IntakeSubsystem() {
        final SparkMaxConfig intakeConfig = new SparkMaxConfig();

        intakeConfig.voltageCompensation(12);
        intakeConfig.smartCurrentLimit(40);

        intakeMotor.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); 

    }

    public void runIntake(double speed) { 
        intakeMotor.set(speed); 
    }

    public void stopIntake() {
        intakeMotor.set(0); 
    }


}