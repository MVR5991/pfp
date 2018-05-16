import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GradeCounterImpl implements GradeCounter {

    @Override
    public GradeCount[] count(final String[] grades, final int nThreads) {
        final ConcurrentHashMap<String, AtomicInteger> gradesMap = new ConcurrentHashMap<>();
        final Thread[] ts;
        ts = initialiseAndStartThreads(grades, nThreads, gradesMap);
        joinThreads(ts);
        return fillResultArray(gradesMap);
    }

    private Thread[] initialiseAndStartThreads(final String[] grades, int nThreads, final ConcurrentHashMap<String, AtomicInteger> gradesMap) {
        final Thread[] ts = new Thread[nThreads];
        int numberOfGrades = grades.length;
        int remainingGrades = numberOfGrades % nThreads;
        final int gradesPerThread = numberOfGrades / nThreads;
        int startIndex = 0;

        for (int threadNumberIndex = 0; threadNumberIndex < nThreads; threadNumberIndex++) {
            final CounterThread pt;
            if (remainingGrades != 0) {
                pt = new CounterThread(startIndex, startIndex + gradesPerThread + 1, gradesMap, grades);
                remainingGrades--;
                startIndex = startIndex + gradesPerThread + 1;
            } else {
                pt = new CounterThread(startIndex, startIndex + gradesPerThread, gradesMap, grades);
                startIndex = startIndex + gradesPerThread;
            }
            ts[threadNumberIndex] = pt;
            pt.start();
        }
        return ts;
    }

    private void joinThreads(final Thread[] ts) {
        for (final Thread t : ts) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private GradeCount[] fillResultArray(final ConcurrentHashMap<String, AtomicInteger> gradesMap) {
        GradeCount[] result;
        result = new GradeCount[gradesMap.size()];
        int mapIteratorIndex = 0;
        for (Map.Entry<String, AtomicInteger> e : gradesMap.entrySet()) {
            result[mapIteratorIndex++] = new GradeCount(e);
        }
        return result;
    }
}

class CounterThread extends Thread {

    private final int startIndex;
    private final int endIndex;
    private final ConcurrentHashMap<String, AtomicInteger> gradesMap;
    private final String[] grades;

    CounterThread(int startIndex, int endIndex, ConcurrentHashMap<String, AtomicInteger> gradesMap, String[] grades) {

        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.gradesMap = gradesMap;
        this.grades = grades;
    }

    @Override
    public void run() {
        for (int i = startIndex; i < endIndex; i++) {
            final String grade = grades[i];
            gradesMap.putIfAbsent(grade, new AtomicInteger(0));
            gradesMap.get(grade).getAndIncrement();
        }
    }
}
