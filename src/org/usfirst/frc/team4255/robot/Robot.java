package org.usfirst.frc.team4255.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.cscore.UsbCamera;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Joystick;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobot {
	Timer time = new Timer();
	
	//SerialPort rpi = new SerialPort(9600, SerialPort.Port.kOnboard);
	
	//I2C pixyPort = new I2C(edu.wpi.first.wpilibj.I2C.Port.kOnboard, 0x45);
	//Pixy pixy = new Pixy(pixyPort);
	int pan =0;
	int tilt = 0;
	
	double speed = 0.0;
	
	Joystick jLeft = new Joystick (0);
    Joystick jRight = new Joystick (1);
    Joystick jSide = new Joystick (2);
    Joystick chooser = new Joystick (3);
    Joystick Esplora1 = new Joystick (4);
    Joystick Esplora2 = new Joystick (5);
	
    AHRS navX = new AHRS(SPI.Port.kMXP);
    
    TalonSRX leftDrive = new TalonSRX(0);
    TalonSRX leftFollow1 = new TalonSRX(1);
    TalonSRX leftRamp = new TalonSRX(2);
    TalonSRX rightDrive = new TalonSRX(3);
    TalonSRX rightFollow1 = new TalonSRX(4);
    TalonSRX rightRamp = new TalonSRX(5);
    
    Drive drive = new Drive(leftDrive, FeedbackDevice.None,
    		rightDrive, FeedbackDevice.CTRE_MagEncoder_Relative);
    NavDrive navDrive = new NavDrive(navX, drive);
    
    DigitalInput limit = new DigitalInput(9);
	Spark liftPrimary = new Spark(0);
	
	CameraServer camserv;
	UsbCamera cam0;
	
	boolean done = false;

	String sides;
	String position;
	int step =0;
	
	@Override
	public void robotInit() {
	    rightDrive.setInverted(true);
	    rightFollow1.setInverted(true);
		leftFollow1.follow(leftDrive);
		rightFollow1.follow(rightDrive);
		
		camserv = CameraServer.getInstance();
	    
	    cam0 = camserv.startAutomaticCapture(0);
	    cam0.setResolution(1920, 1080);
	    cam0.setFPS(30);
	}

	@Override
	public void autonomousInit() {
		sides = DriverStation.getInstance().getGameSpecificMessage();
		if (chooser.getRawButton(1)) position = "Left";
		else if (chooser.getRawButton(2)) position = "Right";
		else position = "Middle";
		System.out.println("Auto Selected: " + position);
		navX.reset();
		navDrive.reset();
		drive.reset();
		done = false;
		step = 0;
	}

	@Override
	public void autonomousPeriodic() {
		switch (position) {
			case "Left":
				if (sides.charAt(0) == 'L'){
					//left side, switch left
					switch(step){
					case 0:
						if(drive.simpleDriveTo(0.3, 14)) nextStep();
							//raise lift
						break;
					case 1:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 2:
						if(drive.simpleDriveTo(0.3, 4.63)) nextStep();
						break;
					case 3:
						//dropoff switch
						nextStep();
						break;
					case 4:
						if(drive.simpleDriveTo(-0.3, -1)) nextStep();
						break;
					case 5:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 6:
						if(drive.simpleDriveTo(0.3, (10/3))) nextStep();
						break;
					case 7:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 8:
						if(drive.simpleDriveTo(0.3, 1.5416)) nextStep();
						break;
					case 9:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 10:
						//activate intake
						if(drive.simpleDriveTo(0.3, (7/3))) nextStep();		
						break;
					case 11:
						//pickup and lift
						nextStep();
						break;
					case 12:
						if(drive.simpleDriveTo(-0.3, -(7/3))){
							nextStep();
							if(sides.charAt(1) == 'R') step = 22;
						}
						break;
					case 13:
						//left side, switch left, scale left
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 14:
						if(drive.simpleDriveTo(0.3, 2.6816)) nextStep();
						break;
					case 15:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 16:
						if(drive.simpleDriveTo(0.3, 9.97)) nextStep();
						break;
					case 17:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 18:
						if(drive.simpleDriveTo(0.3, 1)) nextStep();
						break;
					case 19:
						//dropoff scale
						nextStep();
						break;
					case 20:
						if(drive.simpleDriveTo(-0.3, -1)) nextStep();
						break;
					case 21:
						drive.setDrive(0, 0, false);
						break;
					case 22:
						//left side, switch, left, scale left
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 23:
						if(drive.simpleDriveTo(0.3, 19.0716)) nextStep();
						break;
					case 24:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 25:
						if(drive.simpleDriveTo(0.3, 9.97)) nextStep();
						break;
					case 26:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 27:
						if(drive.simpleDriveTo(0.3, 1)) nextStep();
						break;
					case 28:
						//dropoff scale
						nextStep();
						break;
					case 29:
						if(drive.simpleDriveTo(-0.3, -1)) nextStep();
						break;
					case 30:
						drive.setDrive(0, 0, false);
						break;
					}
				}
				else{
					if(sides.charAt(1) == 'L'){
						//left side, scale left, switch right
						switch(step){
						case 0:
							if(drive.simpleDriveTo(0.3, 27.304)) nextStep();
							//raise lift
							break;
						case 1:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 2:
							if(drive.simpleDriveTo(0.3, 3.49)) nextStep();
							break;
						case 3:
							// dropoff scale 1
							nextStep();
							break;
						case 4:
							if(drive.simpleDriveTo(-0.3, -1)) nextStep();
							//lower lift
							break;
						case 5:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 6:
							if(drive.simpleDriveTo(0.3, 9.97)) nextStep();
							break;
						case 7:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 8:
							if(drive.simpleDriveTo(0.3, 3.223))
							break;
						case 9:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 10:
							if(drive.simpleDriveTo(0.3, 1)) nextStep();
							//activate intake
							break;
						case 11:
							//pickup
							nextStep();
							break;
						case 12:
							if(drive.simpleDriveTo(-0.3, -1)) nextStep();
							break;
						case 13:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 14:
							if(drive.simpleDriveTo(0.3, 3.223))
							break;
						case 15:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 16:
							if(drive.simpleDriveTo(0.3, 9.97)) nextStep();
							break;
						case 17:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 18:
							if(drive.simpleDriveTo(0.3, 1)) nextStep();
							break;
						case 19:
							//dropoff scale 2
							nextStep();
							break;
						case 20:
							if(drive.simpleDriveTo(-0.3, -1)) nextStep();
							break;
						case 21:
							drive.setDrive(0, 0, false);
							break;
						}
					}
					else{
						//left side, scale right, switch right
						switch(step){
						case 0:
							if(drive.simpleDriveTo(0.3, 15)) nextStep();
							break;
						case 1:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 2:
							if(drive.simpleDriveTo(0.3, 22.5616)) nextStep();
							break;
						case 3:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 4:
							if(drive.simpleDriveTo(0, 12)) nextStep();
							break;
						case 5:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 6:
							if(drive.simpleDriveTo(0.3, 1))
							break;
						case 7:
							//dropoff scale 1
							break;
						case 8:
							if(drive.simpleDriveTo(-0.3, -1)) nextStep();
							break;
						case 9:
							if(navDrive.turnTo(-90)) nextStep();
							//lower lift
							break;
						case 10:
							if(drive.simpleDriveTo(0.3, 9.97)) nextStep();
							break;
						case 11:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 12:
							if(drive.simpleDriveTo(0.3, 7.015)) nextStep();
							break;
						case 13:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 14:
							if(drive.simpleDriveTo(0.3, 1)) nextStep();
							//activate intake
							break;
						case 15:
							//pickup
							nextStep();
							break;
						case 16:
							if(drive.simpleDriveTo(-0.3, -1)) nextStep();
							break;
						case 17:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 18:
							if(drive.simpleDriveTo(0.3, 7.015)) nextStep();
							break;
						case 20:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 21:
							if(drive.simpleDriveTo(0.3, 9.97)) nextStep();
							break;
						case 22:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 23:
							if(drive.simpleDriveTo(0.3, 1)) nextStep();
							break;
						case 24:
							//dropoff scale 2
							nextStep();
							break;
						case 25:
							if(drive.simpleDriveTo(-0.3, -1)) nextStep();
							break;
						case 26:
							drive.setDrive(0, 0, false);
							break;
						}
					}
				}
				break;
				
			case "Middle":
				if (sides.charAt(0) == 'L') {
					switch (step) {
					case 0:
						if (drive.simpleDriveTo(0.3, 2.0)) nextStep();
						break;
					case 1:
						if (navDrive.turnTo(-45.0)) nextStep();
						break;
					case 2:
						if (drive.simpleDriveTo(0.3, 6.25)) nextStep();
						break;
					case 3:
						if (navDrive.turnTo(45.0)) nextStep();
						break;
					case 4:
						drive.setDrive(0.0, 0.0, false);
						break;
					}
				} 
				else {
					switch (step) {
					case 0:
						if (drive.simpleDriveTo(0.3, 2.42)) nextStep();
						break;
					case 1:
						if (navDrive.turnTo(45.0)) nextStep();
						break;
					case 2:
						if (drive.simpleDriveTo(0.3, 5.07)) nextStep();
						break;
					case 3:
						if (navDrive.turnTo(-45.0)) nextStep();
						break;
					case 4:
						drive.setDrive(0.0, 0.0, false);
						break;
					}
				}
				break;
				
			case "Right":
				if (sides.charAt(0) == 'R'){
					switch(step){
					case 0:
						if(drive.simpleDriveTo(0.3, 14)) nextStep();
							//lift arm
						break;
					case 1:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 2:
						if(drive.simpleDriveTo(0.3, 4.63)) nextStep();
						break;
					case 3:
						//dropoff switch
						nextStep();
						break;
					case 4:
						if(drive.simpleDriveTo(-0.3, -1)) nextStep();
						break;
					case 5:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 6:
						if(drive.simpleDriveTo(0.3, (10/3))) nextStep();
						break;
					case 7:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 8:
						if(drive.simpleDriveTo(0.3, 1.5416)) nextStep();
						break;
					case 9:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 10:
						//activate intake
						if(drive.simpleDriveTo(0.3, (7/3))) nextStep();		
						break;
					case 11:
						//pickup and lift
						nextStep();
						break;
					case 12:
						if(drive.simpleDriveTo(-0.3, -(7/3))){
							nextStep();
							if(sides.charAt(1) == 'L') step = 22;
						}
						break;
					case 13:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 14:
						if(drive.simpleDriveTo(0.3, 2.6816)) nextStep();
						break;
					case 15:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 16:
						if(drive.simpleDriveTo(0.3, 9.97)) nextStep();
						break;
					case 17:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 18:
						if(drive.simpleDriveTo(0.3, 1)) nextStep();
						break;
					case 19:
						//dropoff scale
						nextStep();
						break;
					case 20:
						if(drive.simpleDriveTo(-0.3, -1)) nextStep();
						break;
					case 21:
						drive.setDrive(0, 0, false);
						break;
					case 22:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 23:
						if(drive.simpleDriveTo(0.3, 19.0716)) nextStep();
						break;
					case 24:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 25:
						if(drive.simpleDriveTo(0.3, 9.97)) nextStep();
						break;
					case 26:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 27:
						if(drive.simpleDriveTo(0.3, 1)) nextStep();
						break;
					case 28:
						//dropoff scale
						nextStep();
						break;
					case 29:
						if(drive.simpleDriveTo(-0.3, -1)) nextStep();
						break;
					case 30:
						drive.setDrive(0, 0, false);
						break;
					}
				}
				else{
					if(sides.charAt(1) == 'R'){
						//right side, scale right, switch left
						switch(step){
						case 0:
							if(drive.simpleDriveTo(0.3, 27.304)) nextStep();
							//raise lift
							break;
						case 1:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 2:
							if(drive.simpleDriveTo(0.3, 3.49)) nextStep();
							break;
						case 3:
							// dropoff scale 1
							nextStep();
							break;
						case 4:
							if(drive.simpleDriveTo(-0.3, -1)) nextStep();
							//lower lift
							break;
						case 5:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 6:
							if(drive.simpleDriveTo(0.3, 9.97)) nextStep();
							break;
						case 7:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 8:
							if(drive.simpleDriveTo(0.3, 3.223))
							break;
						case 9:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 10:
							if(drive.simpleDriveTo(0.3, 1)) nextStep();
							//activate intake
							break;
						case 11:
							//pickup
							nextStep();
							break;
						case 12:
							if(drive.simpleDriveTo(-0.3, -1)) nextStep();
							break;
						case 13:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 14:
							if(drive.simpleDriveTo(0.3, 3.223))
							break;
						case 15:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 16:
							if(drive.simpleDriveTo(0.3, 9.97)) nextStep();
							break;
						case 17:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 18:
							if(drive.simpleDriveTo(0.3, 1)) nextStep();
							break;
						case 19:
							//dropoff scale 2
							nextStep();
							break;
						case 20:
							if(drive.simpleDriveTo(-0.3, -1)) nextStep();
							break;
						case 21:
							drive.setDrive(0, 0, false);
							break;
						}
					}
					else{
						//right side, scale left, switch left
						switch(step){
						case 0:
							if(drive.simpleDriveTo(0.3, 15)) nextStep();
							break;
						case 1:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 2:
							if(drive.simpleDriveTo(0.3, 22.5616)) nextStep();
							break;
						case 3:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 4:
							if(drive.simpleDriveTo(0, 12)) nextStep();
							break;
						case 5:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 6:
							if(drive.simpleDriveTo(0.3, 1))
							break;
						case 7:
							//dropoff scale 1
							break;
						case 8:
							if(drive.simpleDriveTo(-0.3, -1)) nextStep();
							break;
						case 9:
							if(navDrive.turnTo(90)) nextStep();
							//lower lift
							break;
						case 10:
							if(drive.simpleDriveTo(0.3, 9.97)) nextStep();
							break;
						case 11:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 12:
							if(drive.simpleDriveTo(0.3, 7.015)) nextStep();
							break;
						case 13:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 14:
							if(drive.simpleDriveTo(0.3, 1)) nextStep();
							//activate intake
							break;
						case 15:
							//pickup
							nextStep();
							break;
						case 16:
							if(drive.simpleDriveTo(-0.3, -1)) nextStep();
							break;
						case 17:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 18:
							if(drive.simpleDriveTo(0.3, 7.015)) nextStep();
							break;
						case 20:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 21:
							if(drive.simpleDriveTo(0.3, 9.97)) nextStep();
							break;
						case 22:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 23:
							if(drive.simpleDriveTo(0.3, 1)) nextStep();
							break;
						case 24:
							//dropoff scale 2
							nextStep();
							break;
						case 25:
							if(drive.simpleDriveTo(-0.3, -1)) nextStep();
							break;
						case 26:
							drive.setDrive(0, 0, false);
							break;
						}
					}
				}
				break;
			}
	}
	
	@Override
	public void teleopInit() {
		time.start();
		pan = 500;
		tilt = 500;
		speed = 0.0;
		drive.zeroLeftDist();
	}
	
	@Override
	public void teleopPeriodic() {
		if (jRight.getRawButton(1)) {
			if (speed < 0.25) speed += 0.0005;
		} else if (jRight.getRawButton(2)) {
			if (speed > -0.25) speed -= 0.0005;
		} else {
			speed = 0.0;
		}
		rightRamp.set(ControlMode.PercentOutput, speed);
		
		//System.out.println(rpi.readString());
		drive.setDrive(-jLeft.getY(), -jRight.getY(), false);
		liftPrimary.set(-jSide.getY());
		/*if (Esplora1.getRawButton(2)) {
			drive.singleJoystickDrive(-Esplora2.getY(), -Esplora2.getX(), false);
		} else {
			drive.singleJoystickDrive(0.0, 0.0, false);
		}*/
	}
	
	@Override
	public void testPeriodic() {
	}
	
	public void nextStep(){
		navDrive.reset();
		drive.reset();
		step++;
	}
}
