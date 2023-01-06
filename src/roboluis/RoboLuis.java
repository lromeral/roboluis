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
public class RoboLuis extends Robot {
    int numEnemigos;
    boolean atacarRobot;
    int esquinaDestino;

    public void RoboLuis(){
    } 
    
    
    public void escanearTablero(){
        turnLeft(360);
    }
    
    
    public void escanearTablero(double gradosInicio, double gradosFinal){
        //Orientar el robot a inicio
        
        orientarRobotEnGrados (gradosInicio);
        orientarRobotEnGrados (gradosFinal);
        
    }
    
    public void evaluarObjetivo (ScannedRobotEvent robotEnemigo){
        if (robotEnemigo.getDistance() < 30000 || numEnemigos == 1){
            //turnLeft (robotEnemigo.getBearing());
            fire(1);
            
        }
        else{
            out.println ("No disparo");
        }
            
    }
    
    public void  huir(){
   
        out.println ("Dentro Huir: EsquinaDestino " + esquinaDestino);
        
        if (esquinaDestino==1 || esquinaDestino ==4){
            out.println ("Entra en 1-4");
            //Orienta hacia abajo
            orientarRobotEnGrados (180);
            //Avanza tanto como sea necesario dependiendo de la posición hasta que la y=0
            ahead (getY());
            if (esquinaDestino==1){
                turnRight (90);
                ahead (getX());
            }
            else{
                turnLeft (90);
                ahead (getBattleFieldWidth() - getX());
            }
            
        }
        else{
            out.println ("Entra en 2-3");
            orientarRobotEnGrados (0);
            //Sube hasta que la y sea igual a la altura del campo de combate
            ahead (getBattleFieldHeight() - getY());
            if (esquinaDestino==2){
                turnLeft (90);
                ahead (getX());
            }
            else{
                turnRight (90);
                ahead (getBattleFieldWidth() - getX());
            }
        }
        
        //Una vez encaramos la esquina a la que queremos ir
        
        //orientarRobotEnGrados (45 + ((esquinaDestino-1) * 90 ));
        escanearTablero ((esquinaDestino -1)* 90,((esquinaDestino -1)* 90) + 90);
        
    }


    //This method is called when one of your bullets hits another robot.
    @Override
    public void onBulletHit(BulletHitEvent event){

    }
    
    //This method is called when one of your bullets misses, i.e. hits a wall.
    @Override
    public void onBulletMissed(BulletMissedEvent event){

    }
    
    //This method is called when your robot is hit by a bullet
    @Override
    public void onHitByBullet(HitByBulletEvent event) {
    }
    
    //This method is called when your robot collides with another robot.
    @Override
    public void onHitRobot(HitRobotEvent event){

    }
    
    //This method is called when your robot collides with a wall.
    @Override
    public void onHitWall(HitWallEvent event){

    }

    public void orientarRobotEnGrados (double grados){
        
        
        
        double heading = getHeading();
       
        double gradosAjustados = (grados == 0 && heading > 180)?360:grados;
        
        
        out.println ("Heading: " + getHeading() + " Ajustados " + gradosAjustados);
        if (heading > gradosAjustados){
            turnLeft (heading - gradosAjustados);
            out.println ("Izda");
        }
        else{
            turnRight (gradosAjustados - heading);
            out.println ("Dcha");
        }
    }
    
    @Override
    //This method is called every turn in a battle round in order to provide the robot status as a complete snapshot of the robot's current state at that specific time.
    public void onStatus(StatusEvent e){
            numEnemigos = getOthers();
    }
    
    
    //Detecta un robot
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        out.println ("Detectado " + e.getName() + " a " + e.getDistance() );
        stop();
        evaluarObjetivo (e);
        scan();
        resume();
    }
    
    @Override
    public void run(){
        
        //Al arrancar escanear objetivos
        while (true){
            //Si hay mas de 3, esconderse
            escanearTablero();
            if (getOthers() > 0){
                for (esquinaDestino = 1; esquinaDestino <=4;esquinaDestino++){
                    out.println ("EsquinaDestino: " + esquinaDestino);
                    huir();
                }
    
            }

        }
    }
}