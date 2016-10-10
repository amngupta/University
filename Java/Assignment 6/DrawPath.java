import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.net.*;

class Path implements Serializable {
	ArrayList<Point> point = new ArrayList<Point>();
	Color strokeColor;
	int sSize;
}

public class DrawPath {	
	ArrayList<Point> points = new ArrayList<Point>();
	Path ptest = new Path();
	ArrayList<Path> path = new ArrayList<Path>();
	JFileChooser chooser = new JFileChooser();
	FileNameExtensionFilter filter = new FileNameExtensionFilter("Serial", "ser");
	JMenuBar menuBar = new JMenuBar();
	JPanel bottom = new JPanel();
	JPanel strokes = new JPanel();
	JPanel panel = new JPanel();
	JFrame frame = new JFrame("Collaborative Painter");
	JMenu mnAction = new JMenu();
	JPanel pallet = new JPanel();
	JButton color1 = new JButton();
	JButton color2 = new JButton();
	JButton color3 = new JButton();
	JButton color4 = new JButton();
	JButton size_30 = new JButton("30");
	JButton color8 = new JButton("VIOLET");
	JButton color7 = new JButton("PINK");
	JButton color6 = new JButton("YELLOW");
	JButton color5 = new JButton("GREEN");
	JMenuItem clear = new JMenuItem("Clear");
	JMenuItem save = new JMenuItem("Save");
	JMenuItem load = new JMenuItem("Load");
	JMenuItem exit = new JMenuItem("Exit");
	JButton size_2 = new JButton("10");
	JButton size_5 = new JButton("15");
	JButton size_10 = new JButton("20");
	JButton size_20 = new JButton("25");


	MyPanel p = new MyPanel();
	static Color selectedColor = Color.BLACK;
	static int strokeSize = 10;

	//For Sockets
	ServerSocket ss;
	Socket s;
	ObjectInputStream is;
	ObjectOutputStream os;
	ArrayList<ObjectInputStream> clientIS = new ArrayList<ObjectInputStream>();
	ArrayList<ObjectOutputStream> clientOS = new ArrayList<ObjectOutputStream>();

