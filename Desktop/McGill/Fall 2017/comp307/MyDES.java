import java.util.Arrays; // Used for debugging (printing).
import java.util.Scanner;

/**
 * DES encryption & decryption algorithm.
 * NOTE: my Caesar Cipher is circular; in terms of 255 (extended ASCII CODE max.)+1=0 (min. ASCII CODE) and 0-1=255.
 * @author Hae Chan Mark Bong
 */
public class MyDES {
	public static void main(String args[]) {
		// Messages to print if user's inputs are invalid.
		String failDigit = "The number you entered is not a two-digit positive integer number.";
		String failMsg = "The message you entered is either empty or too long.";
		
		// Maximum number of characters in a message.
		int max_Char = 1000;
		
		// Prompt user for the digit.
		Scanner readDigit = new Scanner(System.in); // Read digit from sys. input.
		System.out.println("Enter a two-digit positive integer number: ");
		String digit = readDigit.nextLine(); // Input stored.
		
		// Check if the input is an int.
		try{	
			int number = Integer.parseInt(digit);
					
			// Check if either the digit is out of range[10, 99] or empty. 
			if(number < 10 || number > 99 || digit.isEmpty()) {
				System.out.println(failDigit);
				System.exit(0);
			}			
		}
		// Exception if the digit is not an int.
		catch (NumberFormatException e) {
			System.out.println(failDigit);
			
			// Finish prompt.
			readDigit.close();
			System.exit(0);
		}
		
		// Prompt user for a message.
		Scanner readMsg = new Scanner(System.in); // Read msg. from sys. input.
		System.out.println("Enter a message (max. 1000 characters): ");
		String msg = readMsg.nextLine(); // Input stored.
		
		// Check if either the message is empty or 0.
		 if(msg.isEmpty() || msg.length() > 1000) {
			System.out.println(failMsg);
			
			// Finish prompt.
			readDigit.close();
			readMsg.close();
			System.exit(0);
		}
		 
		// Finish prompt.
		readDigit.close();
		readMsg.close();
		
		// Flow of the program.
		String[] temp_encrypted_Msg = encrypt_Msg(digit, msg, max_Char); // Encrypt user's message.
		
		// Erase all the Null in the encrypted message.
		String encrypted_Msg = "";
		for(int index = 0; index < temp_encrypted_Msg.length; index++) {
			if(temp_encrypted_Msg[index] != null) {
				encrypted_Msg += temp_encrypted_Msg[index];
			}
		}
		System.out.println(encrypted_Msg); // Print encrypted message.

		String[][] temp_decrypted_Msg = decrypt_Msg(digit, temp_encrypted_Msg, max_Char); // Decrypt user's message.
		
		// Erase all the Null in the decrypted message.
		String decrypted_Msg = "";
		int row = 0, col = 0;
		for(int index = 0; index < msg.length(); index++) {
			if(col == digit.charAt(0) - '0') { // Check if column is equal to first digit.
				// Next row
				row++;
				col = 0;
			}
			if(temp_decrypted_Msg[row][col] != null) {
				decrypted_Msg += temp_decrypted_Msg[row][col];
				col++;
			}
		}
		System.out.println(decrypted_Msg); // Print decrypted message.
	}
	
	/**
	 * Primary methods
	 * 
	 * public static String[] encrypt_Msg (String digit, String msg, int max_Char)
	 * public static String[] decrypt_Msg (String digit, String[] encrypted_Msg, int max_Char)
	 * 
	 * Helper methods
	 * 
	 * public static String[] de_Populate(int first_Digit, int max_Char, String[][] T)
	 * public static String[][] transpose1DArray(int first_Digit, int max_Char, String[] T)
	 * public static String[] transpose2DArray(int first_Digit, int max_Char, String[][] T)
	 */
	
