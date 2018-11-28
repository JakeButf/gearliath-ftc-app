package org.firstinspires.ftc.teamcode;

//region IMPORTS
import java.util.ArrayList;
import java.util.List;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Main.Vector3;
//endregion

@Autonomous(name="Main" + " Autonomous", group="Linear Opmode")
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

    private boolean[] tasks = new boolean[] { };
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
        hookMotor       = hardwareMap.get(DcMotor.class, "actuator_motor");

        //Setting Run Mode
        DcMotor[] motorArr = new DcMotor[] { leftMotorFront, leftMotorBack, rightMotorFront, rightMotorBack};
        for(DcMotor d : motorArr)
        {
            d.setMode(DcMotor.RunMode.RUN_TO_POSITION); //1120 ticks per rotation
        }

        depoServo       = hardwareMap.get(Servo.class, "depo_servo");
        //endregion
        //region MOTOR DIRECTION SETTING
        leftMotorBack.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotorFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotorBack.setDirection(DcMotorSimple.Direction.REVERSE);
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
        if(gamepad1.dpad_up) //Task One
        {
            tasks[0] = true;
        }

        if(gamepad1.dpad_right) //Task Two
        {
            tasks[1] = true;
        }

        if(gamepad1.dpad_down) //Task Three
        {
            tasks[2] = true;
        }

        if(gamepad1.dpad_left) //Task Four
        {
            tasks[3] = true;
        }

        if(gamepad1.back) //reset button
        {
            for(int i = 0; i < tasks.length; i++)
            {
                tasks[i] = false;
            }
        }

        enabledModeString = "";
        for(int i = 0; i < tasks.length; i++)
        {
            if(tasks[i])
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

    public void Move(String direction, double seconds, double power)
    {
        ElapsedTime Time = new ElapsedTime();
        Time.reset();

            if(direction == "right")
            {
                rightMotorFront.setPower(power);
                rightMotorBack.setPower(power);
            } else if(direction == "left")
            {
                leftMotorFront.setPower(power);
                leftMotorBack.setPower(power);
            } else if(direction == "straight")
            {
                rightMotorFront.setPower(power);
                rightMotorBack.setPower(power);

                leftMotorFront.setPower(power);
                leftMotorBack.setPower(power);
            } else {
                throw new RuntimeException("Direction is not set to a valid direction");
            }
    }

    boolean configured = false;
    double startTime = 0.0;
    ElapsedTime AutTotTime = new ElapsedTime(); //Elapsed time for entire autonomous
    int state = 0;
    int tempIter = 0;
    int[] iter = new int[200];
    @Override
    public void loop() {
        if(!configured)
        {
            startTime = runtime.time();
            AutTotTime.reset();
            configured = true;
            iter[0] = 0;
        }
        telemetry.addData("Time", AutTotTime);
        telemetry.addData("Test", tempIter);
        switch(state)
        {
            case 0: //Drop from hook
                tempIter++;
                if(tempIter < 1550)
                {
                    hookMotor.setPower(1.0);
                } else if(tempIter >= 1550) {
                    hookMotor.setPower(0.0);
                    tempIter = 0;
                    state = 1;
                    break;
                }

            case 1:
                iter[0]++;
                if(iter[0] < 500 && iter[0] >= 100)
                {
                    rightMotorFront.setPower(-1.0);
                    rightMotorBack.setPower(-1.0);

                    leftMotorFront.setPower(-1.0);
                    leftMotorBack.setPower(-1.0);
                    telemetry.addData("Hi", "jsdfihsfhusodfhsf");
                } else if(iter[0] >= 500)
                {
                    rightMotorFront.setPower(0.0);
                    rightMotorBack.setPower(0.0);

                    leftMotorFront.setPower(0.0);
                    leftMotorBack.setPower(0.0);
                    iter[0] = 0;
                    state = 2;
                    break;
                }
                break;
        }
        //Turns
        //Move("right", 5.0, 1.0);
        //Move("straight", 5.0, 1.0);
        //TODO: Color sensor

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
