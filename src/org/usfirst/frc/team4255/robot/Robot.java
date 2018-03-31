package org.usfirst.frc.team4255.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.cscore.UsbCamera;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Joystick;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Robot extends IterativeRobot {
	Timer time = new Timer();
	Timer liftTime = new Timer();
	
	//SerialPort rpi = new SerialPort(9600, SerialPort.Port.kOnboard);
	
	Joystick jLeft = new Joystick (0);
    Joystick jRight = new Joystick (1);
    Joystick jSide = new Joystick (2);
    Joystick chooser = new Joystick (3);
    Joystick Esplora1 = new Joystick (4);
    Joystick Esplora2 = new Joystick (5);
	
    AHRS navX = new AHRS(SPI.Port.kMXP);
    AnalogInput sonar = new AnalogInput(0); //5v/512
    //I2C pixyPort = new I2C(edu.wpi.first.wpilibj.I2C.Port.kOnboard, 0x45);
  	//Pixy pixy = new Pixy(pixyPort);
    
    TalonSRX leftDrive = new TalonSRX(0);
    TalonSRX leftFollow1 = new TalonSRX(1);
    TalonSRX rightDrive = new TalonSRX(3);
    TalonSRX rightFollow1 = new TalonSRX(4);
    TalonSRX lift2 = new TalonSRX(2);
    TalonSRX rightRamp = new TalonSRX(5);
    
    //DoubleSolenoid release = new DoubleSolenoid(0,1);
    
    Drive drive = new Drive(leftDrive, FeedbackDevice.None,
    		rightDrive, FeedbackDevice.CTRE_MagEncoder_Relative);
    NavDrive navDrive = new NavDrive(navX, drive);
    Route middleL = new Route(etc.middleL, drive, navDrive);
    Route middleR = new Route(etc.middleR, drive, navDrive);
    Route leftL = new Route(etc.leftL, drive, navDrive);
    Route leftR = new Route(etc.leftR, drive, navDrive);
    Route rightR = new Route(etc.rightR, drive, navDrive);
    Route rightL = new Route(etc.rightL, drive, navDrive);
    
	Spark lift = new Spark(0);
	Spark intake = new Spark(1);
	Spark tilt = new Spark(2);
	Spark clamp = new Spark(3);
	Encoder tiltEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
	
	CameraServer camserv;
	UsbCamera cam0;
	UsbCamera cam1;

	String sides;
	String position;
	int step = 0;
	boolean done = false;
	
	@Override
	public void robotInit() {
		//release.set(DoubleSolenoid.Value.kOff);
		
		tiltEncoder.setDistancePerPulse(12/350.0);
		
	    rightDrive.setInverted(true);
	    rightFollow1.setInverted(true);
		leftFollow1.follow(leftDrive);
		rightFollow1.follow(rightDrive);
		
		rightDrive.configPeakCurrentDuration(10, 0);
		rightDrive.configPeakCurrentLimit(50, 0);
		rightDrive.configContinuousCurrentLimit(40, 0);
		rightDrive.enableCurrentLimit(true);
		
		leftDrive.configPeakCurrentDuration(10, 0);
		leftDrive.configPeakCurrentLimit(50, 0);
		leftDrive.configContinuousCurrentLimit(40, 0);
		leftDrive.enableCurrentLimit(true);
		
		rightFollow1.configPeakCurrentDuration(10, 0);
		rightFollow1.configPeakCurrentLimit(50, 0);
		rightFollow1.configContinuousCurrentLimit(40, 0);
		rightFollow1.enableCurrentLimit(true);
		
		leftFollow1.configPeakCurrentDuration(10, 0);
		leftFollow1.configPeakCurrentLimit(50, 0);
		leftFollow1.configContinuousCurrentLimit(40, 0);
		leftFollow1.enableCurrentLimit(true);
		
		/*camserv = CameraServer.getInstance();
	    
	    cam0 = camserv.startAutomaticCapture(0);
	    cam0.setResolution(1920, 1080);
	    cam1 = camserv.startAutomaticCapture(1);
	    cam1.setResolution(1920, 1080);*/
	    //cam0.setFPS(30); //this probably won't work if you activate it
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
		middleL.setTo(0);
		middleR.setTo(0);
		leftL.setTo(0);
		leftR.setTo(0);
		rightR.setTo(0);
		rightL.setTo(0);
	}

	@Override
	public void autonomousPeriodic() {
		switch (position) {
			case "Left":
				if (!done) {
					if (drive.driveTo(8.0)) done = true;
				}
				/*switch (sides.charAt(0)) {
				case 'L': //Left side Left switch
					switch (step) {
					case 0:
						if (liftUp() && leftL.run()) step++;
						break;
					case 1:
						if (deploy()) step++;
						break;
					}
					break;
				case 'R': //Left side Right switch
					switch (step) {
					case 0:
						if (liftUp() && leftR.run()) step++;
						break;
					case 1:
						if (deploy()) step++;
						break;
					}
					break;
				}*/
				/*if (sides.charAt(0) == 'L'){ //these distance values are accurate
					//left side, switch left
					switch(step){
					case 0:
						if(drive.driveTo(12.38)) nextStep();
							//raise lift
						break;
					case 1:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 2:
						if(drive.driveTo(1.59)) nextStep();
						break;
					case 3:
						//dropoff switch
						nextStep();
						break;
					case 4:
						if(drive.driveTo(-1.0)) nextStep();
						break;
					case 5:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 6:
						if(drive.driveTo(6.36)) nextStep();
						break;
					case 7:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 8:
						if(drive.driveTo(3.17)) nextStep();
						break;
					case 9:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 10:
						//activate intake
						if(drive.driveTo(1.58)) nextStep();		
						break;
					case 11:
						//pickup and lift
						nextStep();
						break;
					case 12:
						if(drive.driveTo(-1.0)){
							nextStep();
							if(sides.charAt(1) == 'R') step = 22;
						}
						break;
					case 13:
						//left side, switch left, scale left
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 14:
						if(drive.driveTo(3.785)) nextStep();
						break;
					case 15:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 16:
						if(drive.driveTo(8.89)) nextStep();
						break;
					case 17:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 18:
						if(drive.driveTo(0.5)) nextStep();
						break;
					case 19:
						//dropoff scale
						nextStep();
						break;
					case 20:
						if(drive.driveTo(-1.66)) nextStep();
						break;
					case 21:
						drive.setDrive(0, 0, false);
						break;
					case 22:
						//left side, switch, left, scale left
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 23:
						if(drive.driveTo(11.82)) nextStep();
						break;
					case 24:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 25:
						if(drive.driveTo(6.81)) nextStep();
						break;
					case 26:
						//dropoff scale
						nextStep();
						break;
					case 27:
						if(drive.driveTo(-1.66)) nextStep();
						break;
					case 28:
						drive.setDrive(0, 0, false);
						break;
					}
				}
				else{
					if(sides.charAt(1) == 'L'){
						//left side, scale left, switch right
						switch(step){
						case 0:
							if(drive.simpleDriveTo(0.35, 27.304)) nextStep();
							//raise lift
							break;
						case 1:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 2:
							if(drive.simpleDriveTo(0.35, 3.49)) nextStep();
							break;
						case 3:
							// dropoff scale 1
							nextStep();
							break;
						case 4:
							if(drive.simpleDriveTo(-0.35, -1)) nextStep();
							//lower lift
							break;
						case 5:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 6:
							if(drive.simpleDriveTo(0.35, 9.97)) nextStep();
							break;
						case 7:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 8:
							if(drive.simpleDriveTo(0.35, 3.223))
							break;
						case 9:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 10:
							if(drive.simpleDriveTo(0.35, 1)) nextStep();
							//activate intake
							break;
						case 11:
							//pickup
							nextStep();
							break;
						case 12:
							if(drive.simpleDriveTo(-0.35, -1)) nextStep();
							break;
						case 13:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 14:
							if(drive.simpleDriveTo(0.35, 3.223))
							break;
						case 15:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 16:
							if(drive.simpleDriveTo(0.35, 9.97)) nextStep();
							break;
						case 17:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 18:
							if(drive.simpleDriveTo(0.35, 1)) nextStep();
							break;
						case 19:
							//dropoff scale 2
							nextStep();
							break;
						case 20:
							if(drive.simpleDriveTo(-0.35, -1)) nextStep();
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
							if(drive.simpleDriveTo(0.35, 15)) nextStep();
							break;
						case 1:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 2:
							if(drive.simpleDriveTo(0.35, 22.5616)) nextStep();
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
							if(drive.simpleDriveTo(0.35, 1))
							break;
						case 7:
							//dropoff scale 1
							break;
						case 8:
							if(drive.simpleDriveTo(-0.35, -1)) nextStep();
							break;
						case 9:
							if(navDrive.turnTo(-90)) nextStep();
							//lower lift
							break;
						case 10:
							if(drive.simpleDriveTo(0.35, 9.97)) nextStep();
							break;
						case 11:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 12:
							if(drive.simpleDriveTo(0.35, 7.015)) nextStep();
							break;
						case 13:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 14:
							if(drive.simpleDriveTo(0.35, 1)) nextStep();
							//activate intake
							break;
						case 15:
							//pickup
							nextStep();
							break;
						case 16:
							if(drive.simpleDriveTo(-0.35, -1)) nextStep();
							break;
						case 17:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 18:
							if(drive.simpleDriveTo(0.35, 7.015)) nextStep();
							break;
						case 20:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 21:
							if(drive.simpleDriveTo(0.35, 9.97)) nextStep();
							break;
						case 22:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 23:
							if(drive.simpleDriveTo(0.35, 1)) nextStep();
							break;
						case 24:
							//dropoff scale 2
							nextStep();
							break;
						case 25:
							if(drive.simpleDriveTo(-0.35, -1)) nextStep();
							break;
						case 26:
							drive.setDrive(0, 0, false);
							break;
						}
					}
				}*/
				break;
				
			case "Middle":
				switch (sides.charAt(0)) {
				case 'L':
					switch (step) {
					case 0:
						//if (liftUp() && middleL.run()) step++;
						if (middleL.run()) step++;
						break;
					case 1:
						//if (deploy()) step++;
						step++;
						break;
					}
					break;
				case 'R':
					switch (step) {
					case 0:
						//if (liftUp() && middleR.run()) step++;
						if (middleR.run()) step++;
						break;
					case 1:
						//if (deploy()) step++;
						step++;
						break;
					}
					break;
				}
				break;
			case "Right":
				if (!done) {
					if (drive.driveTo(8.0)) done = true;
				}
				/*switch (sides.charAt(0)) {
				case 'L':
					switch (step) {
					case 0:
						if (liftUp() && rightL.run()) step++;
						break;
					case 1:
						if (deploy()) step++;
						break;
					}
					break;
				case 'R':
					switch (step) {
					case 0:
						if (liftUp() && rightL.run()) step++;
						break;
					case 1:
						if (deploy()) step++;
						break;
					}
					break;
				}*/
				/*if (sides.charAt(0) == 'R'){
					switch(step){
					case 0:
						if(drive.simpleDriveTo(0.35, 12.375)) nextStep();
							//lift arm
						break;
					case 1:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 2:
						if(drive.simpleDriveTo(0.35, 4.63)) nextStep();
						break;
					case 3:
						//dropoff switch
						nextStep();
						break;
					case 4:
						if(drive.simpleDriveTo(-0.35, -1)) nextStep();
						break;
					case 5:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 6:
						if(drive.simpleDriveTo(0.35, (10/3))) nextStep();
						break;
					case 7:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 8:
						if(drive.simpleDriveTo(0.35, 1.5416)) nextStep();
						break;
					case 9:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 10:
						//activate intake
						if(drive.simpleDriveTo(0.35, (7/3))) nextStep();		
						break;
					case 11:
						//pickup and lift
						nextStep();
						break;
					case 12:
						if(drive.simpleDriveTo(-0.35, -(7/3))){
							nextStep();
							if(sides.charAt(1) == 'L') step = 22;
						}
						break;
					case 13:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 14:
						if(drive.simpleDriveTo(0.35, 2.6816)) nextStep();
						break;
					case 15:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 16:
						if(drive.simpleDriveTo(0.35, 9.97)) nextStep();
						break;
					case 17:
						if(navDrive.turnTo(-90)) nextStep();
						break;
					case 18:
						if(drive.simpleDriveTo(0.35, 1)) nextStep();
						break;
					case 19:
						//dropoff scale
						nextStep();
						break;
					case 20:
						if(drive.simpleDriveTo(-0.35, -1)) nextStep();
						break;
					case 21:
						drive.setDrive(0, 0, false);
						break;
					case 22:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 23:
						if(drive.simpleDriveTo(0.35, 19.0716)) nextStep();
						break;
					case 24:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 25:
						if(drive.simpleDriveTo(0.35, 9.97)) nextStep();
						break;
					case 26:
						if(navDrive.turnTo(90)) nextStep();
						break;
					case 27:
						if(drive.simpleDriveTo(0.35, 1)) nextStep();
						break;
					case 28:
						//dropoff scale
						nextStep();
						break;
					case 29:
						if(drive.simpleDriveTo(-0.35, -1)) nextStep();
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
							if(drive.simpleDriveTo(0.35, 27.304)) nextStep();
							//raise lift
							break;
						case 1:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 2:
							if(drive.simpleDriveTo(0.35, 3.49)) nextStep();
							break;
						case 3:
							// dropoff scale 1
							nextStep();
							break;
						case 4:
							if(drive.simpleDriveTo(-0.35, -1)) nextStep();
							//lower lift
							break;
						case 5:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 6:
							if(drive.simpleDriveTo(0.35, 9.97)) nextStep();
							break;
						case 7:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 8:
							if(drive.simpleDriveTo(0.35, 3.223))
							break;
						case 9:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 10:
							if(drive.simpleDriveTo(0.35, 1)) nextStep();
							//activate intake
							break;
						case 11:
							//pickup
							nextStep();
							break;
						case 12:
							if(drive.simpleDriveTo(-0.35, -1)) nextStep();
							break;
						case 13:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 14:
							if(drive.simpleDriveTo(0.35, 3.223))
							break;
						case 15:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 16:
							if(drive.simpleDriveTo(0.35, 9.97)) nextStep();
							break;
						case 17:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 18:
							if(drive.simpleDriveTo(0.35, 1)) nextStep();
							break;
						case 19:
							//dropoff scale 2
							nextStep();
							break;
						case 20:
							if(drive.simpleDriveTo(-0.35, -1)) nextStep();
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
							if(drive.simpleDriveTo(0.35, 15)) nextStep();
							break;
						case 1:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 2:
							if(drive.simpleDriveTo(0.35, 22.5616)) nextStep();
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
							if(drive.simpleDriveTo(0.35, 1))
							break;
						case 7:
							//dropoff scale 1
							break;
						case 8:
							if(drive.simpleDriveTo(-0.35, -1)) nextStep();
							break;
						case 9:
							if(navDrive.turnTo(90)) nextStep();
							//lower lift
							break;
						case 10:
							if(drive.simpleDriveTo(0.35, 9.97)) nextStep();
							break;
						case 11:
							if(navDrive.turnTo(-90)) nextStep();
							break;
						case 12:
							if(drive.simpleDriveTo(0.35, 7.015)) nextStep();
							break;
						case 13:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 14:
							if(drive.simpleDriveTo(0.35, 1)) nextStep();
							//activate intake
							break;
						case 15:
							//pickup
							nextStep();
							break;
						case 16:
							if(drive.simpleDriveTo(-0.35, -1)) nextStep();
							break;
						case 17:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 18:
							if(drive.simpleDriveTo(0.35, 7.015)) nextStep();
							break;
						case 20:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 21:
							if(drive.simpleDriveTo(0.35, 9.97)) nextStep();
							break;
						case 22:
							if(navDrive.turnTo(90)) nextStep();
							break;
						case 23:
							if(drive.simpleDriveTo(0.35, 1)) nextStep();
							break;
						case 24:
							//dropoff scale 2
							nextStep();
							break;
						case 25:
							if(drive.simpleDriveTo(-0.35, -1)) nextStep();
							break;
						case 26:
							drive.setDrive(0, 0, false);
							break;
						}
					}
				}
				*/
				break;
			}
	}
	
	@Override
	public void teleopInit() {
		resetLift();
		time.start();
		drive.zeroLeftDist();
	}
	
	@Override
	public void teleopPeriodic() {
		System.out.println(tiltEncoder.getDistance());
		
		lift.set(jSide.getY()+0.2);
		
		if(jSide.getPOV() == 0) lift2.set(ControlMode.PercentOutput, 0.8);
		else if(jSide.getPOV() == 180) lift2.set(ControlMode.PercentOutput, -0.8);
		else lift2.set(ControlMode.PercentOutput, 0);
		
		if(jSide.getRawButton(5)) clamp.set(-.8);
		else if (jSide.getRawButton(6)) clamp.set(.8);
		else clamp.set(0);
		
		if(jSide.getRawButton(2)) intake.set(1.0);
		else if(jSide.getRawButton(1)) intake.set(-1.0);
		else intake.set(0);
		
		if(jSide.getRawButton(3)) tilt.set(-.4);
		else if(jSide.getRawButton(4)) tilt.set(.4);
		else tilt.set(-.005);
		//System.out.println(rpi.readString());
		drive.setDrive(-jLeft.getY(), -jRight.getY(), false);
		/*if (Esplora1.getRawButton(2)) {
			drive.singleJoystickDrive(-Esplora2.getY(), -Esplora2.getX(), false);
		} else {
			drive.singleJoystickDrive(0.0, 0.0, false);
		}*/
		
		//if(jSide.getRawButton(1)) release.set(DoubleSolenoid.Value.kForward);
		//else release.set(DoubleSolenoid.Value.kReverse);
	}
	
	@Override
	public void testPeriodic() {
	}
	
	public void nextStep(){
		navDrive.reset();
		drive.reset();
		step++;
	}
	
	public boolean liftUp() {
		double timeToLift = 0.6;
		if (liftTime.get() >= timeToLift) {
			lift.set(0.2);
			return setTilt(0.25);
		} else {
			lift.set(0.8);
			tilt.set(0.0);
		}
		return false;
	}
	
	public boolean deploy() {
		double deployDist = 12.0; //inches
		lift.set(0.2);
		if ((sonar.getVoltage()*512.0)/5.0 <= deployDist) {
			if (liftTime.get() >= 1.0) {
				intake.set(0.0);
				return true;
			} else intake.set(1.0);
		} else {
			drive.setDrive(0.5, 0.5, false);
			intake.set(-0.2);
			liftTime.reset();
		}
		return false;
	}
	
	public boolean setTilt(double toVal) {
		double margin = 0.02;
		double motorVal = etc.constrain((toVal-tiltEncoder.getDistancePerPulse()), -1.0, 1.0);
		tilt.set(motorVal);
		if (Math.abs(motorVal) <= margin) {
			tilt.set(0.1);
			return true;
		}
		return false;
	}
	
	public void liftInit() {
		liftTime.reset();
		liftTime.start();
		tiltEncoder.reset();
	}
	
	public void resetLift() {
		liftTime.reset();
	}
}
