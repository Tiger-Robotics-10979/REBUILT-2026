package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberSubsystem extends SubsystemBase {
    private static final int CLIMBER_MOTOR_ID = 11; //TODO: Change CAN ID to correct motor ID
    private static final int CURRENT_LIMIT = 40;
    private static final double VOLTAGE_COMPENSATION = 12.0;

    //Speed constants
    private static final double CLIMB_SPEED = 0.5;

    private final SparkMax climberMotor;
    private final RelativeEncoder climberEncoder;

    public ClimberSubsystem() {
        climberMotor = new SparkMax(CLIMBER_MOTOR_ID, SparkMax.MotorType.kBrushless);
        climberEncoder = climberMotor.getEncoder();

        SparkMaxConfig climberConfig = new SparkMaxConfig();
        climberConfig.voltageCompensation(VOLTAGE_COMPENSATION);
        climberConfig.smartCurrentLimit(CURRENT_LIMIT);

        climberMotor.configure(climberConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    /**
     * Raises the climber (positive direction)
     */
    public void raise() {
        if (climberEncoder.getPosition() < 100) { //TODO: Change encoder value to maximum climber encoder value
            climberMotor.set(-CLIMB_SPEED);
        }
        else { //greater than encoder upper limit
            climberMotor.set(0);
        }
    }

    /**
     * Lowers the climber (negative direction)
     */
    public void lower() {
        if (climberEncoder.getPosition() > 0) {
            climberMotor.set(CLIMB_SPEED);
        }
        else {
            climberMotor.set(0);
        }
        
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
