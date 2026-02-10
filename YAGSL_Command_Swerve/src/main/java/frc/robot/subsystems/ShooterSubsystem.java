package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;

import static edu.wpi.first.units.Units.RPM;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase{
    private final PIDController shooterPID = new PIDController(0, 0, 0);
    public final SparkMax shooterMotor = new SparkMax(12, SparkMax.MotorType.kBrushless); //ID TBD 4

    //private final CameraSubsystem m_camera;

    
    

   
    private double target; // = distanceFromTarget() * RPM; //this method will calculate the target rpm based on the distance from the target, which is calculated using the camera and some trigonometry.
 
    public ShooterSubsystem(CameraSubsystem camera) {

        //double setpoint = camera.getDistanceFromTarget() * 500.0; // RPM TO BE TESTED. USE EITHER THIS OR TARGET

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