	/** Encrypt user's message using user's digit.
	 * @param digit - first input from user prompt.
	 * @param msg - second input from user prompt.
	 * @param max_Char - maximum number of characters in a message. 
	 * @return C- encrypted message.
	 */
	public static String[] encrypt_Msg (String digit, String msg, int max_Char) {
		int first_Digit = digit.charAt(0) - '0';
		
		// Populate T with first digit and message.
		int row = 0, col = 0;
		String[][] T = new String [max_Char][first_Digit];
		for(int index = 0; index < first_Digit*max_Char; index++) {
			// Check if there are more column.  
			if(col == first_Digit) {
				 // Next row.
				 row++;
				 col = 0;
				 
				 // Check if there is no more rows.
				 if(row == max_Char) {
					System.out.println("Your message exceeded the maximum amount of characters.");
					System.exit(0);
				 }
			 }
			
			if(index >= msg.length()) {
				T[row][col] = null;
			}
			else {
				T[row][col] = String.valueOf(msg.charAt(index)); // Insert character at current index to the array.
			}
			
			col++; // Update to next column.
		}//System.out.println(Arrays.deepToString(T)); // TESTER
		
		// Make a new 1D-Array A which is a transpose of T.
		String[] A = new String [first_Digit*max_Char];
		A = transpose2DArray(first_Digit, max_Char, T);
		//System.out.println(Arrays.toString(A)); // TESTER
		
		// Make a new 1D-Array B which is the Caesar Cipher of A.
		String[] B = new String [first_Digit*max_Char];
		for(int index = 0; index < first_Digit*max_Char; index++) {
			if(A[index] == null) {
				B[index] = null;
			}
			else {
				int temp_B = A[index].charAt(0) + first_Digit;
	
				// Check if the ASCII decimal of the character is greater than 255 (max.) after cipher, if it is, then return to ASCII decimal 0 and add first_Digit - 1.
				if (temp_B > 255) {
					temp_B =- 256; 
				}
			
				B[index] = Character.toString((char) temp_B);
			}
		}//System.out.println(Arrays.toString(B)); // TESTER
		
		// Populate T2 with first digit+1 and B.
		row = col = 0;
		String[][] T2 = new String [max_Char][first_Digit+1];
		for(int index = 0; index < (first_Digit+1)*max_Char; index++) {
			// Check if there are more column.  
			if(col == first_Digit+1) { 
				 // Next row.
				 row++;
				 col = 0;
						 
				 // Check if there is no more rows.
				 if(row == max_Char) {
					System.out.println("Your message exceeded the maximum amount of characters.");
					System.exit(0);
				 }
			 }
				
			// Check if there is no more elements in B, fill the rest of T2 with null.
			if(index >= first_Digit*max_Char) {
				T2[row][col] = null; // Insert null at current index to the array.
				col++; // Update to next column.
			}
			else {
				T2[row][col] = B[index]; // Insert character at current index to the array.
				col++; // Update to next column.
			}
		}//System.out.println(Arrays.deepToString(T2)); // TESTER
			
		// Make a new 1D-Array C which is a transpose of T2.
		String[] C = new String [(first_Digit+1)*max_Char];
		C = transpose2DArray(first_Digit+1, max_Char, T2);
		//System.out.println(Arrays.toString(C)); // TESTER

		return C;
	}
	
