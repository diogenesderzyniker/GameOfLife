package fun.sim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import useful.util.Pair;

public class RLE_IO {
	
	public RLE_IO() {}
	
	public boolean[][] RLEtoBoolArray(File rle) {
		boolean[][] boolArray = null;
		Pair arrayPos = new Pair(0,0);
		String line;
		Boolean state = null;
		Boolean previousState = null;
		int x;
		int y;
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(rle));
			while ((line = bReader.readLine()) != null) {
				System.out.println("Line Read : " + line);
				if (line.startsWith("#")) continue;
				else if (line.startsWith("x") || line.startsWith("X")) {
					// we will initialize bool array here
					String[] tmp = line.split(",");
					x = Integer.parseInt(tmp[0].trim().substring(4)) + 10;
					y = Integer.parseInt(tmp[1].trim().substring(4)) + 10;
					boolArray = new boolean[x][y];
					System.out.println("Initialized array of dimensions : " + x + ", " + y);
					// TODO: tell the World class what size we need
					// TODO: also, get the rule-set for bonus points
					
					continue;
				}
				else {
					String run = "";
					StringBuilder sb = new StringBuilder();
					for(int i=0;i<line.length();i++) {
						previousState = state == null ? previousState : state;
						state = null;
						System.out.println("Previous State = " + previousState);
						char z = line.charAt(i);
						if (Character.isDigit(z)) {
							System.out.println("digit : " + String.valueOf(z));
							sb.append(z);
						} else if (Character.isLetter(z)) {
							System.out.println("letter");
							state = z == 'b' ? false : true;
						} else if (Character.toString(z).equals("$")) {
							System.out.println("$: End of Line");
						} else if (Character.toString(z).equals("!")) {
							System.out.println("End of file. Array of size " + arrayPos.toString() + " was loaded.");
							break;
						}
						// if we have a state, then we know we are ready to add to array, whether or not we have a run
						if (state != null) {
							run = sb.toString();
							int runLength = run.equals("") ? 1 : Integer.parseInt(run);
							System.out.println("RunLength: " + runLength);
							if (boolArray != null) {
								for (int k=0;k<runLength;k++) {
									boolArray[arrayPos.a][arrayPos.b] = state;
									arrayPos.a = ++arrayPos.a;
								}
							} else if (boolArray == null) System.out.println("Oops, your boolean array was still null...");
							sb = new StringBuilder();
						}
						if (Character.toString(z).endsWith("$")) {
							run = sb.toString();
							int runLength = run.equals("") ? 1 : Integer.parseInt(run);
							System.out.println("RunLength: " + runLength);
							if (boolArray != null) {
								for (int k=0;k<runLength;k++) {
									boolArray[arrayPos.a][arrayPos.b] = previousState == false ? true : false;
									arrayPos.a = ++arrayPos.a;
								}
								// move coordinates to next row
								arrayPos.b = ++arrayPos.b;
								arrayPos.a = 0;
							} else if (boolArray == null) System.out.println("Oops, your boolean array was still null...");
							sb = new StringBuilder();
						}
					}
				}
					
			}
			System.out.println("Finished reading RLE file. Closing reader...");
			bReader.close();
		} catch (IOException e) {
			System.out.println("The file you selected was not found or could not be read");
			e.printStackTrace();
		}
		
		return boolArray;
	}
}
