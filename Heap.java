import java.util.ArrayList;

public class Heap {
    private ArrayList<Long> heap;

    public Heap() {
        heap = new ArrayList<>();
    }

    Heap(long[] initial) {
        heap = new ArrayList<>();

        for (long i : initial) {
            heap.add(i);
        }

        buildHeap(heap);
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int left(int i) {
        return 2 * i + 1;
    }

    private int right(int i) {
        return 2 * i + 2;
    }

    private boolean exists(int i) {
        return i < heap.size() && i >= 0;
    }

    private void maxHeapify(ArrayList<Long> h, int n) {
        int l = left(n);
        int r = right(n);

        int largest;
        if (exists(l) && h.get(l) > h.get(n)) {
            largest = l;
        } else {
            largest = n;
        }

        if (exists(r) && h.get(r) > h.get(largest)) {
            largest = r;
        }

        if (largest != n) {
            long temp = h.get(n);
            h.set(n, h.get(largest));
            h.set(largest, temp);

            maxHeapify(h, largest);
        }
    }

    private void buildHeap(ArrayList<Long> a) {
        for (int i = a.size() / 2 + 1; i >= 0; i--)
            maxHeapify(a, i);
    }

    boolean isEmpty() {
        return heap.isEmpty();
    }

    public long peek() {
        if (!isEmpty())
            return heap.get(0);

        return Long.MIN_VALUE;
    }

    long extractMax() {
        if (heap.isEmpty())
            return Long.MIN_VALUE;

        long max = heap.get(0);

        heap.set(0, heap.get(heap.size() - 1));

        heap.remove(heap.size() - 1);

        maxHeapify(heap, 0);

        return max;
    }

    void insert(long i) {
        heap.add(i);

        int n = heap.size() - 1;

        while (n != 0 && heap.get(parent(n)) < heap.get(n)) {
            long temp = heap.get(parent(n));
            heap.set(parent(n), heap.get(n));
            heap.set(n, temp);
            n = parent(n);
        }
    }

    int size() {
        return heap.size();
    }
}
