// Vivek Pereira  cs610 0793 prp
import java.io.File;
import java.io.FileNotFoundException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class pgrk0793 {
	static double errorRate = Math.pow(10, -5);
	static List<Integer>[] graphRepresntIn;
	static int[] numOutBound;
	static double d = 0.85;

	public static void main(String arg[]) throws FileNotFoundException {
		int initialValue;
		int iterations;
		List<Integer>[] graphRepresntOut;

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
		double[] pageRank = new double[vertices];
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
				pageRank[i] = 1;
			}
		} else if (initialValue == -1) {
			double value = (double) 1 / vertices;
			for (int i = 0; i < vertices; i++) {
				pageRank[i] = value;
			}
		} else if (initialValue == -2) {
			double value = (double) 1 / Math.sqrt(vertices);
			for (int i = 0; i < vertices; i++) {
				pageRank[i] = value;
			}
		}
		double constant = (1 - d) / vertices;
		double pageRankPrevious[] = new double[vertices];
		double sum = 0;
		int temp;
		double temp2;
		DecimalFormat df = new DecimalFormat("0.0000000");
		df.setRoundingMode(RoundingMode.CEILING);
		if (iterations > 0) {

			String s = "Base : 0 :";

			for (int j = 0; j < vertices; j++) {
				s = s + " P[ " + j + "]=" + pageRank[j] + " ";
			}
			System.out.println(s);

			for (int i = 1; i <= iterations; i++) {

				calculatePR_0793(pageRankPrevious, pageRank, i, constant);

			}
		} else {
			int count = 1;
			if (vertices <= 10) {

				String s = "Base : 0 :";

				for (int j = 0; j < vertices; j++) {
					s = s + " P[ " + j + "]=" + pageRank[j] + " ";
				}
				System.out.println(s);
				do {
					calculatePR_0793(pageRankPrevious, pageRank, count, constant);

					count++;

				} while (!converge_0793(pageRankPrevious, pageRank));
			} else {
				do {
					for (int j = 0; j < vertices; j++) {
						pageRankPrevious[j] = pageRank[j];
					}

					for (int j = 0; j < vertices; j++) {
						if (graphRepresntIn[j] != null) {

							Iterator iterator = graphRepresntIn[j].iterator();
							while (iterator.hasNext()) {
								temp = (int) iterator.next();
								temp2 = (pageRankPrevious[temp] / numOutBound[temp]);
								if (Double.isFinite(temp2))
									sum = sum + temp2;
							}
						}
						sum = constant + (sum * d);
						sum = sum * 10000000.0 / 10000000;
						sum = Double.parseDouble(df.format(sum));
						pageRank[j] = sum;
						sum = 0;

					}
					count++;
				} while (!converge_0793(pageRankPrevious, pageRank));
				String s = "";
				System.out.println("Iter	:"+count);
				for (int j = 0; j < vertices; j++) {
					s = "P[ " + j + "]=" + pageRank[j];
					System.out.println(s);
				}
			}
		}

		sc.close();

	}

	public static boolean converge_0793(double[] pageRankPrevious, double[] pageRank) {
		int vertices = pageRank.length;
		double error;
		for (int i = 0; i < vertices; i++) {
			error = pageRankPrevious[i] - pageRank[i];
			if (error > errorRate) {
				return false;
			}
		}

		return true;
	}

	public static void calculatePR_0793(double[] pageRankPrevious, double[] pageRank, int i, double constant) {
		int vertices = pageRank.length;
		double sum = 0;
		int temp;
		double temp2;
		String s;
		DecimalFormat df = new DecimalFormat("0.0000000");
		df.setRoundingMode(RoundingMode.CEILING);
		for (int j = 0; j < vertices; j++) {
			pageRankPrevious[j] = pageRank[j];
		}

		s = "Iter : " + i + " :";
		for (int j = 0; j < vertices; j++) {
			if (graphRepresntIn[j] != null) {

				Iterator iterator = graphRepresntIn[j].iterator();
				while (iterator.hasNext()) {
					temp = (int) iterator.next();
					temp2 = (pageRankPrevious[temp] / numOutBound[temp]);
					if (Double.isFinite(temp2))
						sum = sum + temp2;
				}
			}
			sum = constant + (sum * d);
			sum = sum * 10000000.0 / 10000000;
			sum = Double.parseDouble(df.format(sum));
			pageRank[j] = sum;
			sum = 0;
			s = s + " P[ " + j + "]=" + pageRank[j] + " ";
		}
		System.out.println(s);

	}

}
