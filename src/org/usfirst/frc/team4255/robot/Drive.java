package org.usfirst.frc.team4255.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;

public class Drive {
	public static TalonSRX leftDrive, rightDrive;
	private static long leftZero, rightZero;
	private double driveSpeed;
	
	public Drive(TalonSRX leftDrive, FeedbackDevice leftFeedback, TalonSRX rightDrive, FeedbackDevice rightFeedback) {
		this.leftDrive = leftDrive;
		this.rightDrive = rightDrive;
		leftZero = 0;
		rightZero = 0;
		driveSpeed = 0;
		leftDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 1, 10);
		leftDrive.configSelectedFeedbackSensor(leftFeedback, 0, 10);
		rightDrive.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 1, 10);
		rightDrive.configSelectedFeedbackSensor(rightFeedback, 0, 10);
	}
	
	public static void setDrive(double leftSpeed, double rightSpeed, boolean reverse) {
		if (reverse) {
			rightSpeed = -rightSpeed;
			leftSpeed = -leftSpeed;
		}
		
		leftDrive.set(ControlMode.PercentOutput, leftSpeed);
		rightDrive.set(ControlMode.PercentOutput, rightSpeed);
	}
	
	public static void singleJoystickDrive(double yVal, double xVal, boolean reverse) {
		double leftSpeed = yVal-xVal;
		if (leftSpeed > 1.0) leftSpeed = 1.0; if (leftSpeed < -1.0) leftSpeed = -1.0;
		double rightSpeed = yVal+xVal;
		if (leftSpeed > 1.0) leftSpeed = 1.0; if (leftSpeed < -1.0) leftSpeed = -1.0;
		
		if (reverse) {
			rightSpeed = -rightSpeed;
			leftSpeed = -leftSpeed;
		}
		
		leftDrive.set(ControlMode.PercentOutput, leftSpeed);
		rightDrive.set(ControlMode.PercentOutput, rightSpeed);
	}
	
	public static void zeroLeftDist() {leftZero = leftDrive.getSelectedSensorPosition(0);}
	
	public static void zeroRightDist() {rightZero = rightDrive.getSelectedSensorPosition(0);}
	
	public static double leftDist() { //returns traveled distance in feet
		return (double)(leftDrive.getSelectedSensorPosition(0)-leftZero)/4096.0*(6*Math.PI)/12.0;
	}
	
	public static double rightDist() { //returns traveled distance in feet
		return (double)(rightDrive.getSelectedSensorPosition(0)-rightZero)/4096.0*(6*Math.PI)/12.0;
	}
	
	public static double leftVelocity() { //returns velocity in feet/second
		return ((double)(leftDrive.getSelectedSensorVelocity(0)*10)/4096.0)*(6*Math.PI)/12.0;
	}
	
	public static double rightVelocity() { //returns velocity in feet/second
		return ((double)(rightDrive.getSelectedSensorVelocity(0)*10)/4096.0)*(6*Math.PI)/12.0;
	}
	
	void reset() {
		zeroLeftDist();
		driveSpeed = 0.0;
	}
	
	public boolean driveTo(double distance) { //don't run yet
		setDrive(driveSpeed, driveSpeed, false);
		double MAINTAINED_VELOCITY = 1.0; //ftps
		double DECELERATION_BUFFER = 1.5; //ft
		
		if (Math.abs(leftDist()) <= Math.abs(distance)- DECELERATION_BUFFER) {
			driveSpeed += (((MAINTAINED_VELOCITY/DECELERATION_BUFFER)*(distance-leftDist())) - leftVelocity())/1000.0;
			if (Math.abs(leftDist()) >= Math.abs(distance)) {
				setDrive(0.0, 0.0, false);
				return true;
			}
		}
		else {
			driveSpeed += (MAINTAINED_VELOCITY*(distance/Math.abs(distance)) - leftVelocity())/8000.0;
		}
		return false;
	}
}
