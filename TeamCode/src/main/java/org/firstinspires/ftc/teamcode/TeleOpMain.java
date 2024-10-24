package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp(name = "TeleOpMain")
public class TeleOpMain extends OpMode {
    RobotHardware robotHw = new RobotHardware();
    IMU imu;
    IMU.Parameters parameters;
    YawPitchRollAngles angles;
    double speed = 0.8;
    final double INTAKE_COLLECT    = -1.0;
    final double INTAKE_OFF        =  0.0;
    final double INTAKE_DEPOSIT    =  0.5;

    // CHANGE LATER
    final double SPECIMEN_COLLECT = 1.0;
    final double SPECIMEN_HOLD = 0;

    double DR4B_SPEED = 0.6;

    public void cattleDrive () {
        angles = imu.getRobotYawPitchRollAngles();
        double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
        double driveAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4 - angles.getYaw(AngleUnit.RADIANS);
        double rightX = gamepad1.right_stick_x;

        if (gamepad1.right_trigger > 0.75) {
            driveAngle = Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4;
        }

        final double FLPower = speed * (r * Math.cos(driveAngle) + rightX);
        final double BLPower = speed * (r * Math.sin(driveAngle) + rightX);
        final double FRPower = speed * (r * Math.sin(driveAngle) - rightX);
        final double BRPower = speed * (r * Math.cos(driveAngle) - rightX);

        robotHw.frontLeft.setPower(FLPower);
        robotHw.frontRight.setPower(FRPower);
        robotHw.backLeft.setPower(BLPower);
        robotHw.backRight.setPower(BRPower);

        if(gamepad1.left_trigger > 0.75) {
            speed = 0.9;

            robotHw.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robotHw.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robotHw.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robotHw.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
        else {
            speed = 0.4;

            robotHw.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robotHw.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robotHw.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robotHw.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    public void circuitDrive(double left_y, double left_x, double right_x){
        double maxPower = Math.max(Math.abs(left_y) + Math.abs(left_x) + Math.abs(right_x), 1);
        robotHw.frontLeft.setPower((left_y + left_x + right_x) / maxPower);
        robotHw.frontRight.setPower((left_y - left_x - right_x) / maxPower);
        robotHw.backLeft.setPower((left_y - left_x + right_x) / maxPower);
        robotHw.backRight.setPower((left_y + left_x - right_x) / maxPower);
    }

    public void moveIntakeServo () {
        if (gamepad2.a) {
            robotHw.intakeServo.setPower(INTAKE_COLLECT);
            telemetry.addLine("yuh");
        }
        else if (gamepad2.x) {
            robotHw.intakeServo.setPower(INTAKE_OFF);
        }
        else if (gamepad2.b) {
            robotHw.intakeServo.setPower(INTAKE_DEPOSIT);
        }
    }

    public void moveDR4BMotors () {
        int leftDR4BMotorPos = robotHw.leftDR4BMotor.getCurrentPosition();
        int rightDR4BMotorPos = robotHw.rightDR4BMotor.getCurrentPosition();

        if(gamepad2.dpad_up) {
            leftDR4BMotorPos += 50;
            rightDR4BMotorPos += 50;
        }
        else if(gamepad2.dpad_down) {
            leftDR4BMotorPos -= 50;
            rightDR4BMotorPos -= 50;
        }

        robotHw.leftDR4BMotor.setTargetPosition(leftDR4BMotorPos);
        robotHw.rightDR4BMotor.setTargetPosition(rightDR4BMotorPos);
        robotHw.leftDR4BMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHw.rightDR4BMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHw.leftDR4BMotor.setPower(DR4B_SPEED);
        robotHw.rightDR4BMotor.setPower(DR4B_SPEED);

        if (robotHw.leftDR4BMotor.getCurrentPosition() == leftDR4BMotorPos && robotHw.rightDR4BMotor.getCurrentPosition() == rightDR4BMotorPos) {
            robotHw.leftDR4BMotor.setPower(0);
            robotHw.rightDR4BMotor.setPower(0);
        }

    }

    public void moveSpecimenServo() {
        if (gamepad2.left_bumper) {
            robotHw.specimenServo.setPosition(SPECIMEN_COLLECT);
            telemetry.addLine("yuh");
        }
        else if (gamepad2.right_bumper) {
            robotHw.specimenServo.setPosition(SPECIMEN_HOLD);
        }

    }

    @Override
    public void init() {
        robotHw.init(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        // circuit drive for now
        circuitDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        moveIntakeServo();
        moveDR4BMotors();
        moveSpecimenServo();

    }
}
