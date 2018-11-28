package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.google.gson.annotations.SerializedName;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Mecanum Teleop", group="Iterative Opmode")
public class MecanumTeleop extends OpMode
{
    private DcMotor leftMotorFront = null;
    private DcMotor rightMotorFront = null;
    private DcMotor leftMotorBack = null;
    private DcMotor rightMotorBack = null;
    private DcMotor actuatorMotor = null;
    private DcMotor railMotor = null;

    private Servo beaconServo = null;
    //Test Sensor
    private ColorSensor colorSensor = null;

    @Override
    public void init()
    {
        //Motor Initialization
        leftMotorFront = hardwareMap.get(DcMotor.class, "left_motor_front");
        rightMotorFront = hardwareMap.get(DcMotor.class, "right_motor_front");
        leftMotorBack = hardwareMap.get(DcMotor.class, "left_motor_back");
        rightMotorBack = hardwareMap.get(DcMotor.class, "right_motor_back");
        actuatorMotor = hardwareMap.get(DcMotor.class, "actuator_motor");
        railMotor = hardwareMap.get(DcMotor.class, "rail_motor");

        beaconServo = hardwareMap.get(Servo.class, "beacon_servo");

        colorSensor = hardwareMap.get(ColorSensor.class, "color_sensor");
    }

    public void ToggleServo(Servo servo)
    {
        final double min = 0.0;
        final double max = 1.0;

        if(servo.getPosition() == min)
        {
            servo.setPosition(max);
        } else if(servo.getPosition() == max)
        {
            servo.setPosition(min);
        } else {
            //Failsafe
            telemetry.addData("Servo Error", "Position is not min or max, setting to min.");
            servo.setPosition(min);
        }
    }

    public int[] GetSensorColor()
    {
        return new int[]  {/*Red*/  colorSensor.red(),
                           /*Green*/colorSensor.green(),
                           /*Blue*/ colorSensor.blue(),
                           /*Alpha*/colorSensor.alpha()};
    }

    @Override
    public void loop()
    {
        //Direction Setting
        rightMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);

        //Value Definition
        float LFspeed = gamepad1.left_stick_y - gamepad1.left_stick_x;
        float LBspeed = gamepad1.left_stick_y + gamepad1.left_stick_x;
        float RFspeed = gamepad1.right_stick_y + gamepad1.left_stick_x;
        float RBspeed = gamepad1.right_stick_y - gamepad1.left_stick_x;

        //Clipping
        LFspeed = Range.clip(LFspeed, -1, 1);
        LBspeed = Range.clip(LBspeed, -1, 1);
        RFspeed = Range.clip(RFspeed, -1, 1);
        RBspeed = Range.clip(RBspeed, -1, 1);

        //Controls
        if(gamepad1.left_bumper)
        {
            actuatorMotor.setPower(1.0);
        }
        else if(gamepad1.right_bumper)
        {
            actuatorMotor.setPower(-1.0);
        }

        if(gamepad2.a)
        {
            ToggleServo(beaconServo);
        }

        if(gamepad1.left_stick_button)
        {
            int[] tempColors = GetSensorColor();
            telemetry.addData("Sensor Colors",
                              "r:" + tempColors[0] +
                                      "g:" + tempColors[1] +
                                      "b:" + tempColors[2] +
                                      "a:" + tempColors[3]);
        }

        //Speed Setting
        leftMotorFront.setPower(LFspeed);
        leftMotorBack.setPower(LBspeed);
        rightMotorBack.setPower(RBspeed);
        rightMotorFront.setPower(RFspeed);
    }
}