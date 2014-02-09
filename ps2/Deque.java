import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private int N;
    private Node<Item> first;
    private Node<Item> last;
    
    public Deque() {
        N = 0;
        first = null;
        last = null;
    }
    
    private static class Node<Item> {
        Item item;
        Node<Item> previous;
        Node<Item> next;
    }
    
    public boolean isEmpty() {
        return size() == 0;
    }
    
    public int size() {
        return N;
    }
    
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        
        Node<Item> node = new Node<Item>();
        node.item = item;
        node.previous = null;
        node.next = first;
        
        if (first != null) {
            first.previous = node;
        }
        
        first = node;
        N++;
    }
    
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        
        Node<Item> node = new Node<Item>();
        node.item = item;
        node.previous = last;
        node.next = null;
        
        if (last != null) {
            last.next = node;
        }
        
        last = node;
        N++;
    }
    
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        
        Item item = first.item;
        if (first.next != null) {
            first.next.previous = null;
        }
        first = first.next;
        N--;
        
        return item;
    }
    
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        
        Item item = last.item;
        if (last.previous != null) {
            last.previous.next = null;
        }
        last = last.previous;
        N--;
        
        return item;
    }
    
    public Iterator<Item> iterator() {
        return new ListIterator<Item>(first);
    }
    
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }
    
    public static void main(String[] args) {
        Deque<String> d = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (item.equals("-")) StdOut.print(d.removeFirst() + " ");
            else if (item.equals("+")) StdOut.print(d.removeLast() + " ");
            else if (item.compareTo("n") < 0) d.addFirst(item);
            else d.addLast(item);
        }
        StdOut.println("(" + d.size() + " left on deque)");
    }
}