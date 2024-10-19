package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "wristServoZero")
public class ZeroServos extends OpMode {

    //Servo claw;
    Servo wristServo;

    @Override
    public void init() {

        wristServo = hardwareMap.get(Servo.class, "wristServo");
        //claw = hardwareMap.get(Servo.class, "armServo");

        //claw.setPosition(0)
        wristServo.setPosition(0);
    }

    @Override
    public void loop() {
        if(gamepad1.a) {
            wristServo.setPosition(0);
        }
    }
}
