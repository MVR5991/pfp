import java.util.List;

/**
 * An interface for 'reduce' computations.
 *
 * @param TmpKey   the type of the input key argument
 * @param TmpValue the type of the input value argument list
 * @param OutKey   the type of the resulting key
 * @param OutValue the type of the resulting value
 */
@FunctionalInterface
public interface Reducer<TmpKey, TmpValue, OutKey, OutValue> {

	/**
	 * Performs the 'reduce' computation on the specified key and associated
	 * values.
	 *
	 * @param key    the key passed to the computation
	 * @param values the List of values associated with key
	 */
	List<Pair<OutKey, OutValue>> reduce(TmpKey key, List<TmpValue> values);
}

