package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Corner Side Aut", group="Pushbot")
public class LinearAutonomous extends LinearOpMode {

    /* Declare OpMode members. */
    //HardwarePushbot         robot   = new HardwarePushbot();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     SLOW_SPEED              = 0.3;
    static final double     TURN_SPEED              = 0.5;

    private DcMotor leftMotorFront = null;
    private DcMotor rightMotorFront = null;
    private DcMotor leftMotorBack = null;
    private DcMotor rightMotorBack = null;
    private DcMotor hookMotor = null;

    private Servo beaconServo = null;
    private Servo rackPinionServo = null;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        leftMotorFront  = hardwareMap.get(DcMotor.class, "left_motor_front");
        leftMotorBack   = hardwareMap.get(DcMotor.class, "left_motor_back");
        rightMotorFront = hardwareMap.get(DcMotor.class, "right_motor_front");
        rightMotorBack  = hardwareMap.get(DcMotor.class, "right_motor_back");
        hookMotor       = hardwareMap.get(DcMotor.class, "actuator_motor");

        beaconServo = hardwareMap.get(Servo.class, "beacon_servo");


        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        leftMotorFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotorFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotorBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotorBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                leftMotorFront.getCurrentPosition(),
                rightMotorFront.getCurrentPosition(),
                leftMotorBack.getCurrentPosition(),
                rightMotorBack.getCurrentPosition());
        telemetry.update();

        beaconServo.setPosition(Servo.MIN_POSITION);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        //Old
        /*moveTime(-.20, .20, 1.45);
        sleep(1100);
        Deposit();
        sleep(1500);
        beaconServo.setPosition(Servo.MIN_POSITION);
        sleep(1500);*/

        String json = "";
        String dir = "src/main/java/org/firstinspires/ftc/teamcode/CommandData.json";
        //json =

        //Command command = new Gson().fromJson(json, Command.class);

        ToggleActuator();
        moveTime(-.20, -.20, .5);

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = leftMotorFront.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = rightMotorFront.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            leftMotorFront.setTargetPosition(newLeftTarget);
            rightMotorFront.setTargetPosition(newRightTarget);
            //leftMotorBack.setTargetPosition(newLeftTarget);
            //rightMotorBack.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            leftMotorFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotorFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //leftMotorBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //rightMotorBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            leftMotorFront.setPower(Math.abs(speed));
            rightMotorFront.setPower(Math.abs(speed));
            //leftMotorBack.setPower(Math.abs(speed));
            //rightMotorBack.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (leftMotorFront.isBusy() && rightMotorFront.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        leftMotorFront.getCurrentPosition(),
                        rightMotorFront.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            rightMotorFront.setPower(0);
            leftMotorFront.setPower(0);
            //rightMotorBack.setPower(0);
            //leftMotorBack.setPower(0);


            // Turn off RUN_TO_POSITION
            leftMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightMotorFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //leftMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //
            // rightMotorBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
    boolean toggled = false;
    public void ToggleActuator()
    {
        ElapsedTime rt = new ElapsedTime();
        rt.reset();
        hookMotor.setPower(1.0);
        //runtime.reset();
        while (opModeIsActive() && (rt.seconds() < 8.18)) {
            telemetry.addData("Path", "Leg 1: %2.5f", rt.seconds());
            telemetry.update();

        }
        hookMotor.setPower(0.0);
    }
//lbrf
    public void moveTime(double leftSpeed, double rightSpeed, double duration)
    {
        ElapsedTime rt = new ElapsedTime();
        rt.reset();
        leftMotorFront.setPower(leftSpeed);
        rightMotorFront.setPower(rightSpeed); //To go straight, right speed needs to be positve
        leftMotorBack.setPower(leftSpeed);
        rightMotorBack.setPower(rightSpeed);
        while(opModeIsActive() && (rt.seconds() < duration))
        {
            telemetry.addData("Path", "temp");
            telemetry.update();
        }
        leftMotorFront.setPower(0.0);
        rightMotorFront.setPower(0.0);
        leftMotorBack.setPower(0.0);
        rightMotorBack.setPower(0.0);
        sleep(100);
    }

    public void Deposit()
    {
        double start = Servo.MIN_POSITION;
        double end = Servo.MAX_POSITION;

        beaconServo.setPosition(start);

        while(beaconServo.getPosition() < end)
        {
            beaconServo.setPosition(beaconServo.getPosition() + .05);
        }
    }

    /*public static void deserialize() {
        Command c = new Command();

        ObjectMapper mapper = new ObjectMapper();

        File file = new File("artist.json");
        try {
            // Serialize Java object info JSON file.
            mapper.writeValue(file, artist);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Deserialize JSON file into Java object.
            Command c = mapper.readValue(file, Command.class);
            System.out.println("newArtist.getId() = " + newArtist.getId());
            System.out.println("newArtist.getName() = " + newArtist.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}

class Command {
    @SerializedName("commandType")
    @Expose
    public String commandType;
    @SerializedName("ticks")
    @Expose
    public int ticks;
    @SerializedName("index")
    @Expose
    public int index;
}
