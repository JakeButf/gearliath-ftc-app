package org.firstinspires.ftc.teamcode.Main;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


public class Vector3
{
    private float[] identifier = new float[3];
    public Vector3(float value1, float value2, float value3)
    {
        identifier[0] = value1;
        identifier[1] = value2;
        identifier[2] = value3;
    }

    public float[] Value()
    {
        return identifier;
    }

    public void Zero()
    {
        identifier[0] = 0;
        identifier[1] = 0;
        identifier[2] = 0;
    }
}
