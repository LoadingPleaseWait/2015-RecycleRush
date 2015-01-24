package org.discobots.recyclerush.subsystems;

import org.discobots.recyclerush.HW;
import org.discobots.recyclerush.commands.drive.CycleDriveCommand;
import org.discobots.recyclerush.commands.drive.StickDriveCommand;
import org.discobots.recyclerush.commands.drive.TankDriveCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveTrainSubsystem extends Subsystem {

	public enum Motor {
		FRONTLEFT, BACKLEFT, FRONTRIGHT, BACKRIGHT, CENTERDROPDOWN;
	}

	CANTalon frontLeft, backLeft, frontRight, backRight, centerDropDown;
	// switch to TalonSRX class if we use pwm instead.
	// with can the following values are available:
	// out curr, out volt, in volt, setpoint, temp, 

	RobotDrive robotDrive;

	DoubleSolenoid centerDropSolenoid;
	
	Encoder encoderForward;
	Encoder encoderSideway;

	Gyro gyroscope;

	public DriveTrainSubsystem() {
		frontLeft = new CANTalon(HW.motorFrontLeft);
		backLeft = new CANTalon(HW.motorBackLeft);
		frontRight = new CANTalon(HW.motorFrontRight);
		backRight = new CANTalon(HW.motorBackRight);
		centerDropDown = new CANTalon(HW.motorCenterDropDown);
		
		encoderForward = new Encoder(HW.encoderForwardA, HW.encoderForwardB, false, EncodingType.k4X);
		encoderSideway = new Encoder(HW.encoderSidewayA, HW.encoderSidewayB, false, EncodingType.k4X);
		resetEncoders();
		
		centerDropSolenoid = new DoubleSolenoid(HW.dsolCenterDropdownA, HW.dsolCenterDropdownB);
		
		gyroscope = new Gyro(HW.gyroscope);

		robotDrive = new RobotDrive(frontLeft, backLeft, frontRight, backRight);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, false);
		robotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, false);
		robotDrive.setSafetyEnabled(false);
	}

	public void tankDrive(double leftStick, double rightStick) {
		robotDrive.tankDrive(leftStick, -rightStick);
	}

	public void arcadeDrive(double y, double x) {
		robotDrive.arcadeDrive(x, -y); // robotdrive is dumb so params are switched
	}

	public void holonomicDrive(double y, double x, double r) { // h-drive
		robotDrive.arcadeDrive(r, -y); // robotdrive is dumb so params are switched
		centerDropDown.set(x);
	}

	public double getMotorSetpoint(Motor motor) {
		if (motor == Motor.BACKLEFT) {
			return this.backLeft.get();
		} else if (motor == Motor.BACKRIGHT) {
			return this.backRight.get();
		} else if (motor == Motor.FRONTLEFT) {
			return this.frontLeft.get();
		} else if (motor == Motor.FRONTRIGHT) {
			return this.frontRight.get();
		} else if (motor == Motor.CENTERDROPDOWN) {
			return this.centerDropDown.get();
		} else {
			return -9001;
		}
	}

	public double getMotorCurrent(Motor motor) {
		if (motor == Motor.BACKLEFT) {
			return this.backLeft.getOutputCurrent();
		} else if (motor == Motor.BACKRIGHT) {
			return this.backRight.getOutputCurrent();
		} else if (motor == Motor.FRONTLEFT) {
			return this.frontLeft.getOutputCurrent();
		} else if (motor == Motor.FRONTRIGHT) {
			return this.frontRight.getOutputCurrent();
		} else if (motor == Motor.CENTERDROPDOWN) {
			return this.centerDropDown.getOutputCurrent();
		} else {
			return -9001;
		}
	}
	
	public void resetEncoders() {
		encoderForward.reset();
		encoderSideway.reset();
	}
	
	public double getEncoderForwardDistance() {
		double encoderDistancePerCount = HW.wheelForwardCircumference / HW.encoderCountsPerRevolution;
		double output = encoderForward.getRaw() * encoderDistancePerCount;
		return output;
	}
	
	public double getEncoderSidewayDistance() {
		double encoderDistancePerCount = HW.wheelSidewayCircumference / HW.encoderCountsPerRevolution;
		double output = encoderSideway.getRaw() * encoderDistancePerCount;
		return output;
	}
	
	public double getGyroscopeAngle() {
		return gyroscope.getAngle();
	}

	public void initDefaultCommand() {
		setDefaultCommand(new TankDriveCommand());
	}
}
