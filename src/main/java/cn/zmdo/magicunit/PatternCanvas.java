package cn.zmdo.magicunit;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class PatternCanvas extends Canvas {
	
	private String seed;
	private double patternRadius = 64;
	
	public PatternCanvas(double width,double height) {
		super(width,height);
		repaint();
	}
	
	public void generatePattern(String seed) {
		this.seed = seed;
		repaint();
	}
	
	private void repaint() {
		GraphicsContext g2d = getGraphicsContext2D();
		g2d.save();
		
		// 清除原来的图像
		g2d.clearRect(0, 0, getWidth(), getHeight());
		
		// 背景填充
		g2d.setFill(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		// 位移
		g2d.translate(2, 2);
		if (seed !=null && !seed.trim().equals("")) {
			
			drawCenter(g2d ,getNumber(seed.charAt(0)));
			drawElement(g2d ,getNumber(seed.charAt(1)));
			
			drawRing1(g2d , getNumber(seed.charAt(2)));
			drawRing2(g2d , getNumber(seed.charAt(3)));
			
		}
		
		g2d.restore();
	}

	private int getNumber (char ch) {
		if ('0' <= ch && ch <= '9') {
			return ch - '0';
		} else if ('a' <= ch && ch <= 'z') {
			return ch - 'a' + 10 ;
		} else if ('A' <= ch && ch <= 'Z') {
			return ch - 'A' + 36;
		}
		return 0;
	}
	
	private void drawCenter(GraphicsContext g2d,int n ) {
		double r = patternRadius - 23;
		if(n > 32) {
			drawSun(
					g2d, 
					18 +  n/8, 
					28 - n%8 , 
					r - 3);
		} else {
			double moonR = r - 5;
			drawMoon(g2d,moonR,moonR,120 - n*4);
		}
	}
	
	private void drawRing1( GraphicsContext g2d,int n ) {
		g2d.save();
		
		n = n+3;
		g2d.setStroke(Color.BLACK);
		g2d.strokeOval(0, 0, patternRadius*2, patternRadius*2);
		
		double r = patternRadius;
		double innerR = patternRadius - 4;
		double angle = 0;
		
		g2d.setStroke(Color.BLACK);
		double da = 360.0/n;
		for (int i = 0 ; i < n; i ++) {
			angle = i*da;
			g2d.strokeLine ( 
					patternRadius + r*Math.sin(Math.toRadians(angle + 90)) ,
					patternRadius + r*Math.cos(Math.toRadians(angle + 90)) , 
					patternRadius + innerR*Math.sin(Math.toRadians(angle + 90)) , 
					patternRadius + innerR*Math.cos(Math.toRadians(angle + 90)));
		}
		
		g2d.strokeOval( patternRadius - innerR, patternRadius - innerR , innerR*2, innerR*2 );
		g2d.restore();
	}
	
	private void drawRing2(GraphicsContext g2d,int n) {
		g2d.save();
		
		g2d.setStroke(Color.BLACK);
		
		int len = n/16;
		int block = n%16 + 1;
		
		drawDecoration(g2d,patternRadius - 8 ,patternRadius - 13 - len,block);
		
		if (len == 0) {
			double r = patternRadius - 19;
			g2d.strokeOval(patternRadius - r, patternRadius - r , 2*r, 2*r);
		} else if (len == 1) {
			double r = patternRadius - 20;
			g2d.strokeOval(patternRadius - r, patternRadius - r , 2*r, 2*r);
		}
		
		double r = patternRadius - 23;
		g2d.strokeOval(patternRadius - r, patternRadius - r , 2*r, 2*r);	
		
		g2d.restore();
	}
	
	private void drawSun(GraphicsContext g2d , int light,double sunR,double lightR ) {
		
		g2d.save();
		
		double da = 360.0/light;
		double angle = 0;
		double ta = da*2.0/3.0 ; // 每道光占的角度
		
		double r = lightR; // 光芒半径
		double dr = 3; // 光跟太阳间的间隔
		double sr = sunR ; // 太阳半径
		double tr = sr + dr; // 光芒开始出现的半径
		
		for (int i = 0 ; i < light; i ++) {
			
			angle = i*da;
			
			g2d.strokeArc(patternRadius - r, patternRadius - r, 2*r, 2*r, angle, ta , ArcType.OPEN);
			g2d.strokeArc(patternRadius - tr, patternRadius - tr, 2*tr, 2*tr, angle, ta , ArcType.OPEN);

			g2d.strokeLine (
					patternRadius + r*Math.sin(Math.toRadians(angle + 90)),
					patternRadius + r*Math.cos(Math.toRadians(angle + 90)),
					patternRadius + tr*Math.sin(Math.toRadians(angle + 90)),
					patternRadius + tr*Math.cos(Math.toRadians(angle + 90)));
			
			g2d.strokeLine ( 
					patternRadius + r*Math.sin(Math.toRadians(angle + ta + 90)) ,
					patternRadius + r*Math.cos(Math.toRadians(angle + ta + 90)) , 
					patternRadius + tr*Math.sin(Math.toRadians(angle + ta + 90)) ,
					patternRadius + tr*Math.cos(Math.toRadians(angle + ta + 90)));
			
		}
		
		g2d.strokeOval(patternRadius - sr, patternRadius - sr, 2*sr, 2*sr);
		
		g2d.restore();
		
	}
	
	private void drawMoon(GraphicsContext g2d,double moonR,double lackCycleR,double lossAngle) {
		g2d.save();
		
		double r = moonR; // 月亮半径
		double lackR = lackCycleR; // 缺口半径
		
		// 计算出缺口圆的圆心到缺口的垂直距离
		double ts = Math.sqrt( lackR*lackR - Math.pow( r*Math.sin( Math.toRadians(lossAngle/2) ) , 2 ));
		// 计算缺口圆圆心横坐标
		double x = patternRadius - (ts + r*Math.cos( Math.toRadians(lossAngle/2) )) ;
		// 计算缺口圆对应的缺口角度
		double dAngle = Math.toDegrees( Math.acos( ts/lackR ) ) * 2;
		
		g2d.strokeArc( patternRadius - r, patternRadius - r, 2*r, 2*r, 180 + lossAngle/2 , 360 - lossAngle ,ArcType.OPEN);
		g2d.strokeArc( x - lackR , patternRadius - lackR ,  2*lackR,  2*lackR , - dAngle/2 , dAngle ,ArcType.OPEN);
		
		g2d.restore();
	}
	
	private void drawElement( GraphicsContext g2d ,int n ) {
		g2d.save();
		
		double r = patternRadius - 23;
		g2d.arc(patternRadius, patternRadius, r, r,0,360);
		g2d.clip();
		
		if (n < 60) {
			int seg = n/12;
			int ds = n%12;
			switch(seg) {
			case 0:
				// 金 - 铎
				{
					double width = 32 - ds;
					double height = 1.25*width;
					drawDuo(g2d,r,width,height);
				}
				break;
			case 1:
				// 木 - 花
				{
					double height = 30;
					double angle = 56;
					int petal = ds/2 + 2;
					if (ds%2 == 1) {
						angle = angle - 5;
						height = height + 5;
					}
					drawFlower(g2d,r,petal,height,angle);
				}
				break;
			case 2:
				// 水 - 浪
				{
					double sprayHeight = 32 - ds;
					drawSpray(g2d,r,sprayHeight ,3);	
				}
				break;
			case 3:
				// 火 - 火
				drawFire(g2d,r,ds/4);
				break;
			case 4:
				// 土 - 山
				{
					double height = 31 - ds;
					double topWidth = 46 - ds;
					drawMountain(g2d,r,height,topWidth);
				}
				break;
			}
		} else if (n == 60) {
			// 地
			drawMountain(g2d,r, 31,46);
			drawSpray(g2d,r,16 ,3);
		} else if (n == 61) {
			// 天
			
		}
		g2d.restore();
	}
	
	private void drawDuo(GraphicsContext g2d, double r,double width,double height) {
		g2d.save();
		
		double headWidth = width*(1d/4d);
		double headHeight = height*(1d/7d);
		double bodyHeight = height - headHeight;
		double topWidth= 0.8*width;
		
		double p = 0.3; // 中间装饰占的比例
		
		// 绘制铎的头部
		g2d.translate(patternRadius, patternRadius + r - height*1.1);
		g2d.fillRect( - headWidth/2, 0, headWidth, headHeight);
		g2d.strokeRect( - headWidth/2, 0, headWidth, headHeight);
		
		// 绘制铎的主体
		g2d.translate(0, headHeight);
		g2d.beginPath();
		
		g2d.moveTo(- topWidth/2, 0);
		g2d.quadraticCurveTo(-width/2,bodyHeight/2,-width/2,bodyHeight);
		g2d.quadraticCurveTo(0,bodyHeight/2,width/2,bodyHeight);
		g2d.quadraticCurveTo(width/2,bodyHeight/2,topWidth/2,0);
		g2d.lineTo(-topWidth/2, 0);
		
		g2d.fill();
		g2d.stroke();
		g2d.clip();
		
		double dh = 5;
		double n = height/dh;
		for (int i = -(int) n - 1 ; i < n ; i ++) {
			g2d.strokeLine(
					-width/2,
					-width/2 + i*dh, 
					width/2, 
					width/2 + i*dh);
			g2d.strokeLine(
					width/2,
					- width/2 + i*dh, 
					- width/2, 
					width/2 + i*dh);
		}
		
		double a = (1-p) * (1d/3d);
		double b = (1-p) * (1- 1d/3d);
		g2d.fillRect(-width/2, 0, width,height*a);
		g2d.strokeRect(-width/2, 0, width,height*a);
		
		g2d.fillRect(-width/2, height*(1-b), width,height*b);
		g2d.strokeRect(-width/2, height*(1-b), width,height*b);
		
		g2d.stroke();
		g2d.closePath();
		
		g2d.restore();
	}
	
	private void drawFire(GraphicsContext g2d,double r,double n) {
		
		double p = n;
		
		double dx = patternRadius;
		double dw = 15 + p;
		double dh = dw*1.5;
		double bottom = 5;
		
		drawFlame(g2d,dx - dw,patternRadius + r - bottom,dw,dh,false);
		drawFlame(g2d,dx + dw,patternRadius + r - bottom,dw,dh,true);
		
		double cy = patternRadius + r - (bottom - 3);
		double ch = dh/2;
		
		g2d.beginPath();
		g2d.moveTo(dx, patternRadius - ch);
		g2d.quadraticCurveTo(dx - dw, cy, dx, cy);
		g2d.quadraticCurveTo(dx + dw, cy, dx, patternRadius - ch);
		g2d.fill();
		g2d.stroke();
		g2d.closePath();
		
		double cr = 5 + p*0.5;
		double y = patternRadius + r - bottom - 2*cr;
		g2d.strokeOval(dx - cr, y, 2*cr, 2*cr);
	}
	
	private void drawFlame(GraphicsContext g2d, double rootX,double rootY ,double dw,double dh,boolean flip ) {
		
		g2d.save();
		
		double top = 1/5;
		
		double dm = dh*(1 - top);
		double dn = dw*(1 - 2d/5d);
		double kw = dw - dn;
		double kh = dh*0.8;
		double fh =dh*0.4;
		
		g2d.translate(rootX, rootY);
		
		if (flip) {
			g2d.scale(-1, 1);
		}
		
		g2d.beginPath();
		
		// 外曲线
		g2d.moveTo(
				- dw, - dh
				);
		
		g2d.bezierCurveTo(
				0, - dm, 
				- dn, 0, 
				0, 0
				);

		g2d.bezierCurveTo(
				kw, 0, 
				kw, - kh, 
				kw*0.5, - kh*1.2
				);
		
		// 内曲线
		g2d.bezierCurveTo(
				kw*0.6, - kh, 
				kw*0.5 , - fh, 
				0, - fh
				);
		
		g2d.bezierCurveTo(
				- dn*0.5, - fh, 
				kw*0.5 , - dm*(1 - top ), 
				- dw, - dh
				);
		
		g2d.fill();
		g2d.stroke();
		g2d.closePath();
		
		g2d.restore();
	}
	
	private void drawFlower(GraphicsContext g2d,double r, int n , double height,double angle) {
		
		// 这个cr是指用于画出花瓣的圆的半径
		double cr = 22;
		
		int dt = n;
		
		cr = cr + dt;
		angle = angle + dt;
		
		double t = angle/dt;
		boolean flag = dt < 5;
		for (int i = dt - 1; i != 0 ; i --) {
			drawPetal(g2d, r, cr, height, t*i,flag);
			drawPetal(g2d, r, cr, height, -t*i,flag);
		}
		
		// 画两边
		drawPetal(g2d, r, cr, height,angle,true);
		drawPetal(g2d, r, cr, height, - angle,true);
		
		// 画中心花瓣
		drawPetal(g2d, r, cr, height,0,true);
		drawPetal(g2d, r, cr, height,0,true);
	}
	
	private void drawPetal(GraphicsContext g2d,double r ,double cr,double height,double angle,boolean innerStroke) {
		
		double sy = height/2;
		double sx = Math.sqrt(cr*cr - sy*sy);
		
		double da = Math.toDegrees(Math.asin(height/(2*cr)));
		
		// 内描边的数值计算
		double padding = 2.5;
		double innerR = cr - padding;
		double b = cr*Math.cos(Math.toRadians(da));
		double c = innerR - b;
		double innerDa = Math.toDegrees(Math.asin(Math.sqrt(c*c + 2*b*c)/innerR));
		
		// 绘制第一半
		double cos = Math.cos(Math.toRadians( - angle));
		double sin = Math.sin(Math.toRadians( - angle));
		
		double dx = sx*cos - sy*sin;
		double dy = sx*sin + sy*cos;

		g2d.fillArc(
				patternRadius + dx - cr, 
				patternRadius + r - dy - cr ,
				cr*2  , 
				cr*2 , 
				180 - da - angle , 
				2*da , 
				ArcType.OPEN);
		g2d.strokeArc(
				patternRadius + dx - cr, 
				patternRadius + r - dy - cr ,
				cr*2  , 
				cr*2 , 
				180 - da - angle , 
				2*da , 
				ArcType.OPEN);
		
		// 绘制内描边
		if (innerStroke) {
			g2d.strokeArc(
					patternRadius + dx - innerR, 
					patternRadius + r - dy - innerR ,
					innerR*2  , 
					innerR*2 , 
					180 - innerDa - angle , 
					2*innerDa , 
					ArcType.OPEN);
		}
		
		// ------------------------------------------- //
		// 绘制另一半
		double symmetricRotateAngle = (180 - 2*da - angle);
		cos = Math.cos( Math.toRadians( symmetricRotateAngle ));
		sin = Math.sin( Math.toRadians( symmetricRotateAngle ));
		
		dx = sx*cos - sy*sin;
		dy = sx*sin + sy*cos;
		
		g2d.fillArc(
				patternRadius + dx - cr, 
				patternRadius + r - dy - cr ,
				cr*2  , 
				cr*2 , 
				- da - angle, 
				2*da, 
				ArcType.OPEN);
		g2d.strokeArc(
				patternRadius + dx - cr, 
				patternRadius + r - dy - cr ,
				cr*2  , 
				cr*2 , 
				- da - angle, 
				2*da, 
				ArcType.OPEN);
		
		// 绘制内描边
		if (innerStroke) {
			
			g2d.strokeArc(
					patternRadius + dx - innerR, 
					patternRadius + r - dy - innerR,
					innerR*2  , 
					innerR*2 , 
					- innerDa - angle, 
					2*innerDa, 
					ArcType.OPEN);
		}
		
	}
	
	// 画山
	private void drawMountain(GraphicsContext g2d,double r,double height,double topWidth) {

		for (int i = 0; i < 3 ; i ++) {
			
			double dh = height;
			double dtw = topWidth;
			
			for (int j = 0 ; j < 3 ; j ++) {
				drawMountainUnit(g2d,r,dh,dh/6,dtw,dtw*(1.2 + 0.125*i),true);
				dh -= 3;
				dtw -= 4;
			}
			
			height = height*1.25;
			topWidth = topWidth/1.6;
		}
		
	}
	
	// 画一个单元山
	private void drawMountainUnit(GraphicsContext g2d, double r, double height, double topHeight, double topWidth, double bottomWidth, boolean clear) {
		
		double topX = patternRadius;
		double topY = patternRadius + r - height;
		double bottomY = patternRadius + Math.sqrt( r*r - Math.pow((bottomWidth)/2,2) );
		
		// 计算点
		double xPoints[] = new double[]{
				patternRadius + bottomWidth/2,
				topX + topWidth/2,
				topX , 
				topX - topWidth/2 ,
				patternRadius - bottomWidth/2 };
		double yPoints[] = new double[]{
				bottomY,
				topY + topHeight , 
				topY , 
				topY + topHeight, 
				bottomY};
		
		// 遮盖背景
		if (clear) {
			double angle = Math.toDegrees( Math.acos( (bottomY - patternRadius )/r ) )*2; 
			g2d.fillArc(patternRadius - r,  patternRadius - r, 2*r, 2*r ,  270 - angle/2 ,  angle,  ArcType.CHORD);
			g2d.fillPolygon(xPoints,yPoints, xPoints.length);
		}
		
		g2d.strokePolyline(xPoints, yPoints, xPoints.length);
	}
	
	// 画水
	private void drawSpray(GraphicsContext g2d,double r,double sprayHeight,double spacing) {
		
		g2d.save();
		
		double dr = sprayHeight;
		double initAngle = Math.toDegrees(Math.atan(r/dr)) ;
		double s = Math.sqrt (r*r + dr*dr);
		double n = 0;
		
		// 画中间浪
		dr = sprayHeight;
		n = dr /spacing;
		for (int i =0 ; i <n ; i ++) {
			double dAngle = Math.toDegrees(Math.acos(dr/(2*sprayHeight)));
			if( i == 0) {
				g2d.fillArc(patternRadius - dr , patternRadius + r - dr , 2*dr, 2*dr, dAngle, 180-2*dAngle, ArcType.ROUND);
			}
			g2d.strokeArc(patternRadius - dr , patternRadius + r - dr , 2*dr, 2*dr, dAngle, 180-2*dAngle, ArcType.OPEN);
			dr -= spacing;
		}
		
		// 画两侧浪
		dr = sprayHeight;
		n = (dr + r - s)/spacing  ;
		for (int i =0 ; i <n ; i ++) {
			
			double dAngle = Math.toDegrees(Math.acos( (s*s + dr*dr - r*r) /(2*s*dr) ));
			double startAngle = initAngle - dAngle;
			double angle = dAngle*2  ;
			
			if (i == 0) {
				g2d.fillArc(patternRadius - spacing*i - 2* dr, patternRadius + r - dr , 2*dr, 2*dr, startAngle , angle, ArcType.ROUND);
				g2d.fillArc(patternRadius + spacing*i, patternRadius + r - dr , 2*dr, 2*dr, 180 - startAngle - angle,angle, ArcType.ROUND);
			}
			g2d.strokeArc(patternRadius - spacing*i - 2* dr, patternRadius + r - dr , 2*dr, 2*dr, startAngle , angle, ArcType.OPEN);
			g2d.strokeArc(patternRadius + spacing*i, patternRadius + r - dr , 2*dr, 2*dr, 180 - startAngle - angle,angle, ArcType.OPEN);
			
			dr -= spacing;
			
		}
		
		g2d.restore();
	}
	
	private void drawDecoration( GraphicsContext g2d ,double r,double dr,double n ) {
		
		double spacingAngle = 3;
		
		// 计算所需的数据
		double tr = dr + ( r - dr )/2;
		double ddr = dr - ( r - dr )/2 ; 
		double eachAngle = (360/n);
		double dAngle = eachAngle - spacingAngle;
		double tAngle = (eachAngle - 4*spacingAngle)/2;
		double innerAngle = 2*(tAngle + spacingAngle) + eachAngle - dAngle ;
		
		double angle = 0;
		for (int i = 0 ; i < n; i ++) {
			
			angle = i*eachAngle;
			
			// 外圈
			g2d.strokeArc(patternRadius - r, patternRadius - r , 2*r, 2*r, angle, dAngle ,ArcType.OPEN);
			g2d.strokeLine ( 
					patternRadius + r*Math.sin(Math.toRadians(angle + 90)) ,
					patternRadius + r*Math.cos(Math.toRadians(angle + 90)) , 
					patternRadius + dr*Math.sin(Math.toRadians(angle + 90)) ,
					patternRadius + dr*Math.cos(Math.toRadians(angle + 90)) );
			g2d.strokeLine ( 
					patternRadius + r*Math.sin(Math.toRadians(angle + dAngle + 90)) ,
					patternRadius + r*Math.cos(Math.toRadians(angle + dAngle + 90)) , 
					patternRadius + dr*Math.sin(Math.toRadians(angle + dAngle + 90)) ,
					patternRadius + dr*Math.cos(Math.toRadians(angle + dAngle + 90)) );
			g2d.strokeArc(patternRadius - dr, patternRadius - dr , 2*dr, 2*dr, angle, tAngle,ArcType.OPEN);
			g2d.strokeArc(patternRadius - dr, patternRadius - dr , 2*dr, 2*dr, angle + dAngle - tAngle, tAngle ,ArcType.OPEN);
			
			// 内圈
			double startAngle = angle + spacingAngle;
			double endAngle = angle + dAngle - tAngle - spacingAngle;
			
			g2d.strokeArc(patternRadius - tr, patternRadius - tr , 2*tr, 2*tr, startAngle, tAngle,ArcType.OPEN);
			g2d.strokeArc(patternRadius - tr, patternRadius - tr , 2*tr, 2*tr, endAngle, tAngle ,ArcType.OPEN);
			g2d.strokeLine ( 
					patternRadius + tr*Math.sin(Math.toRadians(startAngle + tAngle + 90)) ,
					patternRadius + tr*Math.cos(Math.toRadians(startAngle + tAngle + 90)) , 
					patternRadius + ddr*Math.sin(Math.toRadians(startAngle +tAngle + 90)) ,
					patternRadius + ddr*Math.cos(Math.toRadians(startAngle + tAngle + 90)));
			g2d.strokeLine ( 
					patternRadius + tr*Math.sin(Math.toRadians(endAngle + 90)) ,
					patternRadius + tr*Math.cos(Math.toRadians(endAngle + 90)) , 
					patternRadius + ddr*Math.sin(Math.toRadians(endAngle + 90)) ,
					patternRadius + ddr*Math.cos(Math.toRadians(endAngle + 90)));
			
			g2d.strokeArc(patternRadius - ddr, patternRadius - ddr , 2*ddr, 2*ddr, endAngle , innerAngle ,ArcType.OPEN);
		}
	}
	
}
