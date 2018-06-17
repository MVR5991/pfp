import java.util.*;
import java.util.concurrent.*;


/**
 * A parallel implementation of the execution environment for MapReduce tasks.
 *
 * @see MapReduce
 */
public class ParallelMapReduce<InKey, InValue, TmpKey, TmpValue, OutKey, OutValue>
		extends MapReduce<InKey, InValue, TmpKey, TmpValue, OutKey, OutValue> {
	
	private final int numberOfThreads;
    private ExecutorService mappersService;
    private ExecutorService reduceService;

	/**
	 * Creates a parallel execution environment for MapReduce tasks. It uses the
	 * specified number of threads to execute tasks in parallel.
	 */
	public ParallelMapReduce(final Mapper<InKey, InValue, TmpKey, TmpValue> mapper,
			final Reducer<TmpKey, TmpValue, OutKey, OutValue> reducer,
			final int numberOfThreads) {

		super(mapper, reducer);
		this.numberOfThreads = numberOfThreads;

    }

    private ArrayList initThreads(List<Pair<InKey, InValue>> input) {
        ArrayList<Future> futuresList = new ArrayList<>(numberOfThreads);
        mappersService = Executors.newFixedThreadPool(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            futuresList.add(mappersService.submit(new MapperRunnable(i, input)));
        }
        return futuresList;
    }

    /**
	 * {@inheritDoc}
	 */

	@Override
	public Iterable<Pair<OutKey, OutValue>> submit(final List<Pair<InKey, InValue>> input) {
        final ArrayList<Pair<OutKey, OutValue>> result;
		Map<TmpKey, List<TmpValue>> combinedTemporaryValues = new HashMap<>();
        ArrayList<Future> futuresList = initThreads(input);

        combinedTemporaryValues = aggrevateValues(futuresList, combinedTemporaryValues);
        shutdownMappers();
        ArrayList<Future> reduceFuturesList = initReducerService(combinedTemporaryValues);
        result = aggregateReducedLists(reduceFuturesList);
        shutdownReducers();
        return result;
	}

    private ArrayList<Future> initReducerService(Map<TmpKey, List<TmpValue>> combinedTemporaryValues) {
        reduceService = Executors.newFixedThreadPool(numberOfThreads);
        ArrayList<Future> reduceFuturesList = new ArrayList<>(combinedTemporaryValues.size());
        for (final Map.Entry<TmpKey, List<TmpValue>> temporaryPairs
                : combinedTemporaryValues.entrySet()) {
            reduceFuturesList.add(reduceService.submit(new ReducerCallable(temporaryPairs)));
        }
        return reduceFuturesList;
    }

    private void shutdownReducers() {
        try {
            reduceService.shutdown();
            reduceService.awaitTermination(30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void shutdownMappers() {
        try {
            mappersService.shutdown();
            mappersService.awaitTermination(30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private ArrayList<Pair<OutKey, OutValue>> aggregateReducedLists(ArrayList<Future> futuresList) {
        final ArrayList<Pair<OutKey, OutValue>> result = new ArrayList<>();

        while (!futuresList.isEmpty()) {
            for (Iterator<Future> it = futuresList.iterator(); it.hasNext(); ) {
                Future f = it.next();
                if (f.isDone()) {
                    List<Pair<OutKey, OutValue>> tmpList = null;
                    try {
                        tmpList = (List<Pair<OutKey, OutValue>>) f.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    result.addAll(tmpList);
                    it.remove();
                }
            }
        }
        return result;
    }

    private Map<TmpKey, List<TmpValue>> aggrevateValues(ArrayList<Future> futuresList, Map<TmpKey, List<TmpValue>> combinedTemporaryValues) {
        while(!futuresList.isEmpty()){
            for(Iterator<Future> it = futuresList.iterator(); it.hasNext();){
                Future f = it.next();
                if(f.isDone()){
                    try {
                        Map<TmpKey, List<TmpValue>> tmpMap = (Map<TmpKey, List<TmpValue>>) f.get();
                        tmpMap.forEach((key, value) -> combinedTemporaryValues.merge(key, value, (v1, v2) -> {
                            List res = new ArrayList(v1);
                            res.addAll(v2);
                            return res;
                        }));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    it.remove();
                }
            }
        }
        return combinedTemporaryValues;
    }

    class ReducerCallable implements Callable {
        final Map.Entry<TmpKey, List<TmpValue>> temporaryPairs;
        List<Pair<OutKey, OutValue>> result;
        public ReducerCallable(Map.Entry<TmpKey, List<TmpValue>> temporaryPairs) {
            this.temporaryPairs = temporaryPairs;
        }

        @Override
        public Object call() {
            return result = getReducer().reduce(temporaryPairs.getKey(), temporaryPairs.getValue());
        }
    }

    class MapperRunnable implements Callable {
	    private int id;
        private List<Pair<InKey, InValue>> input;
        private Map<TmpKey, List<TmpValue>> temporaryValues = new HashMap<>();
        public MapperRunnable(int id, List<Pair<InKey, InValue>> input) {
            this.id = id;
            this.input = input;
        }

        @Override
        public Object call() {
            for(int c = id; c < input.size(); c = c+numberOfThreads){
                Pair<InKey, InValue> p = input.get(c);
                for (final Pair<TmpKey, TmpValue> mappedPair : getMapper().map(p.getKey(), p.getValue())) {

                    if (!temporaryValues.containsKey(mappedPair.getKey())) {
                        temporaryValues.put(mappedPair.getKey(), new ArrayList<TmpValue>());
                    }
                    temporaryValues.get(mappedPair.getKey()).add(mappedPair.getValue());
                }
            }
            return temporaryValues;
        }
    }
}

