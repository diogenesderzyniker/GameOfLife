package fun.sim;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class WorldGUI {
	
	private static int ROWS = 50;
	private static int COLUMNS = 50;
	
	public static World lifeWorld = new World();
	private static JFrame worldFrame = new JFrame("World Frame");
	public static Container cp = worldFrame.getContentPane();
	private static JPanel mainPanel = new JPanel();
	private static JButton startBtn = new JButton("Start");
	private static JButton pauseBtn = new JButton("Pause");
	private static JSlider speedSlider = new JSlider();
	private static JLabel gens = new JLabel("Generations: ");
	
	private static ActionListener btnListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			switch(cmd) {
				case "start" : 
					Main.go = true;
					Main.start();
					System.out.println("start");
					break;
				case "pause" : 
					Main.go = false;
					Main.start();
					System.out.println("stop");
					break;
			}
		}
	};
	
	private static Cell[][] cells;
	private static List<Cell> cellsAsList = new ArrayList<>();
	private static Border lowBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
	private static Border highBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
	private static Border emptyBorder = BorderFactory.createEmptyBorder();
	private static Map<Integer, Color> colorSuite;
	static {
		colorSuite = new HashMap<>();
		colorSuite.put(1, Color.decode("#000000"));
		colorSuite.put(2, Color.decode("#B2B2FF"));
		colorSuite.put(3, Color.decode("#CCCCFF"));
		colorSuite.put(4, Color.decode("#E6E6FF"));
//		colorSuite.put(5, Color.decode("#B2B2FF"));
//		colorSuite.put(6, Color.decode("#CCCCFF"));
//		colorSuite.put(7, Color.decode("#E6E6FF"));
		colorSuite.put(8, Color.decode("#FFFFFF"));
	}
	
	public WorldGUI() {
		ROWS = World.xAxis;
		COLUMNS = World.yAxis;
		
		worldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		worldFrame.setPreferredSize(new Dimension(1000,1000));
		
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
	}
	
	public static void init() {
		
		new WorldGUI();
		paintGUI();
	}
	
	
	public static void paintGUI() {
		cells = lifeWorld.getEcosystem();
		List<Cell[]> cellist = Arrays.asList(cells);
		for (Cell[] ca : cellist) {
			List<Cell> tmp = Arrays.asList(ca);
			cellsAsList.addAll(tmp);
		}
		mainPanel.setPreferredSize(new Dimension(950, 950));
		mainPanel.setLayout(new GridLayout(ROWS, COLUMNS, 1, 1));
		for (Cell c : cellsAsList) {
			c.setOpaque(true);
			if (c.getState()) {
				c.setBackground(colorSuite.get(1));
			} else {
				c.setBackground(colorSuite.get(8));
			}
				
			c.addMouseListener(clicker);
			mainPanel.add(c);
			}
		cp.add(mainPanel);
		JPanel btnPanel = new JPanel();
		btnPanel.setPreferredSize(new Dimension(250, 50));
		speedSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slider = (JSlider) e.getSource();
				System.out.println("Slider set to: " + slider.getValue());
				Main.sleep = 1000 / slider.getValue();
			}
		});
		speedSlider.setMaximum(60);
		speedSlider.setMinimum(1);
		speedSlider.setPreferredSize(new Dimension(200,30));
		startBtn.addActionListener(btnListener);
		startBtn.setActionCommand("start");
		pauseBtn.addActionListener(btnListener);
		pauseBtn.setActionCommand("pause");
		btnPanel.add(speedSlider);
		btnPanel.add(startBtn);
		btnPanel.add(pauseBtn);
		gens.setName("generations"); //so i can find it later
		btnPanel.add(gens);
		cp.add(btnPanel);
		
		worldFrame.pack();
		worldFrame.setVisible(true);
	}

	public static void repaint() {
		
		for (int i = 0; i < mainPanel.getComponentCount(); i++) {
			Cell c = (Cell) mainPanel.getComponent(i);
//			System.out.println(c.getCoord().toString());
//			System.out.println("State : " + c.getState());
			if (c.getState()) {
				c.setBackground(colorSuite.get(1));
				c.setBorder(highBorder);
			} else {
				if (c.getBorder() != null && c.getBorder() == highBorder) {
					c.setBorder(emptyBorder);
				}
				long rip = c.getGenerationsDead();
				if (rip == 0) c.setBackground(colorSuite.get(8));
				else if (rip < 10) c.setBackground(colorSuite.get(2));
				else if (rip < 50) c.setBackground(colorSuite.get(3));
				else if (rip >= 50) c.setBackground(colorSuite.get(4));
//				else if (rip < 75) c.setBackground(colorSuite.get(5));
//				else if (rip < 100) c.setBackground(colorSuite.get(6));
//				else if (rip < 150) c.setBackground(colorSuite.get(7));
//				else if (rip < 200) c.setBackground(colorSuite.get(8));
				
				
			}
		}
		gens.setText("Generations: " + lifeWorld.getGenerations());
		cp.repaint();
	}
	private static MouseListener clicker = new MouseListener() {
		private boolean isPressed = false;
		
		@Override
		public void mouseReleased(MouseEvent e) {
			isPressed = false;
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			isPressed = true;
			if (!(e.getComponent().getBackground().equals(colorSuite.get(1))))
				e.getComponent().setBackground(colorSuite.get(1));
			else
				e.getComponent().setBackground(colorSuite.get(8));
			
			Cell c = (Cell) e.getSource();
			c.setState(!(c.getState()));
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			if (isPressed) {
				if (!(e.getComponent().getBackground().equals(colorSuite.get(1)))) {
					e.getComponent().setBackground(colorSuite.get(1));
					Cell c = (Cell) e.getSource();
					c.setState(true);
				}
				else {
					e.getComponent().setBackground(colorSuite.get(8));
					Cell c = (Cell) e.getSource();
					c.setState(false);
				}
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
		}
	};

	public static World getLifeWorld() {
		return lifeWorld;
	}

	public static void setLifeWorld(World lifeWorld) {
		WorldGUI.lifeWorld = lifeWorld;
	}
}
