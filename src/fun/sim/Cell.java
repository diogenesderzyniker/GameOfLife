package fun.sim;

import javax.swing.JLabel;

import useful.util.Pair;

public class Cell extends JLabel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// this bool is used as a flag to determine whether worker can set state on this cell
	private boolean workerProceed = false;
	
	private Boolean state;
	
	private int posX;
	private int posY;
	private final int x = 3;
	private final int y = 3;
	private Pair coord;
	
	private State currentState;
	private boolean nextState;
	
	// How many generations since this cell was last alive. 0 if never.
	private long generationsDead = 0l;
	public boolean wasAlive = false;
	
	protected Boolean[][] neighborhood = new Boolean[x][y];
	
	public Cell(int x, int y) {
		super();
		
		this.posX = x;
		this.posY = y;
		this.state = new Boolean(false);
		this.currentState = State.Dead;
		this.coord = new Pair(x,y);
	}

	public Cell(int x, int y, boolean state) {
		super();
		
		this.posX = x;
		this.posY = y;
		this.state = new Boolean(state);
		this.currentState = state == false ? State.Dead : State.Alive;
		this.coord = new Pair(x,y);
	}
	
	public boolean getState() {
		return this.state;
	}
	
	public void setState(boolean b) {
		this.state = b;
		this.currentState = b == true ? State.Alive : State.Dead;
	}
	
	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	protected Boolean[][] meetNeighbors() {
//		System.out.println(posX + ", " + posY);
		// this method will refer to the "World" object, which will contain all Cell objects in play
		// don't look outside the bounds of the array!
		int i = posX == 0 ? 0 : -1;
		for (;i<(x-1);i++) {
			int k = posY == 0 ? 0 : -1;
			for (;k<(y-1);k++) {
				if (posX == (World.xAxis - 1) && i == 1) continue;
				if (posY == (World.yAxis - 1) && k == 1) continue;
				
				Cell c = WorldGUI.lifeWorld.getEcosystem()[posX+i][posY+k];
				if (posX == 0 && posY == 0) {
					neighborhood[i][k] = (Boolean) c.getState();
				} else if (posX == 0 && posY != 0) {
					neighborhood[i][k+1] = (Boolean) c.getState();
				} else if (posX != 0 && posY == 0) {
					neighborhood[i+1][k] = (Boolean) c.getState();
				} else neighborhood[i+1][k+1] = (Boolean) c.getState();
			}
		}
		return neighborhood;
	}
	
	protected boolean determineNextState() {
		int liveCells = 0;
		for (int i = 0;i<this.x;i++){
			for (int k = 0;k<this.y;k++){
				if (neighborhood[i][k] != null) {
				}
				if (!(i==1 && k==1) && neighborhood[i][k] != null && neighborhood[i][k].booleanValue() == true) {
					liveCells++;
				}
			}
		}
		switch (liveCells) {
			case 2 : return getState();
			case 3 : return true;
			default : return false;
		}
	}
	
	public synchronized boolean getNextState() {
		return nextState;
	}

	public void setNextState(boolean nextState) {
		this.nextState = nextState;
		if (!this.wasAlive && nextState) {
			this.wasAlive = true;
		}
	}

	private enum State {
		Dead, Alive;
	}

	public Pair getCoord() {
		return coord;
	}

	public void setCoord(Pair coord) {
		this.coord = coord;
	}

	public boolean isWorkerProceed() {
		return workerProceed;
	}

	public synchronized void setWorkerProceed(boolean workerProceed) {
		this.workerProceed = workerProceed;
	}

	public long getGenerationsDead() {
		return generationsDead;
	}

	public void setGenerationsDead(long generationsDead) {
		this.generationsDead = generationsDead;
	}
	
	public void incrementGenerationsDead() {
		if (this.wasAlive) {
			generationsDead++;
		}
		if (generationsDead >= 250l) {
			this.wasAlive = false;
			setGenerationsDead(0);
		}
	}
}
