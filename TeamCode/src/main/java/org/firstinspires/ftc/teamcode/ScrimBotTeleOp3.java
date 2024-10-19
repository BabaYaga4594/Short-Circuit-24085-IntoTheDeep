package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "ScrimBotTeleOp3")
public class ScrimBotTeleOp3 extends OpMode {

    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor backRight;
    DcMotor armSlide;
    CRServo intakeServo;
    Servo wristServo;
    ElapsedTime timer = new ElapsedTime();
    final double INTAKE_COLLECT    = -1.0;
    final double INTAKE_OFF        =  0.0;
    final double INTAKE_DEPOSIT    =  0.5;

    public void driveMechanum(double left_y, double left_x, double right_x){
        double maxPower = Math.max(Math.abs(left_y) + Math.abs(left_x) + Math.abs(right_x), 1);
        frontLeft.setPower((left_y + left_x + right_x) / maxPower);
        frontRight.setPower((left_y - left_x - right_x) / maxPower);
        backLeft.setPower((left_y - left_x + right_x) / maxPower);
        backRight.setPower((left_y + left_x - right_x) / maxPower);
    }

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        armSlide = hardwareMap.get(DcMotor.class, "armSlide");

        intakeServo = hardwareMap.get(CRServo.class, "intakeServo");
        wristServo = hardwareMap.get(Servo.class, "wristServo");

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    @Override
    public void loop() {
        driveMechanum(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        // arm slide
        if (gamepad2.dpad_up) {
            int oldPosition = armSlide.getCurrentPosition();
            armSlide.setTargetPosition(oldPosition + 100);
            armSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armSlide.setPower(0.5);
            if (timer.milliseconds() >= 3000) {
                armSlide.setPower(0.0);
            }
        } else if (gamepad2.dpad_down) {
            int oldPosition = armSlide.getCurrentPosition();
            armSlide.setTargetPosition(oldPosition-100);
            armSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armSlide.setPower(0.5);
            if (timer.milliseconds() >= 3000) {
                armSlide.setPower(0.0);
            }
        }

        // arm motor


        if (gamepad1.x) {
            double servoPosition = wristServo.getPosition();
            servoPosition += 0.05;
            wristServo.setPosition(servoPosition);
        }
        if (gamepad1.b) {
            double servoPosition = wristServo.getPosition();
            servoPosition -= 0.05;
            wristServo.setPosition(servoPosition);
        }

        while (gamepad1.y) {
            intakeServo.setPower(0.5);
        }
        if (gamepad1.y == false) {
            intakeServo.setPower(0);
        }
        while (gamepad1.a) {
            intakeServo.setPower(-0.5);
        }
        if (gamepad1.a == false) {
            intakeServo.setPower(0);
        }

    }
}
