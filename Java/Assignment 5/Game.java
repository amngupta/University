import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

class Sys {
	final static int WIDTH = 600, HEIGHT = 600;
	static int counter = 0;
}

interface Animation {
	void move();
	void paint(Graphics g);
}

public class Game extends JFrame {

	javax.swing.Timer alarm = new javax.swing.Timer(1000, new TimerAction());
	MyPanel p = new MyPanel();
	ArrayList<Animation> list = new ArrayList<Animation>();
	//Start Ghost Objects Here
	Ghost g1 = new Ghost(this);
	Ghost g2 = new Ghost(this);
	Ghost g3 = new Ghost(this);
	Ghost g4 = new Ghost(this);

	public static void main( String[] args) {
		Game a = new Game();
		a.go();
	}

	public void go(){
		//Add ghost objects to an array list of Animation
		list.add(g1);
		list.add(g2);
		list.add(g3);
		list.add(g4);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		add(p);

		setSize(Sys.WIDTH, Sys.HEIGHT);
		setVisible( true); 
		alarm.start();
	}

	class MyPanel extends JPanel {
		public void paintComponent(Graphics g) {       
			g.setColor(Color.white); 
			g.fillRect(0, 0, Sys.WIDTH, Sys.HEIGHT);

			if(Sys.counter>=4)
			{
				g.setFont(new Font("Serif", Font.PLAIN, 24));
				g.setColor(Color.black);
				g.drawString("Congratulations! You Win!", 10, 20);
			}  
			else
			{
				for (Animation a : list) {   
					a.move();
					a.paint(g);
				}
			}
		}

	}

	class TimerAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			repaint();
		}     	
	}

}
class Ghost implements Animation, MouseListener {
	Game s;
	Random randomx = new Random();
	Random randomy = new Random();
	int x = (randomx.nextInt(450)), y = (randomy.nextInt(450));
	Image pic = new ImageIcon("ccc.jpg").getImage();

	Ghost(Game s) {
		s.addMouseListener(this);
		this.s = s;
	}

	public void mouseClicked(MouseEvent e) {
		int mousex = e.getX();
		int mousey = e.getY();
		if ((mousex>x && mousex <x+100) && (mousey>y && mousey<y+100))
		{
			pic = null;
			Sys.counter++;
		}
	}

	public void move() {
		x = (randomx.nextInt(450)); 
		y = (randomy.nextInt(450));
	}   

	public void paint( Graphics g) {
		g.drawImage(pic, (int)x, (int)y, s);
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}