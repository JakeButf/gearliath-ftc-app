package org.firstinspires.ftc.teamcode.Main;

import com.qualcomm.robotcore.hardware.Gamepad;

public class ControlsEX
{
    private Gamepad gamepad;

    public ControlsEX(Gamepad gamepad)
    {
        this.gamepad = gamepad;
    }

    public void ButtonHeld(boolean buttonBool)
    {

    }

    public final void Update()
    {
        this.ButtonHeld(gamepad.a);
    }
}
