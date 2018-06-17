import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * A simple program that shows how to use the PFP MapReduce framework.
 */
public class HelloWorld {

    /**
     * Map tasks are specified by creating a class that implements the Mapper
     * interface. The Mapper interface is parameterized by the types of the
     * input key and value and by the types of the intermediate keys and values.
     * This Mapper only uses a single input key. Hence, the type of the input
     * value is Void.
     */
    private static class HelloWorldMapper implements Mapper<String, Void, String, Integer> {
        @Override
        public List<Pair<String, Integer>> map(final String key, final Void value) {
            // This Mapper returns one key-value pair for each input.
            return Collections.singletonList(new Pair<>(key, 1));
        }
    }

    /**
     * Similarly, reduce tasks are specified by creating a class that implements
     * the Reducer interface. The Reducer interface is parameterized by the
     * types of the intermediate key and value (the same used for the Mapper)
     * and by the types of the output keys and values. This Mapper only uses a
     * single output key. Hence, the type of the output value is Void.
     */
    private static class HelloWorldReducer implements Reducer<String, Integer, String, Void> {
        @Override
        public List<Pair<String, Void>> reduce(final String key, final List<Integer> values) {
            // This Reducer calculates a sum of the values ...
            int result = 0;
            for (final Integer v : values) {
                result += v;
            }

            // ... and returns a single output key
            return Collections.singletonList(new Pair<>(result + " x Hello " + key, null));
        }
    }


    public static void main(final String[] args) {
        // Create an execution environment for MapReduce tasks. This is comparable
        // to a specialized ExecutorService. The Mapper and Reducer are passed to
        // the constructor
        final MapReduce<String, Void, String, Integer, String, Void> mapReduce
                = new ParallelMapReduce<>(
                new HelloWorldMapper(),
                new HelloWorldReducer(), 2);

        // Specify the input data. The data are represented as a list of key-value
        // pairs, even though we only want to use the keys here. Therefore, all
        // values are set to <code>null</code> (the only possible value for the
        // type Void).
        final List<Pair<String, Void>> input = Arrays.asList(
                new Pair<String, Void>("World", null),
                new Pair<String, Void>("Erlangen", null),
                new Pair<String, Void>("PFP", null),
                new Pair<String, Void>("World", null),
                new Pair<String, Void>("Erlangen", null),
                new Pair<String, Void>("PFP", null),
                new Pair<String, Void>("World", null),
                new Pair<String, Void>("Erlangen", null),
                new Pair<String, Void>("PFP", null),
                new Pair<String, Void>("World", null),
                new Pair<String, Void>("Erlangen", null),
                new Pair<String, Void>("PFP", null),
                new Pair<String, Void>("World", null));

        // Start the MapReduce computation and iterate over its result (the output
        // key-value pairs)
        for (final Pair<String, Void> result : mapReduce.submit(input)) {
            System.out.println(result.getKey());
        }
    }
}

