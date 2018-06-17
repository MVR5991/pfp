import java.util.List;



/**
 * Abstract base class for all execution environments for MapReduce tasks. The
 * map and reduce operations can be specified by passing suitable classes to
 * the constructor.
 * <p>
 * This class assumes the following execution model:
 * <pre>
 * map: (in-key, in-value) --&gt; Mapper --&gt; (tmp-key, tmp-value)*
 *
 * reduce: (tmp-key, tmp-value*) --&gt; Reducer --&gt; (out-key, out-value)*
 * </pre>
 * Here, <code>*</code> refers to the Kleene star, i.e., a List of zero or
 * more occurrences of a value of the previous type.
 *
 * @param InKey the type of the input keys
 * @param InValue the type of the input values
 * @param TmpKey the type of the intermediate keys
 * @param TmpValue the type of the intermediate values
 * @param OutKey the type of the output keys
 * @param OutValue the type of the output values
 */
public abstract class MapReduce<InKey, InValue, TmpKey, TmpValue, OutKey, OutValue> {

	private final Mapper<InKey, InValue, TmpKey, TmpValue> mapper;
	private final Reducer<TmpKey, TmpValue, OutKey, OutValue> reducer;



	/**
	 * Create a new execution environment for MapReduce tasks. This constructor
	 * should be called by sub-classes in order to set the Mapper and Reducer.
	 *
	 * @param mapper the Mapper to use
	 * @param reducer the Reducer to use
	 */
	protected MapReduce(final Mapper<InKey, InValue, TmpKey, TmpValue> mapper,
			final Reducer<TmpKey, TmpValue, OutKey, OutValue> reducer) {

		this.mapper = mapper;
		this.reducer = reducer;
	}



	/**
	 * Returns the Mapper used by this MapReduce instance.
	 */
	public final Mapper<InKey, InValue, TmpKey, TmpValue> getMapper() {
		return this.mapper;
	}



	/**
	 * Returns the Reducer used by this MapReduce instance.
	 */
	public final Reducer<TmpKey, TmpValue, OutKey, OutValue> getReducer() {
		return this.reducer;
	}



	/**
	 * Performs the actual execution of MapReduce tasks and returns its results.
	 * In other words, this method applies the Mapper once to every input.
	 * Afterwards, the results of the Mapper applications are combined. The
	 * Reducer is then applied once to these combined results.
	 * <p>
	 * Contrary to the methods of an ExecutorService, this method waits for the
	 * MapReduce task to complete.
	 *
	 * @param input the input data (as key-value pairs)
	 * @return an iterable over the produced key-value pairs
	 */
	public abstract Iterable<Pair<OutKey, OutValue>> submit(List<Pair<InKey, InValue>> input);
}

