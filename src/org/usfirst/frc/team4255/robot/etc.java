package org.usfirst.frc.team4255.robot;

public class etc {
	public static double[][] middleL = {
		{2.0, 0.0},
		{0.0, -45.0},
		{6.25, 0.0},
		{0.0, 45.0},
	};
	
	public static double[][] middleR = {
		{2.42, 0.0}, //raise lift
		{0.0, 45.0},
		{5.07, 0.0},
		{0.0, -45.0}, //sensor and dropoff
	};
	public static double[][] leftL = {
		{12.38, 0.0}, //raise lift
		{0.0, 90.0} //dropoff switch
	};
	public static double[][] leftR = {
		{17.44, 0.0}, //raise lift
		{0.0, 90.0},
		{13.72, 0.0},
		{0.0, 90.0} //dropoff switch
	};
	public static double[][] rightR = {
		{12.38, 0.0}, //raise lift
		{0.0, -90.0} //dropoff switch
	};
	public static double[][] rightL = {
		{17.44, 0.0}, //raise lift
		{0.0, -90.0},
		{13.72, 0.0},
		{0.0, -90.0} //dropoff switch
	};
	public static double[][] leftLL = {
		{12.38, 0.0}, //raise lift
		{0.0, 90.0},
		{1.59, 0.0}, //dropoff switch
		{-1.0, 0.0},
		{0.0, -90.0},
		{6.36, 0.0},
		{0.0, 90.0},
		{3.17, 0.0},
		{0.0, 90.0},
		{1.58, 0.0}, //with intake activated -> pickup cube
		{-1.0, 0.0},
		{0.0, 90.0},
		{3.79, 0.0},
		{0.0, 90.0},
		{0.5, 0.0}, //dropoff scale
		{-1.66, 0.0}
	};
	public static double[][] leftLR = {
		{12.38, 0.0}, //raise lift
		{0.0, 90.0},
		{1.59, 0.0}, //dropoff switch
		{-1.0, 0.0},
		{0.0, -90.0},
		{6.36, 0.0},
		{0.0, 90.0},
		{3.17, 0.0},
		{0.0, 90.0},
		{1.58, 0.0}, //with intake activated -> pickup cube
		{-1.0, 0.0},
		{0.0, -90.0},
		{11.82, 0.0},
		{0.0, -90.0},
		{6.81, 0.0}, //dropoff scale
		{-1.66, 0.0}
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
