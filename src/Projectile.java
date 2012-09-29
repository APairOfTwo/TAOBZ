import java.awt.Graphics2D;


public class Projectile extends Sprite {
	
	int frame = 0;
	int anim = 0;
	int animspd = 500;
	int timeAnimSum = 0;
	float speedX = 0;
	float speedY = 0;
	Object pai = null;
	float radius = 2;
	
	public Projectile(float x, float y, float speedX, float speedY, Object pai) {
		super();
		this.x = x;
		this.y = y;
		this.speedX = speedX;
		this.speedY = speedY;
		this.pai = pai;
	}
	
	@Override
	public void selfSimulates(long diftime){		
		timeAnimSum += diftime;
		frame = (timeAnimSum/animspd)%3;
		
		x += speedX*diftime/1000.0f;
		y += speedY*diftime/1000.0f;
		
		int bx = (int)((x)/16);
		int by = (int)((y)/16);
		if(x<0){
			active = false;
		}else if(y<0){
			active = false;
		}else if(x>=(CanvasGame.map.Largura<<4)-1){
			active = false;
		}else if(y>=(CanvasGame.map.Altura<<4)-1){
			active = false;
		}else if(CanvasGame.map.mapLayer1[by][bx]>0){
			active = false;
			//Colisão com layer 2 do mapa
			//geraEfeitoSimples(300, 255, 255, 255);
		}
	}
	
	@Override
	public void selfDraws(Graphics2D dbg, int mapX, int mapY) {}
	
	public boolean colisaoCircular(Character c){
		float dx = (c.x + c.centerX) - (x) ;
		float dy = (c.y + c.centerY) - (y) ;
		float r2 = c.radius + radius;
		r2 = r2*r2;
		if(r2 > ((dx*dx)+(dy*dy))){
			return true;
		}
		return false;
	}
}
