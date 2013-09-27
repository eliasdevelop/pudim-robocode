package HE;
import robocode.*;
import java.awt.Color;
import robocode.util.Utils;

/**
 * Pudim - a robot by (Elias & Luis)
 */

public class Pudim extends AdvancedRobot {	
	
	static double direcao = 1.0D;	//Variavel usada para mudar a direção do robot: direcao = frente / -direcao = trás
	
	//Metódo Basico
	public void run() {
		setColors(new Color(205,133,63),Color.black,Color.black); //Cores do Robot
        setScanColor(new Color(255,215,0));                       //Cor do Scan
        setBulletColor(Color.black);                              //Cor do Tiro
		setAdjustGunForRobotTurn(true);                           //Desmembra o canhão do resto do robot, fazendo mover-se separadamente
		setTurnRadarRight((1.0D / 0.0D)); 		                  //Gira o radar pela direita
    }
    
    //Metódo criado para mirar no inimigo recebendo informacoes do inimigo. Ex: distancia, angulo absoluto...
    public void mire(double direcaoInimigo, double velocidadeInimigo, double distanciaInimigo, 
	                 double posicaoInimigoRadiano, double direcaoInimigoRadiano) {	          
	//Calcula o angulo absoluto ao robot
	double localInim = direcaoInimigo + getHeading() - getGunHeading();  		              
    //Calculo basico para ficar girando o canhão até estar na direção do inimigo	
	if (!(localInim > -180 && localInim <= 180)) {	                                          	
		while (localInim <= -180D) {
			localInim = localInim + 360;
		}
		while (localInim > 180D) {
			localInim = localInim - 360;
		}
	} 
      //Se a distancia for Maior que 300Pixels faz um calculo em radianos, simulando uma possivel futura posição do robot usando a velocidade do inimigo e a velocidade da bala
      if(distanciaInimigo > 300D){  
      double localInimRadiano = direcaoInimigoRadiano + getHeadingRadians();
      setTurnGunRightRadians(Utils.normalRelativeAngle((localInimRadiano - getGunHeadingRadians()) + (velocidadeInimigo * Math.sin(posicaoInimigoRadiano - localInimRadiano)) / 12.5D));
      //Se não, simplismente usa o calculo acima apontando diretamento pro robot
      } else {
      setTurnGunRight(Utils.normalRelativeAngleDegrees(localInim));
      }
    }
    //Metódo ao Escanear um robot   
	public void onScannedRobot(ScannedRobotEvent e) {
	   //Vira pra direita, fazendo um movimento de pêndulo	  	
	   setTurnRight(Utils.normalRelativeAngleDegrees(e.getBearing() + 90 + -20 * direcao));		
	   //Se não restar mais nada a andar muda a direção do robo e anda pra frente ou pra traz de maneira random 
		if(getDistanceRemaining() == 0.0D) {
            direcao = -direcao;
            setAhead(Math.random() * 150D * direcao);
        }		        
        //Mira no robot
        mire(e.getBearing(),e.getVelocity(),e.getDistance(),e.getHeadingRadians(), e.getBearingRadians());     
		
		//Se a distancia for maior que 400Pixels
	    if (e.getDistance() > 400D) {  
		 setFire(2D);		                       //Tiro de força 2    
	    //Se estiver entre 400 e 50Pixels
	    } else if (e.getDistance() > 50D) {                
		 setFire(2.5D);                            //Tiro de força 2.5   
	    //Se for menor que 50Pixels
	    } else {		                                   
	 	 setFire(3D);                              //Tiro de força 3      
        }
        //Vira o radar pra esquerda, o angulo restante
	    setTurnRadarLeft(getRadarTurnRemaining());                           
    }	 
	//Metódo da Vitoria \õ/   
    public void onWin(WinEvent event){                 
        for (int i = 0; i < 50; i++){
	       turnRight(30);                //Simula uma risadinha tremendo o robot :D~
	       turnLeft(30);
        }
    }
}