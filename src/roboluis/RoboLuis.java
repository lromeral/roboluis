/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package roboluis;
import robocode.*;

/**
 * Robot para asignatura PROGRAMACION
 * @version 202212_01
 * @author lromeral
 * 
 * ROBOCODE API: https://robocode.sourceforge.io/docs/robocode/
 */
public class RoboLuis extends Robot{
    
    double posX;
    double posY;
    double energy;
    double gunHeat;
    double tableroAncho; 
    double tableroAlto;
    double orientacion;

    
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
        while (true){
            if (orientacion > 1){
                    if (orientacion > 0 && orientacion <180) turnLeft (1);
                    if (orientacion >181) turnRight (1);
        }
            else{
            ahead (100);    
            }
        
        }
    }
    //Detecta un robot
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        double distancia;
        double velocidad;
        double bearing;
        double orientacion;
        String nombre;
        
        distancia = e.getDistance();
        velocidad = e.getVelocity();
        orientacion = e.getHeading();
        bearing = e.getBearing();
        nombre = e.getName();
        out.println ("Robot Enemigo" );
        out.println ("Nombre: " + nombre);
        out.println ("Distancia: " + distancia );
        out.println ("Orientacion: " + orientacion);
        out.println ("Velocidad: " + velocidad );
        out.println ("Bearing: " + bearing );
        //fire(1);
    }    
    
    //This method is called when one of your bullets hits another robot.
    @Override
    public void onBulletHit(BulletHitEvent event){
        out.println ("Me han dado");
    }
    
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
    public void onHitWall(HitWallEvent event){
        //Hay que calcular cuanto ocupa el robot en px porque hay veces que choca y no salta el evento
        
        //Estoy en pared izquierda    
        if (posX == 0){
            turnRight(180);
        }
        //Estoy en pared derecha
        else if (posX==tableroAncho){
            turnLeft(180);
        }
    }

    @Override
    //This method is called every turn in a battle round in order to provide the robot status as a complete snapshot of the robot's current state at that specific time.
    public void onStatus(StatusEvent e){
        posX = getX();
        posY = getY();
        energy = getEnergy();
        gunHeat = getGunHeat();
        orientacion = getHeading();
        tableroAlto = getBattleFieldHeight();
        tableroAncho = getBattleFieldWidth();
        out.println ("CoorX: " + posX + "/" + tableroAncho + " CoorY: " + posY + "/" + tableroAlto + " Orientacion: " + orientacion +  " energía: " + energy);
    }
}
