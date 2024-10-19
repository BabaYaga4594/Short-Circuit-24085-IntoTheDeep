package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

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
    ElapsedTime timer = new ElapsedTime();
    final double ARM_TICKS_PER_DEGREE =
            28 // number of encoder ticks per rotation of the bare motor
                    * 250047.0 / 4913.0 // This is the exact gear ratio of the 50.9:1 Yellow Jacket gearbox
                    * 100.0 / 20.0 // This is the external gear reduction, a 20T pinion gear that drives a 100T hub-mount gear
                    * 1/360.0; // we want ticks per degree, not per rotation

    final double ARM_COLLAPSED_INTO_ROBOT  = 0;
    final double ARM_COLLECT               = 250 * ARM_TICKS_PER_DEGREE;
    final double ARM_CLEAR_BARRIER         = 230 * ARM_TICKS_PER_DEGREE;
    final double ARM_SCORE_SPECIMEN        = 160 * ARM_TICKS_PER_DEGREE;
    final double ARM_SCORE_SAMPLE_IN_LOW   = 160 * ARM_TICKS_PER_DEGREE;
    final double ARM_ATTACH_HANGING_HOOK   = 120 * ARM_TICKS_PER_DEGREE;
    final double ARM_WINCH_ROBOT           = 15  * ARM_TICKS_PER_DEGREE;

    /* Variables to store the speed the intake servo should be set at to intake, and deposit game elements. */
    final double INTAKE_COLLECT    = -1.0;
    final double INTAKE_OFF        =  0.0;
    final double INTAKE_DEPOSIT    =  0.5;

    /* Variables to store the positions that the wrist should be set to when folding in, or folding out. */
    final double WRIST_FOLDED_IN   = 0.8333;
    final double WRIST_FOLDED_OUT  = 0.5;

    /* A number in degrees that the triggers can adjust the arm position by */
    final double FUDGE_FACTOR = 15 * ARM_TICKS_PER_DEGREE;

    /* Variables that are used to set the arm to a specific position */
    double armPosition = (int)ARM_COLLAPSED_INTO_ROBOT;
    double armPositionFudgeFactor;

    private final int EXTEND_POSITION_SLIDE = 1000;  // Change value based on your slide's range
    private final int RETRACT_POSITION_SLIDE = 0;

    // Motor power settings
    private final double POWER_UP_SLIDE = 0.8;
    private final double POWER_DOWN_SLIDE = -0.8;

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
        wristServo.setPosition(WRIST_FOLDED_IN);

        armSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("Robot Ready.");
        telemetry.update();

    }

    @Override
    public void loop() {
        driveMechanum(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        if (gamepad1.y) {
            armSlide.setTargetPosition(EXTEND_POSITION_SLIDE);
            armSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armSlide.setPower(POWER_UP_SLIDE);
        }
        // Retract the slide when the 'A' button is pressed
        else if (gamepad1.a) {
            armSlide.setTargetPosition(RETRACT_POSITION_SLIDE);
            armSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armSlide.setPower(POWER_DOWN_SLIDE);
        }
        // If no button is pressed, stop the motor
        else {
            armSlide.setPower(0);
            armSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // Return to manual control
        }

        if (gamepad1.a) {
            intakeServo.setPower(INTAKE_COLLECT);
        }
        else if (gamepad1.x) {
            intakeServo.setPower(INTAKE_OFF);
        }
        else if (gamepad1.b) {
            intakeServo.setPower(INTAKE_DEPOSIT);
        }

        if(gamepad1.right_bumper){
            /* This is the intaking/collecting arm position */
            armPosition = ARM_COLLECT;
            wristServo.setPosition(WRIST_FOLDED_OUT);
            intakeServo.setPower(INTAKE_COLLECT);
        }

        else if (gamepad1.left_bumper){
                    /* This is about 20° up from the collecting position to clear the barrier
                    Note here that we don't set the wrist position or the intake power when we
                    select this "mode", this means that the intake and wrist will continue what
                    they were doing before we clicked left bumper. */
            armPosition = ARM_CLEAR_BARRIER;
        }

        else if (gamepad1.y){
            /* This is the correct height to score the sample in the LOW BASKET */
            armPosition = ARM_SCORE_SAMPLE_IN_LOW;
        }

        else if (gamepad1.dpad_left) {
                    /* This turns off the intake, folds in the wrist, and moves the arm
                    back to folded inside the robot. This is also the starting configuration */
            armPosition = ARM_COLLAPSED_INTO_ROBOT;
            intakeServo.setPower(INTAKE_OFF);
            wristServo.setPosition(WRIST_FOLDED_IN);
        }

        else if (gamepad1.dpad_right){
            /* This is the correct height to score SPECIMEN on the HIGH CHAMBER */
            armPosition = ARM_SCORE_SPECIMEN;
            wristServo.setPosition(WRIST_FOLDED_IN);
        }

        else if (gamepad1.dpad_up){
            /* This sets the arm to vertical to hook onto the LOW RUNG for hanging */
            armPosition = ARM_ATTACH_HANGING_HOOK;
            intakeServo.setPower(INTAKE_OFF);
            wristServo.setPosition(WRIST_FOLDED_IN);
        }

        else if (gamepad1.dpad_down){
            /* this moves the arm down to lift the robot up once it has been hooked */
            armPosition = ARM_WINCH_ROBOT;
            intakeServo.setPower(INTAKE_OFF);
            wristServo.setPosition(WRIST_FOLDED_IN);
        }


            /* Here we create a "fudge factor" for the arm position.
            This allows you to adjust (or "fudge") the arm position slightly with the gamepad triggers.
            We want the left trigger to move the arm up, and right trigger to move the arm down.
            So we add the right trigger's variable to the inverse of the left trigger. If you pull
            both triggers an equal amount, they cancel and leave the arm at zero. But if one is larger
            than the other, it "wins out". This variable is then multiplied by our FUDGE_FACTOR.
            The FUDGE_FACTOR is the number of degrees that we can adjust the arm by with this function. */

        armPositionFudgeFactor = FUDGE_FACTOR * (gamepad1.right_trigger + (-gamepad1.left_trigger));


            /* Here we set the target position of our arm to match the variable that was selected
            by the driver.
            We also set the target velocity (speed) the motor runs at, and use setMode to run it.*/
        armMotor.setTargetPosition((int) (armPosition + armPositionFudgeFactor));

        ((DcMotorEx) armMotor).setVelocity(2100);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("armTarget: ", armMotor.getTargetPosition());
        telemetry.addData("arm Encoder: ", armMotor.getCurrentPosition());
        telemetry.addData("Target Position", armSlide.getTargetPosition());
        telemetry.addData("Current Position", armSlide.getCurrentPosition());
        telemetry.update();
    }
}