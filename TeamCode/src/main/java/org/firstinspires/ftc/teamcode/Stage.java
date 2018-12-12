package org.firstinspires.ftc.teamcode.Main;

import java.util.ArrayList;

public class Stage
{
    //Set direction value type for command lists
    private enum direction
    {
        up, down, left, right
    }

    //Default map variable
    private static final int[][] stageMap = {
            {0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0},
            {2, 2, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 2, 2},
            {0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0}
    };

    //Keeps track of robot's current position
    public int[] robotCoordinates = new int[2];

    //Coordinate for figuring out the robot's best path
    private int[] trackingCoordinate;

    //Shows which direction the robot is facing at the moment
    private direction facing;

    //When initialized, the stage's parameters will set the robot's coordinates
    public Stage(int position) {
        /* 1 = Red Left
           2 = Red Right
           3 = Blue Left
           4 = Blue Right */
        int index = position - 1;
        int[] exes = {4, 4, 7, 7};
        int[] whys = {4, 7, 7, 4};
        direction[] directions = {direction.left, direction.down, direction.down, direction.right};
        robotCoordinates[0] = exes[index];
        robotCoordinates[1] = whys[index];
        trackingCoordinate = robotCoordinates;
        facing = directions[index];
    }

    //Editable list of directions for moving the robot down path
    private ArrayList<direction> commandsList = new ArrayList<>();

    //Editable list of terrain that robot will cross on path
    private ArrayList<Integer> terrainList = new ArrayList<>();

    //Editable list of indexes for obstacles that are in the way of the tracking coordinate
    private ArrayList<Integer> obstacle = new ArrayList<>();

    //Void Method for adding a direction to the command list a certain amount of times and changing the tracking coordinate based on the direction
    private void addCom(direction direct, int numberOfCommands)
    {
        /* Terrain:
           Smooth = 0
           Rough = 1 */

        for(int i = 0; i < numberOfCommands; i++)
        {
            commandsList.add(direct);
            switch(direct)
            {
                case left:
                    trackingCoordinate[0]--; //Robot will go left
                case right:
                    trackingCoordinate[0]++; //Robot will go right
                case up:
                    trackingCoordinate[1]--; //Robot will go up
                case down:
                    trackingCoordinate[1]++; //Robot will go down
            }
            terrainList.add(stageMap[trackingCoordinate[1]][trackingCoordinate[0]]);
        }
    }

    //For adding just one command
    private void addCom(direction direct)
    {
        addCom(direct, 1);
    }

    //For getting the facing variable
    public int getFacing()
    {
        switch(facing)
        {
            case left:
                return 4;
            case right:
                return 2;
            case up:
                return 1;
            default:
                return 3;
        }
    }

    //For changing facing outside of the class
    public void changeDirection(int direction)
    {
        /* Direction:
           1 = Never
           2 = Eat
           3 = Soggy
           4 = Waffles */
        switch(direction)
        {
            case 1:
                facing = Stage.direction.up;
            case 2:
                facing = Stage.direction.right;
            case 3:
                facing = Stage.direction.down;
            case 4:
                facing = Stage.direction.left;
        }
    }

    //Void Method for finding out what obstacle is in the way of the robot and showing it how to get around the obstacle
    private void findAndGetAroundObstacle(direction direct)
    {
        int left = 1;
        int right = 1;
        switch(direct)
        {
            case left: //Trying to go left
                for(int i = 0; i < stageMap.length; i++)
                {
                    if(stageMap[i][trackingCoordinate[0] - 1] == 1) //Part of obstacle
                    {
                        obstacle.add(i);
                    } else if(!obstacle.contains(trackingCoordinate[1])) //Wrong obstacle
                    {
                        obstacle.clear();
                    } else //Found obstacle
                    {
                        break;
                    }
                }

                for(int i : obstacle)
                {
                    if(i > trackingCoordinate[1]) //This part of the obstacle is left to the robot
                    {
                        left++;
                    } else if(i < trackingCoordinate[1]) //This part of obstacle is right to the robot
                    {
                        right++;
                    }
                }

                if(obstacle.contains(0)) //Obstacle stretches all the way up
                {
                    addCom(direction.down, left);
                } else if(obstacle.contains(stageMap.length - 1)) //Obstacle stretches all the way down
                {
                    addCom(direction.up, right);
                } else //Robot goes whichever direction is shorter
                {
                    addCom((right > left ? direction.down : direction.up), (right > left ? left : right));
                }
            case right: //Trying to go right
                for(int i = 0; i < stageMap.length; i++)
                {
                    if(stageMap[i][trackingCoordinate[0] + 1] == 1) //Part of obstacle
                    {
                        obstacle.add(i);
                    } else if(!obstacle.contains(trackingCoordinate[1])) //Wrong Obstacle
                    {
                        obstacle.clear();
                    } else //Found Obstacle
                    {
                        break;
                    }
                }

                for(int i : obstacle)
                {
                    if(i < trackingCoordinate[1]) //This part of the obstacle is left to the robot
                    {
                        left++;
                    } else if(i > trackingCoordinate[1]) //This part of the obstacle is right to the robot
                    {
                        right++;
                    }
                }

                if(obstacle.contains(0)) //Obstacle stretches all the way up
                {
                    addCom(direction.down, right);
                } else if(obstacle.contains(stageMap.length - 1)) //Obstacle stretches all the way down
                {
                    addCom(direction.up, left);
                } else //Robot goes whichever direction is shorter
                {
                    addCom((right > left ? direction.up : direction.down), (right > left ? left : right));
                }
            case up: //Trying to go up
                for(int i = 0; i < stageMap[trackingCoordinate[1] - 1].length; i++)
                {
                    if(stageMap[trackingCoordinate[1] - 1][i] == 1) //Part of Obstacle
                    {
                        obstacle.add(i);
                    } else if(!obstacle.contains(trackingCoordinate[0])) //Wrong Obstacle
                    {
                        obstacle.clear();
                    } else //Found Obstacle
                    {
                        break;
                    }
                }

                for(int i : obstacle)
                {
                    if(i < trackingCoordinate[0]) //This part of obstacle is left of robot
                    {
                        left++;
                    } else if(i > trackingCoordinate[0]) //This part of obstacle is right of robot
                    {
                        right++;
                    }
                }

                if(obstacle.contains(0)) //Obstacle stretches all the way to the left
                {
                    addCom(direction.right, right);
                } else if(obstacle.contains(stageMap[trackingCoordinate[1] - 1].length - 1)) //Obstacle stretches all the way to the right
                {
                    addCom(direction.left, left);
                } else //Robot will go whichever direction is shorter
                {
                    addCom((right > left ? direction.left : direction.right), (right > left ? left : right));
                }
            case down: //Trying to go down
                for(int i = 0; i < stageMap[trackingCoordinate[1] + 1].length; i++)
                {
                    if(stageMap[trackingCoordinate[1] + 1][i] == 1) //Part of Obstacle
                    {
                        obstacle.add(i);
                    } else if(!obstacle.contains(trackingCoordinate[0])) //Wrong Obstacle
                    {
                        obstacle.clear();
                    } else //Found Obstacle
                    {
                        break;
                    }
                }

                for(int i: obstacle)
                {
                    if(i > trackingCoordinate[0]) //This part of obstacle is left of the robot
                    {
                        left++;
                    } else if(i < trackingCoordinate[0]) //This part of obstacle is right of the robot
                    {
                        right++;
                    }
                }

                if(obstacle.contains(0)) //Obstacle stretches all the way to the left
                {
                    addCom(direction.right, left);
                } else if(obstacle.contains(stageMap[trackingCoordinate[1] + 1].length - 1)) //Obstacle stretches all the way to the right
                {
                    addCom(direction.left, right);
                } else //Robot goes whichever direction is shorter
                {
                    addCom((right > left ? direction.right : direction.left), (right > left ? left : right));
                }
        }
        obstacle.clear();
    }

