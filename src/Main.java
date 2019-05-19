import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Main {
	
	public static void main(String[] args) {
		
		
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				initOnePart();
				
			}
		});
		
	}
	public static void initOnePart() {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400,400);
		
		frame.add(new JCounter());
	}
	
	
	

}
