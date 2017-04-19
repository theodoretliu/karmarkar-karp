import java.util.ArrayList;
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

        // int[] s = randomSolution(n, pp);
        long sResidue = residue(randomSolution(n, pp), a, pp);

        for (int j = 0; j < MAX_ITERS; j++) {
            int[] sPrime = randomSolution(n, pp);
            long sPrimeResidue = residue(sPrime, a, pp);

            if (sPrimeResidue < sResidue) {
                // s = sPrime;
                sResidue = sPrimeResidue;
            }
        }

        return sResidue;
    }

    static long hill(long[] a, boolean pp) {
        int n = a.length;
        int[] s = randomSolution(n, pp);
        long sResidue = residue(s, a, pp);

        for (int k = 0; k < MAX_ITERS; k++) {
            int[] sPrime = randomNeighbor(s, pp);
            long sPrimeResidue = residue(sPrime, a, pp);

            if (sPrimeResidue < sResidue) {
                s = sPrime;
                sResidue = sPrimeResidue;
            }
        }

        return sResidue;
    }

    private static double temperature(int iter) {
        return 10000000000L * Math.pow(0.8, iter / 300);
    }

    private static double t(int iter) {
        return 10000000000L * Math.exp(iter / 3106.674673);
    }

    static long annealing(long[] a, boolean pp) {
        Random r = new Random();
        int n = a.length;
        int[] s = randomSolution(n, pp);
        // int[] best = s;

        long sResidue = residue(s, a, pp);
        long bestResidue = sResidue;
        for (int i = 0; i < MAX_ITERS; i++) {
            int[] sPrime = randomNeighbor(s, pp);

            long sPrimeResidue = residue(sPrime, a, pp);

            if (sPrimeResidue < sResidue) {
                s = sPrime;
                sResidue = sPrimeResidue;
            } else {
                if (r.nextFloat() < Math.exp(-(sPrimeResidue - sResidue) / temperature(i))) {
                    s = sPrime;
                    sResidue = sPrimeResidue;
                }
            }

            if (sResidue < bestResidue) {
                // best = s;
                bestResidue = sResidue;
            }
        }

        return bestResidue;
    }

    static long bubbleSearch(long[] a) {
        Heap h = new Heap(a);
        Random r = new Random();

        while (h.size() > 10) {
            int size = h.size();

            int i = 0;

            while (r.nextFloat() > 0.5)
                i += 1;

            int j = i + 1;

            while (r.nextFloat() > 0.5)
                j += 1;

            i %= size;
            j %= size;

            if (i == j)
                j = (j + 1) % size;

            ArrayList<Long> temp = new ArrayList<>();
            for (int x = 0; x <= Math.max(i, j); x++)
                temp.add(h.extractMax());

            long first = temp.get(i);
            long second = temp.get(j);

            temp.remove(Math.max(i, j));
            temp.remove(Math.min(i, j));

            h.insert(Math.abs(first - second));
            for (long x : temp)
                h.insert(x);
        }

        int size = h.size();
        long bestResidue = Long.MAX_VALUE;
        int limit = (int) Math.pow(2, size);
        long[] values = new long[size];

        for (int i = 0; i < values.length; i++) {
            values[i] = h.extractMax();
        }

        for (int i = 0; i < limit; i++) {
            int t = i;
            int[] s = new int[size];
            for (int j = 0; j < size; j++) {
                if (t % 2 == 1)
                    s[j] = 1;
                else
                    s[j] = -1;

                t >>= 1;
            }

            long residue = residue(s, values, false);

            if (residue < bestResidue)
                bestResidue = residue;
        }

        return bestResidue;
    }
}