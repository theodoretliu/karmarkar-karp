import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.LongSupplier;

public class Driver {
    private static long[] testFunction(LongSupplier func) {
        long time1;
        long time2;
        time1 = System.nanoTime();
        long result = func.getAsLong();
        time2 = System.nanoTime();

        return new long[] {time2 - time1, result};
    }

    private static void printResults(long[] results, String name, int trials) {
        System.out.format("%15s %15d %15f\n", name,
                results[1] / trials, (float) results[0] / trials / 1000000);
    }

    private static long[] add(long[] a, long[] b) throws Exception{
        if (a.length != b.length)
            throw new Exception();

        long[] c = new long[a.length];

        for (int i = 0; i < a.length; i++)
            c[i] = a[i] + b[i];

        return c;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            FileReader file = null;
            try {
                file = new FileReader(args[0]);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }

            if (file == null)
                return;

            BufferedReader in = new BufferedReader(file);

            long[] test = new long[100];
            for (int i = 0; i < 100; i++) {
                String temp = "";
                try {
                    temp = in.readLine();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                test[i] = Long.parseLong(temp);
            }

            System.out.println(KarmarkarKarp.pure(test));
            return;
        }

        // adopt the convention where the 0th index is the timing and the 1st index is the
        // results

        long[] pure = new long[2];
        long[] random = new long[2];
        long[] hill = new long[2];
        long[] annealing = new long[2];
        long[] ppRandom = new long[2];
        long[] ppHill = new long[2];
        long[] ppAnnealing = new long[2];

        int trials = 100;
        for (int i = 0; i < trials; i++) {
            long[] test = new long[100];

            for (int j = 0; j < 100; j++) {
                test[j] = ThreadLocalRandom.current().nextLong(1, 1000000000001L);
            }

            pure = add(pure, testFunction(() -> KarmarkarKarp.pure(test)));
            random = add(random, testFunction(() -> KarmarkarKarp.random(test, false)));
            hill = add(hill, testFunction(() -> KarmarkarKarp.hill(test, false)));
            annealing = add(annealing, testFunction(() -> KarmarkarKarp.annealing(test, false)));
            ppRandom = add(ppRandom, testFunction(() -> KarmarkarKarp.random(test, true)));
            ppHill = add(ppHill, testFunction(() -> KarmarkarKarp.hill(test, true)));
            ppAnnealing = add(ppAnnealing, testFunction(() -> KarmarkarKarp.annealing(test, true)));
        }

        System.out.format("%15s %15s %15s\n\n", "Strategy", "Avg. residue", "Avg. time (ms)");
        printResults(pure, "Pure", trials);
        printResults(random, "Random", trials);
        printResults(hill, "Hill", trials);
        printResults(annealing, "Annealing", trials);
        printResults(ppRandom, "PP-Random", trials);
        printResults(ppHill, "PP-Hill", trials);
        printResults(ppAnnealing, "PP-Annealing", trials);
    }
}
