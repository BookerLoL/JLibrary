package main.collections.Array;

import java.util.Arrays;

public class GapBuffer {
	private static final char GAP = '_';
	private static final int DEFAULT_GAP_SIZE = 10;
	private char[] buffer;
	private int size;
	private int leftGap;
	private int rightGap;
	
	public GapBuffer() {
		buffer = new char[50];
		size = DEFAULT_GAP_SIZE;
		leftGap = 0;
		rightGap = size - 1;
	}
	
	public void printBuffer() {
		System.out.println(Arrays.toString(buffer));
	}
	
	private void grow(int pos) {
		char[] tempAry = new char[size];
		for (int i = 0; i < pos; i++) {
			tempAry[i] = buffer[i];
		}
		for (int i = 0; i < DEFAULT_GAP_SIZE; i++) {
			buffer[i + pos] = GAP;
		}
		for (int i = pos; i < tempAry.length; i++) {
			buffer[i + DEFAULT_GAP_SIZE] = tempAry[i];
		}
		
		rightGap += DEFAULT_GAP_SIZE;
		size += DEFAULT_GAP_SIZE;
	}
	
	public void insert(String input, int position) {
		if (leftGap != position) {
			moveGap(position);
		}
		
		for (int i = 0; i < input.length(); i++) {
			if (leftGap == rightGap) {
				grow(position + i);
			}
			buffer[position + i] = input.charAt(i);
			leftGap++;
		}
	}
	
	public void delete(int position) {
		if (leftGap != position) {
			moveGap(position);
		}
		
		rightGap++;
		buffer[rightGap] = GAP;
	}
	
	void moveGap(int position) {
		if (position < leftGap) {
			moveLeft(position);
		} else {
			moveRight(position);
		}
	}
	
	private void moveLeft(int position) {
		while (leftGap != position) {
			leftGap--;
			buffer[rightGap] = buffer[leftGap];
			buffer[leftGap] = GAP;
			rightGap--;
		}
	}
	
	private void moveRight(int position) {
		while (rightGap != position) {
			rightGap++;
			buffer[leftGap] = buffer[rightGap];
			buffer[rightGap] = GAP;
			leftGap++;
		}
	}

}