//You do not need to look at or edit this file.


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.Color;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.UIManager;

public class Assignment2 extends JFrame {

	private JPopupMenu viewportPopup;
	private JLabel infoLabel = new JLabel("");

	public Assignment2() {
		super("ENGG 1202 - Assignment 2");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JScrollPane scroller = new JScrollPane(new ImagePanel());
		this.add(scroller);
		this.add(infoLabel, BorderLayout.SOUTH);
		this.setSize(500, 500);
		this.setVisible(true);
	}

	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		new Assignment2();
	}

	private class ImagePanel extends JPanel implements MouseListener, ActionListener, MouseMotionListener {
		private BufferedImage img;
		private ImageProcessing imgProcessor;
		int M;
		int N;
		int x;
		int y;

		public ImagePanel() {
			imgProcessor = new ImageProcessing();
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		}

		public Dimension getPreferredSize() {
			if (img != null) return (new Dimension(img.getWidth(), img.getHeight()));
			else return (new Dimension(0, 0));
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (img != null)
				g.drawImage(img, 0, 0, this);
		}

		private void showPopup(MouseEvent e) {
			JPopupMenu.setDefaultLightWeightPopupEnabled(false);
			viewportPopup = new JPopupMenu();

			JMenuItem openImageMenuItem = new JMenuItem("open image ...");
			openImageMenuItem.addActionListener(this);
			openImageMenuItem.setActionCommand("open image");
			viewportPopup.add(openImageMenuItem);

			viewportPopup.addSeparator();

			JMenuItem loadDefaultImageMenuItem = new JMenuItem("load default image (internet)");
			loadDefaultImageMenuItem.addActionListener(this);
			loadDefaultImageMenuItem.setActionCommand("load default image");
			viewportPopup.add(loadDefaultImageMenuItem);
			
			viewportPopup.addSeparator();

			JMenuItem getIntensityValueMenuItem = new JMenuItem("getIntensity");
			getIntensityValueMenuItem.addActionListener(this);
			getIntensityValueMenuItem.setActionCommand("getIntensity");
			viewportPopup.add(getIntensityValueMenuItem);
			
			JMenuItem setEightNeighborsToWhiteMenuItem = new JMenuItem("setEightNeighborsToWhite");
			setEightNeighborsToWhiteMenuItem.addActionListener(this);
			setEightNeighborsToWhiteMenuItem.setActionCommand("setEightNeighborsToWhite");
			viewportPopup.add(setEightNeighborsToWhiteMenuItem);

			viewportPopup.addSeparator();

			JMenuItem thresholdMenuItem = new JMenuItem("Task 1: threshold");
			thresholdMenuItem.addActionListener(this);
			thresholdMenuItem.setActionCommand("threshold");
			viewportPopup.add(thresholdMenuItem);

			JMenuItem negativeMenuItem = new JMenuItem("Task 2: negative");
			negativeMenuItem.addActionListener(this);
			negativeMenuItem.setActionCommand("negative");
			viewportPopup.add(negativeMenuItem);

			JMenuItem showHistogramMenuItem = new JMenuItem("Task 3: show histogram");
			showHistogramMenuItem.addActionListener(this);
			showHistogramMenuItem.setActionCommand("show histogram");
			viewportPopup.add(showHistogramMenuItem);

			JMenuItem boxSmoothMenuItem = new JMenuItem("Task 4: box smooth");
			boxSmoothMenuItem.addActionListener(this);
			boxSmoothMenuItem.setActionCommand("box smooth");
			viewportPopup.add(boxSmoothMenuItem);
			
			JMenuItem laplacianFilterMenuItem = new JMenuItem("Task 4: laplacian filter");
			laplacianFilterMenuItem.addActionListener(this);
			laplacianFilterMenuItem.setActionCommand("laplacian filter");
			viewportPopup.add(laplacianFilterMenuItem);

			JMenuItem medianFilterMenuItem = new JMenuItem("Task 5: median filter");
			medianFilterMenuItem.addActionListener(this);
			medianFilterMenuItem.setActionCommand("median filter");
			viewportPopup.add(medianFilterMenuItem);
			
			viewportPopup.addSeparator();

			JMenuItem loadboxSmoothImageMenuItem = new JMenuItem("load Task 4 (box smooth) result with filter size 7 (internet)");
			loadboxSmoothImageMenuItem.addActionListener(this);
			loadboxSmoothImageMenuItem.setActionCommand("load box smooth image");
			viewportPopup.add(loadboxSmoothImageMenuItem);

			JMenuItem loadLaplacianImageMenuItem = new JMenuItem("load Task 4 (laplacian) result with filter size 7 (internet)");
			loadLaplacianImageMenuItem.addActionListener(this);
			loadLaplacianImageMenuItem.setActionCommand("load laplacian image");
			viewportPopup.add(loadLaplacianImageMenuItem);

			JMenuItem loadMedianImageMenuItem = new JMenuItem("load Task 5 result with filter size 7 (internet)");
			loadMedianImageMenuItem.addActionListener(this);
			loadMedianImageMenuItem.setActionCommand("load median image");
			viewportPopup.add(loadMedianImageMenuItem);

			viewportPopup.addSeparator();			

			JMenuItem exitMenuItem = new JMenuItem("exit");
			exitMenuItem.addActionListener(this);
			exitMenuItem.setActionCommand("exit");
			viewportPopup.add(exitMenuItem);

			viewportPopup.show(e.getComponent(), e.getX(), e.getY());
		}

		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			if (viewportPopup != null) {
				viewportPopup.setVisible(false);
				viewportPopup = null;
			} else
				showPopup(e);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("open image")) {
				final JFileChooser fc = new JFileChooser();
				FileFilter imageFilter = new FileNameExtensionFilter("Image files", "bmp", "gif", "jpg");
				fc.addChoosableFileFilter(imageFilter);
				fc.setDragEnabled(true);
				fc.setMultiSelectionEnabled(false);
				fc.showOpenDialog(this);
				File file = fc.getSelectedFile();
				try {
					long start = System.nanoTime();
					img = colorToGray((ImageIO.read(file)));
					M = img.getHeight();
					N = img.getWidth();
					double seconds = (System.nanoTime() - start) / 1000000000.0;
					infoLabel.setText(seconds+"");
				} catch (Exception ee) {
					//ee.printStackTrace();
				}
			} else if (e.getActionCommand().equals("load default image")) {
				try {
					long start = System.nanoTime();
					img = colorToGray(ImageIO.read(new URL("http://www.cs.hku.hk/~sdirk/georgesteinmetz.jpg")));
					M = img.getHeight();
					N = img.getWidth();
					double seconds = (System.nanoTime() - start) / 1000000000.0;
					infoLabel.setText(seconds+"");
				} catch (Exception ee) {
					JOptionPane.showMessageDialog(this, "Unable to fetch image from URL", "Error",
							JOptionPane.ERROR_MESSAGE);
					ee.printStackTrace();
				}
			} else if (e.getActionCommand().equals("getIntensity")) {
				if ((img!= null) && (x<M) && (y<N)) {
					byte[] imgData = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
					int[][] f = new int[M][N];
					for (int i=0;i<M;i++)
						for (int j=0;j<N;j++)
							f[i][j] = (int)(imgData[N*i+j] & 0xFF);
					int i = imgProcessor.getIntensity(f, x, y);
					JOptionPane.showMessageDialog(this, "f("+x+","+y+") = "+i, "getIntensity", JOptionPane.INFORMATION_MESSAGE);
				}
			} else if (e.getActionCommand().equals("setEightNeighborsToWhite")) {
				if ((img!= null)&& (x<M-1) && (y<N-1) && (x>0) && (y>0)) {
					byte[] imgData = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
					int[][] f = new int[M][N];
					for (int i=0;i<M;i++)
						for (int j=0;j<N;j++)
							f[i][j] = (int)(imgData[N*i+j] & 0xFF);
					imgProcessor.setEightNeighborsToWhite(f, x, y);
					for (int i=0;i<M;i++)
						for (int j=0;j<N;j++)
							 imgData[N*i+j] = (byte)f[i][j];
				}
			} else if (e.getActionCommand().equals("threshold")) {
				if ((img!= null)) {
					byte[] imgData = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
					int[][] f = new int[M][N];
					for (int i=0;i<M;i++)
						for (int j=0;j<N;j++)
							f[i][j] = (int)(imgData[N*i+j] & 0xFF);
					imgProcessor.threshold(f, f[x][y]);
					for (int i=0;i<M;i++)
						for (int j=0;j<N;j++)
							 imgData[N*i+j] = (byte)f[i][j];
				}
			} else if (e.getActionCommand().equals("negative")) {
				if ((img!= null)) {
					byte[] imgData = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
					int[][] f = new int[M][N];
					for (int i=0;i<M;i++)
						for (int j=0;j<N;j++)
							f[i][j] = (int)(imgData[N*i+j] & 0xFF);
					imgProcessor.negative(f);
					for (int i=0;i<M;i++)
						for (int j=0;j<N;j++)
							 imgData[N*i+j] = (byte)f[i][j];
				}
			} else if (e.getActionCommand().equals("show histogram")) {
				if (img!=null) {
					JFrame frame = new JFrame();
					frame.setTitle("Histogram");					
					byte[] imgData = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
					int[][] f = new int[M][N];
					for (int i=0;i<M;i++)
						for (int j=0;j<N;j++)
							f[i][j] = (int)(imgData[N*i+j] & 0xFF);
					frame.add(new HistogramPanel(imgProcessor.histogram(f)));
					frame.pack();
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.setResizable(false);
					frame.setVisible(true);
				}
			} else if (e.getActionCommand().equals("box smooth")) {
				if (img!=null) {
					int filterSize = 7;
					boolean notOk = true;
					while (notOk) {
						String s = (String)JOptionPane.showInputDialog(this, "Please enter a filter size! \n(Must be odd and smaller than image width and height)", ""+filterSize);
						if (s==null) {
							return;
						}
						try {
							int i = Integer.parseInt(s);
							if (i%2==1 && i<img.getWidth() && i<img.getHeight()) {
								notOk = false;
								filterSize = i;
							}
						} catch (Exception ee){
						}
					}
					byte[] imgData = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
					int[][] f = new int[M][N];
					for (int i=0;i<M;i++)
						for (int j=0;j<N;j++)
							f[i][j] = (int)(imgData[N*i+j] & 0xFF);
					long start = System.nanoTime();
					imgProcessor.boxSmoothFilter(f, filterSize);
					for (int i=0;i<M;i++)
						for (int j=0;j<N;j++)
							 imgData[N*i+j] = (byte)f[i][j];
					double seconds = (System.nanoTime() - start) / 1000000000.0;
					infoLabel.setText(seconds+"");
					//
					//try {
					//	ImageIO.write(img, "png", new File("boxSmooth.png"));	
					//} catch (Exception ex) {
					//	ex.printStackTrace();
					//}
					//
				}
			} else if (e.getActionCommand().equals("median filter")) {
				if (img!=null) {
					int filterSize = 7;
					boolean notOk = true;
					while (notOk) {
						String s = (String)JOptionPane.showInputDialog(this, "Please enter a filter size! \n(Must be odd and smaller than image width and height)", ""+filterSize);
						if (s==null) {
							return;
						}
						try {
							int i = Integer.parseInt(s);
							if (i%2==1 && i<img.getWidth() && i<img.getHeight()) {
								notOk = false;
								filterSize = i;
							}
						} catch (Exception ee){
						}
					}
					byte[] imgData = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
					int[][] f = new int[M][N];
					for (int i=0;i<M;i++)
						for (int j=0;j<N;j++)
							f[i][j] = (int)(imgData[N*i+j] & 0xFF);
					long start = System.nanoTime();
					imgProcessor.medianFilter(f, filterSize);
					for (int i=0;i<M;i++)
						for (int j=0;j<N;j++)
							 imgData[N*i+j] = (byte)f[i][j];
					double seconds = (System.nanoTime() - start) / 1000000000.0;
					infoLabel.setText(seconds+"");
					//
					//try {
					//	ImageIO.write(img, "png", new File("median.png"));	
					//} catch (Exception ex) {
					//	ex.printStackTrace();
					//}
					//
				}
			} else if (e.getActionCommand().equals("laplacian filter")) {
				if (img!=null) {
					byte[] imgData = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
					int[][] f = new int[M][N];
					for (int i=0;i<M;i++)
						for (int j=0;j<N;j++)
							f[i][j] = (int)(imgData[N*i+j] & 0xFF);
					long start = System.nanoTime();
					imgProcessor.laplacianFilter(f);
					for (int i=0;i<M;i++)
						for (int j=0;j<N;j++)
							 imgData[N*i+j] = (byte)f[i][j];
					double seconds = (System.nanoTime() - start) / 1000000000.0;
					infoLabel.setText(seconds+"");	
					//
					//try {
					//	ImageIO.write(img, "png", new File("laplacian.png"));	
					//} catch (Exception ex) {
					//	ex.printStackTrace();
					//}
					//
				}
			} else if (e.getActionCommand().equals("load box smooth image")) {
				try {
					long start = System.nanoTime();
					img = colorToGray(ImageIO.read(new URL("http://www.cs.hku.hk/~sdirk/boxSmooth.png")));
					M = img.getHeight();
					N = img.getWidth();
					double seconds = (System.nanoTime() - start) / 1000000000.0;
					infoLabel.setText(seconds+"");
				} catch (Exception ee) {
					JOptionPane.showMessageDialog(this, "Unable to fetch image from URL", "Error",
							JOptionPane.ERROR_MESSAGE);
					ee.printStackTrace();
				}
			} else if (e.getActionCommand().equals("load laplacian image")) {
				try {
					long start = System.nanoTime();
					img = colorToGray(ImageIO.read(new URL("http://www.cs.hku.hk/~sdirk/laplacian.png")));
					M = img.getHeight();
					N = img.getWidth();
					double seconds = (System.nanoTime() - start) / 1000000000.0;
					infoLabel.setText(seconds+"");
				} catch (Exception ee) {
					JOptionPane.showMessageDialog(this, "Unable to fetch image from URL", "Error",
							JOptionPane.ERROR_MESSAGE);
					ee.printStackTrace();
				}
			} else if (e.getActionCommand().equals("load median image")) {
				try {
					long start = System.nanoTime();
					img = colorToGray(ImageIO.read(new URL("http://www.cs.hku.hk/~sdirk/median.png")));
					M = img.getHeight();
					N = img.getWidth();
					double seconds = (System.nanoTime() - start) / 1000000000.0;
					infoLabel.setText(seconds+"");
				} catch (Exception ee) {
					JOptionPane.showMessageDialog(this, "Unable to fetch image from URL", "Error",
							JOptionPane.ERROR_MESSAGE);
					ee.printStackTrace();
				}
			} 

			else if (e.getActionCommand().equals("exit")) {
				System.exit(0);
			}
			viewportPopup = null;
			this.updateUI();
		}
		
		public BufferedImage colorToGray(BufferedImage source) {
	        BufferedImage returnValue = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
	        Graphics g = returnValue.getGraphics();
	        g.drawImage(source, 0, 0, null);
	        return returnValue;
	    }

		public void mouseDragged(MouseEvent e) {

		}

		public void mouseMoved(MouseEvent e) {
			x = e.getY();
			y = e.getX();
			infoLabel.setText("("+x+","+y+")");
		}
	}

	private class HistogramPanel extends JPanel {
		
		private int[] histogram;
		int width = 512;
		int height = 256;
		
		public HistogramPanel(int[] histogram) {
			this.histogram = histogram;
		}
		
		public Dimension getPreferredSize() {
			return (new Dimension(width, height));
		}
		
		public int rescale(int in, int max) {
			return (int)(in*(255.0)/(max));
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			
			int max = 0;
			for (int i=0;i<histogram.length;i++) {
				if (histogram[i]>max)
					max = histogram[i];
			}
			g2.setColor(Color.RED);
			for (int i=0;i<histogram.length;i++) {
				g2.draw(new Line2D.Double(2*i, height, 2*i, height-rescale(histogram[i], max)));
				g2.draw(new Line2D.Double(2*i+1, height, 2*i+1, height-rescale(histogram[i], max)));
			}	
		}	
	}
}