    //Void Method for finding a route for the robot to get to the destination's coordinates and using that route
    public ArrayList<ArrayList<Integer>> GetRoute(int x, int y)
    {
        /* Coordinates are 1-12
           Computer sees them as 0-11 */
        int comX = x - 1;
        int comY = y - 1;

        //Comparing the robot's coordinates with the destination coordinates to see what directions we need to prioritize
        boolean up = comY < robotCoordinates[1];
        boolean right = comX > robotCoordinates[0];

        //Tracking coordinate is not at destination yet
        while(trackingCoordinate[0] != comX && trackingCoordinate[1] != comY) {
            while (trackingCoordinate[1] != comY) //Tracker's y coordinate has not reached the destination yet
            {
                if (up) //Robot is planning to go up
                {
                    if (stageMap[trackingCoordinate[1] - 1][trackingCoordinate[0]] == 1) //Obstacle in the way
                    {
                        findAndGetAroundObstacle(direction.up);
                    }
                    addCom(direction.up);
                } else //Robot is planning to go down
                {
                    if (stageMap[trackingCoordinate[1] + 1][trackingCoordinate[0]] == 1) //Obstacle in the way
                    {
                        findAndGetAroundObstacle(direction.down);
                    }
                    addCom(direction.down);
                }
            }

            while (trackingCoordinate[0] != comX) //Tracker's x coordinate has not reached the destination yet
            {
                if (right) //Robot is planning to go right
                {
                    if (stageMap[trackingCoordinate[1]][trackingCoordinate[0] + 1] == 1) //Obstacle in the way
                    {
                        findAndGetAroundObstacle(direction.right);
                    }
                    addCom(direction.right);
                } else //Robot is planning to go left
                {
                    if (stageMap[trackingCoordinate[1]][trackingCoordinate[0] - 1] == 1) //Obstacle in the way
                    {
                        findAndGetAroundObstacle(direction.left);
                    }
                    addCom(direction.left);
                }
            }
        }

        //List of things commands and terrains for autonomous to use
        ArrayList<ArrayList<Integer>> ordersAndTerrains = new ArrayList<>();

        //First ArrayList is commands
        ordersAndTerrains.add(new ArrayList<Integer>());

        int[] indexes = new int[2];
        while(indexes[0] < commandsList.size())
        {
            switch(commandsList.get(indexes[0]))
            {
                case left:
                    ordersAndTerrains.get(0).add(4);
                case right:
                    ordersAndTerrains.get(0).add(2);
                case up:
                    ordersAndTerrains.get(0).add(1);
                case down:
                    ordersAndTerrains.get(0).add(3);
            }

            //Every ArrayList after is terrains that that the corresponding command goes over
            ArrayList<Integer> terrains = new ArrayList<>();

            while(commandsList.get(indexes[1]) == commandsList.get(indexes[0]))
            {
                terrains.add(terrainList.get(indexes[1]));
                indexes[1] += 1;
                if(indexes[1] == commandsList.size())
                {
                    break;
                }
            }
            indexes[0] = indexes[1];

            ordersAndTerrains.add(terrains);
        }

        return ordersAndTerrains;
    }
}