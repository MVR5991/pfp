import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Computes shortest paths using Dijksta's algorithm modified for MapReduce.
 */
public class Dijkstra {

    private static class DijkstraMapper implements Mapper<String, Void, String, Integer> {
        @Override
        public List<Pair<String, Integer>> map(final String key, final Void value) {
           return null;
        }
    }

    private static class DijkstraReducer implements Reducer<String, Integer, String, Void> {
        @Override
        public List<Pair<String, Void>> reduce(final String key, final List<Integer> values) {
           return null;
        }
    }



	/**
	 * Finds the shortest paths from the specified node to all other nodes.
	 *
	 * @param adjacencyMatrix the graph represented as an adjacency matrix
	 * @param startNode the start node
	 * @return an array containing the lengths of the shortest paths from
	 *         startNode to the node corresponding to the index
	 */
	public static int[] findShortestPaths(final int[][] adjacencyMatrix, final int startNode) {
	    MapReduce mapReduce = new SequentialMapReduce(new DijkstraMapper(), new DijkstraReducer());
        List nachfolger = findeNachfolger(startNode, adjacencyMatrix);
		while(!nachfolger.isEmpty()){

			mapReduce.submit(nachfolger);

		}
		return null;
	}
    private static List findeNachfolger(int knoten, int[][] adjacencyMatrix){
	    final List<Pair<Integer, Integer>> result = new ArrayList();
	    for(int x = 0; x < adjacencyMatrix.length; x++){
	        if(adjacencyMatrix[knoten][x] != -1){
	            result.add(new Pair<>(x, adjacencyMatrix[knoten][x]));
            }
        }
        return result;
    }


	public static void main(final String[] args) {

		final int[][] adjacencyMatrix = {
			{  0, 10,  1,  8, -1, -1 },
			{ 10,  0,  7, -1, -1,  2 },
			{  1,  7,  0,  4, -1,  6 },
			{  8, -1,  4,  0,  6, -1 },
			{ -1, -1, -1,  6,  0,  3 },
			{ -1,  2,  6, -1,  3,  0 }
		};

		final int[] shortest = findShortestPaths(adjacencyMatrix, 0);
		System.out.println(Arrays.toString(shortest));
	}
}

