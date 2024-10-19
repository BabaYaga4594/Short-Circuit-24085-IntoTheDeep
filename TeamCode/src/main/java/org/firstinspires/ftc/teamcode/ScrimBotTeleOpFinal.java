package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ScrimBotTeleOpFinal")
public class ScrimBotTeleOpFinal extends OpMode {

    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor backRight;
    DcMotor armSlide;
    DcMotor armMotor;
    CRServo intakeServo;
    Servo wristServo;

    /* Variables to store the speed the intake servo should be set at to intake, and deposit game elements. */
    final double INTAKE_COLLECT    = -1.0;
    final double INTAKE_OFF        =  0.0;
    final double INTAKE_DEPOSIT    =  0.5;

    final double WRIST_ON_SIDE = 0.8333;
    final double WRIST_EXTENDING_OUT = 0.5;

    int[] maxPositions = {4200}; // potentially 4300

    int[] minPositions = {0};

    double slideSpeed = 0.6;

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

        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        armSlide = hardwareMap.get(DcMotor.class, "armSlide");

        intakeServo = hardwareMap.get(CRServo.class, "intakeServo");
        wristServo = hardwareMap.get(Servo.class, "wristServo");

        armMotor.setTargetPosition(0);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        intakeServo.setPower(INTAKE_OFF);
        wristServo.setPosition(WRIST_ON_SIDE);

        armSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("Robot Ready.");
        telemetry.update();

    }

    @Override
    public void loop() {
        driveMechanum(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        //intake servo
        if (gamepad1.a) {
            intakeServo.setPower(INTAKE_COLLECT);
        }
        else if (gamepad1.x) {
            intakeServo.setPower(INTAKE_OFF);
        }
        else if (gamepad1.b) {
            intakeServo.setPower(INTAKE_DEPOSIT);
        }

        // wrist servo
        double servoPosition = wristServo.getPosition();
        if (gamepad1.x) {
            servoPosition += 0.05;
            wristServo.setPosition(servoPosition);
        }
        else if (gamepad1.b) {
            servoPosition -= 0.05;
            wristServo.setPosition(servoPosition);
        }

        // arm motor
        int ArmPos = armMotor.getCurrentPosition();

        if(gamepad2.dpad_up) {
            ArmPos += 100;
        }
        else if(gamepad2.dpad_down) {
            ArmPos -= 100;
        }

        armMotor.setTargetPosition(ArmPos);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(slideSpeed);

        // arm slide
        int slidePos = armSlide.getCurrentPosition();

        if(gamepad2.y && slidePos <= maxPositions[0]) {
            slidePos += 100;
        }
        else if(gamepad2.a && slidePos >= minPositions[0]) {
            slidePos -= 100;
        }

        armSlide.setTargetPosition(slidePos);
        armSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armSlide.setPower(slideSpeed);

        telemetry.addData("armTarget: ", armMotor.getTargetPosition());
        telemetry.addData("arm Encoder: ", armMotor.getCurrentPosition());
        telemetry.addData("Target Position", armSlide.getTargetPosition());
        telemetry.addData("Current Position", armSlide.getCurrentPosition());
        telemetry.update();
    }
}