/**
 * This is a class made for testing the Feistel Class.
 * @author Pablo Scarpati
 *
 */
public class Main {
	public static void main(String[] args) {
		byte[] plaintext = Feistel.generateRandomArray(64);
		
		Feistel f = new Feistel(plaintext);
		
		byte[] ciphertext = f.encrypt();
		
		System.out.print("Plaintext:  ");
		printByteArray(plaintext);
		
		System.out.print("Ciphertext: ");
		printByteArray(ciphertext);
	}
	
	/**
	 * This method prints a byte array in a readable way.
	 * @param array - The byte array.
	 */
	public static void printByteArray(byte[] array) {
		for(int i = 0; i < array.length; i++) {
			System.out.print(array[i]);
		}
		
		System.out.println();
	}
}
