// Starter code for LP5

// Change to your netid
package rbk;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class BinaryHeap<T extends Comparable<? super T>> {
    Comparable[] pq;
    int size;
    Comparable temp;

    // Constructor for building an empty priority queue using natural ordering of T
    public BinaryHeap(int maxCapacity) {
	pq = new Comparable[maxCapacity];
	size = 0;
    }

    // add method: resize pq if needed
    public boolean add(T x) {
    	if (size == pq.length) {
			return false;
		} else {
			pq[size] = x; // Adding to the leaf
			percolateUp(size); // Moving to the appropriate place
			size++;
			return true;
		}
    }

    public boolean offer(T x) {
	return add(x);
    }

    // throw exception if pq is empty
    public T remove() throws NoSuchElementException {
	T result = poll();
	if(result == null) {
	    throw new NoSuchElementException("Priority queue is empty");
	} else {
	    return result;
	}
    }

    // return null if pq is empty
    public T poll() {
    	if (size == 0) {
			return null;
		} else {
			temp = pq[0]; // The first element which is to be removed
			pq[0] = pq[size - 1];
			size--;
			percolateDown(0); // Moving newly added element to appropriate place
			return (T) temp;
		}
	
    }
    
    public T min() { 
	return peek();
    }

    // return null if pq is empty
    public T peek() {
    	if (size == 0) {
			return null;
		} else {
			return (T) pq[0];
		}
    }

    int parent(int i) {
	return (i-1)/2;
    }

    int leftChild(int i) {
	return 2*i + 1;
    }

    /** pq[index] may violate heap order with parent */
    void percolateUp(int index) {
    	Comparable x = pq[index];
		//pq[i] may violate heap order with parent
		while (index > 0 && ((pq[parent(index)].compareTo(x)) == 1)) {
			pq[index] = pq[parent(index)];
			index = parent(index);
		}		
		pq[index] = x;
    }

    /** pq[index] may violate heap order with children */
    void percolateDown(int index) {
    	Comparable x = pq[index];
		int c = (2 * index) + 1;
		// pq[i] may violate heap order with children
		while (c <= size - 1) {
			if (c < size - 1 && pq[c].compareTo(pq[c + 1]) == 1)
				c = c + 1;
			if (x.compareTo(pq[c]) <= 0)
				break;
			pq[index] = pq[c];
			index = c;
			c = 2 * index + 1;

		}
		pq[index] = x;
    }

    void move(int dest, Comparable x) {
	pq[dest] = x;
    }

    int compare(Comparable a, Comparable b) {
	return ((T) a).compareTo((T) b);
    }
    
    /** Create a heap.  Precondition: none. */
    void buildHeap() {
	for(int i=parent(size-1); i>=0; i--) {
	    percolateDown(i);
	}
    }

    public boolean isEmpty() {
	return size() == 0;
    }

    public int size() {
	return size;
    }

    // Resize array to double the current size
    void resize() {
    	 int newMaxCapacity =  pq.length * 2;		//not done need to change the existing array size
    }
    
    public interface Index {
        public void putIndex(int index);
        public int getIndex();
    }

    public static class IndexedHeap<T extends Index & Comparable<? super T>> extends BinaryHeap<T> {
        /** Build a priority queue with a given array */
        IndexedHeap(int capacity) {
            super(capacity);
	}

        /** restore heap order property after the priority of x has decreased */
        void decreaseKey(T x) {
        	percolateUp(x.getIndex());
        }

	@Override
        void move(int i, Comparable x) {
            super.move(i, x);
        }
    }

    public static void main(String[] args) {
//	Integer[] arr = {0,9,7,5,3,1,8,6,4,2};
    Integer[] arr = {45,9,7,5,37,14,8,6,4,2};

    BinaryHeap<Integer> h = new BinaryHeap(arr.length);

	System.out.print("Before:");
	for(Integer x: arr) {
	    h.offer(x);
	    System.out.print(" " + x);
	}
	System.out.println();

	for(int i=0; i<arr.length; i++) {
	    arr[i] = h.poll();
	}

	System.out.print("After :");
	for(Integer x: arr) {
	    System.out.print(" " + x);
	}
	System.out.println();
    }
}
