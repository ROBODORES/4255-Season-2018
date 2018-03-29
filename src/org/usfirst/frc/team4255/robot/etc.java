package org.usfirst.frc.team4255.robot;

public class etc {
	public static double[][] middleL = {
		{2.0, 0.0},
		{0.0, -45.0},
		{6.25, 0.0},
		{0, 45.0},
	};
	
	public static double[][] middleR = {
		{2.42, 0.0},
		{0.0, 45.0},
		{5.07, 0.0},
		{0, -45.0},
	};
	
	public static double map(double val, double fromMin, double fromMax, double toMin, double toMax) {
		double output = ((val-fromMin)*((toMax-toMin)/(fromMax-fromMin)))+toMin;
		return output;
	}
	
	public static double constrain(double val, double min, double max) {
		if (val > max) val = max;
		if (val < min) val = min;
		return val;
	}
}
