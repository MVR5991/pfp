import java.util.*;


/**
 * Computes shortest paths using Dijksta's algorithm modified for MapReduce.
 */
public class Dijkstra {
    private static int[][] adjacencyMatrix;
    private static  Queue<Integer> queue = new LinkedList();
    private static class DijkstraMapper implements Mapper<Integer, Pair<Integer, List<Integer>>, Integer, Integer> {
        @Override
        public List<Pair<Integer, Integer>> map(final Integer key, final Pair<Integer, List<Integer>> value) {
            List<Pair<Integer, Integer>> returnValues = new ArrayList();
            returnValues.add(new Pair(key, value.getKey()));
            for(int x : value.getValue()){
                returnValues.add(new Pair(x, value.getKey()+adjacencyMatrix[key][x]));
            }
            return returnValues;
        }
    }

    private static class DijkstraReducer implements Reducer<Integer, Integer, Integer, Integer> {
        @Override
        public List<Pair<Integer, Integer>> reduce(final Integer key, final List<Integer> values) {
            int minimum = -1;
            for (int value : values) {
                if (minimum == -1) {
                    minimum = value;
                } else if (value < minimum) {
                    minimum = value;
                }
            }
            List<Pair<Integer, Integer>> returnValue = new ArrayList();
            returnValue.add(new Pair(key, minimum));

            return returnValue;
        }
    }

    /**
     * Finds the shortest paths from the specified node to all other nodes.
     *
     * @param adjacencyMatrix the graph represented as an adjacency matrix
     * @param startNode       the start node
     * @return an array containing the lengths of the shortest paths from
     * startNode to the node corresponding to the index
     */
    public static int[] findShortestPaths(final int[][] adjacencyMatrix, final int startNode) {
        Dijkstra.adjacencyMatrix = adjacencyMatrix;
        MapReduce mapReduce = new ParallelMapReduce(new DijkstraMapper(), new DijkstraReducer(),2);
        List<Pair<Integer, Pair>> list = new ArrayList<>();
        list.add(new Pair(startNode,new Pair<Integer, List>(0,findeNachfolger(startNode, adjacencyMatrix))));
        Iterable oldList = null;
        Iterable returnFromMapReduce = mapReduce.submit(list);
        while(!returnFromMapReduce.equals(oldList)){
            oldList = returnFromMapReduce;
            returnFromMapReduce = mapReduce.submit(createNextInput(returnFromMapReduce));
        }
        return castToArray(returnFromMapReduce, adjacencyMatrix.length);
    }

    private static List<Pair<Integer, Pair>> createNextInput(Iterable<Pair<Integer, Integer>> returnFromMapReduce) {
        List<Pair<Integer, Pair>> result = new ArrayList<>();
        for(Pair<Integer, Integer> x:returnFromMapReduce){
            result.add(new Pair(x.getKey(),new Pair<Integer, List>(x.getValue(),findeNachfolger(x.getKey(), adjacencyMatrix))));
        }
        return result;
    }


//    /**
//     * Finds the shortest paths from the specified node to all other nodes.
//     *
//     * @param adjacencyMatrix the graph represented as an adjacency matrix
//     * @param startNode       the start node
//     * @return an array containing the lengths of the shortest paths from
//     * startNode to the node corresponding to the index
//     */
//    public static int[] findShortestPaths(final int[][] adjacencyMatrix, final int startNode) {
//        boolean isDone = false;
//        Dijkstra.adjacencyMatrix = adjacencyMatrix;
//        MapReduce mapReduce = new SequentialMapReduce(new DijkstraMapper(), new DijkstraReducer());
//        Iterable oldList = initOldList(startNode, adjacencyMatrix);
//        Iterable nextList = null;
//
//        ((LinkedList) queue).add(startNode);
//        while (!isDone) {
//            if (compareLists(oldList, nextList)) {
//                isDone = true;
//                continue;
//            } else {
//                oldList = nextList;
//            }
//            nextList = mapReduce.submit(findeNachfolger(queue.poll(), adjacencyMatrix));
//        }
//        int result[] = castToArray((List<Pair<Integer, Integer>>) nextList);
//        return result;
//
//    }

    private static int[] castToArray(Iterable<Pair<Integer, Integer>> list, int length) {
        int[] result = new int[length];
        for (Pair<Integer, Integer> x : list) {
            result[x.getKey()] = x.getValue();
        }
        return result;
    }

    private static boolean compareLists(Iterable<Pair<Integer, Integer>> oldList, Iterable<Pair<Integer, Integer>> nextList) {
        boolean result = true;
        if (nextList == null) {
            return false;
        } else if (oldList == null) {
            for (Pair<Integer, Integer> next : nextList) {
                result = false;
                queue.add(next.getKey());
//                queue.poll();
            }
        } else {
            for (Pair<Integer, Integer> pairNext : nextList) {
                boolean foundPair = false;
                for (Pair<Integer, Integer> pairOld : oldList) {
                    if (pairNext.equals(pairOld)) {
                        foundPair = true;
                        continue;
                    }
                    if (!foundPair) {
                        queue.add(pairNext.getKey());
                        result = false;
                    }
                }
            }
        }
        return result;
    }

    private static Iterable initOldList(int startNode, int[][] adjacencyMatrix) {
        Iterable result = new ArrayList();
        for (int x = 0; x < adjacencyMatrix.length; x++) {
            if (adjacencyMatrix[startNode][x] != -1) {
                ((ArrayList) result).add(new Pair(x, adjacencyMatrix[startNode][x]));
            }
        }
        return result;
    }

    private static boolean areWeDone() {
        return false;
    }

    private static List findeNachfolger(int knoten, int[][] adjacencyMatrix) {
        final List<Integer> result = new ArrayList();
        for (int x = 0; x < adjacencyMatrix.length; x++) {
            if (adjacencyMatrix[knoten][x] != -1 && x != knoten) {
                result.add(x);
            }
        }
        return result;
    }

//    private static List findeNachfolger(int knoten, int[][] adjacencyMatrix) {
//        final List<Pair<Integer, Integer>> result = new ArrayList();
//        for (int x = 0; x < adjacencyMatrix.length; x++) {
//            if (adjacencyMatrix[knoten][x] != -1) {
//
//                result.add(new Pair<>(x, adjacencyMatrix[knoten][x]));
//            }
//        }
//        return result;
//    }


    public static void main(final String[] args) {

        final int[][] adjacencyMatrix = {
                {0, 10, 1, 8, -1, -1},
                {10, 0, 7, -1, -1, 2},
                {1, 7, 0, 4, -1, 6},
                {8, -1, 4, 0, 6, -1},
                {-1, -1, -1, 6, 0, 3},
                {-1, 2, 6, -1, 3, 0}
        };

        final int[] shortest = findShortestPaths(adjacencyMatrix, 0);
        System.out.println(Arrays.toString(shortest));
    }
}

