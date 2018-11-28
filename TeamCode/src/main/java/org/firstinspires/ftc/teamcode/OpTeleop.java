package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Main.ButtonsEX;
import org.firstinspires.ftc.teamcode.Main.ControlsEX;

@TeleOp(name="Main Teleop", group="Iterative Opmode")

/* Teleop Mode */
public class OpTeleop extends OpMode
{
    //region PRIVATE VARIABLES
    private ControlsEX controllerOne;
    private ControlsEX controllerTwo;

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor leftMotorFront = null;
    private DcMotor rightMotorFront = null;
    private DcMotor leftMotorBack = null;
    private DcMotor rightMotorBack = null;
    private DcMotor hookMotor = null;
    private DcMotor linearMotor = null;
    private DcMotor railMotor = null;
    private DcMotor leftColMotor = null;
    private DcMotor armMotor = null;

    private Servo beaconServo = null;
    private Servo collectionServo = null;
    private final double _maxServoPos = 0.0;
    private final double _minServoPos = 1.0;
    //private double armServoPosition = (_maxArmServoPos - _minArmServoPos);
    private boolean armClosed = false;
    //endregion
    //region GLOBAL VARIABLES
    public int DriveMode = -1;
    public int DriveMode2 = -1;
    //endregion

    /*
    DriveMode Types:
    0 - POV Mode
    1 - Tank Mode
     */

    //region INITIALIZATION
    @Override
    public void init()
    {
        //region CONTROLLERS
        controllerOne = new ControlsEX(gamepad1);
        controllerTwo = new ControlsEX(gamepad2);
        //endregion
        //region MOTOR INITIALIZATION
        leftMotorFront  = hardwareMap.get(DcMotor.class, "left_motor_front");
        rightMotorFront = hardwareMap.get(DcMotor.class, "right_motor_front");
        leftMotorBack = hardwareMap.get(DcMotor.class, "left_motor_back");
        rightMotorBack = hardwareMap.get(DcMotor.class, "right_motor_back");
        //linearMotor = hardwareMap.get(DcMotor.class, "slider_motor");
        railMotor = hardwareMap.get(DcMotor.class, "rail_motor");
        leftColMotor = hardwareMap.get(DcMotor.class, "left_coll_motor");

        hookMotor = hardwareMap.get(DcMotor.class, "actuator_motor");
        //endregion
        //region SERVOS
        beaconServo = hardwareMap.get(Servo.class, "beacon_servo");
        collectionServo = hardwareMap.get(Servo.class, "pinion_servo");
        //collectionServo = hardwareMap.get(Servo.class, "collection_servo");
        //endregion
        //region MOTOR DIRECTION SETTING
        leftMotorFront .setDirection(DcMotor.Direction.REVERSE);
        leftMotorBack  .setDirection(DcMotor.Direction.REVERSE);
        rightMotorFront.setDirection(DcMotor.Direction.FORWARD);
        rightMotorBack .setDirection(DcMotor.Direction.FORWARD);

        //hookMotor      .setDirection(DcMotorSimple.Direction.FORWARD);
        //endregion
        //region DRIVE TYPE DEFINITION
        DriveMode  = 1;
        DriveMode2 = 0;
        //endregion
        telemetry.addData("Status", "Initialized"); //Initialization Complete.
    }
    //endregion
    //region INITIALIZATION LOOP
    @Override
    public void init_loop()
    {

    }
    //endregion
    //region START
    @Override
    public void start()
    {
        runtime.reset();
    }
    //endregion
    //region LOOP
    private void ToggleServo(Servo servo)
    {
        //local vars
        double localMax = _maxServoPos;
        double localMin = _minServoPos;

        if(servo.getPosition() == localMin)
        {
            servo.setPosition(localMax);
        } else if(servo.getPosition() == localMax) {
            servo.setPosition(localMin);
        } else
        {
            throw new RuntimeException("Servo Position is not at max or min.");
        }
    }

    boolean hookExtended = false;
    private void ToggleHook(DcMotor motor, double power)
    {
        int time = 0;
        int maxTime = 200;

        while(time < maxTime)
        {
            if(hookExtended)
            {
                motor.setPower(-power);
            } else {
                motor.setPower(power);
            }
            time++;
        }
        motor.setPower(0.0);
        hookExtended = !hookExtended;
    }
    @Override
    public void loop()
    {
        //region CONTROLLER UPDATING
        controllerOne.Update();
        controllerTwo.Update();
        //endregion
        //region DRIVER ONE
        // Setup a variable for each drive wheel to save power level for telemetry
        double leftPower = 0.0;
        double rightPower = 0.0;

        switch(DriveMode)
        {
            case -1: //Undefined
                telemetry.addData("Error", "Drive Mode Undefined");
            case 0: //POV Mode
                double drive = -gamepad1.left_stick_y;
                double turn  =  gamepad1.right_stick_x;
                leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
                rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
                break;
            case 1: //Tank Mode
                leftPower  = -gamepad1.left_stick_y ;
                rightPower = -gamepad1.right_stick_y ;
                break;
            case 2: //Traditional One Stick Mode
                double driveTrad = -gamepad1.left_stick_y;
                double turnTrad  =  gamepad1.left_stick_x;
                leftPower    = Range.clip(driveTrad + turnTrad, -1.0, 1.0) ;
                rightPower   = Range.clip(driveTrad - turnTrad, -1.0, 1.0) ;
        }
        //endregion
        //region DRIVER TWO
        double actuatorPower = 0.0;
        final double maxStrengthMod = 0.3;
        double strengthModifier = 0.0;
        boolean moveServo = false;

        switch(DriveMode2)
        {
            case -1: //Undefined
                telemetry.addData("Error", "Drive Mode 2 Undefined");
                break;
            case 0:
                strengthModifier = (gamepad2.a) ? maxStrengthMod : 1;
                if(gamepad2.left_trigger > 0f)
                {
                    actuatorPower = gamepad2.left_trigger * strengthModifier;
                } else if(gamepad2.right_trigger > 0f)
                {
                    actuatorPower = -gamepad2.right_trigger * strengthModifier;
                } else {
                    actuatorPower = 0;
                }

                if(gamepad2.a)
                {
                    railMotor.setPower(1.0);
                } else if(gamepad2.b)
                {
                    railMotor.setPower(-1.0);
                }

                if(gamepad2.left_bumper)
                {
                    leftColMotor.setPower(1.0);
                } else if(gamepad2.right_bumper){
                    leftColMotor.setPower(-1.0);
                } else {
                    leftColMotor.setPower(0.0);
                }

                break;
        }
        //endregion
        //region MOTOR POWER SETTING
        if(DriveMode != -1 && DriveMode2 != -1)  //Checks if drive modes are set
        {
            //Driver 1
            leftMotorFront.setPower(leftPower);
            rightMotorFront.setPower(rightPower);
            leftMotorBack.setPower(leftPower);
            rightMotorBack.setPower(rightPower);

            hookMotor.setPower(actuatorPower);
            //Driver 2
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Driver 1 Motors", "left (%.2f), right (%.2f), actuator (%.2f)", leftPower, rightPower, actuatorPower);
        }
        //endregion

    }
    //endregion
    //region STOP
    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop()
    {
        telemetry.addData("Status", "Stopped");
    }
    //endregion
}
