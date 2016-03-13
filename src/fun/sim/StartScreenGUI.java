package fun.sim;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.ButtonUI;

public class StartScreenGUI extends JFrame {
	private Container cp;
	
	/**
	 * Don't know why I need this
	 */
	private static final long serialVersionUID = 1L;
	private JLabel fileNameLabel;
	
	public StartScreenGUI() {
		super();
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(300,250));
		this.setLocation(500, 300);
		cp = this.getContentPane();
		cp.setLayout(new GridLayout(2, 1, 0, 5));
		fillFrame();
		this.setVisible(true);
	}
	
	private void fillFrame() {
		JPanel patternChooserPanel = new JPanel();
		JPanel gameSettingsPanel = new JPanel();
		
		JButton startFileChooser = new JButton();
		startFileChooser.setText("Load Pattern");
		startFileChooser.setSize(50, 25);
		startFileChooser.setVerticalAlignment(SwingConstants.CENTER);
		startFileChooser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// popup JFileChooser and send the file to RLE_IO
				JFileChooser fileChooser = new JFileChooser();
				FileSystemView fsv = fileChooser.getFileSystemView();
				fileChooser.setFileFilter(new FileNameExtensionFilter(".txt and .rle", "txt", "rle"));
				fileChooser.setVisible(true);
				if (new File(fsv.getDefaultDirectory().toString() + "\\GoL_Patterns").exists()) {
					fileChooser.setCurrentDirectory(new File(fsv.getDefaultDirectory().toString() + "\\GoL_Patterns"));
				}
				int acceptFile = fileChooser.showOpenDialog(fileChooser);
				if (acceptFile == JFileChooser.APPROVE_OPTION) {
					File f = fileChooser.getSelectedFile();
					JLabel fileName = new JLabel(f.getName());
					fileName.setVerticalAlignment(SwingConstants.CENTER);
					fileName.setVisible(true);
					
					Thread worker = new Thread(new Runnable() {
						@Override
						public void run() {
							System.out.println("Decoding RLE file");
							boolean[][] rle = new RLE_IO().RLEtoBoolArray(f);
							System.out.println("RLE size " + rle.length);
							World.primordialSoup = rle;
							World.xAxis = 200;
							World.yAxis = 200;
						}
					});
					worker.run();
					
					if (fileNameLabel != null) patternChooserPanel.remove(fileNameLabel);
					patternChooserPanel.add(fileName);
					cp.revalidate();
					fileNameLabel = fileName;
					try {
						worker.join();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					// closes JFRAME
//					dispose();
				}
			}
		});
		
		patternChooserPanel.add(startFileChooser);
		
		JButton startBtn = new JButton("Start");
		startBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				WorldGUI.init();
				dispose();
			}
		});
		gameSettingsPanel.add(startBtn);
		
		cp.add(patternChooserPanel);
		cp.add(gameSettingsPanel);
		this.pack();
	}
	
	
}
