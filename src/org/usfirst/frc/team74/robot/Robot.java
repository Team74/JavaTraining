package org.usfirst.frc.team74.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    public static final int LEFT_TALON = 2;
    public static final int LEFT_VICTOR = 11;
    public static final int RIGHT_TALON = 1;
    public static final int RIGHT_VICTOR = 9;
    
    private AnalogGyro gyro;
    
    private double kP = .03;
    
    private static boolean StartDrive = true;
    private static boolean StartTurn = true;
    
    private TalonSRX leftTalon = new TalonSRX(LEFT_TALON);
    private VictorSPX leftVictor = new VictorSPX(LEFT_VICTOR);
    private TalonSRX rightTalon = new TalonSRX(RIGHT_TALON);
    private VictorSPX rightVictor = new VictorSPX(RIGHT_VICTOR);
    
    private Joystick stick = new Joystick(0);
    private Timer time = new Timer();
    
    private double LoopCounter;
    private double CurrentCommand;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    
    public void robotInit() {
    	System.out.println("Robot Init");
        leftTalon.set(ControlMode.PercentOutput, 0);
        rightTalon.set(ControlMode.PercentOutput, 0);
        leftVictor.set(ControlMode.Follower, LEFT_TALON);
        rightVictor.set(ControlMode.Follower, RIGHT_TALON);
        
        leftTalon.setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode.Brake);
        rightTalon.setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode.Brake);
        leftVictor.setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode.Brake);
        rightVictor.setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode.Brake);
    }
    
    /**
     * Initialization code for disabled mode should go here. 
     * 
     * Users should override this method for initialization code which will be called each time 
     * the robot enters disabled mode. 
     */ 
    
    public void disabledInit() { 
        System.out.println("Disabled Init"); 
    } 
    
    /**
     * This function is run once each time the robot enters autonomous mode.
     */
    
    public void autonomousInit() {
    	System.out.println("Auton Init");
    	CurrentCommand = 1;
        time.reset();
        time.start();
    }

    /**
     * This function is called periodically during autonomous.
     */
    
    public void autonomousPeriodic() {
//Go forwards 5 sec, 
    	//power value 30
    	//Rotate 90 degrees
    	autonDrive(30,5,1);
    	autonTurn(90,2);
//Go forward 5 sec
    	//power value 30
    	//Rotate 90 degrees
    	autonDrive(30,5,3);
    	autonTurn(90,4);
//Go forwards 5 sec, 
    	//power value 30
    	//Rotate 90 degrees
    	autonDrive(30,5,5);
    	autonTurn(90,6);
//Go forwards 5 sec
    	//power value 30
    	//Rotate 90 degrees
        autonDrive(30,5,7);
        autonTurn(90,8);
        autonDrive(50,1,9);
        autonTurn(30,10);
        autonDrive(50,1,11);
        autonTurn(30,12);
        autonDrive(50,1,13);
        autonTurn(30,14);
        autonDrive(50,1,11);
        autonTurn(30,12);
    }

    /**
     * This function is called once each time the robot enters teleoperated mode.
     */
 
    
    public void teleopInit() {
        time.reset();
        time.start();
    }

    /**
     * This function is called periodically during teleoperated mode.
     */
    
    public void teleopPeriodic() {
        
    }

    /**
     * This function is called periodically during test mode.
     */
    
    public void testPeriodic() {
    }
    public void autonDrive(double speed, int time, int commandorder) {
    	if (commandorder == CurrentCommand) {
	    	double CurrentAngle = 0; //gyro.getAngle();
	    	time = time * 50;
	    	System.out.println(time);
	    	if (StartDrive) {
	    		LoopCounter = 0;
	    		StartDrive = false;
	    		//gyro.reset();
	    	}
	    	if (LoopCounter <= time) {
	    		LoopCounter++;
	    		drive(speed, (CurrentAngle * kP));
	    	} else {
	    		drive(0, 0);
	    		StartDrive = true;
	    		CurrentCommand++;
	    	}
    	}
    }
    public void autonTurn(double turntime, int commandorder) {
    	if (commandorder == CurrentCommand) {
    		turntime = turntime * 50;
        	double speed = .5;
	    	double CurrentAngle = 0; //gyro.getAngle();
	    	if (StartTurn) {
	    		StartTurn = false;
	    		LoopCounter = 0;
	    		//gyro.reset();
	    	}
	    	if (LoopCounter <= turntime) {
	    		turn(50);
	    		LoopCounter++;
	    	} else {
	    		turn(0);
	    		StartTurn = true;
	    		CurrentCommand++;
	    	}
    	}
    }
    private void drive(double speed, double wheel) {
        double scaledSpeed = speed / 100.0;
        scaledSpeed = Math.min(1.0, scaledSpeed);
        scaledSpeed = Math.max(-1.0, scaledSpeed);
        double leftspeed = scaledSpeed - wheel;
        double rightspeed = scaledSpeed + wheel;
        rightspeed = rightspeed * -1;
        leftTalon.set(ControlMode.PercentOutput, leftspeed);
        rightTalon.set(ControlMode.PercentOutput, rightspeed);
    }

    private void turn(int speed) {
        double scaledSpeed = speed / 100.0;
        scaledSpeed = Math.min(1.0, scaledSpeed);
        scaledSpeed = Math.max(-1.0, scaledSpeed);
        leftTalon.set(ControlMode.PercentOutput, -1*scaledSpeed/2);
        rightTalon.set(ControlMode.PercentOutput, scaledSpeed/2);
    }
    
    private boolean getButtonA() {
        return stick.getRawButton(1);
    }
    
    private boolean getButtonB() {
        return stick.getRawButton(2);
    }
    
    private boolean getButtonX() {
        return stick.getRawButton(3);
    }
    
    private boolean getButtonY() {
        return stick.getRawButton(4);
    }
}
