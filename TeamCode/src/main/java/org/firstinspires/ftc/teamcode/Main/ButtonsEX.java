package org.firstinspires.ftc.teamcode.Main;

public class ButtonsEX
{
    protected boolean buttonBool;
    protected boolean beingHeld;
    private boolean analog = false;
    private float analogVal;

    protected boolean pressedBool; //Dont touch

    public ButtonsEX(boolean buttonBool)
    {
        this.buttonBool = buttonBool;
    }

    public ButtonsEX(float analogValue)
    {
        this.analogVal = analogValue;
        this.analog = true;
    }

    public void Update()
    {
        this.beingHeld = buttonBool;
    }

    public boolean Held() //Code runs in a loop while a button is held
    {
        if(!this.beingHeld)
        {
            pressedBool = false;
        }

        if(!pressedBool)
        {
            pressedBool = true;
            return true;
        } else {
            return false;
        }
    }

    Boolean temp = false;
    public Boolean Pressed() //Code runs once while button is held
    {
        if(!this.beingHeld)
        {
            temp = false;
        }

        if(this.beingHeld && !temp)
        {
            temp = true;
            return true;
        } else if(this.beingHeld && temp)
        {
            return false;
        }
        throw new RuntimeException("I have no clue how this happened.");
    }

    //Getters
    public boolean GetButtonBool()
    {
        return buttonBool;
    }

    public boolean GetBeingHeld()
    {
        return beingHeld;
    }

    public float GetAnalogValue() { if(analog) { return analogVal; } else { return 0.0f; } }

    //Setters
    public void SetButtonBool(boolean replacement)
    {
        this.buttonBool = replacement;
    }

    public void SetBeingHeld(boolean replacement)
    {
        this.beingHeld = replacement;
    }


}
