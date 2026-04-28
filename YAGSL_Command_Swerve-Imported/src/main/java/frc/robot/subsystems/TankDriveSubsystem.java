package frc.robot.subsystems;


import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;


public class TankDriveSubsystem extends SubsystemBase {
    private final SparkMaxConfig driveConfig = new SparkMaxConfig();
    SparkMax compresser;
    SparkMax leftFollower;
    SparkMax rightShooter;
    SparkMax rightFollower;

    // private final SparkMax leftLeader = new SparkMax(1, SparkMax.MotorType.kBrushed);   
    // private final SparkMax leftFollower = new SparkMax(4, SparkMax.MotorType.kBrushed);

    // private final SparkMax rightLeader = new SparkMax(2, SparkMax.MotorType.kBrushed);
    // private final SparkMax rightFollower = new SparkMax(3, SparkMax.MotorType.kBrushed);

    // public final DifferentialDrive myDrive = new DifferentialDrive(leftLeader, rightLeader); //used for motor calls

    public TankDriveSubsystem() {
        driveConfig.smartCurrentLimit(60); //sets amp limit for each motor
        driveConfig.voltageCompensation(12); //sends half of given voltage to motor to help keep driving consistent

    compresser = new SparkMax(1, SparkMax.MotorType.kBrushed);   
    leftFollower = new SparkMax(4, SparkMax.MotorType.kBrushed);

    rightShooter = new SparkMax(2, SparkMax.MotorType.kBrushed);
    rightFollower = new SparkMax(3, SparkMax.MotorType.kBrushed);


    }

    public void drive(double leftSpeed, double rightSpeed) {
        //leftLeader.set(leftSpeed);
        leftFollower.set(leftSpeed);
       // rightLeader.set(rightSpeed);
        rightFollower.set(rightSpeed);

       
    }
    public void compresser(boolean on) {
        if (on) {
            compresser.set(1);
        } else {
            compresser.set(0);
        }
    }
    public void driveJoystick(XboxController controller) {
        double leftSpeed = controller.getLeftY(); // Invert Y-axis for intuitive control
        double rightSpeed = -controller.getRightY(); // Invert Y-axis for intuitive control
        drive(leftSpeed, rightSpeed);
    }

    public void shooter(boolean on) {
        if (on) {
            rightShooter.set(1);
        } else {
            rightShooter.set(0);
        }
    }
    
    public void stop() {
       // leftLeader.set(0);
        leftFollower.set(0);

       // rightLeader.set(0);
        rightFollower.set(0);
    }
}