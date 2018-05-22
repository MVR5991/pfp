import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class GradeCounterTest {

    private GradeCounter counter;

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[1000][0];
    }

    private static GradeCount[] filterZeros(final GradeCount[] counts) {
        int numZeros = 0;
        for (final GradeCount count : counts) {
            numZeros += count.count == 0 ? 1 : 0;
        }

        final GradeCount[] result = new GradeCount[counts.length - numZeros];
        for (int i = 0, j = 0; i < counts.length && j < result.length; ++i) {
            if (counts[i].count != 0) {
                result[j++] = counts[i];
            }
        }

        return result;
    }

    @Before
    public void setup() {
        counter = new GradeCounterImpl();
    }

    @Test
    public void testSingleStudentArray() {
        final String[] input = {"3.0"};
        final GradeCount[] result = filterZeros(counter.count(input, 1));
        assertEquals(1, result.length);
        assertEquals(1, result[0].count);
        assertEquals(input[0], result[0].grade);
    }

    @Test
    public void testTwoStudentsWithSameGrade() {
        final String[] input = {"3.0", "3.0"};
        final GradeCount[] result = filterZeros(counter.count(input, 1));
        assertEquals(1, result.length);
        assertEquals(2, result[0].count);
        assertEquals(input[0], result[0].grade);
    }

    @Test
    public void testFourStudentsWithSameGradeWithTwoThreads() {
        final String[] input = {"3.0", "3.0", "3.0", "3.0"};
        final GradeCount[] result = filterZeros(counter.count(input, 2));
        assertEquals(1, result.length);
        assertEquals(4, result[0].count);
        assertEquals(input[0], result[0].grade);
    }

    @Test
    public void testTwoStudentsWithDifferentGrade() {
        final String[] input = {"1.0", "3.0"};
        final GradeCount[] result = filterZeros(counter.count(input, 1));
        Arrays.sort(result);
        assertEquals(2, result.length);
        assertEquals(1, result[0].count);
        assertEquals(1, result[1].count);
        assertEquals(input[0], result[0].grade);
        assertEquals(input[1], result[1].grade);
    }

    @Test
    public void testTwoStudentsWithDifferentGradeWithTwoThreads() {
        final String[] input = {"1.0", "3.0"};
        final GradeCount[] result = filterZeros(counter.count(input, 2));
        Arrays.sort(result);
        assertEquals(2, result.length);
        assertEquals(1, result[0].count);
        assertEquals(1, result[1].count);
        assertEquals(input[0], result[0].grade);
        assertEquals(input[1], result[1].grade);
    }

    @Test
    public void testGivenHistogram() {
        final String[] grades = {"1.0", "2.0", "3.0", "4.0", "5.0"};
        final int[] counts = {3, 8, 11, 6, 9};

        final String[] input = new String[
                counts[0] + counts[1] + counts[2] + counts[3] + counts[4]
                ];
        for (int i = 0, j = 0; i < counts.length; ++i) {
            for (int k = 0; k < counts[i]; ++k, ++j) {
                input[j] = grades[i];
            }
        }

        final GradeCount[] result = filterZeros(counter.count(input, 4));
        Arrays.sort(result);

        assertEquals(counts.length, result.length);
        for (int i = 0; i < result.length; ++i) {
            assertEquals(grades[i], result[i].grade);
            assertEquals(counts[i], result[i].count);
        }
    }

    @Test
    public void testPseudorandomDataSmall() {
        final Random rng = new Random(42);
        final int count = 10000;
        final String[] possibleGrades = {
                "1.0", "1.3", "1.7", "2.0", "2.3", "2.7", "3.0", "3.3", "3.7", "4.0", "5.0"
        };
        final Map<String, Integer> reference = new HashMap<>();
        final String[] input = new String[count];
        for (int i = 0; i < count; ++i) {
            final String grade = possibleGrades[rng.nextInt(possibleGrades.length)];
            input[i] = grade;
            if (reference.containsKey(grade)) {
                reference.put(grade, reference.get(grade) + 1);
            } else {
                reference.put(grade, 1);
            }
        }

        final GradeCount[] result = filterZeros(counter.count(input, 2));

        assertEquals(reference.size(), result.length);
        int sum = 0;
        for (int i = 0; i < result.length; ++i) {
            assertEquals(reference.get(result[i].grade).intValue(), result[i].count);
            sum += result[i].count;
        }
        assertEquals(count, sum);
    }

    @Test
    public void testPseudorandomDataMedium() {
        final Random rng = new Random(42);
        final int count = 500000;
        final String[] possibleGrades = {
                "1.0", "1.3", "1.7", "2.0", "2.3", "2.7", "3.0", "3.3", "3.7", "4.0", "5.0"
        };
        final Map<String, Integer> reference = new HashMap<>();
        final String[] input = new String[count];
        for (int i = 0; i < count; ++i) {
            final String grade = possibleGrades[rng.nextInt(possibleGrades.length)];
            input[i] = grade;
            if (reference.containsKey(grade)) {
                reference.put(grade, reference.get(grade) + 1);
            } else {
                reference.put(grade, 1);
            }
        }

        final GradeCount[] result = filterZeros(counter.count(input, 3));

        assertEquals(reference.size(), result.length);
        int sum = 0;
        for (int i = 0; i < result.length; ++i) {
            assertEquals(reference.get(result[i].grade).intValue(), result[i].count);
            sum += result[i].count;
        }
        assertEquals(count, sum);
    }

    @Test
    public void testPseudorandomDataLarge() {
        final Random rng = new Random(42);
        final int count = 5000000;
        final String[] possibleGrades = {
                "1.0", "1.3", "1.7", "2.0", "2.3", "2.7", "3.0", "3.3", "3.7", "4.0", "5.0"
        };
        final Map<String, Integer> reference = new HashMap<>();
        final String[] input = new String[count];
        for (int i = 0; i < count; ++i) {
            final String grade = possibleGrades[rng.nextInt(possibleGrades.length)];
            input[i] = grade;
            if (reference.containsKey(grade)) {
                reference.put(grade, reference.get(grade) + 1);
            } else {
                reference.put(grade, 1);
            }
        }

        final GradeCount[] result = filterZeros(counter.count(input, 4));

        assertEquals(reference.size(), result.length);
        for (int i = 0; i < result.length; ++i) {
            assertEquals(reference.get(result[i].grade).intValue(), result[i].count);
        }
    }
}

