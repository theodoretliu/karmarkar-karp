import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class Driver {
    private static long[] testFunction(Callable<Long> func) throws Exception {
        long time1;
        long time2;
        time1 = System.nanoTime();
        long result = func.call();
        time2 = System.nanoTime();

        return new long[] {time2 - time1, result};
    }

    private static void printResults(String name, long residue, double time) {
        System.out.format("%15s %15d %15f\n", name, residue, time);
    }

    private static long[] add(long[] a, long[] b) throws Exception{
        if (a.length != b.length)
            throw new Exception();

        long[] c = new long[a.length];

        for (int i = 0; i < a.length; i++)
            c[i] = a[i] + b[i];

        return c;
    }

    private static long sum(long[] a) {
        long sum = 0;

        for (long i : a)
            sum += i;

        return sum;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 2 && Integer.parseInt(args[0]) == 1) {
            int trials = 100;
            long[] bbResults = new long[2];
            for (int i = 0; i < trials; i++) {
                final long[] test = new long[100];

                for (int j = 0; j < 100; j++) {
                    test[j] = ThreadLocalRandom.current().nextLong(1, 1000000000001L);
                }

                bbResults = add(bbResults, testFunction(new Callable<Long>() {
                    public Long call() {
                        long best = Long.MAX_VALUE;

                        for (int o = 0; o < 100; o++) {
                            long result = KarmarkarKarp.bubbleSearch(test);

                            if (result < best)
                                best = result;
                        }

                        return best;
                    }
                }));
            }

            System.out.format("%15s %15f\n", "Avg. time (ms)", (float) bbResults[0] / trials / 1000000);
            System.out.format("%15s %15d\n", "Avg. residue", bbResults[1] / trials);
            return;
        }
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

        int trials = 100;

        long[][] pure = new long[2][trials];
        long[][] random = new long[2][trials];
        long[][] hill = new long[2][trials];
        long[][] annealing = new long[2][trials];
        long[][] ppRandom = new long[2][trials];
        long[][] ppHill = new long[2][trials];
        long[][] ppAnnealing = new long[2][trials];


        for (int i = 0; i < trials; i++) {
            final long[] test = new long[100];

            for (int j = 0; j < 100; j++) {
                test[j] = ThreadLocalRandom.current().nextLong(1, 1000000000001L);
            }

            long[] temp = testFunction(new Callable<Long>() {
                public Long call() {
                    return KarmarkarKarp.pure(test);
                }
            });

            pure[0][i] = temp[0];
            pure[1][i] = temp[1];

            temp = testFunction(new Callable<Long>() {
                public Long call() {
                    return KarmarkarKarp.random(test, false);
                }
            });

            random[0][i] = temp[0];
            random[1][i] = temp[1];

            temp = testFunction(new Callable<Long>() {
                public Long call() {
                    return KarmarkarKarp.hill(test, false);
                }
            });

            hill[0][i] = temp[0];
            hill[1][i] = temp[1];

            temp = testFunction(new Callable<Long>() {
                public Long call() {
                    return KarmarkarKarp.annealing(test, false);
                }
            });

            annealing[0][i] = temp[0];
            annealing[1][i] = temp[1];

            temp = testFunction(new Callable<Long>() {
                public Long call() {
                    return KarmarkarKarp.random(test, true);
                }
            });

            ppRandom[0][i] = temp[0];
            ppRandom[1][i] = temp[1];

            temp = testFunction(new Callable<Long>() {
                public Long call() {
                    return KarmarkarKarp.hill(test, true);
                }
            });

            ppHill[0][i] = temp[0];
            ppHill[1][i] = temp[1];

            temp = testFunction(new Callable<Long>() {
                public Long call() {
                    return KarmarkarKarp.annealing(test, true);
                }
            });

            ppAnnealing[0][i] = temp[0];
            ppAnnealing[1][i] = temp[1];
        }

        for (int i = 0; i < trials; i++)
            System.out.println(pure[1][i]);
        for (int i = 0; i < trials; i++)
            System.out.println(random[1][i]);
        for (int i = 0; i < trials; i++)
            System.out.println(hill[1][i]);
        for (int i = 0; i < trials; i++)
            System.out.println(annealing[1][i]);
        for (int i = 0; i < trials; i++)
            System.out.println(ppRandom[1][i]);
        for (int i = 0; i < trials; i++)
            System.out.println(ppHill[1][i]);
        for (int i = 0; i < trials; i++)
            System.out.println(ppAnnealing[1][i]);

        System.out.format("%15s %15s %15s\n\n", "Strategy", "Avg. residue", "Avg. time (ms)");
        printResults("Pure", sum(pure[1]) / trials,
                (double) sum(pure[0]) / trials / 1000000);
        printResults("Random", sum(random[1]) / trials,
                (double) sum(random[0]) / trials / 1000000);
        printResults("Hill", sum(hill[1]) / trials,
                (double) sum(hill[0]) / trials / 1000000);
        printResults("Annealing", sum(annealing[1]) / trials,
                (double) sum(annealing[0]) / trials / 1000000);
        printResults("PP-Random", sum(ppRandom[1]) / trials,
                (double) sum(ppRandom[0]) / trials / 1000000);
        printResults("PP-Hill", sum(ppHill[1]) / trials,
                (double) sum(ppHill[0]) / trials / 1000000);
        printResults("PP-Annealing", sum(ppAnnealing[1]) / trials,
                (double) sum(ppAnnealing[0]) / trials / 1000000);
    }
}
