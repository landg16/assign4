import javax.swing.JLabel;
import javax.swing.JTextField;

public class Worker extends Thread{
	private int count = 0;
	private int limit;
	private JLabel lab;
	public Worker(JLabel label,JTextField field) {
		try {
			limit = Integer.parseInt(field.getText());
		}catch(NumberFormatException e) {
			System.out.println("type number pls");
		}
		
		lab = label;
	}
	
	@Override
	public void run() {
		
		
		for(int i=0;i<limit;i++) {
			try {
				sleep(116);
			} catch (InterruptedException e) {
				return;
			}
			lab.setText(String.valueOf(i));
		}
	}
	
}
