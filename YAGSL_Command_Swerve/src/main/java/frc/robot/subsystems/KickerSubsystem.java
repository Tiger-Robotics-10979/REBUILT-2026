package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class KickerSubsystem extends SubsystemBase{
    private static final int KICKER_MOTOR_ID = 20;
    private static final int CURRENT_LIMIT = 40;
    private static final double VOLTAGE_COMPENSATION = 12.0;

    private final SparkMax kickerMotor;
    public KickerSubsystem() {
        kickerMotor = new SparkMax(KICKER_MOTOR_ID, SparkMax.MotorType.kBrushless);
        
        SparkMaxConfig climberConfig = new SparkMaxConfig();
        climberConfig.voltageCompensation(VOLTAGE_COMPENSATION);
        climberConfig.smartCurrentLimit(CURRENT_LIMIT);

}
public void setSpeed(double speed) {
    kickerMotor.set(speed);
    
}

public void stop() {
    kickerMotor.set(0);
}
}
