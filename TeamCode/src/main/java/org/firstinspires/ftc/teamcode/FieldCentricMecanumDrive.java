package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name = "Field Centric Mecanum Drive", group = "TeleOp")
public class FieldCentricMecanumDrive extends OpMode {

    // Declare hardware variables
    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private BNO055IMU imu;
    private Orientation angles;

    @Override
    public void init() {
        // Initialize motors
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        // Set motor directions
        frontLeft.setDirection(DcMotor.Direction.FORWARD);  // Front left stays forward
        frontRight.setDirection(DcMotor.Direction.REVERSE); // Reverse direction for front right
        backLeft.setDirection(DcMotor.Direction.REVERSE);   // Reverse direction for back left
        backRight.setDirection(DcMotor.Direction.REVERSE);  // Reverse direction for back right

        // Initialize IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    @Override
    public void loop() {
        // Get joystick values
        double y = -gamepad1.left_stick_y; // Remember, y is reversed!
        double x = gamepad1.left_stick_x * 1.1; // Adjust for strafing imbalance
        double rotate = gamepad1.right_stick_x;

        // Get the robot's heading in radians
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
        double robotHeading = angles.firstAngle;

        // Calculate field-centric x and y values
        double xField = x * Math.cos(robotHeading) - y * Math.sin(robotHeading);
        double yField = x * Math.sin(robotHeading) + y * Math.cos(robotHeading);

        // Calculate motor power for field-centric control
        double frontLeftPower = yField + xField + rotate;
        double frontRightPower = yField - xField - rotate;
        double backLeftPower = yField - xField + rotate;
        double backRightPower = yField + xField - rotate;

        // Normalize the values so no value exceeds 1.0
        double max = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(frontRightPower), Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))));
        if (max > 1.0) {
            frontLeftPower /= max;
            frontRightPower /= max;
            backLeftPower /= max;
            backRightPower /= max;
        }

        // Set motor powers
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);
    }
}