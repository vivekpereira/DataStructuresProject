
// Vivek Pereira  cs610 0793 prp
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Henc_0793 {
	static int[] frequency = new int[256];
	static String[] frequencyEncoded = new String[256];
	static int totalSize = 0;

	// Node Class for Huffman tree
	static class Node_0793 implements Comparable<Node_0793> {
		private char charSymbol;
		private int frequency;
		private Node_0793 leftChild;
		private Node_0793 rightChild;

		public Node_0793(char charSymbol, int frequency, Node_0793 leftChild, Node_0793 rightChild) {

			this.charSymbol = charSymbol;
			this.frequency = frequency;
			this.leftChild = leftChild;
			this.rightChild = rightChild;
		}

		// to check if node is a leaf
		boolean isLeaf_0793() {
			return this.leftChild == null && this.rightChild == null;
		}

		@Override
		public int compareTo(Node_0793 arg0) {
			int frequencyComp = Integer.compare(this.frequency, arg0.frequency);
			if (frequencyComp != 0) {
				return frequencyComp;
			}
			return Integer.compare(this.charSymbol, arg0.charSymbol);
		}
	}

	// Class to Create Huffman tree from given frequency list
	public static Node_0793 huffmanTree_0793(int[] freq) {

		ArrayList<Node_0793> charList = new ArrayList<Node_0793>();
		PriorityQueue<Node_0793> pq = new PriorityQueue<>();
		for (char i = 0; i < freq.length; i++) {
			if (freq[i] > 0) {
				charList.add(new Node_0793(i, freq[i], null, null));
				pq.add(new Node_0793(i, freq[i], null, null));
			}
		}
		if (pq.size() == 1) {
			pq.add(new Node_0793('\0', 1, null, null));
		}
		while (pq.size() > 1) {
			Node_0793 leftChild = pq.poll();
			Node_0793 rightChild = pq.poll();
			Node_0793 parent = new Node_0793('\0', leftChild.frequency + rightChild.frequency, leftChild, rightChild);
			pq.add(parent);
		}
		return pq.poll();
	}

	// Creating Huffman code for each Character
	public static void createCode_0793(Node_0793 root, String s) {

		if (root.leftChild == null && root.rightChild == null) {

			frequencyEncoded[root.charSymbol] = s;

			return;
		}

		createCode_0793(root.leftChild, s + "0");
		createCode_0793(root.rightChild, s + "1");
	}

	public static void main(String arg[]) throws IOException, ClassNotFoundException {
		File fileName = new File(arg[0]);

		FileInputStream in = new FileInputStream(fileName);

		BufferedInputStream buffer = new BufferedInputStream(in);
		totalSize = buffer.available();

		// marking the input buffer to not read the input file again
		buffer.mark(totalSize);
		int r;
		int num = 0;
		while (num < totalSize) {
			r = buffer.read();
			char ch = (char) r;
			num++;

			frequency[ch]++;
		}
		Node_0793 root = Henc_0793.huffmanTree_0793(frequency);
		Henc_0793.createCode_0793(root, "");
		FileOutputStream out = new FileOutputStream(fileName.getName() + ".huf");

		// resetting the buffer at the previous marked position
		buffer.reset();

		FileDescription_0793 fileDescription = new FileDescription_0793();
		fileDescription.setFileName_0793(fileName.getName());
		fileDescription.setFileSize_0793(totalSize);
		fileDescription.setFrequency_0793(frequency);

		BufferedOutputStream bufferOut = new BufferedOutputStream(out);
		ObjectOutputStream objOut = new ObjectOutputStream(out);
		// storing information about the input file
		objOut.writeObject(fileDescription);

		String writeBits = "";

		for (int i = 0; i < totalSize; i++) {
			r = buffer.read();

			writeBits = writeBits + frequencyEncoded[r];
			if (writeBits.length() >= 8) {
				int dec = Henc_0793.convertToDecimal_0793(writeBits.substring(0, 8));
				bufferOut.write(dec);
				writeBits = writeBits.substring(8);
			}

		}
		while (writeBits.length() > 8) {
			int dec = Henc_0793.convertToDecimal_0793(writeBits.substring(0, 8));
			bufferOut.write(dec);
			writeBits = writeBits.substring(8);
		}
		int lastByte = writeBits.length();
		if (lastByte > 0) {
			while (writeBits.length() < 8)
				writeBits = writeBits + 0;

			bufferOut.write(Henc_0793.convertToDecimal_0793(writeBits));

		}

		bufferOut.write(lastByte);

		// closing input/output streams
		bufferOut.close();
		buffer.close();
		// Deleting the Original File
		fileName.delete();

	}

	// converting the 8-bit binary code to Decimal
	public static int convertToDecimal_0793(String s) {
		int max = 128;
		int count = 0;
		int dec = 0;
		while (count < 8) {
			dec = dec + (max * Integer.parseInt("" + s.charAt(count)));
			max = max / 2;
			count++;
		}
		return dec;

	}

}
