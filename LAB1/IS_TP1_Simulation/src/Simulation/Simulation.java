package Simulation;

import Common.MessageManagement;
import static Common.MessageManagement.createPlaceStateContent;
import static Common.MessageManagement.retrievePlaceStateObject;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.netbeans.xml.schema.updateschema.TMyPlace;
import org.netbeans.xml.schema.updateschema.TPlace;
import org.netbeans.xml.schema.updateschema.TPosition;

/**
 *
 * @author adroc
 */
public class Simulation extends Thread {

    private TPlace[][] myEnvironment;
    private EnvironmentGUI myGUI;
    private HashMap<String, TPosition> wolfList = new HashMap<>();
    private HashMap<String, TPosition> cowList = new HashMap<>();
    private HashMap<String, TPosition> dogList = new HashMap<>();
    private int simulationSpeed;

    public Simulation(int Cows, int Wolfs, int Dogs,int Obstacles, int Stamina1, int Stamina2, int speed) {
        myEnvironment = new TPlace[15][15];
        int obstacles = Obstacles;
        int wolfs = Wolfs;
        int dogs = Dogs;
        int cows = Cows;
        int stamina1 = Stamina1;
        int stamina2 = Stamina2;
        System.out.printf("'''''''''''%d%n",Stamina2);
        System.out.printf("'''''''''''%d%n",Stamina1);
        simulationSpeed = speed;
        generateEnvironment(obstacles, wolfs, cows, dogs, stamina1, stamina2);
        /*
             * Start GUI
         */
        myGUI = new EnvironmentGUI();
        myGUI.updateGUI(myEnvironment);
        myGUI.setVisible(true);
    }

    private void generateEnvironment(int obstacles, int wolfs, int cows, int dogs, int stamina1, int stamina2) {
        //System.out.printf("++++++++++%d%n",stamina2);
        //System.out.printf("++++++++++%d%n",stamina1);
        startBase();
        putObstacles(obstacles);
        putWolfs(wolfs, stamina2);
        putCows(cows, stamina1);
        putDogs(dogs, stamina2);
    }

