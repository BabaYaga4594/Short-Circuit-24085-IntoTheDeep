package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class ZeroServos extends OpMode {

    Servo claw;
    Servo armServo;

    @Override
    public void init() {

        armServo = hardwareMap.get(Servo.class, "claw");
        claw = hardwareMap.get(Servo.class, "armServo");

        armServo.setPosition(0);
        claw.setPosition(0);

    }

    @Override
    public void loop() {

    }
}
