
/*
 * Morphing.java
 * Reference
 * http://codezine.jp/article/detail/30
 * http://www.mlab.im.dendai.ac.jp/javabasic/2010/gui1/
 */
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.lang.Math;

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

		line1 = new Point2D.Float[10];
		line2 = new Point2D.Float[10];
		line3 = new Point2D.Float[10];

		float x1;
		float y1;
		float x2;
		float y2;
		for(int i = 0; i < 10; i++) {
			x1 = 150+(40 + 90 * ((i + 1) % 2))
                           * (float)Math.sin(Math.PI * i * 36 / 180);
			y1 = 150 - (40 + 90 * ((i + 1) % 2))
                           * (float)Math.cos(Math.PI * i * 36 / 180);
			x2 = 150 + 90 * (float)Math.sin(Math.PI * i * 36 / 180);
			y2 = 150 - 90 * (float)Math.cos(Math.PI * i * 36 / 180);
			line1[i] = new Point2D.Float(x1, y1);
			line2[i] = new Point2D.Float(x2, y2);
		}


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
				bufferGraphics.setColor(Color.black);
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
					bufferGraphics.clearRect(0, 0, WIDTH, HEIGHT);
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