package org.firstinspires.ftc.teamcode;

//region IMPORTS
import java.util.ArrayList;
import java.util.List;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Main.Vector3;
//endregion

//@TeleOp(name="Test Autonomous", group="Linear Opmode")
public class OpAutonomous extends OpMode
{
    //region PRIVATE VARIABLES
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor leftMotorFront = null;
    private DcMotor rightMotorFront = null;
    private DcMotor leftMotorBack = null;
    private DcMotor rightMotorBack = null;
    private DcMotor hookMotor = null;

    private Servo depoServo = null;
    private final double _maxServoPos = 0.0;
    private final double _minServoPos = 1.0;
    private double armServoPosition = (_maxServoPos - _minServoPos);
    private boolean armClosed = false;

    private boolean[] modes = new boolean[] { };
    //endregion
    //region INITIALIZATION
    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        //region HARDWARE INITIALIZATION
        leftMotorFront  = hardwareMap.get(DcMotor.class, "left_motor_front");
        leftMotorBack   = hardwareMap.get(DcMotor.class, "left_motor_back");
        rightMotorFront = hardwareMap.get(DcMotor.class, "right_motor_front");
        rightMotorBack  = hardwareMap.get(DcMotor.class, "right_motor_back");
        hookMotor       = hardwareMap.get(DcMotor.class, "hook_motor");

        depoServo       = hardwareMap.get(Servo.class, "depo_servo");
        //endregion
        //region MOTOR DIRECTION SETTING
        leftMotorBack.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightMotorBack.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotorFront.setDirection(DcMotorSimple.Direction.REVERSE);
        hookMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        //endregion
        telemetry.addData("Status", "Initialized"); //Initialization Complete.
    }
    //endregion
    //region INITIALIZATION LOOP
    String enabledModeString;
    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop()
    {
        //region MODES
        if(gamepad1.dpad_up) //Mode One
        {
            modes[0] = true;
        }

        if(gamepad1.back) //reset button
        {
            for(int i = 0; i < modes.length; i++)
            {
                modes[i] = false;
            }
        }

        enabledModeString = "";
        for(int i = 0; i < modes.length; i++)
        {
            if(modes[i])
            {
                enabledModeString += i + " ";
            }
        }

        telemetry.addData("Enabled Modes", enabledModeString);
        //endregion
    }
    //endregion
    //region START
    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start()
    {
        telemetry.addData("Status", "Started");
    }
    //endregion
    //region LOOP
    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */

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

    boolean configured = false;
    double startTime = 0.0;
    @Override
    public void loop() {
        if(!configured)
        {
            startTime = runtime.time();
        }
        if(modes[0])
        {
            //TODO: Input mode one code.
            ToggleServo(this.depoServo);
            ToggleHook(this.hookMotor, 2.0);
        }

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
