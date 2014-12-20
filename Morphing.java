
/*
 * Morphing.java
 * http://codezine.jp/article/detail/30
 */
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Morphing extends JPanel implements Runnable {

	private final static int WIDTH = 300;
	private final static int HEIGHT = 300;

	private Point2D.Float[] line1;
	private Point2D.Float[] line2;
	private Point2D.Float[] line3;
	private BufferedImage buffer;
	private Graphics2D bufferGraphics;

	/*
	 * constructor
	 */
	public Morphing() {
		buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);
		bufferGraphics = buffer.createGraphics();
		bufferGraphics.setBackground(Color.white);
    	bufferGraphics.clearRect(0, 0, WIDTH, HEIGHT);

		line1 = new Point2D.Float[6];
		line1[0] = new Point2D.Float(1.0f, 1.1f);
		line1[1] = new Point2D.Float(2.0f, 1.1f);
		line1[2] = new Point2D.Float(2.1f, 2.1f);
		line1[3] = new Point2D.Float(2.4f, 3.1f);
		line1[4] = new Point2D.Float(1.1f, 1.5f);
		line1[5] = new Point2D.Float(250.0f, 250.2f);

		line2 = new Point2D.Float[6];
		line2[0] = new Point2D.Float(0.2f, 1.1f);
		line2[1] = new Point2D.Float(0.6f, 1.3f);
		line2[2] = new Point2D.Float(2.9f, 5.5f);
		line2[3] = new Point2D.Float(5.4f, 5.1f);
		line2[4] = new Point2D.Float(1.2f, 6.5f);
		line2[5] = new Point2D.Float(140.0f, 210f);

		line3 = new Point2D.Float[6];

		Thread refresh = new Thread(this);
		refresh.start();
	}

	/*
	 * calculate point
	 */
	public void calculate(float t) {
		float x;
		float y;

		for(int i = 0; i < line1.length; i++) {
			x = line1[i].x * (1.0f - t) + line2[i].x * t;
			y = line1[i].y * (1.0f - t) + line2[i].y * t;
			System.out.println(x + ", " + y);
			line3[i] = new Point2D.Float(x, y);

			if(0 < i) {
				bufferGraphics.drawLine((int)line3[i-1].x, (int)line3[i-1].y, (int)line3[i].x, (int)line3[i].y);
				repaint();
			}
 		}
		 
	}

	/*
	 * paint
	 */
	public void paintComponent(Graphics g) {
	  	super.paintComponent(g);
	  	g.setColor(Color.black);
	    if(buffer != null) {
	      g.drawImage(buffer, 0, 0, this);
	    }
	}

	/*
	 * animation
	 */
	public void run() {
		float t = 0.0f;

		while(true) {
	    	try {
	    		calculate(t);
	    		t += 0.1f;
	    		if(1.0f < t) {
	    			t = 0.0f;
	    		}
				Thread.sleep(1000);
	    	} catch(Exception e) {
	    		System.out.println("error");
	    	}
		}
    }

	public static void main(String[] args) {
		Morphing morph = new Morphing();
		morph.setPreferredSize(new Dimension(300, 300));
		System.out.println(morph.line1[0]);

		final JFrame jframe = new  JFrame("hoge");
		Container container = jframe.getContentPane();
	    container.setLayout(new BorderLayout());
	    
	    jframe.add(morph, BorderLayout.CENTER);
	    jframe.pack();
	    jframe.setResizable(false);//画面の大きさは変更できなくする
	    jframe.setBackground(Color.white);
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    SwingUtilities.invokeLater(new Runnable() {//時間のかかる処理を任せるらしい
	      public void run() {//ThreadTestTreadクラスのrunメソッド
	          jframe.setVisible(true);//フレームを表示
	      }
	    });
	}
}