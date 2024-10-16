package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "ScrimBotAutoBlueBasket")
public class ScrimBotAutoBlueBasket extends OpMode {

    ScrimBotAutoMethods auto = new ScrimBotAutoMethods();

    double forwardToHighRung = 24; // Distance to drive forward to the high rung (inches)
    double backwardDistance = 12; // Distance to drive backward slightly (inches)
    double turnLeft90 = 12; // Adjust as needed for turning
    double turnRight90 = 12; // Adjust as needed for turning
    double driveToYellowBlock = 36; // Distance to drive forward to the yellow blocks (inches)
    double strafeLeftToPickup = 12; // Distance to strafe left to position for pickup (inches)
    double pickupTurn180 = 180; // Turn 180 degrees
    double armExtendDistance = 12; // Distance to extend the arm to score (inches)
    double drivePower = 0.5; // Power for driving
    double armPower = 1.0; // Power for the arm

    @Override
    public void init() {
        auto.resetEncoders();

    }

    @Override
    public void loop() {

        // 1. Drive forward to the high rung and release the preloaded sample
        auto.drive(forwardToHighRung, "FORWARD");
        auto.releaseSampleSideToSideServo();

        // Repeat steps 2-8 for the remaining two yellow blocks

        // 2. Drive backward slightly
        //driveBackward(backwardDistance);

        // 3. Turn 90 degrees left
        //turnLeft(turnLeft90);

        // 4. Drive forward to the yellow blocks
        //driveForward(driveToYellowBlock);

        // 5. Strafe left to position for pickup
        //strafeLeft(strafeLeftToPickup);

        // 6. Turn 90 degrees right
        //turnRight(turnRight90);

        // 7. Pick up the yellow block
        //pickUpBlock();

        // 8. Turn 180 degrees
        //turn180(pickupTurn180);

        // 9. Extend the arm and score the block on the high rung
        //extendArm(armExtendDistance);
        //scoreBlock();

    }
    // Later
}
