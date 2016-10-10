import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

//import DrawPath.startPainter;

public class CoPainter {
	JFrame CoPainterFrame = new JFrame("Collaborative Painter");
	JPanel CoPainterPanel = new JPanel();
	JTextField portField = new JTextField();
	JLabel lblNewLabel_1 = new JLabel("Host");
	JLabel lblNewLabel = new JLabel("Port");
	JTextField hostField = new JTextField();
	JButton sHost = new JButton("Start as a Host");
	JButton cHost = new JButton("Connect to a Host");
	public static String IP;
	public static int serverPort;
	public static int Port;
	public static int choice;
	public static boolean windowCheck = true;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CoPainter hello = new CoPainter();
		hello.CoPainterF();
	}
	public void CoPainterF(){
		CoPainterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CoPainterFrame.setSize(300, 150);
		CoPainterFrame.setLocationRelativeTo(null);
		CoPainterFrame.getContentPane().add(CoPainterPanel, BorderLayout.NORTH);
		CoPainterPanel.setLayout(new GridLayout(3, 2, 0, 0));
		CoPainterPanel.add(lblNewLabel);	
		CoPainterPanel.add(portField);
		CoPainterPanel.add(lblNewLabel_1);
		CoPainterPanel.add(hostField);
		CoPainterPanel.add(cHost);
		cHost.addActionListener(new startClient());
		CoPainterPanel.add(sHost);
		sHost.addActionListener(new startServer());
		CoPainterFrame.setVisible(true);
	}

	class startClient implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub

			if(portField.getText().equals("") || hostField.getText().equals(""))
			{
				JOptionPane.showMessageDialog(CoPainterFrame, "Please Recheck PORT and IP Address");
			}
			else{				
				Port = Integer.parseInt(portField.getText());
				IP = hostField.getText();
				choice = 2;
				CoPainterFrame.dispose();
				DrawPath test = new DrawPath();
				test.goClient();
			}

		}
	}
	class startServer implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(portField.getText().equals(""))
			{
				JOptionPane.showMessageDialog(CoPainterFrame, "Please Enter PORT");
			}
			else
			{
				Port = Integer.parseInt(portField.getText());
				serverPort = Port;
				choice = 1;
				CoPainterFrame.dispose();
				DrawPath test = new DrawPath();
				test.goServer();
			}
		}

	}
}
