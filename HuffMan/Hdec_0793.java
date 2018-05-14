
// Vivek Pereira  cs610 0793 prp
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Hdec_0793 {
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
			Node_0793 left = pq.poll();
			Node_0793 right = pq.poll();
			Node_0793 parent = new Node_0793('\0', left.frequency + right.frequency, left, right);
			pq.add(parent);
		}
		return pq.poll();
	}

	public static void createCode_0793(Node_0793 root, String s) {

		if (root.leftChild == null && root.rightChild == null) {

			// c is the character in the node
			// System.out.println(root.charSymbol + ":" + s);
			frequencyEncoded[root.charSymbol] = s;

			return;
		}

		createCode_0793(root.leftChild, s + "0");
		createCode_0793(root.rightChild, s + "1");
	}

	public static void main(String arg[]) throws IOException, ClassNotFoundException {
		File fileName = new File(arg[0]);
		FileInputStream in = new FileInputStream(fileName);
		ObjectInputStream inObj = new ObjectInputStream(in);
		BufferedInputStream buffer = new BufferedInputStream(in);
		FileDescription_0793 fileEncoded = (FileDescription_0793) inObj.readObject();

		File outputFile = new File(fileEncoded.getFileName_0793());

		FileOutputStream outf = new FileOutputStream(outputFile);

		Node_0793 root = Hdec_0793.huffmanTree_0793(fileEncoded.getFrequency_0793());

		int a = 0;
		int totalBytes = buffer.available();

		int array = 0;
		String writeBits = "";
		for (int i = 0; i < totalBytes; i++) {

			array = buffer.read();
			writeBits = writeBits + Hdec_0793.convertToBinary_0793(array);

			while (writeBits.length() > 32) {
				for (a = 0; a < 32; a++) {
					int codeValue = huffmanCode_0793(root, writeBits.substring(0, a + 1));
					if (codeValue == -1)
						continue;
					else {
						outf.write(codeValue);
						writeBits = writeBits.substring(a + 1);
						break;
					}
				}
			}

		}
		writeBits = writeBits.substring(0, (writeBits.length() - 8));
		writeBits = writeBits.substring(0, (writeBits.length() - 8 + array));
		int counter;

		while (writeBits.length() > 0) {
			if (writeBits.length() > 16)
				counter = 16;
			else
				counter = writeBits.length();
			for (a = 0; a < counter; a++) {
				int codeValue = huffmanCode_0793(root, writeBits.substring(0, a + 1));
				if (codeValue == -1)
					continue;
				else {
					outf.write(codeValue);
					writeBits = writeBits.substring(a + 1);
					break;
				}
			}
		}

		// Closing input/output streams
		buffer.close();
		outf.close();
		// Deleting the Original File
		fileName.delete();

	}

	// converting decimal to binary
	public static String convertToBinary_0793(int b) {
		int arr[] = new int[8];
		String s = "";
		int count = 0;
		while (count < 8) {
			arr[count] = b % 2;
			b = b / 2;
			count++;
		}
		count = 7;
		while (count >= 0) {
			s = s + arr[count];
			count--;

		}

		return s;
	}

	// to get huffmancode
	private static int huffmanCode_0793(Node_0793 node, String huffmanCode) {
		while (true) {
			if (huffmanCode.charAt(0) == '0')
				node = node.leftChild;
			else
				node = node.rightChild;

			if (node.isLeaf_0793())
				return node.charSymbol;

			if (huffmanCode.length() == 1)
				break;
			huffmanCode = huffmanCode.substring(1);
		}
		return -1;
	}

}
