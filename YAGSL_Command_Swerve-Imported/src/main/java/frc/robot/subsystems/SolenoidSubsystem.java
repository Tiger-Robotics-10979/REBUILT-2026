package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticHub;

public class SolenoidSubsystem {
    public Solenoid solenoid = new Solenoid(5, PneumaticsModuleType.REVPH, 0);
    public PneumaticHub hub = new PneumaticHub(5);

    public void solenoidOn() {
        solenoid.set(true);
    }

    public void solenoidOff() {
        solenoid.set(false);
    }

    public boolean getSolenoidEnabled() {
        return solenoid.get();
    }

    public void enableMotor() {
    }

    public void disableMotor() {
    }
}