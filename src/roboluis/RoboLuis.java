/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package roboluis;
import robocode.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 * Robot para asignatura PROGRAMACION
 * @version 202212_01
 * @author lromeral
 * 
 * ROBOCODE API: https://robocode.sourceforge.io/docs/robocode/
 */
public class RoboLuis extends Robot {
    
    double posX;
    double posY;
    double energy;
    double gunHeat;
    double tableroAncho; 
    double tableroAlto;
    double orientacion;
    static int meHanDado;
    static int leHeDado;
    static int numRobotsEnCombate;
    int poderDeDisparo;
    boolean hayRobot = false;
    final static double ROBOT_HEIGHT = 36 * 2;
    final static double ROBOT_WIDTH = 36 * 2;
    final static int ESQ_INF_IZDA = 1;
    final static int ESQ_SUP_IZDA = 2;
    final static int ESQ_SUP_DCHA = 3;
    final static int ESQ_INF_DCHA = 4;
    final static int CENTRO = 0;
  
    
    public void RoboLuis(){
        posX = 0.0;
        posY=0.0;
        energy = 100;
        gunHeat = 0;
        tableroAlto = getBattleFieldHeight();
        tableroAncho = getBattleFieldWidth();
        orientacion =0.0;
        poderDeDisparo = 1;
        numRobotsEnCombate = 0;

    } 
    
    public void centroDeDisparo(ScannedRobotEvent enemigo){
        double distanciaDisparo = 50;
        double energiaDisparo = 90;
        boolean disparo = false;
        
        out.println ("Centro de Disparo");
        //Merece la pena disparar? 
        //Depende de:
        //  -Energia Actual
        //  -Calentamiento actual de la torreta
        //  -Le hemos dado anteriormente
        //  -Distancia a la que está
        
        //Si le hemos dado anteriormente, me da igual otra cosa que disparamos
        if (leHeDado > 1){
            disparo = true;
        }
        //No le hemos dado anteriormente, pero...
        else {
            //Estamos sobrantes de energia, por probar que no quede
            if (energy >energiaDisparo){
                disparo = true;
            }
            //No tenemos mucha energia pero....
            else {
                //Esta muy muy cerca
                if (enemigo.getDistance() < distanciaDisparo){
                    disparo = true;
                }
            }
        }
        //Si hay condiciones para disparo, pum!!
        if (disparo) { 
            out.println ("Disparo: " + poderDeDisparo);
            fire(poderDeDisparo);
        }
    }
    
    
    @Override
    public void run(){
        
        //Cuantos robots hay
        numRobotsEnCombate = getOthers();
        
        //Vamos a nuestra esquina preferida
        out.println ("Empieza");
        
        while (true){
            for (int i=1;i<5;i++){
                out.println ("ESquina:" + i);
                irAEsquina (i);
            }
        }
    }
    
    //This method is called when one of your bullets hits another robot.
    @Override
    public void onBulletHit(BulletHitEvent event){
        out.println ("Le hemos dado");
        
        leHeDado++;
        //Si le doy dos veces seguidas, incremento el poder
        if (leHeDado > 1) poderDeDisparo +=3;
    }
    
    //This method is called when one of your bullets misses, i.e. hits a wall.
    @Override
    public void onBulletMissed(BulletMissedEvent event){
        //Si fallo, reseteo el contador de veces que le he dado
        leHeDado = 0;
        //Y bajo el poder de ataque
        poderDeDisparo = 1;
    }
    
    //This method is called when your robot is hit by a bullet
    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        double[] coor = new double[1];

        //Me han dado
        out.println ("Danger!! Me han dado");
        //Quien me ha dado
        out.println ("Nombre: " + event.getName());
        //Vamos a por el o nos vamos a otro sitio?
        
        
        
    
    }
    
    //This method is called when your robot collides with another robot.
    @Override
    public void onHitRobot(HitRobotEvent event){

    }
    
    //This method is called when your robot collides with a wall.
    @Override
    public void onHitWall(HitWallEvent event){
        out.println ("impacto pared");
    }

    @Override
    //This method is called every turn in a battle round in order to provide the robot status as a complete snapshot of the robot's current state at that specific time.
    public void onStatus(StatusEvent e){
        posX = getX();
        posY = getY();
        energy = getEnergy();
        gunHeat = getGunHeat();
        orientacion = getHeading();
        numRobotsEnCombate = getOthers();
        out.println ("CoorX: " + posX + " CoorY: " + posY + " Orientacion: " + orientacion +  "O. Norm: " + normalRelativeAngleDegrees(orientacion)    + " energía: " + energy);
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
        
        hayRobot = true;
        
        //Pasamos el control al centro de disparo
        centroDeDisparo(e);
    }    
    
    //Se desplaza a una esquina
    //Las esquinas estan nombradas 1,2,3 y 4 en sentido horario empezando por la inferior izquierda
    //1 - Esquina inferior Izquierda
    //2 - Esquina Superior Izquierda
    //3 - Esquina Superior Derecha
    //4 - Esquina Inferior Derecha
    public void irAEsquina (int numEsquina){
        out.println ("Ir a esquina");
        switch (numEsquina){
            case 0:
                break;
            case 1:
                //Girar para encarse a la pared izquierda
                girarHastaGrado (getHeading(), 270);
                //Avanzar hasta la pared
                ahead(getX());
                //Gira 90 grados a la izquierda
                turnLeft(90);
                //Avanza hasta la esquina (0,0)
                ahead (getY());
                //Gira 135º para encarse al tablero
                turnLeft (135);
                break;
            case 2:
                //Girar para encarse a la pared izquierda
                girarHastaGrado (getHeading(), 270);
                //Avanzar hasta la pared
                ahead(getX());
                //Gira 90 grados a la izquierda
                turnRight(90);
                //Avanza hasta la esquina (0,0)
                ahead (getBattleFieldHeight() - getY());
                //Gira 135º para encarse al tablero
                turnRight (135);
                break;
            case 3:
                //Girar para encarse a la pared izquierda
                girarHastaGrado (getHeading(), 90);
                //Avanzar hasta la pared
                ahead(getBattleFieldWidth () - getX());
                //Gira 90 grados a la izquierda
                turnLeft(90);
                //Avanza hasta la esquina (0,0)
                ahead (getBattleFieldHeight() - getY());
                //Gira 135º para encarse al tablero
                turnLeft (135);
                break;
            case 4:
                //Girar para encarse a la pared izquierda
                girarHastaGrado (getHeading(), 90);
                //Avanzar hasta la pared
                ahead(getBattleFieldWidth () - getX());
                //Gira 90 grados a la izquierda
                turnRight(90);
                //Avanza hasta la esquina (0,0)
                ahead (getY());
                //Gira 135º para encarse al tablero
                turnRight (135);
                break;
            default:
                break;
        }
    }
    
    private void girarHastaGrado (double origen, double destino){
        if (origen > destino){
            turnLeft (origen - destino);
        }else if (origen < destino){
            turnRight(destino - origen);
        }else{
            //Nada
        }
    }
}
