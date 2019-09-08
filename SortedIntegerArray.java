package A2Q1;

import java.util.*;

/**
 * Represents a sorted integer array. Provides a method, kpairSum, that
 * determines whether the array contains two elements that sum to a given
 * integer k. Runs in O(n) time, where n is the length of the array.
 * 
 * @author jameselder
 */
public class SortedIntegerArray {

	protected int[] sortedIntegerArray;

	public SortedIntegerArray(int[] integerArray) {
		sortedIntegerArray = integerArray.clone();
		Arrays.sort(sortedIntegerArray);
	}

	/**
	 * Determines whether the array contains two elements that sum to a given
	 * integer k. Runs in O(n) time, where n is the length of the array.
	 * 
	 * @author jameselder
	 */
	public boolean kPairSum(Integer k) {
		Long kLong = Long.valueOf(k); // Convert k to long.
		int start = 0;
		int end = sortedIntegerArray.length - 1;
		return kPairSumInterval(kLong, start, end); // Call helper method with the variables initialized above.
	}

	private boolean kPairSumInterval(Long k, int i, int j) {
		if (i >= j) { // Base case.
			return false;
		}
		long sum = (long) sortedIntegerArray[i] + (long) sortedIntegerArray[j]; // Sum as a type of long.
		if (sum < k) { // If sum is less than k increase left pointer index by 1.
			return kPairSumInterval(k, i + 1, j);
		} else if (sum > k) { // If sum is greater than k decrease right pointer index by 1.
			return kPairSumInterval(k, i, j - 1);
		} else { // If sum is equal to k then return true;
			return true;
		}
	}
}
