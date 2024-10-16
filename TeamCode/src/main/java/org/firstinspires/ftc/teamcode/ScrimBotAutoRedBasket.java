package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "ScrimmageBotAutonomous")
public class ScrimBotAutoRedBasket extends OpMode {
    ScrimBotAutoMethods auto = new ScrimBotAutoMethods();
    double forwardDistance = 30; // Distance to move forward to the submersible
    double backwardDistance = 30; // Distance to move backward
    double strafeDistance = 20; // Distance to strafe to the right
    double armLiftAmount = 1000; // amount of encoder value to put in
    double armLowerAmount = 1000; // amount of encoder value to put in
    double intakeTime = 2000; // Time to run the intake (milliseconds)

    @Override
    public void init() {
        auto.init(hardwareMap);
        auto.resetEncoders();

    }

    @Override
    public void loop() {
        /*methods usable
        reset encoders
        stop motors
        set motor powers
        drive (forward or reverse)
        strafe (left or right)
        set target position
        move arm slide
        run intake
        move arm motor
         */

        auto.stopMotors();
    }
}
