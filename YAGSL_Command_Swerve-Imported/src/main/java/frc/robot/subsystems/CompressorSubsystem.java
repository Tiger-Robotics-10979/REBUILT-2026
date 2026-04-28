package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.PneumaticHub;

public class CompressorSubsystem {
    //public SolenoidSubsystem solenoidSubsystem = new SolenoidSubsystem()
    public Compressor compressor = new Compressor(5, PneumaticsModuleType.REVPH);
    public PneumaticHub hub = new PneumaticHub(5);
    //public SolenoidSubsystem shooter = new SolenoidSubsystem(0);
    Solenoid shooter = new Solenoid(PneumaticsModuleType.REVPH, 0);
    public void compressorOn() {
        hub.enableCompressorDigital();
        compressor.enableDigital();
    }

    public void compressorOff() {
        hub.disableCompressor();
        compressor.disable();
    }

    public void enableMotor() {

    }

    public void disableMotor() {
    }

    public boolean getCompressorEnabled() {
        return hub.getCompressor();
    }

    public void shoot() {
        shooter.toggle();
    }
}
