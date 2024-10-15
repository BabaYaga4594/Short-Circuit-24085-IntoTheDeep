package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name = "ScrimmageBot")
public class ScrimmageBotTeleOp extends OpMode {
    double drive, turn, strafe, servoPosition;
    double flpower, frpower, blpower, brpower;
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    DcMotor armSlide;
    DcMotor armMotor;
    CRServo intakeServo;
    Servo sideToSideServo;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        // find the name on driver hub for this
        armSlide = hardwareMap.get(DcMotor.class, "arm slide");

        // get some names on the driver hub for this
        intakeServo = hardwareMap.get(CRServo.class, "intakeServo");
        sideToSideServo = hardwareMap.get(Servo.class, "sidetoSideServo");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        armSlide.setDirection(DcMotorSimple.Direction.FORWARD);
        sideToSideServo.setDirection(Servo.Direction.FORWARD);
        armMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    @Override
    public void loop() {
        drive = gamepad1.left_stick_y * -1;
        turn = gamepad1.right_stick_x;
        strafe = gamepad1.left_stick_x;

        flpower = drive + turn  + strafe;
        frpower = drive - turn - strafe;
        blpower = drive + turn - strafe;
        brpower = drive - turn + strafe;

        double[] appliedPowers = scalePowers(flpower, frpower, blpower, brpower);

        frontLeft.setPower(appliedPowers[0]);
        frontRight.setPower(appliedPowers[1]);
        backLeft.setPower(appliedPowers[2]);
        backRight.setPower(appliedPowers[3]);

        //arm slide

        if (gamepad2.dpad_up) {
            armSlide.setTargetPosition(100);
            armSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        else if (gamepad2.dpad_down) {
            armSlide.setTargetPosition(-100);
            armSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        //arm motor
        if (gamepad1.dpad_up) {
            armMotor.setTargetPosition(100);
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        else if (gamepad1.dpad_down) {
            armMotor.setTargetPosition(-100);
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        //side to side motor
        if (gamepad1.x) {
            double servoPosition = sideToSideServo.getPosition();
            servoPosition += 0.05;
            sideToSideServo.setPosition(servoPosition);
        }
        if (gamepad1.b) {
            double servoPosition = sideToSideServo.getPosition();
            servoPosition -= 0.05;
            sideToSideServo.setPosition(servoPosition);
        }

        // CRServo which is the intake servo
        intakeServo.setPower(0);

        while (gamepad1.y) {
            intakeServo.setPower(0.5);
        }
        while (gamepad1.a) {
            intakeServo.setPower(-0.5);
        }

    }

    public double[] scalePowers(double flpower, double frpower, double blpower, double brpower){
        double max = Math.max(Math.abs(flpower), Math.max(Math.abs(frpower), Math.max(Math.abs(blpower),Math.abs(brpower))));
        if(max > 1){
            flpower /= max;
            frpower /= max;
            blpower /= max;
            brpower /= max;
        }

        double[] motorPowers = new double[]{flpower, frpower, blpower, brpower};
        return motorPowers;
    }
}