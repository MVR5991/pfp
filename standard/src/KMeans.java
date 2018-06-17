import java.io.IOException;
import java.util.List;



/**
 * Performs k-means clustering using the MapReduce framework. The goal of
 * k-means clustering is to group data points into k groups by iteratively
 * refining the groups.
 * <p>
 * k-means clustering uses designated points (centroids) to define the
 * cluster. Here, a data point belongs to the cluster of a centroid if it is
 * closer to this centroid than to any other centroid.
 * <p>
 * A single iteration of k-means clustering performs the following steps:
 * <ol>
 *   <li>Compute the clusters, i.e., for each centroid find the data points
 *       that belong to it</li>
 *   <li>Update the centroids. The new centroid for a cluster is set to the
 *       center point of the data points in the cluster.
 * </ol>
 * <p>
 * The above steps are repeated until the algorithm converges.
 * <p>
 * More details can be found on the respective
 * <a href="https://en.wikipedia.org/wiki/K-means_clustering">Wikipedia page</a>.
 */
public class KMeans {

	/**
	 * Perform k-means clustering and return the resulting centroids. The
	 * returned centroids define the clusters after the algorithm has converged.
	 * The algorithm should terminate when the centroids change less than
	 * epsilon in a single iteration.
	 *
	 * @param dataSet the data points
	 * @param numberOfClusters the number of clusters to generate
	 * @param epsilon the epsilon parameter to detect convergence
	 * @return the resulting centroids
	 */
	public static List<List<Double>> findCentroids(final List<List<Double>> dataSet,
			final int numberOfClusters, final double epsilon) {
		
		final int dimensions = dataSet.get(0).size();

		final List<List<Double>> centroids
				= KMeansUtils.getInitialCentroids(dataSet, numberOfClusters);

		// TODO

		return null;
	}



	public static void main(final String[] args) throws IOException {
		final Pair<List<List<Double>>, List<Integer>> inputDataSet = KMeansUtils.loadIrisDataset();
		//final Pair<List<List<Double>>, List<Integer>> inputDataSet = KMeansUtils.loadMAGICDataset();
		//final Pair<List<List<Double>>, List<Integer>> inputDataSet = KMeansUtils.loadISOLETDataset();

		final long startTime = System.nanoTime();

		final List<List<Double>> centroids = findCentroids(inputDataSet.getFirst(), 3, 0.001);
		//final List<List<Double>> centroids = findCentroids(inputDataSet.getFirst(), 2, 0.001);
		//final List<List<Double>> centroids = findCentroids(inputDataSet.getFirst(), 26, 0.001);

		System.out.printf("Computed centroids in %.2f ms%n",
				(System.nanoTime() - startTime) / 1000000.0);

		System.out.printf("%.2f%% precision%n", 100 * KMeansUtils.getPrecision(
				inputDataSet.getFirst(), inputDataSet.getSecond(), centroids));

		// Uncomment one of these to generate Gnuplot files for visualization
		//KMeansUtils.writeIrisGnuplotScript("iris-res.plot", "iris-res.dat", inputDataSet,
		//		centroids);
		//KMeansUtils.writeMAGICGnuplotScript("magic-res.plot", "magic-res.dat", inputDataSet,
		//		centroids);
		//KMeansUtils.writeISOLETGnuplotScript("isolet-res.plot", "isolet-res.dat", inputDataSet,
		//		centroids);
	}
}

