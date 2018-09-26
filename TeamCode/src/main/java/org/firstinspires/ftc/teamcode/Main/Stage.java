package org.firstinspires.ftc.teamcode.Main;

import java.util.ArrayList;
import java.util.Arrays;

public class Stage
{
    public int[][] Reference;

    public int[] RobotCoordinates;

    public int[][] GetReference()
    {
        /*:
         * This is a 2D array. It is meant to be a digital map of the field.
         */
        Reference = new int[][]
        {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
        };

        return Reference;
    }

    public void GetRoute(int X, int Y)
    {
        int[][] stageReference = this.GetReference();
        int[] destination = new int[] {X, Y};
        int[] robotCoordRef = RobotCoordinates;

        Integer[] commands = new int[] {};
        /*:
        1: up
        2: right
        3: down
        4: left
         */
        ArrayList<Integer> commandsList = new ArrayList<Integer>(Arrays.asList(commands));

        boolean higher;
        boolean left;

        //Compare Coordinates
        if(destination[2] > robotCoordRef[2]) { higher = true; }
        else { higher = false; }

        if(destination[1] < robotCoordRef[1]) { left = true; }
        else { left = false; };

        boolean pathFound = false;
        int[] trackingCoord = new int[] {};

        trackingCoord = robotCoordRef;
        while(!pathFound)
        {
            if(higher)
            {
                if(stageReference[trackingCoord[0]][trackingCoord[1] + 1] != 1) //Path Not Blocked
                {
                    commandsList.add(1);
                    trackingCoord[1] += 1;
                }
                //Path Blocked

                //TODO: Try left and right paths, if none are available return an error
            }

            if(left)
            {
            }
        }

    }

    public void SetCoordinates(int X, int Y)
    {
        RobotCoordinates[0] = X;
        RobotCoordinates[1] = Y;
    }

}
