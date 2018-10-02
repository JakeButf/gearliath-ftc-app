package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name="Test Teleop", group="Iterative Opmode")

/* Teleop Mode */
public class OpTeleop extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor leftMotor = null;
    private DcMotor rightMotor = null;
    private DcMotor armMotor = null;

    private Servo armServo = null;
    private final double _maxArmServoPos = 0.0;
    private final double _minArmServoPos = 1.0;
    private double armServoPosition = (_maxArmServoPos - _minArmServoPos);
    private boolean armClosed = false;

    public int DriveMode = -1;
    public int DriveMode2 = -1;
    /*
    DriveMode Types:
    0 - POV Mode
    1 - Tank Mode
     */

    @Override
    public void init() {
        //Motors
        leftMotor  = hardwareMap.get(DcMotor.class, "left_motor");
        rightMotor = hardwareMap.get(DcMotor.class, "right_motor");
        armMotor   = hardwareMap.get(DcMotor.class, "arm_motor");

        //Servos
        armServo   = hardwareMap.get(Servo.class, "servo_arm");

        leftMotor.setDirection(DcMotor.Direction.FORWARD);
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        //Setting Drive Type
        DriveMode  = 2;
        DriveMode2 = 0;

        //Initialization Complete
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        //Driver One
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

        //Driver 2
        double armPower = 0.0;
        final double maxStrengthMod = 0.3;
        double strengthModifier = 0.0;

        switch(DriveMode2)
        {
            case -1: //Undefined
                telemetry.addData("Error", "Drive Mode 2 Undefined");
                break;
            case 0: //One Button One Stick

                strengthModifier = (gamepad2.a) ? 1 : maxStrengthMod;

                armPower = gamepad2.left_stick_y * strengthModifier;

                armServoPosition = (gamepad2.left_bumper) ? _maxArmServoPos : _minArmServoPos;

                break;
        }

        if(DriveMode != -1 && DriveMode2 != -1)  //Checks if drive modes are set
        {
            //Driver 1
            // Send calculated power to wheels
            leftMotor.setPower(leftPower);
            rightMotor.setPower(rightPower);
            //Driver 2
            armMotor.setPower(armPower);
            armServo.setPosition(armServoPosition);
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Driver 1 Motors", "left (%.2f), right (%.2f), arm (%.2f)", leftPower, rightPower, armPower);
        }


    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop()
    {
        telemetry.addData("Status", "Stopped");
    }
}
