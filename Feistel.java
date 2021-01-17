import java.lang.Math;

/**
 * This is a class that encapsulates functionality necessary for implementing a feistel network.
 * @author Pablo Scarpati
 *
 */
public class Feistel {
	//this is a list of substitution boxes that are used in this class
	private static final int[][][] SUBSTITUTION_BOXES = {/* Substitution Box 1 */{{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
			                                                                      {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
			                                                                      {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
			                                                                      {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}},
			                                             /* Substitution Box 2 */{{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
				                                                                  {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
				                                                                  {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
				                                                                  {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}},
			                                             /* Substitution Box 3 */{{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
					                                                              {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
					                                                              {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
					                                                              {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}},
			                                             /* Substitution Box 4 */{{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
					                                                              {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
					                                                              {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
					                                                              {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}},
			                                             /* Substitution Box 5 */{{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
					                                                              {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
					                                                              {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
					                                                              {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}},
			                                             /* Substitution Box 6 */{{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
					                                                              {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
					                                                              {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
					                                                              {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}},
					                                     /* Substitution Box 7 */{{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
					                                                              {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
					                                                              {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
					                                                              {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}},
					                                     /* Substitution Box 8 */{{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
					                                                              {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
					                                                              {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
					                                                              {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}}};
	
	private byte[] key;
	private byte[] inputText;
	
	public Feistel(byte[] inputText) {
		//the key is initialised as a random 48-bit key
		key = generateRandomArray(48);
		this.setInputText(inputText);
	}

	//setters
	public void setKey(byte[] key) {
		this.key = key;
	}

	public void setInputText(byte[] inputText) {
		this.inputText = inputText;
	}
	
	//public methods
	
	/**
	 * This method encrypts the plaintext, returning the ciphertext.
	 * @return The ciphertext as an array of bytes.
	 */
	public byte[] encrypt() {
		byte[] left = new byte[this.inputText.length / 2];
		System.arraycopy(this.inputText, 0, left, 0, this.inputText.length / 2);
		byte[] right = new byte[this.inputText.length / 2];
		System.arraycopy(this.inputText, this.inputText.length / 2, right, 0, this.inputText.length / 2);
		
		byte[] feistelFunctionResult = this.feistelFunction(right, this.key);
		byte[] xorResult = this.xor(feistelFunctionResult, left);
		
		byte[] result = new byte[this.inputText.length];
		
		System.arraycopy(right, 0, result, 0, right.length);
		System.arraycopy(xorResult, 0, result, right.length, xorResult.length);
		
		return result;
	}
	
	/**
	 * This method treats the right half of the plaintext as specified in the Feistel Structure specification.
	 * @param input - The plaintext (ideally, the half right of the original plaintext).
	 * @param subkey - They key for this round.
	 * @return The result of the function, as an array of bytes.
	 */
	public byte[] feistelFunction(byte[] input, byte[] subkey) {
		//expansion
		byte[] expandedInput = this.expansion(input);
		
		//XOR with subkey
		byte[] xoredInput = this.xor(expandedInput, subkey);
		
		//substitution layer
		byte[] substitutedInput = this.substitution(xoredInput);
		
		//permutation
		byte[] permutatedInput = this.permutation(substitutedInput);
		
		return permutatedInput;
	}
	
	/**
	 * This method performs permutation.
	 * @param input - The input of the function.
	 * @return The permutated input.
	 */
	public byte[] permutation(byte[] input) {
		byte[] result = new byte[input.length];
		
		int[] permutationMatrix = {16,  7, 20, 21, 19, 12, 28, 17,
				                    1, 15, 23, 26,  5, 18, 31, 10,
				                    2,  8, 24, 14, 32, 27,  3,  9,
				                   19, 13, 30,  6, 22, 11,  4, 25};
		
		for(int i = 0; i < input.length; i++) {
			result[i] = input[permutationMatrix[i] - 1];
		}
		
		return result;
	}
	
	/**
	 * This method performs substitution on an input by using a list of S-Boxes.
	 * @param input - The input of the function.
	 * @return The substituted input.
	 */
	public byte[] substitution(byte[] input) {
		int rows = 8;
		int cols = 6;
		
		//the input is divided into 8 parts of 6 bits each.
		byte[][] dividedInput = new byte[rows][cols];
		
		for(int i = 0; i < rows; i++) {
			System.arraycopy(input, i * cols, dividedInput[i], 0, cols);
		}
		
		//the output of the substitution will be 8 4-bit blocks.
		int newCols = 4;
		
		byte[][] dividedResult = new byte[rows][newCols];
		
		for(int i = 0; i < rows; i++) {
			dividedResult[i] = this.singleSubstitution(dividedInput[i], SUBSTITUTION_BOXES[i]);
		}
		
		//the 8 4-bit blocks are united in the result array.
		byte[] result = new byte[32];
		
		for(int i = 0; i < newCols; i++) {
			System.arraycopy(dividedResult[i], 0, result, i * newCols, newCols);
		}
		
		return result;
	}
	
	/**
	 * This method performs a single substitution operation with a given S-Box.
	 * @param input - The text to be substituted.
	 * @param sBox - The chosen S-Box.
	 * @return The substituted 4-bit block.
	 */
	public byte[] singleSubstitution(byte[] input, int[][] sBox) {
		
		//the extreme bits are used to choose the row of the S-Box.
		byte[] extremeBits = {input[0], input[input.length - 1]};
		//the middle bits are used to choose the column of the S-Box.
		byte[] middleBits = new byte[input.length - 2];
		
		int counter = 0;
		
		for(int i = 1; i < input.length - 2; i++) {
			middleBits[counter] = input[i];
			counter++;
		}
		
		int y = this.toDecimal(extremeBits);
		int x = this.toDecimal(middleBits);
		
		int substitutionValue = sBox[y][x];
		
		byte[] byteSubstitution = this.toBinary(substitutionValue);
		
		return byteSubstitution;
	}
	
	/**
	 * This method performs expansion on the input text.
	 * @param input - The input text.
	 * @return The expanded input.
	 */
	public byte[] expansion(byte[] input) {
		int[] expansionMatrix = {32, 1,  2,  3,  4,  5, 
								 4,  5,  6,  7,  8,  9,
								 8,  9,  10, 11, 12, 13,
								 12, 13, 14, 15, 16, 17,
								 16, 17, 18, 19, 20, 21,
								 20, 21, 22, 23, 24, 25,
								 24, 25, 26, 27, 28, 29,
								 28, 29, 30, 31, 32,  1};
		
		int newLength = 48;
		byte[] expandedInput = new byte[newLength];
		
		for(int i = 0; i < newLength; i++) {
			expandedInput[i] = input[expansionMatrix[i] - 1];
		}
		
		return expandedInput;
	}
	
	/**
	 * This method generates a random byte array of a chosen length.
	 * @param length - The chosen length.
	 * @return The random byte array.
	 */
	public static byte[] generateRandomArray(int length) {
		byte[] randomArray = new byte[length];
		
		for(int i = 0; i < length; i++) {
			double randomValue = Math.random();
			byte randomBit = 0;
			
			if(randomValue > 0.5) {
				randomBit = 1;
			}
			
			randomArray[i] = randomBit;
		}
		
		return randomArray;
	}
	
	/**
	 * This operation performs XOR onto two byte arrays and returns the result.
	 * @param array1 - The first array.
	 * @param array2 - The second array.
	 * @return The XOR result between array1 and array2.
	 */
	private byte[] xor(byte[] array1, byte[] array2) {
		if(array1.length == array2.length) {
			int l = array1.length;
			
			byte[] result = new byte[l];
			
			for(int i = 0; i < l; i++) {
				if(array1[i] == 0 && array2[i] == 0) {
					result[i] = 0;
				} else if((array1[i] == 1 && array2[i] == 0) || ((array1[i] == 0 && array2[i] == 1))) {
					result[i] = 1;
				} else {
					result[i] = 0;
				}
			}
			
			return result;
		} else {
			return null;
		}
	}
	
	/**
	 * This method converts a byte array into a decimal number.
	 * @param input - The byte array.
	 * @return The decimal number.
	 */
	private int toDecimal(byte[] input) {
		int result = 0;
		int position = 0;
		
		for(int i = input.length - 1; i >= 0; i--) {
			if(input[i] == 1) {
				result += Math.pow(2, position);
			}
			
			position++;
		}
		
		return result;
	}
	
	/**
	 * This method converts a decimal input into binary.
	 * @param input - The integer input.
	 * @return The binary output.
	 */
	private byte[] toBinary(int input) {
		byte[] result = new byte[4];
		
		int curr = input;
		
		for(int i = result.length - 1; i >= 0; i--) {
			result[i] = (curr % 2 == 0) ? (byte)0 : (byte)1;
			curr /= 2;
		}
		
		return result;
	}
}
