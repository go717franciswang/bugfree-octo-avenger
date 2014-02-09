import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int N;
    private Item[] a;
    private int sampleId;
    
    public RandomizedQueue() {
        N = 0;
        a = (Item[]) new Object[2];
        sampleId = -1;
    }
    
    public boolean isEmpty() {
        return size() == 0;
    }
    
    public int size() {
        return N;
    }
    
    public void enqueue(Item item) {
        if (a.length == N) {
            resize(N*2);
        }
        
        a[N++] = item;
    }
    
    private void resize(int capacity) {
        assert capacity >= N;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }
    
    public Item dequeue() {
        // sample a random item, pop it, place the last item there, unsample the item
        Item item = sample();
        N--;
        
        if (!isEmpty()) {
            a[sampleId] = a[N];
            a[N] = null;
            
            if (N == a.length / 4) {
                resize(a.length / 2);
            }
        } else {
            a[sampleId] = null;
        }
        
        StdOut.println("sampleId: " + sampleId + ", N: " + N);
        
        sampleId = -1;
        
        return item;
    }
    
    public Item sample() {
        if (N == 0) {
            throw new java.util.NoSuchElementException();
        }
        
        if (sampleId == -1) {
            sampleId = StdRandom.uniform(0, N);
        }
        
        return a[sampleId];
    }
    
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }
    
    private class RandomIterator implements Iterator<Item> {
        // looks like iterator needs to preserve its own order, so int[N] is needed
        private int[] order;
        private int i;

        public RandomIterator() {
            i = 0;
            order = new int[N];
            
            for (int j = 0; j < N; j++) {
                order[j] = j;
            }
        }

        public boolean hasNext() {
            return i < N;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            
            int randomItemId = StdRandom.uniform(i, N);
            int tmp = order[randomItemId];
            order[randomItemId] = order[i];
            order[i] = tmp;
            
            return a[order[i++]];
        }
    }
    
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) q.enqueue(item);
            else if (!q.isEmpty()) StdOut.print(q.dequeue() + " ");
        }
        StdOut.println("(" + q.size() + " left on queue)");
    }
}