package org.usfirst.frc.team5240.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CANTalon;
import com.usfirst.team5240.nav6.frc.IMUAdvanced;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
 



/**
 * @author JoshMiller
 * This is a demo program showing the use of the RobotDrive class.
 * The SampleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {
	
	 RobotDrive robotDrive;
	    Joystick stick;
	    CANTalon frontRight;
	    // Channels for the wheels
		CANTalon frontLeft;
		CANTalon rearLeft;
		CANTalon rearRight;
		
	    final int joystickChannel	= 0;
	    
	    //<Gyro code start
	    SerialPort serial_port;
	    //IMU imu;  // Alternatively, use IMUAdvanced for advanced features
	    IMUAdvanced imu;
	    boolean first_iteration;
	    //Gyro code stop

	    

    public Robot() {
    	 frontRight = new CANTalon(1);
    	 frontLeft = new CANTalon(4);
    	 rearRight = new CANTalon(2);
    	 rearLeft = new CANTalon(3);
         robotDrive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
        
        stick = new Joystick(joystickChannel);
        
        try {
        serial_port = new SerialPort(57600,SerialPort.Port.kMXP);
                
                // You can add a second parameter to modify the 
                // update rate (in hz) from 4 to 100.  The default is 100.
                // If you need to minimize CPU load, you can set it to a
                // lower value, as shown here, depending upon your needs.
                
                // You can also use the IMUAdvanced class for advanced
                // features.
                
                byte update_rate_hz = 50;
                //imu = new IMU(serial_port,update_rate_hz);
                imu = new IMUAdvanced(serial_port,update_rate_hz);
        } catch( Exception ex ) {
                
        }
        if ( imu != null ) {
            LiveWindow.addSensor("IMU", "Gyro", imu);
        }
        first_iteration = true;
    }
        
    

    public void autonomous() {
       
    }

    /**
     * Runs the motors with arcade steering.
     */
    public void operatorControl() {
        robotDrive.setSafetyEnabled(false);
        while (isOperatorControl() && isEnabled()) {
        	
        	//gyro code>>>>
        	// When calibration has completed, zero the yaw
            // Calibration is complete approaximately 20 seconds
            // after the robot is powered on.  During calibration,
            // the robot should be still
            
            boolean is_calibrating = imu.isCalibrating();
            if ( first_iteration && !is_calibrating ) {
                Timer.delay( 0.3 );
                imu.zeroYaw();
                first_iteration = false;
            }
            
            // Update the dashboard with status and orientation
            // data from the nav6 IMU
            
            SmartDashboard.putBoolean(  "IMU_Connected",        imu.isConnected());
            SmartDashboard.putBoolean(  "IMU_IsCalibrating",    imu.isCalibrating());
            SmartDashboard.putNumber(   "IMU_Yaw",              imu.getYaw());
            SmartDashboard.putNumber(   "IMU_Pitch",            imu.getPitch());
            SmartDashboard.putNumber(   "IMU_Roll",             imu.getRoll());
            SmartDashboard.putNumber(   "IMU_CompassHeading",   imu.getCompassHeading());
            SmartDashboard.putNumber(   "IMU_Update_Count",     imu.getUpdateCount());
            SmartDashboard.putNumber(   "IMU_Byte_Count",       imu.getByteCount());

            // If you are using the IMUAdvanced class, you can also access the following
            // additional functions, at the expense of some extra processing
            // that occurs on the CRio processor
            
            SmartDashboard.putNumber(   "IMU_Accel_X",          imu.getWorldLinearAccelX());
            SmartDashboard.putNumber(   "IMU_Accel_Y",          imu.getWorldLinearAccelY());
            SmartDashboard.putBoolean(  "IMU_IsMoving",         imu.isMoving());
            SmartDashboard.putNumber(   "IMU_Temp_C",           imu.getTempC());
            
            Timer.delay(0.2);
        }
        
     
    
    /**
     * This function is called once each time the robot enters test mode.
     */
        	//frontRight.set(stick.getY());
        	// Use the joystick X axis for lateral movement, Y axis for forward movement, and Z axis for rotation.
        	// This sample does not use field-oriented drive, so the gyro input is set to zero.
            robotDrive.mecanumDrive_Cartesian(stick.getRawAxis(3)-stick.getRawAxis(2),stick.getRawAxis(4), stick.getRawAxis(5), 0);
 
            Timer.delay(0.01);	// wait 5ms to avoid hogging CPU cycles
        }
           
    

    /**
     * Runs during test mode
     */
    public void test() {
    }


}