	/** Decrypt user's message using user's digit.
	 * @param digit - first input from user prompt.
	 * @param encrypted_Msg - encrypted message.
	 * @return decrypted_msg - decrypted message.
	 */
	public static String[][] decrypt_Msg (String digit, String[] encrypted_Msg, int max_Char) {
		int first_Digit = digit.charAt(0) - '0';
		
		// Transpose of C=encrypted_Msg to get T2.
		String[][] T2 = new String [max_Char][first_Digit+1];
		T2 = transpose1DArray(first_Digit+1, max_Char, encrypted_Msg);
		//System.out.println(Arrays.deepToString(T2)); // TESTER
		
		// De-populate T2 to get B.
		String[] B = new String [first_Digit*max_Char];
		B = de_Populate(first_Digit+1, max_Char, T2);
		//System.out.println(Arrays.toString(B)); // TESTER
		 
		// Get A by decrypting Caesar Cipher of B using first digit key.
		String[] A = new String [first_Digit*max_Char];
		for(int index = 0; index < first_Digit*max_Char; index++) {
			if(B[index] == null) {
				A[index] = null;
			}
			else {
				int temp_A = B[index].charAt(0) - first_Digit;
	
				// Check if the ASCII decimal of the character is lower than 0 (min.) after cipher, if it is, then return to ASCII decimal 255 and subtract first_Digit - 1.
				if (temp_A < 0) {
					temp_A =+ 256; 
				}
			
				A[index] = Character.toString((char) temp_A);
			}
		}//System.out.println(Arrays.toString(A)); // TESTER
		
		// Transpose of A to get decrypted_Msg.
		String[][] decrypted_Msg = new String [max_Char][first_Digit];
		decrypted_Msg = transpose1DArray(first_Digit, max_Char, A);
		//System.out.println(Arrays.deepToString(decrypted_Msg)); // TESTER
	
		return decrypted_Msg;
	}
	
	/** De-populate a 2D-array to a 1D-array.
	 * @param first_Digit - first digit from user prompt.
	 * @param max_Char - maximum number of characters in a message.
	 * @param T - given array. 
	 * @return temp - de-populated 1D-array.
	 */
	public static String[] de_Populate(int first_Digit, int max_Char, String[][] T) {
		// Make a new 1D-Array temp which is a de-populated array of T.
		int row = 0, col = 0;
		String[] temp = new String [first_Digit*max_Char];
		for(int index = 0; index < first_Digit*max_Char; index++) {
			// Check if there are more columns.
			if(col == first_Digit) {
				// Next row.
				row++;
				col = 0;
			}
				
			temp[index] = T[row][col];  // Insert character at current index to the array.
			col++; // Update to next row.
		}
		
		return temp;
	}
	
	/** Make Transpose of a 1D-array.
	 * @param first_Digit - first digit from user prompt.
	 * @param max_Char - maximum number of characters in a message.
	 * @param T - given array. 
	 * @return temp - transpose of a given array.
	 */
	public static String[][] transpose1DArray(int first_Digit, int max_Char, String[] T)
	{
		// Make a new 2D-Array temp which is a transpose of T.
		int row = 0, col = 0;
		String[][] temp = new String [max_Char][first_Digit];
		for(int index = 0; index < (first_Digit)*max_Char; index++) {
			// Check if there are more row.
			if(row == max_Char) {
				// Next column.
				col++;
				row = 0;
				
				// Check if there is no more columns.
				if(col == first_Digit) {
					System.out.println("Your message exceeded the maximum amount of characters.");
					System.exit(0);
				}
			}
				
			temp[row][col] = T[index];  // Insert character at current index to the array.
			row++; // Update to next row.
		}
		
		return temp;
	}
	
	/** Make Transpose of a 2D-array.
	 * @param first_Digit - first digit from user prompt.
	 * @param max_Char - maximum number of characters in a message.
	 * @param T - given array. 
	 * @return temp - transpose of a given array.
	 */
	public static String[] transpose2DArray(int first_Digit, int max_Char, String[][] T)
	{
		// Make a new 1D-Array temp which is a transpose of T.
		int row = 0, col = 0;
		String[] temp = new String [first_Digit*max_Char];
		for(int index = 0; index < first_Digit*max_Char; index++) {
			// Check if there are more row.
			if(row == max_Char) {
				// Next column.
				col++;
				row = 0;
				
				// Check if there is no more columns.
				if(col == first_Digit) {
					System.out.println("Your message exceeded the maximum amount of characters.");
					System.exit(0);
				}
			}
				
			temp[index] = T[row][col];  // Insert character at current index to the array.
			row++; // Update to next row.
		}
		
		return temp;
	}
}