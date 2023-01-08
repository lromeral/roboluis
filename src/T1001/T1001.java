/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package T1001;
import robocode.*;
import static robocode.util.Utils.*;
/**
 * Robot para asignatura PROGRAMACION basado en el robot de ejemplo TRACKER. El funcionamiento del robot es simple. Girar
 * hasta encontrar un objetivo e ir a su ubicacion para confiar en nuestra potencia de fuego y ganar. Si durante
 * 
 * @version 202212_01
 * @author lromeral
 * 
 * ROBOCODE API: https://robocode.sourceforge.io/docs/robocode/
 */
public class T1001 extends Robot {
    String robotASeguir;
    int numEnemigos;
    int contador;
    boolean huir;
    
    /**
     *  Constructor por defecto. Nada en especial
     */
    public void T1001(){
    } 
    
    /** 
     * Calcula el poder de disparo en funcion de la distancia del objetivo 
    *   @param distancia    double  Distancia a la que esta el objetivo
    *   @param max          boolean Es true, ignora la distancia y dispara con la maxima potencia
    *    
    *   @return int                 Potencia de disparo calculada
    **/ 
    public int calcularPoderDeDisparo (double distancia, boolean max){
        
            //Si es maximo, da 10 y sales.
            if (max) return 10;
      
            if (distancia < 100) {
                return 10;
            }
            else if (distancia >=100 && distancia < 150) {
                return 5;
            }
            else if (distancia >= 150 && distancia <200){
                return 2;
            }
            else {
                return 1;
            }
        
    }
    /** 
     * Calcula el poder de disparo en funcion de la distancia del objetivo 
    *   @param distancia    double  Distancia a la que esta el objetivo
    *    
    *   @return int                 Potencia de disparo calculada
    **/ 
     public int calcularPoderDeDisparo (double distancia){
         return this.calcularPoderDeDisparo (distancia, false);
     }

    /** 
     * Evento que se genera cuando nos nuestro disparo acierta
     *
     * @param event    BulletHitEvent  Datos del evento
    **/ 
    @Override
    public void onBulletHit(BulletHitEvent event){
        out.println ("Hemos dado a " + event.getName());
    }
    
    /** 
     * Evento que se genera cuando fallamos un disparo
     * @param event     BulletMissedEvent    Datos del evento
    **/ 
    @Override
    public void onBulletMissed(BulletMissedEvent event){
    }
    
    /**  
     * Evento que se genera cuando nos alcanza un disparo. Vemos si nos ataca
    *  nuestro objetivo y si es asi, nos giramos hacia el.
    *
    *  @param event HitByBulletEvent   Datos del evento
    **/ 
    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        //Si me dan huir rapidamente
        out.println ("Me ha dado: " + event.getName());
        //Me ha dado el que vamos siguiendo
        if (event.getName().equals (robotASeguir)){
            turnRight (event.getBearing());
        }
        else {
            huir = true;
        }
    }
    
    /**
     * Evento generado cuando se colisiona contra otro robot.
     * 
     * @param event HitRobotEvent Datos del evento
     */
    @Override
    public void onHitRobot(HitRobotEvent event){
        double giro;
        
	// Si me choco con otro robot al que no estoy siguiendo, como está mas cerca, seguirlo.
	if (robotASeguir != null && !robotASeguir.equals(event.getName())) {
            out.println("Cambio de objetivo a " + event.getName() + " por choque");
	}
	// Asigno el objetivo
	robotASeguir = event.getName();
        
        //Me giro hacia él y como esta a bocajarro, le disparo a maxima potencia y me echo hacia atrás.
	giro = normalRelativeAngleDegrees(event.getBearing() + (getHeading()));
	turnRight(giro);
	fire(calcularPoderDeDisparo(0, true));
        back(50);
    }
    
    
    /**
     * Evento generado cuando se colisiona contra una pared
     * 
     * @param event HitWallEvent    Datos del evento generado
     */
    
    @Override
    public void onHitWall(HitWallEvent event){
        out.println ("Choque contra pared");
        stop();
        back (100);
        turnRight (180);
        resume ();
    }
    /**
     * Cada turno se ejecuta y reporta los datos actuales del robot
     * 
     * @param e StatusEvent Datos del evento
     */
    @Override
    public void onStatus(StatusEvent e){
            numEnemigos = getOthers ();
    }
    /**
     * Obtiene los datos generados al detectar un robot con el radar.
     * 
     * @param e ScannedRobotEvent   Datos generados por el evento
     */
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        out.println ("onScannedRobot");
        
        //Basado en robot.Tracker
        
        //Si detectamos otro, no seguimos
        if (robotASeguir != null && !e.getName().equals(robotASeguir)){
            return;
        }
        
        // Blanco seleccionado
        if (robotASeguir == null) {
            robotASeguir = e.getName();
            out.println("Blanco fijado: " + robotASeguir);            
	}
        
        //Ponemos a 0 el contador que determina si le hemos perdido definitivamente
        //o solo temporalmente
        contador = 0;
        //Blanco detectado
        if (e.getDistance() > 150){
            out.println ("Yendo a por " + e.getName() + " a una distancia de " + e.getDistance() );
            turnRight (e.getBearing());
            //Todavia esta lejos
            if (e.getDistance() > 300){
                //Nos movemos poco a poco hacia el para poder ir escaneando y realizando otras cosas mientras nos acercamos
                ahead (75);
                scan();
            }else{
                //Esta cerca asi que vamos directamente a por él.
                ahead (e.getDistance() - 50 );
            }
            return;
        }
        
        //Aqui estamos en distancia menor a 300
        out.println ("Disparando a " + e.getName());
        fire (calcularPoderDeDisparo(e.getDistance()));
        
        //Si estamos muy cerca nos echamos hacia atras o hacia delante dependiendo
        //de donde esté
        if (e.getDistance() < 100) {
            if (e.getBearing() > -90 && e.getBearing() <= 90) {  
                back(40);
            } else {
		ahead(40);
            }
            
        }
	scan();   
    }
    /**
     * Metodo que se ejecuta al comienzo.
     */
    
    @Override
    public void run(){
        robotASeguir = null;
        while (true){
            //Si no tenemos a nadie, girar hasta encontrar a alguien
            while (robotASeguir ==null){
                turnLeft(10);
                if (huir) {
                    ahead (50);
                    huir = false;
                }
            }
            contador++;
            // Si perdemos al objetivo mas de 2 turnos, buscarlo a la izquierda
            if (contador > 2 && contador <10) {
                    turnLeft(10);
            }
            // Si todavia perdemos al objetivo en 11 turnos, seguir girando
            if (contador >= 11 && contador <20) {
                    turnRight (10);
            }
            // Y si no le vemos en mucho tiempo, o esta muerto o se ha escondido
            if (contador >= 21) {
                    robotASeguir = null;
            }
        }
    }
}