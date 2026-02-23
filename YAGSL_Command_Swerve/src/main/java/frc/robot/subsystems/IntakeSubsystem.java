package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {
    private static final int INTAKE_MOTOR_ID = 16;
    private static final int CURRENT_LIMIT = 40;
    private static final double VOLTAGE_COMPENSATION = 12.0;

    //Speed constants
    private static final double INTAKE_SPEED = 1.0;
    private static final double OUTTAKE_SPEED = -1.0;

    private final SparkMax intakeMotor;

    public IntakeSubsystem() {
        intakeMotor = new SparkMax(INTAKE_MOTOR_ID, SparkMax.MotorType.kBrushless);

        SparkMaxConfig intakeConfig = new SparkMaxConfig();
        intakeConfig.voltageCompensation(VOLTAGE_COMPENSATION);
        intakeConfig.smartCurrentLimit(CURRENT_LIMIT);

        intakeMotor.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    /**
     * Runs intake inward to collect game pieces
     */
    public void intake() {
        intakeMotor.set(INTAKE_SPEED);
    }

    /**
     * Runs intake outward to eject game pieces
     */
    public void outtake() {
        intakeMotor.set(OUTTAKE_SPEED);
    }

    /**
     * Runs intake at a custom speed
     * @param speed Speed from -1.0 to 1.0 (negative = outtake, positive = intake)
     */
    public void setSpeed(double speed) {
        intakeMotor.set(speed);
    }

    /**
     * Stops the intake motor
     */
    public void stop() {
        intakeMotor.set(0);
    }
}
