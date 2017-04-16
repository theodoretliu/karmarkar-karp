import java.util.Random;

class KarmarkarKarp {
    private final static int MAX_ITERS = 25000;

    static long pure(long[] a) {
        Heap h = new Heap(a);
        long ret = Long.MIN_VALUE;
        while (!h.isEmpty()) {
            long first = h.extractMax();
            long second = h.extractMax();

            if (second == Long.MIN_VALUE) {
                ret = first;
                break;
            }

            h.insert(first - second);
        }

        return ret;
    }

    private static long residue(int[] s, long[] a, boolean pp) {
        if (!pp) {
            long sum = 0;

            for (int i = 0; i < s.length; i++)
                sum += s[i] * a[i];

            return Math.abs(sum);
        } else {
            long[] aPrime = new long[a.length];

            for (int i = 0; i < s.length; i++) {
                long temp = 0;

                for (int j = 0; j < s.length; j++) {
                    if (s[j] == i)
                        temp += a[j];
                }

                aPrime[i] = temp;
            }

            return pure(aPrime);
        }
    }

    private static int[] randomSolution(int n, boolean pp) {
        Random r = new Random();

        int[] s = new int[n];

        if (!pp) {
            for (int i = 0; i < s.length; i++) {
                if (r.nextFloat() < 0.5) {
                    s[i] = -1;
                } else {
                    s[i] = 1;
                }
            }

        } else {
            for (int i = 0; i < s.length; i++)
                s[i] = r.nextInt(s.length);
        }

        return s;
    }

    private static int[] randomNeighbor(int[] s, boolean pp) {
        Random r = new Random();

        int[] sPrime = new int[s.length];

        System.arraycopy(s, 0, sPrime, 0, s.length);
        int i = r.nextInt(s.length);
        int j;

        if (!pp) {
            do {
                j = r.nextInt(s.length);
            } while (i == j);

            sPrime[i] = -sPrime[i];
            if (r.nextFloat() < 0.5)
                sPrime[j] = -sPrime[j];
        } else {
            do {
                j = r.nextInt(s.length);
            } while (sPrime[i] == j);

            sPrime[i] = j;
        }

        return sPrime;
    }

    static long random(long[] a, boolean pp) {
        int n = a.length;

        int[] s = randomSolution(n, pp);

        for (int j = 0; j < MAX_ITERS; j++) {
            int[] sPrime = randomSolution(n, pp);

            if (residue(sPrime, a, pp) < residue(s, a, pp))
                s = sPrime;
        }

        return residue(s, a, pp);
    }

    static long hill(long[] a, boolean pp) {
        int n = a.length;
        int[] s = randomSolution(n, pp);

        for (int k = 0; k < MAX_ITERS; k++) {
            int[] sPrime = randomNeighbor(s, pp);

            if (residue(sPrime, a, pp) < residue(s, a, pp))
                s = sPrime;
        }

        return residue(s, a, pp);
    }

    private static double temperature(int iter) {
        return 10000000000L * Math.pow(0.8, iter / 300);
    }

    static long annealing(long[] a, boolean pp) {
        Random r = new Random();
        int n = a.length;
        int[] s = randomSolution(n, pp);
        int[] best = s;
        for (int i = 0; i < MAX_ITERS; i++) {
            int[] sPrime = randomNeighbor(s, pp);

            if (residue(sPrime, a, pp) < residue(s, a, pp))
                s = sPrime;
            else {
                if (r.nextFloat() < Math.exp(-(residue(sPrime, a, pp) - residue(s, a, pp))
                        / temperature(i)))
                    s = sPrime;
            }

            if (residue(s, a, pp) < residue(best, a, pp))
                best = s;
        }

        return residue(best, a, pp);
    }
}