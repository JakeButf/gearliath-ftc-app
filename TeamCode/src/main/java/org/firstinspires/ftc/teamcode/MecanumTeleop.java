package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.google.gson.annotations.SerializedName;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Mecanum Teleop", group="Iterative Opmode")
public class MecanumTeleop extends OpMode
{
    private DcMotor leftMotorFront = null;
    private DcMotor rightMotorFront = null;
    private DcMotor leftMotorBack = null;
    private DcMotor rightMotorBack = null;
    private DcMotor actuatorMotor = null;
    private DcMotor horizontalRailMotor = null;
    private DcMotor verticalRailMotor = null;
    private DcMotor collectionMotor = null;

    private Servo beaconServo = null;
    private Servo collectionServo = null;
    private Servo deliveryServo = null;
    private Servo boxServo = null;
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
        horizontalRailMotor = hardwareMap.get(DcMotor.class, "horizontal_rail_motor");
        verticalRailMotor = hardwareMap.get(DcMotor.class, "vertical_rail_motor");
        collectionMotor = hardwareMap.get(DcMotor.class, "collection_motor");

        beaconServo = hardwareMap.get(Servo.class, "beacon_servo");
        collectionServo = hardwareMap.get(Servo.class, "collection_servo");
        deliveryServo = hardwareMap.get(Servo.class, "delivery_servo");
        boxServo = hardwareMap.get(Servo.class, "box_servo");

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

    public float[] GetSensorColor()
    {
         float[] HSV = {0F, 0F, 0F};
        Color.RGBToHSV((colorSensor.red() * 255),
                (colorSensor.green() * 255),
                (colorSensor.blue() * 255),
                HSV);

         return new float[]  {/*Red*/  colorSensor.red(),
                           /*Green*/colorSensor.green(),
                           /*Blue*/ colorSensor.blue(),
                           /*Alpha*/colorSensor.alpha(),
                           /*Hue*/  HSV[0]};
   }

    @Override
    public void loop()
    {
        //Direction Setting
        //rightMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);

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

        //Controls GamePad 1
        if(gamepad1.left_bumper)
        {
            actuatorMotor.setPower(1.0);
        }
        else if(gamepad1.right_bumper)
        {
            actuatorMotor.setPower(-1.0);
        } else {
            actuatorMotor.setPower(0.0);
        }



        if(gamepad1.a)
        {
            collectionServo.setPosition(1.0);
        } else if(gamepad1.y) {
            collectionServo.setPosition(0.0);
        } /*else {
            collectionServo.setPosition(1.0);
        }*/

        if(gamepad1.dpad_left)
        {
            deliveryServo.setPosition(.75);
        } else if(gamepad1.dpad_right)
        {
            deliveryServo.setPosition(.2);
        } else if(gamepad1.dpad_down){
            deliveryServo.setPosition(0.0);
        }

        //Controls GamePad 2
        if(gamepad2.a)
        {
            collectionMotor.setPower(1.0);
        } else if(gamepad2.b)
        {
            collectionMotor.setPower(-1.0);
        } else {
            collectionMotor.setPower(0.0);
        }

        if(gamepad2.left_bumper) //Horizontal X-Rail
        {
            horizontalRailMotor.setPower(1.0);
        } else if(gamepad2.left_trigger > 0)
        {
            horizontalRailMotor.setPower(-1.0);
        } else {
            horizontalRailMotor.setPower(0.0);
        }

        if(gamepad2.right_bumper)
        {
            verticalRailMotor.setPower(1.0);
        } else if(gamepad2.right_trigger > 0)
        {
            verticalRailMotor.setPower(-1.0);
        } else {
            verticalRailMotor.setPower(0.0);
        }

        if(gamepad2.x)
        {
            boxServo.setPosition(1.0);
        } else {
            boxServo.setPosition(0.6);
        }


        if(gamepad1.left_stick_button) //Needs Color Sensor
        {
            float[] tempColors = GetSensorColor();
            telemetry.addData("Sensor Colors",
                              "r:" + tempColors[0] +
                                      "g:" + tempColors[1] +
                                      "b:" + tempColors[2] +
                                      "a:" + tempColors[3] +
                                      "h:" + tempColors[4]);
        }

        //Speed Setting
        leftMotorFront.setPower(LFspeed);
        leftMotorBack.setPower(LBspeed);
        rightMotorBack.setPower(RBspeed);
        rightMotorFront.setPower(RFspeed);
    }
}