	public void goClient()
	{
		try {

			frame.setTitle("Client");
			clear.setVisible(false);
			load.setVisible(false);
			s = new Socket(CoPainter.IP, CoPainter.Port);
			os = new ObjectOutputStream(s.getOutputStream());
			is = new ObjectInputStream(s.getInputStream());
			go();
		} 			
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(frame, "No Server Open on Port");
			closeWindow portNotOpen = new closeWindow();
			portNotOpen.actionPerformed(null);
		}
		Thread t = new Thread(new InReaderClient());
		t.start();
	}
	public void goServer() {
		try {
			frame.setTitle("Server");
			ss = new ServerSocket(CoPainter.Port);
			Thread portStart = new Thread(new runPort());
			portStart.start();
			go();
		}catch (Exception e) { 
			JOptionPane.showMessageDialog(frame, "Port Already in Use! Please Try Again");
			closeWindow portInUse = new closeWindow();
			portInUse.actionPerformed(null);
		}
	}

	public void go()
	{
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().add(p);
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		frame.setSize(700, 600);
		frame.setLocationRelativeTo(null);
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(menuBar);
		mnAction.setText("Action");
		mnAction.add(clear);
		clear.addActionListener(new clearWindow());
		mnAction.add(save);
		save.addActionListener(new saveWindow());
		mnAction.add(load);
		load.addActionListener(new loadWindow());
		mnAction.add(exit);
		exit.addActionListener(new closeWindow());
		menuBar.add(mnAction);
		p.addMouseListener(p);
		p.addMouseMotionListener(p);
		frame.getContentPane().add(bottom, BorderLayout.SOUTH);
		bottom.setLayout(new GridLayout(0, 2, 0, 0));
		bottom.add(pallet);

		pallet.setLayout(new GridLayout(2, 4, 0, 0));
		color1.setText("BLUE");
		color1.setBackground(Color.BLUE);
		color1.addActionListener(new switchColor());
		pallet.add(color1);
		color2.setText("WHITE");
		color2.setBackground(Color.WHITE);
		color2.setHorizontalAlignment(SwingConstants.LEFT);
		color2.addActionListener(new switchColor());
		pallet.add(color2);
		color3.setText("BLACK");
		color3.setBackground(Color.BLACK);
		color3.addActionListener(new switchColor());
		pallet.add(color3);
		color4.setText("RED");
		color4.setBackground(Color.RED);
		color4.addActionListener(new switchColor());
		pallet.add(color4);
		color8.setBackground(Color.MAGENTA);
		pallet.add(color8);
		color7.setBackground(Color.PINK);
		pallet.add(color7);
		color6.setBackground(Color.YELLOW);
		pallet.add(color6);
		color5.setBackground(Color.GREEN);	
		color5.addActionListener(new switchColor());
		color6.addActionListener(new switchColor());
		color7.addActionListener(new switchColor());
		color8.addActionListener(new switchColor());
		pallet.add(color5);
		bottom.add(strokes);
		strokes.setLayout(new GridLayout(0, 5, 0, 0));
		size_2.setForeground(Color.WHITE);
		size_2.setIcon(new ImageIcon("10.PNG"));
		strokes.add(size_2);
		size_5.setForeground(Color.WHITE);
		size_5.setIcon(new ImageIcon("15.PNG"));
		strokes.add(size_5);
		size_10.setForeground(Color.WHITE);
		size_10.setIcon(new ImageIcon("20.PNG"));
		strokes.add(size_10);
		size_20.setForeground(Color.WHITE);
		size_20.setIcon(new ImageIcon("25.PNG"));
		strokes.add(size_20);
		size_30.setIcon(new ImageIcon("30.PNG"));
		size_30.setForeground(Color.WHITE);
		size_2.setBackground(Color.WHITE);
		size_5.setBackground(Color.WHITE);
		size_10.setBackground(Color.WHITE);
		size_20.setBackground(Color.WHITE);
		size_30.setBackground(Color.WHITE);
		strokes.add(size_30);
		size_2.addActionListener(new switchStroke());
		size_5.addActionListener(new switchStroke());
		size_10.addActionListener(new switchStroke());
		size_20.addActionListener(new switchStroke());
		size_30.addActionListener(new switchStroke());
		frame.setVisible(true);
	}

	class switchColor implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			switch (e.getActionCommand())
			{
			case "BLACK":
			{selectedColor = color3.getBackground(); break;}
			case "BLUE":
			{selectedColor = color1.getBackground(); break;}
			case "RED":
			{selectedColor = color4.getBackground(); break;}
			case "WHITE":
			{selectedColor = color2.getBackground(); break;}
			case "YELLOW":
			{selectedColor = color5.getBackground(); break;}
			case "PINK":
			{selectedColor = color7.getBackground(); break;}
			case "VIOLET":
			{selectedColor = color8.getBackground(); break;}
			case "GREEN":
			{selectedColor = color6.getBackground(); break;}
			}
		}			
	}

	class switchStroke implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			strokeSize= Integer.parseInt(e.getActionCommand());
		}			
	}
	class clearWindow implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			path.clear();
			points.clear();
			try {
				for(ObjectOutputStream clients: clientOS){
					clients.reset();
					clients.writeObject("CLEAR");
					clients.flush();
				}
			} catch (IOException e1) {
				//e1.printStackTrace();
			}
			p.repaint();
		}		
	}
	class saveWindow implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			chooser.setFileFilter(filter);
			int returnVal = chooser.showSaveDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File filepath = chooser.getSelectedFile();
				try
				{
					FileOutputStream fileOut = new FileOutputStream(filepath);
					ObjectOutputStream out = new ObjectOutputStream(fileOut);
					out.writeObject(path);
					out.close();
					fileOut.close();
				}
				catch(IOException i)
				{
					//i.printStackTrace();
				}
			}
		}

	}
	class loadWindow implements ActionListener{		
		public void actionPerformed(ActionEvent arg0) {
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File filepath = chooser.getSelectedFile();
				try
				{		         
					ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(filepath));
					try {
						path = (ArrayList<Path>) inFile.readObject();
						p.repaint();
						for(ObjectOutputStream clients: clientOS){
							updateClient(clients);
						}
					} catch (ClassNotFoundException e) {
						//e.printStackTrace();
					}
					inFile.close();
				}
				catch(IOException i)
				{
					//i.printStackTrace();
				}
			}
		}

	}
	class closeWindow implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(CoPainter.choice==1){
				try {
					for(ObjectOutputStream clients: clientOS){
						clients.reset();
						clients.writeObject("EXIT");
						clients.flush();
					}
					ss.close();
					frame.dispose();
				} catch (Exception e) {
					//e.printStackTrace();
				}}
			if(CoPainter.choice == 2 && CoPainter.windowCheck == true)
			{
				try {
					CoPainter.windowCheck = false;
					os.reset();
					os.writeObject("EXIT");
					os.flush();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
			System.exit(0);
		}

	}
	class MyPanel extends JPanel implements MouseListener, MouseMotionListener {   //An inner class
		public void paintComponent( Graphics g) {
			g.setColor(Color.white);   //Erase the previous figures
			g.fillRect(0, 0, getWidth(), getHeight());
			try{
				for (Path pa: path){
					g.setColor(pa.strokeColor);
					if(g instanceof Graphics2D) {
						Graphics2D g2D = (Graphics2D) g;
						g2D.setStroke(new BasicStroke(pa.sSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
					}
					Point prevPoint = null;
					for (Point p: pa.point) {
						if (prevPoint != null) {
							g.drawLine(prevPoint.x, prevPoint.y, p.x, p.y);
						}
						prevPoint = p;
					}	
				}

				Point pPoint = null;
				g.setColor(selectedColor);
				if(g instanceof Graphics2D) {
					Graphics2D g2D = (Graphics2D) g;
					g2D.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				}		
				for (Point p: points){
					if(pPoint !=null)
						g.drawLine(pPoint.x, pPoint.y, p.x, p.y);
					pPoint = p ;
				}
			} catch(Exception e)
			{
				//e.printStackTrace();
			}
		}
		@Override
		public void mouseDragged(MouseEvent event) {
			points.add(event.getPoint());
			repaint();
		}
		@Override
		public void mouseMoved(MouseEvent event) {
		}
		@Override
		public void mouseClicked(MouseEvent event) {
		}
		@Override
		public void mouseEntered(MouseEvent event) {
		}
		@Override
		public void mouseExited(MouseEvent event) {
		}
		@Override
		public void mousePressed(MouseEvent event) {
			points.clear();
			points.add(event.getPoint());
			repaint();
		}
		@Override
		public void mouseReleased(MouseEvent event) {
			Path pathn = new Path();
			pathn.point.addAll(points);
			pathn.strokeColor = selectedColor;
			pathn.sSize = strokeSize;
			if(CoPainter.choice==1)
			{
				path.add(pathn);
				sendServer(pathn);
			}
			else if (CoPainter.choice == 2)
			{
				sendClient(pathn, os);
			}
			points.clear();
			pathn = null;
		}
	}
	/*
	 * Method to send the path from the server to the clients.
	 */	
	public void sendServer(Path lPath){
		try
		{
			for(ObjectOutputStream clients: clientOS){
				clients.reset();
				clients.writeObject(lPath);
				clients.flush();
			}
		}
		catch(Exception e)
		{
			//e.printStackTrace();;
		}
	}
	/*
	 * Method to send the path from the client to the server.
	 */	
	public void sendClient(Path path, ObjectOutputStream osos){
		try{
			osos.reset();
			osos.writeObject(path);
			osos.flush();
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
	}
	/*
	 * Method to update new clients with all paths on server..
	 */	
	public void updateClient(ObjectOutputStream w) {
		try{
			for(Path p1:path)
			{
				w.reset();
				w.writeObject(p1);
				w.flush();	
			}
		}
		catch(Exception e)
		{
			//			System.out.println(e);
		}
	}
	/*
	 * A thread to run PortListener forever
	 */
	public class runPort implements Runnable{

		@Override
		public void run() {
			while(true)
			{
				Thread accept = new Thread(new portListener(ss));
				accept.start();
			}
		} 
	}
	/*
	 * Method to start accepting clients and receiving paths from the clients.
	 */
	public class portListener implements Runnable{

		ObjectOutputStream w;
		ObjectInputStream isis;
		Socket sock;
		public portListener(ServerSocket client)
		{
			if(ss.isClosed() == false)
			{
				try{
					sock = ss.accept();
					isis = new ObjectInputStream(sock.getInputStream());
					w = new ObjectOutputStream(sock.getOutputStream());
					clientIS.add(isis);
					clientOS.add(w);
					updateClient(w);
					System.out.println("Serverside Connection Established");
				}
				catch(Exception e)
				{
					//e.printStackTrace();
				}
			}
		}

		/*
		 * (non-Javadoc) Receiving the path from the client
		 * @see java.lang.Runnable#run()
		 */
		public void run(){
			try{
				boolean openCheck = true;
				while(openCheck){
					try {
						Object o =  isis.readObject();
						if(o instanceof String )
						{
							String x = (String) o;
							if (x.equals("EXIT"))
							{
								CoPainter.windowCheck=false;
								sock.close();
								openCheck = false;
								clientOS.remove(w);
								System.out.println("Socket Closed");
							}
						}
						else
						{
							ptest = (Path) o;
							path.add(ptest);
							sendServer(ptest);
							p.repaint();
						}
					} catch (ClassNotFoundException | IOException e) {
						//e.printStackTrace();
					}
				}
			}
			catch(Exception e)
			{
				//e.printStackTrace();
			}
		}

	}
	/*
	 * Method to read the path sent from the server.
	 */
	public class InReaderClient implements Runnable {
		public synchronized void run() {
			while(CoPainter.windowCheck){
				{
					if(CoPainter.windowCheck){
						try {
							Object o =  is.readObject();
							if(o instanceof String )
							{
								String x = (String) o;
								if(x.equals("CLEAR"))
								{
									path.clear();
									points.clear();
									p.repaint();
								}
								else if (x.equals("EXIT"))
								{

									try {
										JOptionPane.showMessageDialog(frame, "Server Has Been Shut Down!");
										closeWindow serverClose = new closeWindow();
										serverClose.actionPerformed(null);
									} catch (Exception e) {
										//e.printStackTrace();
									}
								}
							}
							else
							{
								Path pt =  (Path) o;
								path.add(pt);
								p.repaint();
							}
						} 
						catch (Exception e) {
							//e.printStackTrace();
						}
					}
				}
			}
		}
	}
}

