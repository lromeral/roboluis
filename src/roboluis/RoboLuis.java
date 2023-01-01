/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package roboluis;
import robocode.*;
import robocode.util.*;
import robocode.AdvancedRobot;
import java.util.*;

/**
 * Robot para asignatura PROGRAMACION
 * @version 202212_01
 * @author lromeral
 * 
 * ROBOCODE API: https://robocode.sourceforge.io/docs/robocode/
 */
public class RoboLuis extends AdvancedRobot {
    
    double posX;
    double posY;
    double energy;
    double gunHeat;
    double tableroAncho; 
    double tableroAlto;
    double orientacion;
    final static double ROBOT_HEIGHT = 36 * 2;
    final static double ROBOT_WIDTH = 36 * 2;
    
    Robot[] enemigos;
    
    public void RoboLuis(){
        posX = 0.0;
        posY=0.0;
        energy = 100;
        gunHeat = 0;
        tableroAlto = getBattleFieldHeight();
        tableroAncho = getBattleFieldWidth();
        orientacion =0.0;
    }
    

    @Override
    public void run(){
        double xToGo = 0;
        double yToGo = 0;
        
        while (true){
            posX = getX();
            posY = getY();
            
            if (posX < ROBOT_WIDTH && posY < ROBOT_HEIGHT){
                xToGo = tableroAncho;
                yToGo = tableroAlto;
            }
            else if (posX > tableroAncho - ROBOT_WIDTH && posY > tableroAlto + ROBOT_HEIGHT){
                xToGo = 0;
                yToGo = 0;
            }
            
            
            goTo(xToGo,yToGo);
            execute();
        }
    }
    //Detecta un robot
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        out.println ("Robot encontrado: " + e.getName());
        out.println ("Robot a:" + e.getDistance());
        out.println ("Robot Orientacion: " + e.getHeading());
        out.println ("Robot Velocidad: "+ e.getVelocity());
        out.println ("Robot energia: " + e.getEnergy());
        out.println ("Robot bearing: " + e.getBearing());
    }   
    
    //This method is called when one of your bullets hits another robot.
    @Override
    public void onBulletHit(BulletHitEvent event){}
    
    //This method is called when one of your bullets misses, i.e. hits a wall.
    @Override
    public void onBulletMissed(BulletMissedEvent event){}
    
    //This method is called when your robot is hit by a bullet
    @Override
    public void onHitByBullet(HitByBulletEvent event) {}
    
    //This method is called when your robot collides with another robot.
    @Override
    public void onHitRobot(HitRobotEvent event){}
    
    //This method is called when your robot collides with a wall.
    @Override
    public void onHitWall(HitWallEvent event){}

    @Override
    //This method is called every turn in a battle round in order to provide the robot status as a complete snapshot of the robot's current state at that specific time.
    public void onStatus(StatusEvent e){
        //posX = getX();
        //posY = getY();
        energy = getEnergy();
        gunHeat = getGunHeat();
        orientacion = getHeading();
        out.println ("CoorX: " + posX + " CoorY: " + posY + " Orientacion: " + orientacion +  " energía: " + energy);
    }
    
/**
 * This method is very verbose to explain how things work.
 * Do not obfuscate/optimize this sample.
 */
private void goTo(double x, double y) {
	/* Transform our coordinates into a vector */
	x -= getX();
	y -= getY();
	
	/* Calculate the angle to the target position */
	double angleToTarget = Math.atan2(x, y);
	
	/* Calculate the turn required get there */
	double targetAngle = Utils.normalRelativeAngle(angleToTarget - getHeadingRadians());
	
	/* 
	 * The Java Hypot method is a quick way of getting the length
	 * of a vector. Which in this case is also the distance between
	 * our robot and the target location.
	 */
	double distance = Math.hypot(x, y);
	
	/* This is a simple method of performing set front as back */
	double turnAngle = Math.atan(Math.tan(targetAngle));
	setTurnRightRadians(turnAngle);
	if(targetAngle == turnAngle) {
		setAhead(distance);
	} else {
		setBack(distance);
	}
}
}
