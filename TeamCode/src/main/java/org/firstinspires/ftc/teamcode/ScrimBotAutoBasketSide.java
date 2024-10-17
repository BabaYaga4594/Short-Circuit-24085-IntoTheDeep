package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "ScrimBotAutoBlueBasket")
public class ScrimBotAutoBasketSide extends OpMode {

    ScrimBotAutoMethods auto = new ScrimBotAutoMethods();

    double forwardToHighRung = 24; // Distance to drive forward to the high rung (inches)

    int moveArmMotorToScoreSpecimen = 12; //encoder needed to move arm up for position
    double backwardDistance = 12; // Distance to drive backward slightly (inches)
    double turnLeft90 = 12; // Adjust as needed for turning
    double turnRight90 = 12; // Adjust as needed for turning
    double driveToYellowBlock = 36; // Distance to drive forward to the yellow blocks (inches)
    double strafeRightToPickup = 12; // Distance to strafe left to position for pickup (inches)
    double pickupTurn180 = 180; // Turn 180 degrees

    int moveArmMotorToScoreBlock = 12; // amount to move arm up to score block (encoder)
    int armExtendDistance = 12; // Distance to extend the arm to score (inches)
    double drivePower = 0.5; // Power for driving
    double armPower = 1.0; // Power for the arm


    @Override
    public void init() {
        auto.resetEncoders();
        auto.init(hardwareMap);
    }

    @Override
    public void loop() {

        /*methods usable for later
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
        //going to start near the left center near the basket to release specimen

        // 1. Drive forward to the high rung and release the preloaded sample
        auto.drive(forwardToHighRung, "FORWARD");
        auto.moveArmMotor(armPower, moveArmMotorToScoreSpecimen, "FORWARD");
        auto.releaseSampleSideToSideServo();

        // Repeat steps 2-8 for the remaining two yellow blocks

        // 2. Drive backwards slightly
        auto.drive(backwardDistance, "REVERSE");
        auto.moveArmMotor(armPower, moveArmMotorToScoreSpecimen, "REVERSE");

        // 3. Turn 90 degrees left
        //turnLeft(turnLeft90);

        // 4. Drive forward to the yellow blocks
        auto.drive(driveToYellowBlock, "FORWARD");

        // 5. Strafe right to position for pickup
        auto.strafe(strafeRightToPickup, "RIGHT");

        // 6. Pick up the yellow block
        auto.runIntake(2000, "IN");

        // 7. Turn 180 degrees
        //turn180(pickupTurn180);

        // 8. Extend the arm and score the block on the high rung
        auto.moveArmMotor(armPower, moveArmMotorToScoreBlock, "FORWARD");
        auto.extendArmSlide(armPower,armExtendDistance, "FORWARD");
        auto.runIntake(2000,"OUT");

        auto.stopMotors();

    }
    // Later
}
