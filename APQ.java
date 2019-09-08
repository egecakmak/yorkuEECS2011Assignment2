package A2Q2;

import java.util.*;

/**
 * Adaptible priority queue using location-aware entries in a min-heap, based on
 * an extendable array. The order in which equal entries were added is
 * preserved.
 *
 * @author jameselder
 * @param <E>
 *            The entry type.
 */
public class APQ<E> {

	private final ArrayList<E> apq; // will store the min heap
	private final Comparator<E> comparator; // to compare the entries
	private final Locator<E> locator; // to locate the entries within the queue

	/**
	 * Constructor
	 * 
	 * @param comparator
	 *            used to compare the entries
	 * @param locator
	 *            used to locate the entries in the queue
	 * @throws NullPointerException
	 *             if comparator or locator parameters are null
	 */
	public APQ(Comparator<E> comparator, Locator<E> locator) throws NullPointerException {
		if (comparator == null || locator == null) {
			throw new NullPointerException();
		}
		apq = new ArrayList<>();
		apq.add(null); // dummy value at index = 0
		this.comparator = comparator;
		this.locator = locator;
	}

	/**
	 * Inserts the specified entry into this priority queue.
	 *
	 * @param e
	 *            the entry to insert
	 * @throws NullPointerException
	 *             if parameter e is null
	 */
	public void offer(E e) throws NullPointerException {
		if (e == null) {
			throw new NullPointerException();
		}
		apq.add(e); // Add the element to the end of the heap.
 		locator.set(e, size()); // Set the element's location property to make sure the element is aware of it's position.
		upheap(size()); // Upheap the last element if necessary to preserve the heap property.
	}

	/**
	 * Removes the entry at the specified location.
	 *
	 * @param pos
	 *            the location of the entry to remove
	 * @throws BoundaryViolationException
	 *             if pos is out of range
	 */
	public void remove(int pos) throws BoundaryViolationException {
		if (pos <= 0 || pos > size()) {
			throw new BoundaryViolationException();
		}
		swap(pos, size()); // Swap the last element and the element at given position.
		apq.remove(size()); // Remove the last element of ArrayList. We are deleting the element we want to delete here.
		downheap(pos); // Downheap the element at given position to preserve the heap property.
	}

	/**
	 * Removes the first entry in the priority queue.
	 */
	public E poll() {
		if (isEmpty()) {
			return null;
		} else {
			int lastElementsIndex = size();
			int firstElementsIndex = 1;
			E firstElement = apq.get(firstElementsIndex);
			swap(firstElementsIndex, lastElementsIndex);
			apq.remove(lastElementsIndex); // Remove the last element of the ArrayList.
			downheap(firstElementsIndex); // Downheap the element at first position the preserve heap property.
			return firstElement;
		}
	}

	/**
	 * Returns but does not remove the first entry in the priority queue.
	 */
	public E peek() {
		if (isEmpty()) {
			return null;
		}
		return apq.get(1);
	}

	public boolean isEmpty() {
		return (size() == 0);
	}

	public int size() {
		return apq.size() - 1; // dummy node at location 0
	}

	/**
	 * Shift the entry at pos upward in the heap to restore the minheap property
	 * 
	 * @param pos
	 *            the location of the entry to move
	 */
	private void upheap(int pos) {
		while (pos > 1) {
			int parentsIndex = parent(pos);
			E currentElement = apq.get(pos);
			E parentElement = apq.get(parentsIndex);
			if (comparator.compare(currentElement, parentElement) <= -1) { // If current element is less than it's parent.
				swap(pos, parentsIndex);
				pos = parentsIndex; 
			} else {
				break; // Stop upheaping process.
			}
		}
	}

	/**
	 * Shift the entry at pos downward in the heap to restore the minheap property
	 * 
	 * @param pos
	 *            the location of the entry to move
	 */
	private void downheap(int pos) {
		while (hasLeft(pos)) {
			E currentElement = apq.get(pos);
			int elementToMoveTowardsIndex = leftChild(pos);
			if (hasRight(pos)) {
				E leftElement = apq.get(leftChild(pos));
				E rightElement = apq.get(rightChild(pos));
				if (comparator.compare(leftElement, rightElement) <= 0) { // If left element is less than equal to the right element.
					elementToMoveTowardsIndex = leftChild(pos); // Unnecessary to do this again, it is only here to clarify the process.
				} else {
					elementToMoveTowardsIndex = rightChild(pos);
				}
			}
			E elementToMoveTowards = apq.get(elementToMoveTowardsIndex);
			if (comparator.compare(currentElement, elementToMoveTowards) >= 1) { // If the currentElement is greater than the element at the direction we are heaping towards.
				swap(pos, elementToMoveTowardsIndex);
				pos = elementToMoveTowardsIndex;
			} else {
				break;
			}

		}
	}

	/**
	 * Swap the entries at the specified locations.
	 *
	 * @param pos1
	 *            the location of the first entry
	 * @param pos2
	 *            the location of the second entry
	 */
	private void swap(int pos1, int pos2) {
		E element1 = apq.get(pos1);
		E element2 = apq.get(pos2);
		locator.set(element1, pos2); // Set the location property of element.
		locator.set(element2, pos1); // Set the location property of element.
		apq.set(pos1, element2); // Swap elements.
		apq.set(pos2, element1); // Swap elements.
	}

	/**
	 * Calculate the position of left child of a given element at index i.
	 * 
	 * @param i
	 *            the position of given element
	 * @return index of left child of the given element
	 */
	private int leftChild(int i) {
		return (2 * i);
	}

	/**
	 * Calculate the position of right child of a given element at index i.
	 * 
	 * @param i
	 *            the position of given element
	 * @return index of right child of the given element
	 */
	private int rightChild(int i) {
		return (2 * i) + 1;
	}

	/**
	 * Calculate the position of parent of a given element at index i.
	 * 
	 * @param i
	 *            the position of given element
	 * @return index of parent of the given element
	 */
	private int parent(int i) {
		return i / 2;
	}

	/**
	 * Check if a given element has a left element
	 * 
	 * @param i
	 *            the position of given element
	 * @return either true or false depending on if the element has a left child
	 */
	private boolean hasLeft(int i) {
		int leftChild = leftChild(i);
		return leftChild <= size();
	}

	/**
	 * Check if a given element has a right element
	 * 
	 * @param i
	 *            the position of given element
	 * @return either true or false depending on if the element has a right child
	 */
	private boolean hasRight(int i) {
		int rightChild = rightChild(i);
		return rightChild <= size();
	}

}