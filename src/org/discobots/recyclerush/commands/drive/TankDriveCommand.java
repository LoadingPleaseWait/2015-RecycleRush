package org.discobots.recyclerush.commands.drive;

import org.discobots.recyclerush.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class TankDriveCommand extends Command {

	public TankDriveCommand() {
		requires(Robot.driveTrainSub);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.driveTrainSub.tankDriveRamp(Robot.oi.getRawAnalogStickALY(),
				Robot.oi.getRawAnalogStickARY());
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.driveTrainSub.tankDriveUnramped(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
