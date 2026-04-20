package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberSubsystem extends SubsystemBase {
    private static final int CLIMBER_MOTOR_ID = 12;
    private static final int CURRENT_LIMIT = 40;
    private static final double VOLTAGE_COMPENSATION = 12.0;

    //Speed constants
    private static final double CLIMB_SPEED = 1;

    private final SparkMax climberMotor;

    public ClimberSubsystem() {
        climberMotor = new SparkMax(CLIMBER_MOTOR_ID, SparkMax.MotorType.kBrushless);

        SparkMaxConfig climberConfig = new SparkMaxConfig();
        climberConfig.voltageCompensation(VOLTAGE_COMPENSATION);
        climberConfig.smartCurrentLimit(CURRENT_LIMIT);

        climberMotor.configure(climberConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    /**
     * Raises the climber (positive direction)
     */
    public void raise() {
        climberMotor.set(-CLIMB_SPEED);
    }

    /**
     * Lowers the climber (negative direction)
     */
    public void lower() {
        climberMotor.set(CLIMB_SPEED);
    }

    /**
     * Runs climber at a custom speed
     * @param speed Speed from -1.0 to 1.0 (negative = lower, positive = raise)
     */
    public void setSpeed(double speed) {
        climberMotor.set(speed);
    }

    /**
     * Stops the climber motor
     */
    public void stop() {
        climberMotor.set(0);
    }
}
