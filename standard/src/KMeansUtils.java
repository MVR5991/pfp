import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.RandomAccess;
import java.util.Set;



/**
 * A class containing helper methods for the implementation of k-means
 * clustering.
 */
public class KMeansUtils {

	/**
	 * A data set containing properties of specimen of Iris flowers.
	 */
	public static Pair<List<List<Double>>, List<Integer>> loadIrisDataset() throws IOException {
		return loadDataset("iris.data", 5);
	}



	/**
	 * A data set containing simulated high energy gamma rays observed by a
	 * Cherenkov telescope.
	 */
	public static Pair<List<List<Double>>, List<Integer>> loadMAGICDataset() throws IOException {
		return loadDataset("magic04.data", 11);
	}



	/**
	 * A data set containing letter names spoken by different persons.
	 */
	public static Pair<List<List<Double>>, List<Integer>> loadISOLETDataset() throws IOException {
		return loadDataset("isolet1+2+3+4.data", 618);
	}



	private static Pair<List<List<Double>>, List<Integer>> loadDataset(final String fileName,
			final int numberOfColumns) throws IOException {

		final List<List<Double>> vectors = new ArrayList<>();
		final List<Integer> classes = new ArrayList<>();
		final Map<String, Integer> classMap = new HashMap<>();
		try (final BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = reader.readLine()) != null) {
				final String[] splitted = line.split(",");
				if (splitted.length == numberOfColumns) {
					final List<Double> row = new ArrayList<>();
					for (int i = 0; i < numberOfColumns - 1; ++i) {
						row.add(Double.parseDouble(splitted[i]));
					}
					vectors.add(row);

					final String sampleClass = splitted[numberOfColumns - 1];
					if (!classMap.containsKey(sampleClass)) {
						classMap.put(sampleClass, classMap.size());
					}
					classes.add(classMap.get(sampleClass));
				} // Ignore otherwise
			}
		}

		return new Pair<>(vectors, classes);
	}



	/**
	 * Create a Gnuplot Script to display the clustering results.
	 */
	public static void writeIrisGnuplotScript(final String scriptFileName, final String dataFileName,
			final Pair<List<List<Double>>, List<Integer>> dataSet,
			final List<List<Double>> centroids) throws IOException {

		writeGnuplotScript(scriptFileName, dataFileName, "Clustering on Iris data set",
				0, 2, 3, "sepal length", "petal length", "petal width", dataSet.getFirst(),
				dataSet.getSecond(), centroids);
	}



	/**
	 * Create a Gnuplot Script to display the clustering results.
	 */
	public static void writeMAGICGnuplotScript(final String scriptFileName,
			final String dataFileName, final Pair<List<List<Double>>, List<Integer>> dataSet,
			final List<List<Double>> centroids) throws IOException {

		writeGnuplotScript(scriptFileName, dataFileName, "Clustering on MAGIC data set",
				0, 5, 6, "fLength", "fAsym", "fM3Long", dataSet.getFirst(), dataSet.getSecond(),
				centroids);
	}



	/**
	 * Create a Gnuplot Script to display the clustering results.
	 */
	public static void writeISOLETGnuplotScript(final String scriptFileName,
			final String dataFileName, final Pair<List<List<Double>>, List<Integer>> dataSet,
			final List<List<Double>> centroids) throws IOException {

		// Order of features is not known...
		writeGnuplotScript(scriptFileName, dataFileName, "Clustering on ISOLET data set",
				102, 440, 470, "x", "y", "z", dataSet.getFirst(), dataSet.getSecond(), centroids);
	}



	private static void writeGnuplotScript(final String scriptFileName, final String dataFileName,
			final String title, final int firstIdx, final int secondIdx, final int thirdIdx,
			final String firstLabel, final String secondLabel, final String thirdLabel,
			final List<List<Double>> dataPoints, final List<Integer> classes,
			final List<List<Double>> centroids) throws IOException {

		final Map<Integer, Integer> clusterIndexToClassMap = new HashMap<>();
		associateClustersWithLabels(dataPoints, classes, centroids, clusterIndexToClassMap);

		try (final PrintWriter writer = new PrintWriter(new BufferedWriter(
				new FileWriter(scriptFileName)))) {

			writer.println("set title \"" + title + "\"");
			writer.println("set xlabel \"" + firstLabel + "\"");
			writer.println("set ylabel \"" + secondLabel + "\"");
			writer.println("set zlabel \"" + thirdLabel + "\"");
			writer.println("set palette rgbformulae 33,13,10");
			writer.println("unset colorbox");
			// Dirty Hack: Use an undefined value to conditionally plot some rows
			writer.print("splot "
					+ "\"" + dataFileName + "\" using 1:2:3:($4==$5 ? $5 : 1/0) with points pt 7 palette "
					+ "title \"Right Label\", "
					+ "\"" + dataFileName + "\" using 1:2:3:($4!=$5 ? $5 : 1/0) with points pt 4 palette "
					+ "title \"Wrong Label\"");

			int centroidIndex = 0;
			for (final List<Double> centroid : centroids) {
				writer.printf(Locale.ENGLISH,
						", \"< echo '%.4f %.4f %.4f %d'\" with points lw 3 pt 2 ps 2 palette notitle",
						centroid.get(firstIdx), centroid.get(secondIdx), centroid.get(thirdIdx),
						clusterIndexToClassMap.get(centroidIndex));
				centroidIndex += 1;
			}
			writer.println();

			writer.println("pause -1");
		}

		try (final PrintWriter writer = new PrintWriter(new BufferedWriter(
				new FileWriter(dataFileName)))) {

			for (int i = 0; i < dataPoints.size(); ++i) {
				final List<Double> vector = dataPoints.get(i);

				int clusterIndex = -1;
				double bestDistance = Double.POSITIVE_INFINITY;
				for (int j = 0; j < centroids.size(); ++j) {
					final double distance = distance(vector, centroids.get(j));
					if (distance < bestDistance) {
						bestDistance = distance;
						clusterIndex = j;
					}
				}

				writer.printf(Locale.ENGLISH, "%.4f %.4f %.4f %d %d%n",
						vector.get(firstIdx),
						vector.get(secondIdx),
						vector.get(thirdIdx),
						classes.get(i),
						clusterIndexToClassMap.get(clusterIndex));
			}
		}
	}



	/**
	 * Computes the euclidean distance between two vectors.
	 *
	 * @param first the first vector (represented as a list of double values)
	 * @param second the second vector (represented as a list of double values)
	 * @return the euclidean distance between first and second.
	 */
	public static double distance(final List<Double> first, final List<Double> second) {

		double distSquared = 0.0;

		if (first instanceof RandomAccess && second instanceof RandomAccess) {
			// Optimized code for ArrayList etc.
			final int len = Math.min(first.size(), second.size());
			for (int i = 0; i < len; ++i) {
				final double a = first.get(i);
				final double b = second.get(i);
				distSquared += (a - b) * (a - b);
			}
		} else {
			// Fallback code for LinkedList etc.
			final Iterator<Double> firstIterator = first.iterator();
			final Iterator<Double> secondIterator = second.iterator();
			while (firstIterator.hasNext() && secondIterator.hasNext()) {
				final double a = firstIterator.next();
				final double b = secondIterator.next();
				distSquared += (a - b) * (a - b);
			}
		}

		return Math.sqrt(distSquared);
	}

	/**
	 * Returns the precision of a clustering.
	 */
	public static double getPrecision(final List<List<Double>> dataSet, final List<Integer> classes,
			final List<List<Double>> centroids) {

		return (double) associateClustersWithLabels(dataSet, classes, centroids, null)
				/ dataSet.size();
	}

	/**
	 * Returns a list of vectors that may be used to bootstrap the k-means
	 * clustering.
	 */
	public static List<List<Double>> getInitialCentroids(final List<List<Double>> dataSet,
			final int numberOfCentroids) {

		final Random rng = new Random(1);
		final int NUM_CANDS = 3;

		final List<List<List<Double>>> candidates = new ArrayList<>(NUM_CANDS);
		for (int k = 0; k < NUM_CANDS; ++k) {
			final List<List<Double>> candidate = new ArrayList<>(numberOfCentroids);

			final double[] distances = new double[dataSet.size()];
			for (int i = 0; i < distances.length; ++i) {
				distances[i] = Double.POSITIVE_INFINITY;
			}

			for (int j = 0; j < numberOfCentroids; ++j) {
				List<Double> newCentroid = dataSet.get(dataSet.size() - 1);
				if (candidate.isEmpty()) {
					newCentroid = dataSet.get(rng.nextInt(dataSet.size()));
				} else {
					double distancesSum = 0.0;
					for (int i = 0; i < distances.length; ++i) {
						distancesSum += distances[i];
					}

					double selector = rng.nextDouble() * distancesSum;
					for (int i = 0; i < distances.length; ++i) {
						selector -= distances[i];
						if (selector <= 0) {
							newCentroid = dataSet.get(i);
							break;
						}
					}
				}

				for (int i = 0; i < distances.length; ++i) {
					final double distance = distance(dataSet.get(i), newCentroid);
					distances[i] = Math.min(distances[i], distance * distance);
				}

				candidate.add(newCentroid);
			}

			candidates.add(candidate);
		}

		final List<List<Double>> result = Collections.max(candidates,
				new Comparator<List<List<Double>>>() {
					@Override
					public int compare(final List<List<Double>> first, final List<List<Double>> second) {
						return Double.compare(minComponentDifference(first), minComponentDifference(second));
					}
				});

		return result;
	}

	private static int associateClustersWithLabels(final List<List<Double>> dataSet,
												   final List<Integer> classes, final List<List<Double>> centroids,
												   final Map<Integer, Integer> clusterIndexToClassMap) {

		final Set<Integer> classSet = new HashSet<>(classes);
		final int numberOfClasses = centroids.size();
		final List<List<Integer>> clusteredDataSet = new ArrayList<>();

		for (int i = 0; i < numberOfClasses; ++i) {
			clusteredDataSet.add(new ArrayList<>());
		}

		for (int j = 0; j < dataSet.size(); ++j) {
			final List<Double> vector = dataSet.get(j);
			double bestDistance = Double.POSITIVE_INFINITY;
			int bestIndex = -1;
			for (int i = 0; i < centroids.size(); ++i) {
				final double distance = distance(vector, centroids.get(i));
				if (distance < bestDistance || (distance == bestDistance
						&& before(centroids.get(i), centroids.get(bestIndex)))) {
					bestDistance = distance;
					bestIndex = i;
				}
			}

			clusteredDataSet.get(bestIndex).add(j);
		}

		final Map<Integer, Integer> currentClusterToClassMap = clusterIndexToClassMap == null
				? new HashMap<>()
				: clusterIndexToClassMap;
		final Map<Integer, Integer> currentClassToClusterMap = new HashMap<>();

		final Map<Pair<Integer, Integer>, Integer> correctlyLabeledMap = new HashMap<>();

		final BitSet[] blackSets = new BitSet[numberOfClasses];
		for (int i = 0; i < numberOfClasses; ++i) {
			blackSets[i] = new BitSet();
		}

		while (currentClusterToClassMap.size() < numberOfClasses) {
			int clusterIndex;
			for (clusterIndex = 0; clusterIndex < numberOfClasses; ++clusterIndex) {
				if (!currentClusterToClassMap.containsKey(clusterIndex)) {
					break;
				}
			}

			final List<Integer> cluster = clusteredDataSet.get(clusterIndex);

			int bestIndex = -1;
			double bestCorrectlyLabeled = -1;
			for (int classIndex = 0; classIndex < numberOfClasses; ++classIndex) {
				if (!blackSets[clusterIndex].get(classIndex)) {
					final Pair<Integer, Integer> clusterClassPair = new Pair<>(clusterIndex, classIndex);

					int correctlyLabeled = 0;
					if (correctlyLabeledMap.containsKey(clusterClassPair)) {
						correctlyLabeled = correctlyLabeledMap.get(clusterClassPair);
					} else {
						for (final int vectorIndex : cluster) {
							if (classes.get(vectorIndex).equals(classIndex)) {
								correctlyLabeled += 1;
							}
						}
						correctlyLabeledMap.put(clusterClassPair, correctlyLabeled);
					}

					if (correctlyLabeled > bestCorrectlyLabeled) {
						bestIndex = classIndex;
						bestCorrectlyLabeled = correctlyLabeled;
					}
				}
			}

			blackSets[clusterIndex].set(bestIndex);

			if (!currentClassToClusterMap.containsKey(bestIndex)) {
				currentClassToClusterMap.put(bestIndex, clusterIndex);
				currentClusterToClassMap.put(clusterIndex, bestIndex);
			} else if (correctlyLabeledMap.get(new Pair<>(
					currentClassToClusterMap.get(bestIndex), bestIndex)) < bestCorrectlyLabeled) {

				currentClusterToClassMap.remove(currentClassToClusterMap.get(bestIndex));
				currentClassToClusterMap.put(bestIndex, clusterIndex);
				currentClusterToClassMap.put(clusterIndex, bestIndex);
			}
		}

		int totalCorrectlyLabeled = 0;
		for (final Map.Entry<Integer, Integer> entry : currentClusterToClassMap.entrySet()) {
			totalCorrectlyLabeled += correctlyLabeledMap
					.get(new Pair<>(entry.getKey(), entry.getValue()));
		}

		//final BitSet blackSet = new BitSet();
		//int totalCorrectlyLabeled = 0;
		//for (final int c : classSet) {
		//	int bestCorrectlyLabeled = -1;
		//	int bestClusterSize = 1;
		//	int bestIndex = -1;
		//	for (int i = 0; i < numberOfClasses; ++i) {
		//		if (!blackSet.get(i)) {
		//			final List<Integer> cluster = clusteredDataSet.get(i);

		//			int correctlyLabeled = 0;
		//			for (final int vectorIndex : cluster) {
		//				if (classes.get(vectorIndex).equals(c)) {
		//					correctlyLabeled += 1;
		//				}
		//			}

		//			if (cluster.size() > 0) {
		//				if (bestIndex < 0 || correctlyLabeled > bestCorrectlyLabeled) {
		//					bestCorrectlyLabeled = correctlyLabeled;
		//					bestClusterSize = cluster.size();
		//					bestIndex = i;
		//				}
		//			}
		//		}
		//	}

		//	if (clusterIndexToClassMap != null) {
		//		clusterIndexToClassMap.put(bestIndex, c);
		//	}

		//	blackSet.set(bestIndex);

		//	totalCorrectlyLabeled += bestCorrectlyLabeled;
		//}

		return totalCorrectlyLabeled;
	}


	private static boolean before(final List<Double> vector1, final List<Double> vector2) {
		for (int i = 0; i < vector1.size(); ++i) {
			if (vector1.get(i) < vector2.get(i)) {
				return true;
			} else if (vector1.get(i) > vector2.get(i)) {
				return false;
			}
		}
		return false;
	}


	private static double minComponentDifference(final List<List<Double>> vec) {
		double result = Double.POSITIVE_INFINITY;
		for (int i = 0; i < vec.size(); ++i) {
			for (int j = i + 1; j < vec.size(); ++j) {
				for (int k = 0; k < vec.get(i).size(); ++k) {
					result = Math.min(result, Math.abs(vec.get(i).get(k) - vec.get(j).get(k)));
				}
			}
		}
		return result;
	}
}

