package org.firstinspires.ftc.teamcode.Main;

import android.widget.Button;

import com.qualcomm.robotcore.hardware.Gamepad;

public class ControlsEX
{
    private Gamepad gamepad;

    public ButtonsEX buttonArr[] = new ButtonsEX[15];

    public ButtonsEX A;
    public ButtonsEX B;
    public ButtonsEX X;
    public ButtonsEX Y;
    public ButtonsEX Start;
    public ButtonsEX Back;
    public ButtonsEX LeftBumper;
    public ButtonsEX RightBumper;
    public ButtonsEX DPadUp;
    public ButtonsEX DPadDown;
    public ButtonsEX DPadLeft;
    public ButtonsEX DPadRight;
    public ButtonsEX LeftStickButton;
    public ButtonsEX RightStickButton;
    //Analog Buttons
    public ButtonsEX LeftTrigger;
    public ButtonsEX RightTrigger;

    public ControlsEX()
    {
        this(new Gamepad());
    }

    public ControlsEX(Gamepad gamepad)
    {
        this.gamepad = gamepad;

        //Button Initialization
        A                = new ButtonsEX(this.gamepad.a);
        B                = new ButtonsEX(this.gamepad.b);
        X                = new ButtonsEX(this.gamepad.x);
        Y                = new ButtonsEX(this.gamepad.y);
        Start            = new ButtonsEX(this.gamepad.start);
        Back             = new ButtonsEX(this.gamepad.back);
        LeftBumper       = new ButtonsEX(this.gamepad.left_bumper);
        RightBumper      = new ButtonsEX(this.gamepad.right_bumper);
        DPadDown         = new ButtonsEX(this.gamepad.dpad_down);
        DPadLeft         = new ButtonsEX(this.gamepad.dpad_left);
        DPadRight        = new ButtonsEX(this.gamepad.dpad_right);
        DPadUp           = new ButtonsEX(this.gamepad.dpad_up);
        LeftStickButton  = new ButtonsEX(this.gamepad.left_stick_button);
        RightStickButton = new ButtonsEX(this.gamepad.right_stick_button);

        LeftTrigger      = new ButtonsEX(this.gamepad.left_trigger);
        RightTrigger     = new ButtonsEX(this.gamepad.right_trigger);

        //Array Initialization
        buttonArr[0]  = A;
        buttonArr[1]  = B;
        buttonArr[2]  = X;
        buttonArr[3]  = Y;
        buttonArr[4]  = Start;
        buttonArr[5]  = Back;
        buttonArr[6]  = LeftBumper;
        buttonArr[7]  = RightBumper;
        buttonArr[8]  = DPadDown;
        buttonArr[9]  = DPadLeft;
        buttonArr[10] = DPadRight;
        buttonArr[11] = DPadUp;
        buttonArr[12] = LeftStickButton;
        buttonArr[13] = RightStickButton;
        buttonArr[14] = LeftTrigger;
        buttonArr[15] = RightTrigger;
    }

    public boolean ButtonHeld(ButtonsEX button)
    {
        return button.beingHeld;
    }

    public final void Update() {
        //Array Updating
        buttonArr[0] = A;
        buttonArr[1] = B;
        buttonArr[2] = X;
        buttonArr[3] = Y;
        buttonArr[4] = Start;
        buttonArr[5] = Back;
        buttonArr[6] = LeftBumper;
        buttonArr[7] = RightBumper;
        buttonArr[8] = DPadDown;
        buttonArr[9] = DPadLeft;
        buttonArr[10] = DPadRight;
        buttonArr[11] = DPadUp;
        buttonArr[12] = LeftStickButton;
        buttonArr[13] = RightStickButton;
        buttonArr[14] = LeftTrigger;
        buttonArr[15] = RightTrigger;

        for (ButtonsEX b : this.buttonArr)
        {
            b.Update();
        }
    }
}

