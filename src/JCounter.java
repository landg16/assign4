import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JCounter extends JPanel{
	
	private JLabel[] labels = new JLabel[4];
	private JButton[] stops = new JButton[4];
	private JButton[] starts = new JButton[4];
	private JTextField[] texts = new JTextField[4];
	
	private Worker[] workers = new Worker[4];
	
	public JCounter() {
		super();
		for(int i=0;i<4;i++) {
			initOne(i);
		}
		
	}
	
	private void initOne(int i) {
		labels[i] = new JLabel("0");
		stops[i] = new JButton("stop");
		starts[i] = new JButton("starts");
		texts[i] = new JTextField(12);
		
		
		
		add(texts[i]);
		add(labels[i]);
		add(stops[i]);
		add(starts[i]);
		
		starts[i].addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(workers[i] != null) {
					if(workers[i].isAlive()) {
						workers[i].interrupt();
						try {
							workers[i].join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
					}
				}
				
				workers[i] = new Worker(labels[i],texts[i]);
				workers[i].start();
			}
		});
		
		stops[i].addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				stop(workers[i]);
			}
		});
		
		
	}
	
	private void stop(Worker work) {
		if(work != null) {
			if(work.isAlive()) {
				work.interrupt();
				try {
					work.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
		}
	}
}
