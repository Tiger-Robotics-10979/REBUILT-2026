package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class StorageSubsystem extends SubsystemBase {
    private static final int STORAGE_MOTOR_ID = 16;
    private static final int CURRENT_LIMIT = 40;
    private static final double VOLTAGE_COMPENSATION = 12.0;

    private static final double STORAGE_SPEED = 1.0;

    private final SparkMax storageMotor;

    public StorageSubsystem() {
        storageMotor = new SparkMax(STORAGE_MOTOR_ID, SparkMax.MotorType.kBrushless);

        SparkMaxConfig intakeConfig = new SparkMaxConfig();
        intakeConfig.voltageCompensation(VOLTAGE_COMPENSATION);
        intakeConfig.smartCurrentLimit(CURRENT_LIMIT);

        storageMotor.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    /**
     * Runs storage intake inward to collect game pieces
     */
    public void intake() {
        storageMotor.set(STORAGE_SPEED);
    }

    /**
     * Runs storage intake outward to eject game pieces
     */
    public void outtake() {
        storageMotor.set(-STORAGE_SPEED);
    }

    /**
     * Runs storage intake at a custom speed
     * @param speed Speed from -1.0 to 1.0 (negative = outtake, positive = intake)
     */
    public void setSpeed(double speed) {
        storageMotor.set(speed);
    }

    /**
     * Stops the storage intake motor
     */
    public void stop() {
        storageMotor.set(0);
    }
}
