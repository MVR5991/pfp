import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GradeCounterImpl implements GradeCounter {

    private ConcurrentHashMap<String, Integer> gradesMap;

    @Override
    public GradeCount[] count(String[] grades, int nThreads) {
        final Thread[] ts = new Thread[nThreads];
        final GradeCount[] result;
        gradesMap = new ConcurrentHashMap();

        int numberOfGrades = grades.length;
        int gradesPerThread = numberOfGrades / nThreads;
        int remainingGrades = numberOfGrades % nThreads;
        int startIndex = 0;

        for (int x = 0; x < nThreads; x++) {
            CounterThread pt;
            if (remainingGrades != 0) {
                pt = new CounterThread(startIndex, startIndex + gradesPerThread + 1, gradesMap, grades);
                remainingGrades--;
                startIndex = startIndex + gradesPerThread + 1;
            } else {
                pt = new CounterThread(startIndex, startIndex + gradesPerThread, gradesMap, grades);
                startIndex = startIndex + gradesPerThread;
            }
            ts[x] = pt;
            pt.start();
        }
        for (Thread t : ts) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        result = new GradeCount[gradesMap.size()];
        int index = 0;
        for (Map.Entry<String, Integer> e : gradesMap.entrySet()) {
            result[index] = new GradeCount(e.getKey(), e.getValue());
            index++;
        }
        return result;
    }
}

class CounterThread extends Thread {


    private final int startIndex;
    private final int endIndex;
    private final ConcurrentHashMap<String, Integer> gradesMap;
    private final String[] grades;

    public CounterThread(int startIndex, int endIndex, ConcurrentHashMap<String, Integer> gradesMap, String[] grades) {

        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.gradesMap = gradesMap;
        this.grades = grades;
    }

    @Override
    public void run() {

        for (int i = startIndex; i < endIndex; i++) {
            String grade = grades[i];

            Integer oldValue, newValue;
            Integer containsKey = 1;
            if (!gradesMap.containsKey(grade)) {
                containsKey = gradesMap.putIfAbsent(grade, 1);
            }
            if (containsKey != null) {
                do {
                    oldValue = gradesMap.get(grade);
                    newValue = oldValue + 1;
                } while (!gradesMap.replace(grade, oldValue, newValue));
            }

        }
    }
}
