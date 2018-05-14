
// Vivek Pereira  cs610 0793 prp
import java.io.File;
import java.io.FileNotFoundException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Hits_0793 {

	static double errorRate = Math.pow(10, -5);
	static List<Integer>[] graphRepresntIn;
	static List<Integer>[] graphRepresntOut;
	static double d = 0.85;

	public static void main(String arg[]) throws FileNotFoundException {
		int initialValue;
		int iterations;
		int[] numOutBound;
		int vertices;
		int edges;

		iterations = Integer.parseInt(arg[0]);
		initialValue = Integer.parseInt(arg[1]);
		File fileName = new File(arg[2]);
		Scanner sc = new Scanner(fileName);

		vertices = sc.nextInt();
		edges = sc.nextInt();
		numOutBound = new int[vertices];
		int[] numInBound = new int[vertices];
		double[] hubs = new double[vertices];
		double[] authority = new double[vertices];
		graphRepresntOut = new List[vertices];
		graphRepresntIn = new List[vertices];
		if (vertices > 10) {
			iterations = 0;
			initialValue = -1;
		}
		if (iterations < 0) {
			errorRate = Math.pow(10, iterations);
		}
		int tempOut, tempIn;
		for (int i = 0; i < edges; i++) {
			tempOut = sc.nextInt();
			tempIn = sc.nextInt();
			if (graphRepresntOut[tempOut] != null) {
				graphRepresntOut[tempOut].add(tempIn);
				numOutBound[tempOut]++;
				numInBound[tempIn]++;
			} else {
				ArrayList<Integer> edgeList = new ArrayList<Integer>();
				edgeList.add(tempIn);
				graphRepresntOut[tempOut] = edgeList;
				numOutBound[tempOut]++;
				numInBound[tempIn]++;
			}
			if (graphRepresntIn[tempIn] != null) {
				graphRepresntIn[tempIn].add(tempOut);

			} else {
				ArrayList<Integer> edgeList = new ArrayList<Integer>();
				edgeList.add(tempOut);
				graphRepresntIn[tempIn] = edgeList;

			}

		}

		if (initialValue == 1) {
			for (int i = 0; i < vertices; i++) {
				hubs[i] = 1;
				authority[i] = 1;
			}
		} else if (initialValue == -1) {
			double value = (double) 1 / vertices;
			for (int i = 0; i < vertices; i++) {
				hubs[i] = value;
				authority[i] = value;
			}
		} else if (initialValue == -2) {
			double value = (double) 1 / Math.sqrt(vertices);
			for (int i = 0; i < vertices; i++) {
				hubs[i] = value;
				authority[i] = value;
			}
		}
		double constant = (1 - d) / vertices;
		double hubPrevious[] = new double[vertices];
		double authorityPrevious[] = new double[vertices];
		double temp = 0;
		DecimalFormat df = new DecimalFormat("0.0000000");
		df.setRoundingMode(RoundingMode.CEILING);
		if (iterations > 0) {

			String s = "Base : 0 :";

			for (int j = 0; j < vertices; j++) {
				s = s + " A/H[ " + j + "]=" + df.format(authority[j]) + "/" + df.format(hubs[j]) + " ";
			}
			System.out.println(s);

			for (int i = 1; i <= iterations; i++) {

				calculateHITS_0793(hubs, authority, hubPrevious, authorityPrevious, i, constant);

			}
		} else {
			int count = 1;
			if (vertices <= 10) {

				String s = "Base : 0 :";

				for (int j = 0; j < vertices; j++) {
					s = s + " A/H[ " + j + "]=" + df.format(authority[j]) + "/" + df.format(hubs[j]) + " ";
				}
				System.out.println(s);
				do {
					calculateHITS_0793(hubs, authority, hubPrevious, authorityPrevious, count, constant);

					count++;

				} while (!converge_0793(authority, authorityPrevious, hubs, hubPrevious));
			} else {
				temp = 0;

				while (!converge_0793(authority, authorityPrevious, hubs, hubPrevious)) {
					for (int j = 0; j < vertices; j++) {
						hubPrevious[j] = hubs[j];
						authorityPrevious[j] = authority[j];
					}
					Iterator iterator;
					for (int j = 0; j < vertices; j++) {
						temp = 0;

						if (graphRepresntIn[j] != null) {
							iterator = graphRepresntIn[j].iterator();
							while (iterator.hasNext()) {
								temp = temp + hubPrevious[(int) iterator.next()];
							}
						}

						authority[j] = temp;

					}
					for (int j = 0; j < vertices; j++) {
						temp = 0;
						if (graphRepresntOut[j] != null) {
							iterator = graphRepresntOut[j].iterator();
							while (iterator.hasNext()) {
								temp = temp + authority[(int) iterator.next()];
							}
						}

						hubs[j] = temp;
					}

					double sumAuth = 0;
					double sumHub = 0;
					for (int k = 0; k < vertices; k++) {
						sumAuth = sumAuth + authority[k];
						sumHub = sumAuth + hubs[k];
					}
					for (int j = 0; j < vertices; j++) {
						authority[j] = authorityPrevious[j] / Math.sqrt(sumAuth);
						hubs[j] = hubPrevious[j] / Math.sqrt(sumHub);

					}

					count++;
				}
				String s = "";
				System.out.println("Iter	:" + count);
				for (int j = 0; j < vertices; j++) {
					s = "A/H[ " + j + "]=" + df.format(authority[j]) + "/" + df.format(hubs[j]);
					System.out.println(s);
				}
			}
		}

		sc.close();

	}

	public static boolean converge_0793(double[] authority, double[] authorityPrevious, double[] hubs,
			double[] hubPrevious) {
		int vertices = authority.length;
		double errorAuth;
		double errorHub;
		for (int i = 0; i < vertices; i++) {
			errorAuth = authorityPrevious[i] - authority[i];
			errorHub = hubPrevious[i] - hubs[i];
			if (errorHub > errorRate || errorAuth > errorRate) {
				return false;
			}
		}

		return true;
	}

	public static void calculateHITS_0793(double[] hubs, double[] authority, double[] hubsPrevious,
			double[] authorityPrevious, int i, double constant) {
		int vertices = hubs.length;
		double sumAuth = 0;
		double sumHub = 0;

		for (int j = 0; j < vertices; j++) {
			hubsPrevious[j] = hubs[j];
			authorityPrevious[j] = authority[j];
		}
		Iterator iterator;
		for (int j = 0; j < vertices; j++) {
			double temp = 0;

			if (graphRepresntIn[j] != null) {
				iterator = graphRepresntIn[j].iterator();
				while (iterator.hasNext()) {
					temp = temp + hubsPrevious[(int) iterator.next()];
				}
			}

			authority[j] = temp;

		}
		for (int j = 0; j < vertices; j++) {
			double temp = 0;
			if (graphRepresntOut[j] != null) {
				iterator = graphRepresntOut[j].iterator();
				while (iterator.hasNext()) {
					temp = temp + authority[(int) iterator.next()];
				}
			}

			hubs[j] = temp;
		}
		String s;
		DecimalFormat df = new DecimalFormat("0.0000000");
		df.setRoundingMode(RoundingMode.CEILING);

		s = "Iter : " + i + " :";
		for (int k = 0; k < vertices; k++) {
			sumAuth = sumAuth + (authority[k] * authority[k]);
			sumHub = sumHub + (hubs[k] * hubs[k]);
		}
		for (int j = 0; j < vertices; j++) {
			authority[j] = authority[j] / Math.sqrt(sumAuth);
			hubs[j] = hubs[j] / Math.sqrt(sumHub);
			s = s + " A/H[ " + j + "]=" + df.format(authority[j]) + "/" + df.format(hubs[j]) + " ";
		}
		System.out.println(s);

	}

}
