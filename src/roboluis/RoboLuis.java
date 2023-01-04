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
    int esquinaDestino;
    boolean trayectoInterrumpido;
    boolean enTrayecto;
    
    public void RoboLuis(){
        posX = 0.0;
        posY=0.0;
        energy = 100;
        gunHeat = 0;
        orientacion =0.0;
        numRobotsEnCombate = 0;
        meHanDado = 0;
        leHeDado = 0;
    } 
    
    public void centroDeDisparo(ScannedRobotEvent enemigo){
        final double distanciaDisparo = (tableroAlto + tableroAncho)/4;
        final double energiaDisparo = 90;
        final int impactosAnteriores = 1;
        boolean disparo = false;
        
        //Se ha solicitado una evaluación de disparo por lo que hay que parar todo
        
        
        //stop();
        //if (enTrayecto) trayectoInterrumpido = true;
        
        out.println ("Centro de Disparo");
        //Merece la pena disparar? 
        //Depende de:
        //  -No hay sobrecalentamiento (getGunHeat() < 0)
        //  -Energia Actual
        //  -Calentamiento actual de la torreta
        //  -Le hemos dado anteriormente
        //  -Distancia a la que está
        
        
        //Condiciones:
        out.println ("Condiciones de disparo:");
        out.println ("Calentamiento: " + gunHeat);
        out.println ("Impacto anterior: "  + impactosAnteriores + "/" + leHeDado);
        out.println ("Energia: " + energiaDisparo + "/" + energy);
        out.println ("Distancia: " + enemigo.getDistance() + "/" + distanciaDisparo);
        
        //Si le hemos dado anteriormente, me da igual otra cosa que disparamos
        if (getGunHeat() <= 0){
            if (leHeDado > impactosAnteriores) {
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
        }
        else{
            out.println ("No puedo disparar. GunHeat: " + gunHeat);
        }
        //Si hay condiciones para disparo, pum!!
        if (disparo) { 
            out.println ("Disparando: " + poderDeDisparo);
            fire(poderDeDisparo);
        }
        //Devolvemos el control
        //resume ();
    }

    @Override
    public void run(){  
        //Variables varias
        poderDeDisparo = 1;
        //Cuantos robots hay
        numRobotsEnCombate = getOthers();
        //Vamos a nuestra esquina preferida
        while (true){
            for (esquinaDestino=1;esquinaDestino<5;esquinaDestino++){
                out.println ("Objetivo: Esquina " + esquinaDestino);
                irAEsquina (esquinaDestino);
                escanearTablero();
            }
        }
    }
    
    //This method is called when one of your bullets hits another robot.
    @Override
    public void onBulletHit(BulletHitEvent event){
        //TODO: Incrementar solo si al robot que le hemos dado es el mismo
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
        double locEnemigo;
        //Quien me ha dado
        
        out.println ("Me ha disparado: " + event.getName());       
        //¿Donde está el que me ha dado? 
        //Relativo a mi posicion -180 a 180
        locEnemigo = event.getBearing();
        out.println ("Desde: " + locEnemigo );
        
        //TODO: Evaluar si merece la pena dispararle o huir
    }
    
    //This method is called when your robot collides with another robot.
    @Override
    public void onHitRobot(HitRobotEvent event){
            out.println  ("Colision con:" + event.getName());
            //TODO:Proteger al robot contra colisiones
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
        tableroAlto = getBattleFieldHeight();
        tableroAncho = getBattleFieldWidth();
        energy = getEnergy();
        gunHeat = getGunHeat();
        orientacion = getHeading();
        numRobotsEnCombate = getOthers();
//        out.println ("CoorX: " + posX + " CoorY: " + posY + " Orientacion: " + orientacion +  "O. Norm: " + normalRelativeAngleDegrees(orientacion)    + " energía: " + energy);
//        out.println ("Trayecto Interrumpido: " + trayectoInterrumpido);
//        out.println ("En Trayecto: " + enTrayecto);
//        out.println ("Destino: "+ esquinaDestino);
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
        
        out.println ("GunHeat: " + getGunHeat()); 
        
        //Pasamos el control al centro de disparo
        centroDeDisparo(e);
        
    }    
    
    private void escanearTablero (){
        this.escanearTablero (0,180);
    }
    
    
    private void escanearTablero (double gradosInicio, double gradosFinal){
        //Donde estoy para girar el tablero
        
        out.println ("Escanear cuadrante: " + getCuadrante(posX, posY));
        //Como tiene vueltas infinitas, buscamos origen por el giro mas corto dependiendo de la posicion inicial
         
        
        orientarRobotEnEsquina (getCuadrante(posX, posY));
   
        //Despues de colocarnos, escaneamos los 90 grados
        if (orientacion <=0){
            //Desde 0 a 90º y desde 90º a 0º
            turnRight (90);
            turnLeft (90);
        }
        else{
            turnLeft( 90);
            turnRight (90);
                }
   
 
        out.println ("Fin escanear tablero");
    }
    
    //Se desplaza a una esquina
    //Las esquinas estan nombradas 1,2,3 y 4 en sentido horario empezando por la inferior izquierda
    //1 - Esquina inferior Izquierda
    //2 - Esquina Superior Izquierda
    //3 - Esquina Superior Derecha
    //4 - Esquina Inferior Derecha
    public void irAEsquina (int numEsquina){
        //Activamos flag por si el trayecto se interrumpe con un enemigo
        enTrayecto = true;
        trayectoInterrumpido = false;
        out.println ("Ir a esquina");
        switch (numEsquina){
            case 0:
                break;
            case 1:
                //Girar para encarse a la pared izquierda
                girarHastaGrado (orientacion, 270);
                //Avanzar hasta la pared
                ahead(posX);
                //Gira 90 grados a la izquierda
                turnLeft(90);
                //Avanza hasta la esquina (0,0)
                ahead (posY);
                //Gira 135º para encarse al tablero
                turnLeft (90);
                break;
            case 2:
                //Girar para encarse a la pared izquierda
                girarHastaGrado (orientacion, 270);
                //Avanzar hasta la pared
                ahead(posX);
                //Gira 90 grados a la izquierda
                turnRight(90);
                //Avanza hasta la esquina (0,0)
                ahead (tableroAlto - posY);
                //Gira 135º para encarse al tablero
                turnRight (90);
                break;
            case 3:
                //Girar para encarse a la pared izquierda
                girarHastaGrado (orientacion, 90);
                //Avanzar hasta la pared
                ahead(tableroAncho - posX);
                //Gira 90 grados a la izquierda
                turnLeft(90);
                //Avanza hasta la esquina (0,0)
                ahead (tableroAncho - posY);
                //Gira 135º para encarse al tablero
                turnLeft (90);
                break;
            case 4:
                //Girar para encarse a la pared izquierda
                girarHastaGrado (orientacion, 90);
                //Avanzar hasta la pared
                ahead(tableroAncho - posX);
                //Gira 90 grados a la izquierda
                turnRight(90);
                //Avanza hasta la esquina (0,0)
                ahead (posY);
                //Gira 135º para encarse al tablero
                turnRight (90);
                break;
            default:
                break;
        }
        //Movimiento completado
        enTrayecto = false;
    }
    
    private void orientarRobotEnEsquina (int cuadrante){
    
        double orientacionInicio, orientacionFinal;
        
        //Ajustamos valores dependiendo del cuadrante donde esté
        switch (cuadrante){
            case 1:
                orientacionInicio = 0;
                orientacionFinal = 90;
                break;
            case 2:
                orientacionInicio = 90;
                orientacionFinal = 180;
                break;
            case 3:
                orientacionInicio = 180;
                orientacionFinal = 270;
                break;
            case 4:
                orientacionInicio = 270;
                orientacionFinal = 0;
                break;
            default:
                orientacionInicio = 0;
                orientacionFinal = 0;
                break;
                
        }

        //Comienza la orientacion
        if (orientacion >= orientacionInicio && orientacionFinal <=90){
            out.println ("Bien orientado");
           //Estoy bien orientado. Giro a 0º o a 90º (lo que este mas cerca y desde esa posicion voy a la contraria
           if (orientacion >orientacionFinal/2){
               turnRight (normalizarAngulo(orientacionFinal - orientacion));
           }
           else{
               turnLeft (orientacion);
           }
        }
        else{
            out.println ("MAL orientado");
            //Estoy mal orientado (apuntando fuera del tablero).
            if (orientacion > (normalizarAngulo (180 + orientacionFinal/2))){
                turnLeft (orientacionInicio - orientacion);
            }
            else {
                turnRight (orientacion);
            }
        }
    }
    
    
    private int getCuadrante (double x, double y){
        int cuadrante=0;
        // 2 | 3
        // --|---
        // 1 | 4      
        //Cuadrantes 1 y 2
        if (x <= (tableroAncho/2)){
            //Cuadrante 1
            if (y <= tableroAlto/2){
                out.println ("Cuadrante 1");
                cuadrante=1;
            } 
            else{
                out.println ("Cuadrante 2");
                cuadrante = 2;
            }
            //Cuadrantes 3 y 4
        }
        else{
            //Cuadrante 3
            if (x > (tableroAncho/2)){
                out.println ("Cuadrante 3");
                cuadrante = 3;
            }else {
                out.println ("Cuadrante 4");
                cuadrante = 4;
            }
        }
        out.println ("getCuadranteReturn: " + cuadrante);
        return cuadrante;
    }
    
    private void girarHastaGrado (double origen, double destino){
        double cuantoGiro;
        //Valor absoluto porque vamos a girar o a izda o dcha
       cuantoGiro = Math.abs (destino - origen);
        out.println ("girarHastaGrado Origen: " + origen + " Destino:" + destino + " CuantoGiro: " + cuantoGiro);
        
        //Giro por el camino mas corto
        //TODO: Mejorar el giro en esquinas para que encare siempre el tablero
            if (origen >= destino) turnLeft(cuantoGiro);
            else turnRight (cuantoGiro);       
    }
    
    private double normalizarAngulo (double angulo){
        //En grados. Si es mayor de 360 de
        return Math.abs (angulo) > 360 ? angulo%360:angulo;
    }
    

}