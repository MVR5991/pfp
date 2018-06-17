import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * A simple sequential implementation of a MapReduce executor.
 *
 * @see MapReduce
 */
public class SequentialMapReduce<InKey, InValue, TmpKey, TmpValue, OutKey, OutValue>
		extends MapReduce<InKey, InValue, TmpKey, TmpValue, OutKey, OutValue> {

	/**
	 * Creates a sequential execution environment for MapReduce tasks.
	 */
	public SequentialMapReduce(final Mapper<InKey, InValue, TmpKey, TmpValue> mapper,
			final Reducer<TmpKey, TmpValue, OutKey, OutValue> reducer) {

		super(mapper, reducer);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<Pair<OutKey, OutValue>> submit(final List<Pair<InKey, InValue>> input) {
		final Map<TmpKey, List<TmpValue>> combinedTemporaryValues = new HashMap<>();

		for (final Pair<InKey, InValue> pair : input) {

			for (final Pair<TmpKey, TmpValue> mappedPair : this.getMapper().map(pair.getKey(), pair.getValue())) {
				
				if (!combinedTemporaryValues.containsKey(mappedPair.getKey())) {
					combinedTemporaryValues.put(mappedPair.getKey(), new ArrayList<TmpValue>());
				}
				combinedTemporaryValues.get(mappedPair.getKey()).add(mappedPair.getValue());
			}
		}

		final ArrayList<Pair<OutKey, OutValue>> result = new ArrayList<>();

		for (final Map.Entry<TmpKey, List<TmpValue>> temporaryPairs
				: combinedTemporaryValues.entrySet()) {
			
			result.addAll(this.getReducer().reduce(temporaryPairs.getKey(), temporaryPairs.getValue()));
		}

		return Collections.unmodifiableList(result);
	}
}