    /*
     * Start all places with grass and position
     */
    private void startBase() {
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                myEnvironment[x][y] = new TPlace();
                Random rand = new Random();
                myEnvironment[x][y].setGrass(rand.nextInt((3 - 1) + 1) + 1);
                TPosition pos = new TPosition();
                pos.setXx(x);
                pos.setYy(y);
                myEnvironment[x][y].setPosition(pos);
            }
        }
    }

    /*
     * Put obstacles
     */
    private void putObstacles(int obstacles) {
        for (int i = 0; i < obstacles; i++) {
            int posX;
            int posY;
            do {
                Random randX = new Random();
                Random randY = new Random();
                posX = randX.nextInt((14 - 0) + 1) + 0;
                posY = randY.nextInt((14 - 0) + 1) + 0;
            } while (myEnvironment[posX][posY].isObstacle() || myEnvironment[posX][posY].isCow() || myEnvironment[posX][posY].isWolf() || myEnvironment[posX][posY].isDog());
            myEnvironment[posX][posY].setObstacle(true);
        }
    }

    /*
     * Put wolfs in the environment
     */
    private void putWolfs(int wolfs, int stamina) {
        for (int i = 0; i < wolfs; i++) {
            int posX;
            int posY;
            do {
                Random randX = new Random();
                Random randY = new Random();
                posX = randX.nextInt((14 - 0) + 1) + 0;
                posY = randY.nextInt((14 - 0) + 1) + 0;
            } while (myEnvironment[posX][posY].isObstacle() || myEnvironment[posX][posY].isCow() || myEnvironment[posX][posY].isWolf() || myEnvironment[posX][posY].isDog());
            myEnvironment[posX][posY].setWolf(true);
            if((i%2) == 0)
                myEnvironment[posX][posY].setSex(2);
            else
                myEnvironment[posX][posY].setSex(1);
            myEnvironment[posX][posY].setEntity("Wolf_" + i);
            TPosition tPosition = new TPosition();
            tPosition.setXx(posX);
            tPosition.setYy(posY);
            this.wolfList.put("Wolf_" + i, tPosition);

            //myEnvironment[posX][posY].setStamina(3);

            myEnvironment[posX][posY].setStamina(stamina);

        }
    }

    private void putDogs(int dogs,int stamina) {
        for (int i = 0; i < dogs; i++) {
            int posX;
            int posY;
            do {
                Random randX = new Random();
                Random randY = new Random();
                posX = randX.nextInt((14 - 0) + 1) + 0;
                posY = randY.nextInt((14 - 0) + 1) + 0;
            } while (myEnvironment[posX][posY].isObstacle() || myEnvironment[posX][posY].isCow() || myEnvironment[posX][posY].isWolf() || myEnvironment[posX][posY].isDog());
            myEnvironment[posX][posY].setDog(true);
            if((i%2) == 0)
                myEnvironment[posX][posY].setSex(1);
            else
                myEnvironment[posX][posY].setSex(2);
            myEnvironment[posX][posY].setEntity("Dog_" + i);
            TPosition tPosition = new TPosition();
            tPosition.setXx(posX);
            tPosition.setYy(posY);
            this.dogList.put("Dog_" + i, tPosition);
            myEnvironment[posX][posY].setStamina(stamina);
        }
    }
    
    /*
     * Put cows in the environment
     */
    private void putCows(int cows, int stamina) {
        //int sex = 0;
        int CowID = 0;
        for (int i = 0; i < cows; i++) {
            int posX;
            int posY;
            do {
                Random randX = new Random();
                Random randY = new Random();
                posX = randX.nextInt((14 - 0) + 1) + 0;
                posY = randY.nextInt((14 - 0) + 1) + 0;
            } while (myEnvironment[posX][posY].isObstacle() || myEnvironment[posX][posY].isCow() || myEnvironment[posX][posY].isWolf() || myEnvironment[posX][posY].isDog());
            myEnvironment[posX][posY].setCow(true);
            myEnvironment[posX][posY].setEntity("Cow_" + CowID);
            TPosition tPosition = new TPosition();
            tPosition.setXx(posX);
            tPosition.setYy(posY);
            myEnvironment[posX][posY].setStamina(5);

            this.cowList.put("Cow_" + CowID, tPosition);
            //System.out.printf("----------%d%n",stamina);
            myEnvironment[posX][posY].setStamina(stamina);
            CowID++;
        }
    }

    /*
     * Update Grass
     */
    protected void updateGrass() {
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                if (this.myEnvironment[x][y].isCow()) {
                    this.myEnvironment[x][y].setGrass(this.myEnvironment[x][y].getGrass() - 1);
                } else if (!myEnvironment[x][y].isDog() && !myEnvironment[x][y].isWolf() && !this.myEnvironment[x][y].isObstacle() && this.myEnvironment[x][y].getGrass() < 3) {
                    this.myEnvironment[x][y].setGrass(this.myEnvironment[x][y].getGrass() + 1);
                }
            }
        }
    }

    /*
         * Create MyPlace for each entity (Wolf/Cow/Dog)
     */
    private TMyPlace createMyPlace(int posX, int posY) {
        TMyPlace myPlace = new TMyPlace();
        myPlace.getPlace().add(this.myEnvironment[posX][posY]);
        if (posY == 14 && posX == 0) {
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(this.myEnvironment[posX + 1][posY]);
            myPlace.getPlace().add(this.myEnvironment[posX + 1][posY - 1]);
            myPlace.getPlace().add(this.myEnvironment[posX][posY - 1]);
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
        } else if (posY == 14 && posX == 14) {
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(this.myEnvironment[posX][posY - 1]);
            myPlace.getPlace().add(this.myEnvironment[posX - 1][posY - 1]);
            myPlace.getPlace().add(this.myEnvironment[posX - 1][posY]);
        } else if (posY == 0 && posX == 14) {
            myPlace.getPlace().add(this.myEnvironment[posX - 1][posY + 1]);
            myPlace.getPlace().add(this.myEnvironment[posX][posY + 1]);
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(this.myEnvironment[posX - 1][posY]);
        } else if (posY == 0 && posX == 0) {
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(this.myEnvironment[posX][posY + 1]);
            myPlace.getPlace().add(this.myEnvironment[posX + 1][posY + 1]);
            myPlace.getPlace().add(this.myEnvironment[posX + 1][posY]);
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
        } else if (posY == 14) {
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(this.myEnvironment[posX + 1][posY]);
            myPlace.getPlace().add(this.myEnvironment[posX + 1][posY - 1]);
            myPlace.getPlace().add(this.myEnvironment[posX][posY - 1]);
            myPlace.getPlace().add(this.myEnvironment[posX - 1][posY - 1]);
            myPlace.getPlace().add(this.myEnvironment[posX - 1][posY]);
        } else if (posX == 14) {
            myPlace.getPlace().add(this.myEnvironment[posX - 1][posY + 1]);
            myPlace.getPlace().add(this.myEnvironment[posX][posY + 1]);
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(this.myEnvironment[posX][posY - 1]);
            myPlace.getPlace().add(this.myEnvironment[posX - 1][posY - 1]);
            myPlace.getPlace().add(this.myEnvironment[posX - 1][posY]);
        } else if (posY == 0) {
            myPlace.getPlace().add(this.myEnvironment[posX - 1][posY + 1]);
            myPlace.getPlace().add(this.myEnvironment[posX][posY + 1]);
            myPlace.getPlace().add(this.myEnvironment[posX + 1][posY + 1]);
            myPlace.getPlace().add(this.myEnvironment[posX + 1][posY]);
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(this.myEnvironment[posX - 1][posY]);
        } else if (posX == 0) {
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(this.myEnvironment[posX][posY + 1]);
            myPlace.getPlace().add(this.myEnvironment[posX + 1][posY + 1]);
            myPlace.getPlace().add(this.myEnvironment[posX + 1][posY]);
            myPlace.getPlace().add(this.myEnvironment[posX + 1][posY - 1]);
            myPlace.getPlace().add(this.myEnvironment[posX][posY - 1]);
            myPlace.getPlace().add(new TPlace());
            myPlace.getPlace().add(new TPlace());
        } else {
            myPlace.getPlace().add(this.myEnvironment[posX - 1][posY + 1]);
            myPlace.getPlace().add(this.myEnvironment[posX][posY + 1]);
            myPlace.getPlace().add(this.myEnvironment[posX + 1][posY + 1]);
            myPlace.getPlace().add(this.myEnvironment[posX + 1][posY]);
            myPlace.getPlace().add(this.myEnvironment[posX + 1][posY - 1]);
            myPlace.getPlace().add(this.myEnvironment[posX][posY - 1]);
            myPlace.getPlace().add(this.myEnvironment[posX - 1][posY - 1]);
            myPlace.getPlace().add(this.myEnvironment[posX - 1][posY]);
        }
        return myPlace;
    }

    @Override
    public void run() {

        while (true) {
            try {
                ArrayList<String> cowsToKill = new ArrayList<>();
                for (String cowName : this.cowList.keySet()) {
                    //call service to update Cow position
                    TMyPlace myNewPosition = updateCowPosition(createMyPlace(this.cowList.get(cowName).getXx(), this.cowList.get(cowName).getYy()));
                    TPlace myPlace = myNewPosition.getPlace().get(0); //New position
                    //updateCowPosition
                    int lastX = this.cowList.get(cowName).getXx(); //Last position Xx
                    int lastY = this.cowList.get(cowName).getYy(); //Last position Yy
                    
                    if (!(this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isWolf() //New position without Wolf
                            && this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].getGrass() == 0) //With grass
                            && this.myEnvironment[lastX][lastY].getStamina() != 0
                            && !this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isObstacle() //Without obstacle
                            && !this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isCow()
                            && !this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isDog()) {  //Without cow
                        this.myEnvironment[lastX][lastY].setEntity(null); //Remove from last postion
                        this.myEnvironment[lastX][lastY].setCow(false); //Remove from last position

                        
                        this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setStamina(this.myEnvironment[lastX][lastY].getStamina() - 1);//update stamina
                        this.myEnvironment[lastX][lastY].setStamina(0);//remove stamina from last position
                        

                        this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setEntity(cowName); //Put in new position
                        this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setCow(true); //Put in new position
                        this.cowList.get(cowName).setXx(myPlace.getPosition().getXx()); //Put in new position
                        this.cowList.get(cowName).setYy(myPlace.getPosition().getYy()); //Put in new position
                    
                            
                            
                    } else {
                        if (this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isWolf()) { //Wolf than eats a Cow
                            this.myEnvironment[lastX][lastY].setEntity(null); //Remove from last position
                            this.myEnvironment[lastX][lastY].setCow(false); //Remove from last position
                            cowsToKill.add(cowName);//Add to the list to remove later
                        }
                        if (this.myEnvironment[lastX][lastY].getStamina() == 0) { //Without stamina in position
                            this.myEnvironment[lastX][lastY].setEntity(null); //Remove from last position
                            this.myEnvironment[lastX][lastY].setCow(false); //Remove from last position
                            cowsToKill.add(cowName);//Add to the list to remove later
                        }
                        if (this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].getGrass() == 0) { //Without grass in position
                            this.myEnvironment[lastX][lastY].setEntity(null); //Remove from last position
                            this.myEnvironment[lastX][lastY].setCow(false); //Remove from last position
                            cowsToKill.add(cowName);//Add to the list to remove later
                        }
                        if (this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isObstacle()) { //With obstacle
                            if (this.myEnvironment[lastX][lastY].getGrass() == 0) { //No grass in last position
                                this.myEnvironment[lastX][lastY].setEntity(null); //Remove from last position
                                this.myEnvironment[lastX][lastY].setCow(false);   //Remove from last position
                                cowsToKill.add(cowName);//Add to the list to remove later
                            }                                                                                      //else keep cow in the some position
                        }
                        if (this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isCow()) { //Another cow
                            if (this.myEnvironment[lastX][lastY].getGrass() == 0) {
                                this.myEnvironment[lastX][lastY].setEntity(null); //Remove from last position
                                this.myEnvironment[lastX][lastY].setCow(false);   //Remove from last position
                                cowsToKill.add(cowName);        //Add to the list to remove later
                            }                                                                                      //else keep cow in the some position
                        }
                    }
                }
                //Remove Cow from HashMap
                for (String cowID : cowsToKill) {
                    this.cowList.remove(cowID);
                }

                //send update for all wolfs
                ArrayList<String> wolfsToKill = new ArrayList<>();
                for (String wolfName : this.wolfList.keySet()) {
                    TMyPlace myNewPosition = updateWolfPosition(createMyPlace(this.wolfList.get(wolfName).getXx(), this.wolfList.get(wolfName).getYy()));
                    TPlace myPlace = myNewPosition.getPlace().get(0); //New position
                    int n = myPlace.getPosition().getXx() + 1;
                    int t = myPlace.getPosition().getXx() - 1;
                    //updateWolfPosition
                    if (this.wolfList.containsKey(wolfName)) { //Search for the name in the hashmap (Wolf)        

                        int lastX = this.wolfList.get(wolfName).getXx();   //Last position Xx
                        int lastY = this.wolfList.get(wolfName).getYy();   //Last position Yy

                        if (!(this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isWolf()) //Movement to an empty position 
                                && !this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isObstacle()
                                && !this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isCow()
                                && this.myEnvironment[lastX][lastY].getStamina() != 0
                                && !this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isDog()) {
                            this.myEnvironment[lastX][lastY].setEntity(null);
                            this.myEnvironment[lastX][lastY].setWolf(false);
                            
                            this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setStamina(this.myEnvironment[lastX][lastY].getStamina() - 1);//update stamina
                            this.myEnvironment[lastX][lastY].setStamina(0);//remove stamina from last position
                        
                            
                            this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setEntity(wolfName);
                            this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setWolf(true);
                            this.wolfList.get(wolfName).setXx(myPlace.getPosition().getXx());
                            this.wolfList.get(wolfName).setYy(myPlace.getPosition().getYy());

                        } else {
                            if (this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isWolf()) {
                                //Wolf in the same 
//                                if (this.myEnvironment[lastX][lastY].isWolf() == true) {//wolf in the last position
//                                    this.myEnvironment[lastX][lastY].setEntity(null); //Remove from last position
//                                    this.myEnvironment[lastX][lastY].setWolf(false);   //Remove from last position
//                                    wolfsToKill.add(wolfName);        //Add to the list to remove later
//                                }
                                
                            }
                            if (this.myEnvironment[lastX][lastY].getStamina() == 0) { //Without stamina in position
                                this.myEnvironment[lastX][lastY].setEntity(null); //Remove from last position
                                this.myEnvironment[lastX][lastY].setWolf(false); //Remove from last position
                                wolfsToKill.add(wolfName);//Add to the list to remove later
                            }
                            if (this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isObstacle()) {
//                                //Moving to obstacle
//                                if (this.myEnvironment[lastX][lastY].isWolf() == true) {//wolf in the last position
//                                    this.myEnvironment[lastX][lastY].setEntity(null); //Remove from last position
//                                    this.myEnvironment[lastX][lastY].setWolf(false);   //Remove from last position
//                                    wolfsToKill.add(wolfName);        //Add to the list to remove later
//                                }
                                
                            }
                            
                            if (this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isCow() && this.myEnvironment[lastX][lastY].getSex() == 2 && this.myEnvironment[lastX][lastY].getStamina() != 0) {
                                String lastEntity = this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].getEntity(); //Entity of Cow in the new positin

                                this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setStamina(this.myEnvironment[lastX][lastY].getStamina() - 1);//update stamina
                                this.myEnvironment[lastX][lastY].setStamina(0);//remove stamina from last position
                            
                                //UpdateTable
                                this.myEnvironment[lastX][lastY].setEntity(null); //Remove wolf from last position
                                this.myEnvironment[lastX][lastY].setSex(0);
                                this.myEnvironment[lastX][lastY].setWolf(false);  //Remove wolf from last position
                                this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setEntity(wolfName);
                                this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setWolf(true);
                                this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setSex(2);

                                this.wolfList.get(wolfName).setXx(myPlace.getPosition().getXx());
                                this.wolfList.get(wolfName).setYy(myPlace.getPosition().getYy());
                                this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setCow(false);
                                this.cowList.remove(lastEntity);
                            }
                        }
                    }

                }
                //Remove Wolfs from HashMap
                for (String wolfID : wolfsToKill) {
                    this.wolfList.remove(wolfID);
                }

                //send update for all wolfs
                ArrayList<String> dogsToKill = new ArrayList<>();
                for (String dogName : this.dogList.keySet()) {
                    TMyPlace myNewPosition = updateDogPosition(createMyPlace(this.dogList.get(dogName).getXx(), this.dogList.get(dogName).getYy()));
                    TPlace myPlace = myNewPosition.getPlace().get(0); //New position
                    //updateWolfPosition
                    if (this.dogList.containsKey(dogName)) { //Search for the name in the hashmap (Dog)        

                        int lastX = this.dogList.get(dogName).getXx();   //Last position Xx
                        int lastY = this.dogList.get(dogName).getYy();   //Last position Yy

                        if (!(this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isDog()) //Movement to an empty position 
                                && !this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isObstacle()
                                && !this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isCow()
                                && this.myEnvironment[lastX][lastY].getStamina() != 0
                                && !this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isWolf()) {
                            this.myEnvironment[lastX][lastY].setEntity(null);
                            this.myEnvironment[lastX][lastY].setDog(false);
                            
                            this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setStamina(this.myEnvironment[lastX][lastY].getStamina() - 1);//update stamina
                            this.myEnvironment[lastX][lastY].setStamina(0);//remove stamina from last position

                            this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setEntity(dogName);
                            this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setDog(true);
                            this.dogList.get(dogName).setXx(myPlace.getPosition().getXx());
                            this.dogList.get(dogName).setYy(myPlace.getPosition().getYy());

                        } else {
                            if (this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isDog()) {
                                //Dog in the same position
                            }
                            if (this.myEnvironment[lastX][lastY].getStamina() == 0) { //Without stamina in position
                                this.myEnvironment[lastX][lastY].setEntity(null); //Remove from last position
                                this.myEnvironment[lastX][lastY].setDog(false); //Remove from last position
                                dogsToKill.add(dogName);//Add to the list to remove later
                            }
                            if (this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isObstacle()) {
                                //Moving to obstacle
                            }
                            if (this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].isWolf() && this.myEnvironment[lastX][lastY].getSex() == 2 && this.myEnvironment[lastX][lastY].getStamina() != 0) {
                                String lastEntity = this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].getEntity(); //Entity of Cow in the new positin

                                this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setStamina(this.myEnvironment[lastX][lastY].getStamina() - 1);//update stamina
                                this.myEnvironment[lastX][lastY].setStamina(0);//remove stamina from last position
                                
                                //UpdateTable
                                this.myEnvironment[lastX][lastY].setEntity(null); //Remove wolf from last position
                                this.myEnvironment[lastX][lastY].setDog(false);  //Remove wolf from last position
                                this.myEnvironment[lastX][lastY].setSex(0);
                                this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setEntity(dogName);
                                this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setDog(true);
                                this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setSex(2);

                                this.dogList.get(dogName).setXx(myPlace.getPosition().getXx());
                                this.dogList.get(dogName).setYy(myPlace.getPosition().getYy());
                                this.myEnvironment[myPlace.getPosition().getXx()][myPlace.getPosition().getYy()].setWolf(false);
                                this.wolfList.remove(lastEntity);
                            }
                        }
                    }

                }
                //Remove Dogs from HashMap
                for (String dogID : dogsToKill) {
                    this.dogList.remove(dogID);
                }
                this.myGUI.updateGUI(this.myEnvironment);
                this.updateGrass();

                Thread.sleep(simulationSpeed);
            } catch (InterruptedException ex) {
                Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JAXBException ex) {
                Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private TMyPlace updateCowPosition(TMyPlace currentMyPlace) throws JAXBException, IOException {
        
       
        System.out.println("------client-----");
        
        InetAddress host = InetAddress.getLocalHost();
        
        Socket client = new Socket(host.getHostName(),4444);
        //Socket client = new Socket("localhost",4444);


        System.out.println("entered client");

        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String s = createPlaceStateContent(currentMyPlace);
        System.out.println("serialized message-client");


        out.println(s);
        //out.println();

        System.out.println("sent message client");

        String input = in.readLine();
        System.out.println("recived message");

        currentMyPlace = retrievePlaceStateObject(input);
        System.out.println("deserialized message");
        
        client.close();

       
       return currentMyPlace;
    }

    private TMyPlace updateWolfPosition(TMyPlace currentMyPlace) throws JAXBException, IOException {

//        for (int i = 0; i < currentMyPlace.getPlace().size(); i++){
//                if (currentMyPlace.getPlace().get(i).isCow() == true && currentMyPlace.getPlace().get(i).isObstacle() == false){
//                    currentMyPlace.getPlace().get(0).setPosition(currentMyPlace.getPlace().get(i).getPosition());
//                    currentMyPlace.getPlace().get(0).setWolf(true);
//                }
//        }    
//        return currentMyPlace;  
        System.out.println("------client-----");
        
        InetAddress host = InetAddress.getLocalHost();
        
        //Socket client = new Socket("localhost ",4445);
        Socket client = new Socket(host.getHostName(),4445);


        System.out.println("entered client");

        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String s = createPlaceStateContent(currentMyPlace);
        System.out.println("serialized message-client");


        out.println(s);
        //out.println();

        System.out.println("sent message client");

        String input = in.readLine();
        System.out.println("recived message");

        currentMyPlace = retrievePlaceStateObject(input);
        System.out.println("deserialized message");
        
        client.close();

       
       return currentMyPlace;
       
    }
    
    private TMyPlace updateDogPosition(TMyPlace currentMyPlace) throws JAXBException, IOException {

//        for (int i = 0; i < currentMyPlace.getPlace().size(); i++){
//                if (currentMyPlace.getPlace().get(i).isCow() == true && currentMyPlace.getPlace().get(i).isObstacle() == false){
//                    currentMyPlace.getPlace().get(0).setPosition(currentMyPlace.getPlace().get(i).getPosition());
//                    currentMyPlace.getPlace().get(0).setWolf(true);
//                }
//        }    
//        return currentMyPlace;  
        System.out.println("------client-----");
        
        InetAddress host = InetAddress.getLocalHost();
        
        Socket client = new Socket(host.getHostName(),4446);



        System.out.println("entered client");

        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String s = createPlaceStateContent(currentMyPlace);
        System.out.println("serialized message-client");


        out.println(s);
        //out.println();

        System.out.println("sent message client");

        String input = in.readLine();
        System.out.println("recived message");

        currentMyPlace = retrievePlaceStateObject(input);
        System.out.println("deserialized message");
        
        client.close();

       
       return currentMyPlace;
       
    }
}
