package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class RobotHardwareFinal {
    // Later uses for this
    // Mecanum Drive
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    // coaxial virtual four bar
    Servo leftCoaxialFourBar;
    Servo rightCoaxialFourBar;
    Servo rotateIntakeServo;
    Servo intakeServo;

    //Specimen
    Servo specimenServo;

    public void init(HardwareMap hwMap) {
        frontLeft = hwMap.get(DcMotor.class, "frontLeft");
        frontRight = hwMap.get(DcMotor.class, "frontRight");
        backLeft = hwMap.get(DcMotor.class, "backLeft");
        backRight = hwMap.get(DcMotor.class, "backRight");
    }

}
