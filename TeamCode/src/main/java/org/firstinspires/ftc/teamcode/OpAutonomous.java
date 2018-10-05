package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Main.Vector3;

@TeleOp(name="Test Autonomous", group="Linear Opmode")
public class OpAutonomous extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor leftMotor = null;
    private DcMotor rightMotor = null;
    private DcMotor armMotor = null;

    private Servo armServo = null;
    private final double _maxArmServoPos = 0.0;
    private final double _minArmServoPos = 1.0;
    private double armServoPosition = (_maxArmServoPos - _minArmServoPos);
    private boolean armClosed = false;

    private boolean mode1Enabled = false;

    /*
     * Code to run ONCE when the driver hits INIT
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

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop()
    {
        if(gamepad1.dpad_up) //Mode One
        {
            mode1Enabled = true;
        }

        if(gamepad1.back) //reset button
        {
            mode1Enabled = false;
        }

    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        telemetry.addData("Enabled Modes", "Mode 1: " + mode1Enabled);
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    boolean configured = false;
    double startTime = 0.0;
    @Override
    public void loop() {
        if(!configured)
        {
            startTime = runtime.time();
        }
        if(mode1Enabled)
        {
            telemetry.addData("Status", "Mode 1");
            while(startTime + 5.0 > runtime.time())
            {
                leftMotor.setPower(1);
                rightMotor.setPower(-1);
            }
        } else if(!mode1Enabled)
        {
            while(startTime + 5.0 > runtime.time())
            {
                leftMotor.setPower(-1);
                rightMotor.setPower(1);
            }
        }

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }



    //Other Autonomous Calls
    void ConsoleLog(String message)
    {
        telemetry.addData("Console Log", message);
    }

    void ConsoleError(Exception error)
    {
        String errorString = error.toString();
        telemetry.addData("Console Error", errorString);
    }
}
