package fun.sim;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import useful.util.Pair;

//This object is responsible for managing the game world. It will maintain reference to all Cell objects in play.
public class World {

	public static int xAxis;
	public static int yAxis;
	public static boolean[][] primordialSoup;
	public static Cell[][] ecosystem;
	
	
	private long generations;
	
	public World() {
		if (primordialSoup != null) {
			System.out.println("soup is being stirred!");
			ecosystem = boolToCellArray(primordialSoup);
		} else {
			xAxis = 50;
			yAxis = 50;
			ecosystem = new Cell[xAxis][yAxis];
			initializeCellArray(ecosystem);
		}
		this.setGenerations(0);
		
		//initialize cells in world
	}
	
	public World(int x, int y, boolean[][] initArray) {
		xAxis = x;
		yAxis = y;
		ecosystem = boolToCellArray(initArray);
	}
	
	public void initializeCellArray(Cell[][] cArray) {
		for (int i = 0;i < xAxis;i++){
			for (int k = 0;k < yAxis;k++){
				cArray[i][k] = new Cell(i, k);
				cArray[i][k].setState(false);
			}
		}
	}
	
	private Cell[][] boolToCellArray(boolean[][] bArray) {
		Cell[][] cArray = new Cell[xAxis][yAxis];
		initializeCellArray(cArray);
		System.out.println(new Pair(xAxis,yAxis).toString());
		for (int i=10;i<xAxis;i++) {
			for (int k=25;k<yAxis;k++) {
				try {
					cArray[i][k].setState(bArray[i-10][k-25]);
				} catch (Exception e) {
					break;
				}
			}
		}
		return cArray;
	}
	
	/**
	 * Move forward one generation
	 */
	public void tick() {
		
		updateCells();
		setGenerations(getGenerations() + 1);
		System.out.println("Generation : " + generations);
	}
	
	/**
	 * Instruct each cell to discover the state of its neighbors
	 */
	private void updateCells() {
		System.out.println("updating cells...");
		
		// this thread should run behind the main thread and update state of cells
		// as main thread progresses beyond the neighborhood of this threads cell
//		exService.execute(new Runnable() {
//			
//			@Override
//			public void run() {
//				for (int i=0;i<World.xAxis;i++){
//					for (int k=0;k<World.yAxis;k++){
//						if (ecosystem[i][k].isWorkerProceed()) {
//							ecosystem[i][k].setState(ecosystem[i][k].getNextState());
//							ecosystem[i][k].setWorkerProceed(false);
//						} else {
//							// try the same cell again until workerProceed is true;
//							System.out.println("Waiting for permission at : " + i + ", " + k );
//							k--;
//							continue;
//						}
//					}
//				}
//			}
//		});
		for (int i=0;i<xAxis;i++){
			for (int k=0;k<yAxis;k++){
				ecosystem[i][k].meetNeighbors();
				boolean b = ecosystem[i][k].determineNextState();
				ecosystem[i][k].setNextState(b);
				if (b) ecosystem[i][k].setGenerationsDead(0);
				else ecosystem[i][k].incrementGenerationsDead();
				
				// set workerProceed flag to true
				if ((i > 0 && i < xAxis - 1) && (k > 0 && k < yAxis - 1)) {
//					ecosystem[i-1][k-1].setWorkerProceed(true);
					ecosystem[i-1][k-1].setState(ecosystem[i-1][k-1].getNextState());
				}
				else if ((k == yAxis - 1) && (i != 0 && i < xAxis - 1)) {
//					ecosystem[i - 1][k - 1].setWorkerProceed(true);
//					ecosystem[i - 1][k].setWorkerProceed(true);
					ecosystem[i - 1][k - 1].setState(ecosystem[i-1][k-1].getNextState());
					ecosystem[i - 1][k].setState(ecosystem[i-1][k].getNextState());
				}
				else if ((i == xAxis - 1) && (k != 0 && k < yAxis - 1)) {
//					ecosystem[i][k-1].setWorkerProceed(true);
//					ecosystem[i-1][k-1].setWorkerProceed(true);
					ecosystem[i][k-1].setState(ecosystem[i][k-1].getNextState());
					ecosystem[i-1][k-1].setWorkerProceed(ecosystem[i-1][k-1].getNextState());
				}
				else if (k == 0 || i == 0) continue;
				else if (i == xAxis - 1 && k == yAxis - 1){
//					ecosystem[i][k].setWorkerProceed(true);
//					ecosystem[i-1][k].setWorkerProceed(true);
//					ecosystem[i][k-1].setWorkerProceed(true);
//					ecosystem[i-1][k-1].setWorkerProceed(true);
					ecosystem[i][k].setState(ecosystem[i][k].getNextState());
					ecosystem[i-1][k].setState(ecosystem[i-1][k].getNextState());
					ecosystem[i][k-1].setState(ecosystem[i][k-1].getNextState());
					ecosystem[i-1][k-1].setState(ecosystem[i-1][k-1].getNextState());
				}
			}
		}
	}
	
	public Cell[][] getEcosystem() {
		if(ecosystem != null){
			return ecosystem;
		} else return null;
	}

	public long getGenerations() {
		return generations;
	}

	public void setGenerations(long generations) {
		this.generations = generations;
	}
}
