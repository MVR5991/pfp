import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    private static List<List<Double>> centroids;

    private static class KMeansMapper implements Mapper<List<Double>, Void, List<Double>, List<Double>> {
        @Override
        public List<Pair<List<Double>, List<Double>>> map(final List<Double> key, final Void value) {
            List<Pair<List<Double>, List<Double>>> result = new ArrayList();
            List<Double> minCentroid = null;
            for(List<Double> cen : centroids){
                if(minCentroid == null){
                    minCentroid =cen;
                } else if(KMeansUtils.distance(key, cen)< KMeansUtils.distance(key, minCentroid)){
                    minCentroid = cen;
                }

            }
            result.add(new Pair<List<Double>, List<Double>>(minCentroid, key));
            return result;
        }
    }

    private static class KMeansReducer implements Reducer<ArrayList<Double>, ArrayList<Double>, List<Double>, Void> {
        @Override
        public List<Pair<List<Double>, Void>> reduce(ArrayList<Double> key, List<ArrayList<Double>> values) {
            double sum = 0;
            int count = 0;
            int position = 0;
            List<Double> result = new ArrayList<Double>();
            for(double x : key){
                for(List<Double> l : values){
                    count++;
                    sum = sum + l.get(position);
                }
                result.add(position,(sum/count));
                position++;
            }
            List<Pair<List<Double>, Void>> resultList = new ArrayList<>();
            resultList.add(new Pair(result, null));
            return resultList;
        }
    }

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
	    List<Pair<List, Void>>initialSubmit = new ArrayList<>();
		for(List x : dataSet){
            initialSubmit.add(new Pair(x, null));
        }
		final int dimensions = dataSet.get(0).size();
		MapReduce mapReduce = new SequentialMapReduce(new KMeansMapper(), new KMeansReducer());
		centroids = KMeansUtils.getInitialCentroids(dataSet, numberOfClusters);
         List<List<Double>> oldCentroids = null;
         double newDistance = Double.MAX_VALUE;
        while(newDistance > epsilon || newDistance == Double.MAX_VALUE){
            oldCentroids = centroids;
			centroids = castTOCentroidList((List<Pair<List<Double>, Void>>) mapReduce.submit(initialSubmit));
            newDistance = getMaxDistanceBetweenCentroids(oldCentroids, centroids);
            System.out.println("here");
		}

		return centroids;
	}

    private static double getMaxDistanceBetweenCentroids(List<List<Double>> oldCentroids, List<List<Double>> centroids) {
	    double result = Double.MAX_VALUE;
	    int index = 0;
	    for(List<Double> cen:oldCentroids){
	        if(result == Double.MAX_VALUE){
                result = KMeansUtils.distance(cen,centroids.get(index));
            } else {
	            if(centroids.size() == index) continue;
                double distanceBetweenCentroids = KMeansUtils.distance(cen,centroids.get(index));
	            if(distanceBetweenCentroids< result){
	                result = distanceBetweenCentroids;
                }
            }
            index++;
        }
        return result;
    }

    private static List<List<Double>> castTOCentroidList(List<Pair<List<Double>, Void>> submit) {
	    List<List<Double>> newCentroids = new ArrayList<>();
	    for(Pair<List<Double>, Void> l : submit){
	        List<Double> newCentroid = new ArrayList<>();
	        for(Double d : l.getKey()){
	            if(d != null){
                    newCentroid.add(d);
                }
            }
            newCentroids.add(newCentroid);
        }
        return newCentroids;
    }


    public static void main(final String[] args) throws IOException {
//		final Pair<List<List<Double>>, List<Integer>> inputDataSet = KMeansUtils.loadIrisDataset();
		final Pair<List<List<Double>>, List<Integer>> inputDataSet = KMeansUtils.loadMAGICDataset();
//		final Pair<List<List<Double>>, List<Integer>> inputDataSet = KMeansUtils.loadISOLETDataset();

		final long startTime = System.nanoTime();

		final List<List<Double>> centroids = findCentroids(inputDataSet.getFirst(), 3, 0.001);
//		final List<List<Double>> centroids = findCentroids(inputDataSet.getFirst(), 2, 0.001);
//		final List<List<Double>> centroids = findCentroids(inputDataSet.getFirst(), 26, 0.001);

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

