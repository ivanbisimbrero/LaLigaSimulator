/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 *
 * @author Antonio Gabarrús Nerin (alu142920)
 * @description Non-static sorting class that includes different sorting methods
 * and stores stats of last and total executions
 * @param <T> any class T that extends from Comparable&ltT&gt, that means that 
 * different instances of T must be able to be compared in order to sort them
 */
public class SortingLibrary<T extends Comparable<T>> {

    private long totalExecutionTime;
    private long lastExecutionTime;
    
    private long totalComparisons;
    private long lastComparisons;
    
    private long totalArrayAccesses;
    private long lastArrayAccesses;
    
    private String lastExecutedMethod;

    public SortingLibrary() {
        this.totalExecutionTime = 0;
        this.lastExecutionTime = 0;
        this.totalComparisons = 0;
        this.lastComparisons = 0;
        this.totalArrayAccesses = 0;
        this.lastArrayAccesses = 0;
        this.lastExecutedMethod = "NONE";
    }

    @Override
    public String toString() {
        return "SortingLibrary{"
                + "\n\t\"totalExecutionTime\" : " + this.totalExecutionTime
                + "\n\t\"totalComparisons\" : " + this.totalComparisons
                + "\n\t\"totalArrayAccesses\" : " + this.totalArrayAccesses
                + "\n\t\"lastExecutedMethod\" : " + this.lastExecutedMethod
                + "\n\t\"lastExecutionTime\" : " + this.lastExecutionTime
                + "\n\t\"lastComparisons\" : " + this.lastComparisons
                + "\n\t\"lastArrayAccesses\" : " + this.lastArrayAccesses;
    }

    
    
    
    
    /**
     * returns an array with the same elements sorted with the selection method
     *
     * @param array
     * @return sorted array
     */
    public T[] selectionSort(T[] array) {
        System.out.println("Selection Sorting Start");
        System.out.println("param |array|: " + Arrays.deepToString(array));
        
        this.lastExecutedMethod = "SELECTION SORT";
        this.lastArrayAccesses = 0;
        this.lastComparisons = 0;
        this.lastExecutionTime = (-System.currentTimeMillis());
        
        if (array == null || array.length < 1) {
            throw new RuntimeException("Cannot sort an empty array");
        }
        T[] myArray = (T[]) Array.newInstance(array[0].getClass(), array.length);
        System.arraycopy(array, 0, myArray, 0, array.length);
        for (int i = 0; i < myArray.length - 1; ++i) {
            int minPos = i;
            for (int j = i; j < myArray.length; ++j) {
                minPos = (myArray[minPos].compareTo(myArray[j]) > 0) ? j : minPos;
                this.lastComparisons++;
                this.lastArrayAccesses+=2;
            }
            T aux = myArray[minPos];
            myArray[minPos] = myArray[i];
            myArray[i] = aux;
            this.lastArrayAccesses+=3;
            System.out.println(Arrays.deepToString(myArray));
        }
        System.out.println("Selection Sorting End");
        
        this.lastExecutionTime += System.currentTimeMillis();
        
        this.totalArrayAccesses += this.lastArrayAccesses;
        this.totalComparisons += this.lastComparisons;
        this.totalExecutionTime += this.lastExecutionTime;
        
        return myArray;
    }

    
    /**
     * Sort the elements swapping them from pos to 0 
     * @param array
     * @param pos
     * @param gap the gap to know the element to compare
     */
    private void backtrackingSort(T[] array, int pos, int gap) {
        if (pos >= 0 && pos - gap >= 0) {
            T aux = array[pos];
            if (aux.compareTo(array[pos - gap]) < 0) {
                array[pos] = array[pos - gap];
                array[pos - gap] = aux;
                this.lastArrayAccesses+=3;
                System.out.println(Arrays.deepToString(array));
                backtrackingSort(array, pos - gap, gap);
            }
            this.lastArrayAccesses+=2;
            this.lastComparisons++;
        }
    }

    /**
     * Sorts the array with th insertion method
     * @param array
     * @return 
     */
    public T[] insertionSort(T[] array) {
        System.out.println("Insertion Sorting Start");
        System.out.println("param |array|: " + Arrays.deepToString(array));
        this.lastExecutedMethod = "INSERTION SORT";
        this.lastArrayAccesses = 0;
        this.lastComparisons = 0;
        this.lastExecutionTime = (-System.currentTimeMillis());
        if (array == null || array.length < 1) {
            throw new RuntimeException("Cannot sort an empty array");
        }
        T[] myArray = (T[]) Array.newInstance(array[0].getClass(), array.length);
        System.arraycopy(array, 0, myArray, 0, array.length);

        for (int i = 0; i < myArray.length - 1; ++i) {
            if (myArray[i].compareTo(myArray[i + 1]) > 0) {
                T aux = myArray[i];
                myArray[i] = myArray[i + 1];
                myArray[i + 1] = aux;
                this.lastArrayAccesses+=3;
                System.out.println(Arrays.deepToString(myArray));
                backtrackingSort(myArray, i, 1);
            }
            this.lastArrayAccesses+=2;
            this.lastComparisons++;
        }
        System.out.println("Insertion Sorting End");
        this.lastExecutionTime += System.currentTimeMillis();
        
        this.totalArrayAccesses += this.lastArrayAccesses;
        this.totalComparisons += this.lastComparisons;
        this.totalExecutionTime += this.lastExecutionTime;
        return myArray;
    }

    /**
     * Sorts the array with the bubble method
     * @param array
     * @return 
     */
    public T[] bubbleSort(T[] array) {
        System.out.println("Bubble Sorting Start");
        System.out.println("param |array|: " + Arrays.deepToString(array));
        this.lastExecutedMethod = "BUBBLE SORT";
        this.lastArrayAccesses = 0;
        this.lastComparisons = 0;
        this.lastExecutionTime = (-System.currentTimeMillis());
        if (array == null || array.length < 1) {
            throw new RuntimeException("Cannot sort an empty array");
        }
        T[] myArray = (T[]) Array.newInstance(array[0].getClass(), array.length);
        System.arraycopy(array, 0, myArray, 0, array.length);

        boolean hasInterchanged;
        int i,
                j,
                iterationNumber = 0;
        do {
            hasInterchanged = false;
            i = 0;
            j = 1;
            while (j < myArray.length - iterationNumber) {
                if (myArray[i].compareTo(myArray[j]) > 0) {
                    T aux = myArray[i];
                    myArray[i] = myArray[j];
                    myArray[j] = aux;
                    this.lastArrayAccesses+=3;
                    hasInterchanged = true;
                    System.out.println(Arrays.deepToString(myArray));
                }
                this.lastComparisons++;
                this.lastArrayAccesses+=2;
                ++i;
                ++j;
            }
        } while (hasInterchanged);
        System.out.println("Bubble Sorting End");
        
        this.lastExecutionTime += System.currentTimeMillis();
        
        this.totalArrayAccesses += this.lastArrayAccesses;
        this.totalComparisons += this.lastComparisons;
        this.totalExecutionTime += this.lastExecutionTime;
        return myArray;
    }

    /**
     * Sorts the array with the Shell method
     * @param array
     * @return 
     */
    public T[] shellSort(T[] array) {
        System.out.println("Shell Sorting Start");
        System.out.println("param |array|: " + Arrays.deepToString(array));
        this.lastExecutedMethod = "SHELL SORT";
        this.lastArrayAccesses = 0;
        this.lastComparisons = 0;
        this.lastExecutionTime = (-System.currentTimeMillis());
        if (array.length < 1) {
            throw new RuntimeException("Cannot sort an empty array");
        }
        T[] myArray = (T[]) Array.newInstance(array[0].getClass(), array.length);
        System.arraycopy(array, 0, myArray, 0, array.length);
        int gap,
                iterationNum = 0;

        do {
            ++iterationNum;
            gap = myArray.length / (2 * iterationNum);

            for (int i = 0; i + gap < myArray.length; ++i) {
                if (myArray[i].compareTo(myArray[i + gap]) > 0) {
                    T aux = myArray[i];
                    myArray[i] = myArray[i + gap];
                    myArray[i + gap] = aux;
                    this.lastArrayAccesses+=3;
                    System.out.println(Arrays.deepToString(myArray));
                    backtrackingSort(myArray, i, gap);
                }
                this.lastComparisons++;
                this.lastArrayAccesses+=2;
            }
            
        } while (gap != 1);
        System.out.println("Shell Sorting End");
        this.lastExecutionTime += System.currentTimeMillis();
        
        this.totalArrayAccesses += this.lastArrayAccesses;
        this.totalComparisons += this.lastComparisons;
        this.totalExecutionTime += this.lastExecutionTime;
        return myArray;
    }
    
    public T[] heapSort(T[] array){
        System.out.println("Heap Sorting Start");
        System.out.println("param |array|: " + Arrays.deepToString(array));
        this.lastExecutedMethod = "HEAP SORT";
        this.lastArrayAccesses = 0;
        this.lastComparisons = 0;
        this.lastExecutionTime = (-System.currentTimeMillis());
        if (array.length < 1) {
            throw new RuntimeException("Cannot sort an empty array");
        }
        T[] myArray = (T[]) Array.newInstance(array[0].getClass(), array.length);
        System.arraycopy(array, 0, myArray, 0, array.length);
        //TODO: Implement HeapSort
        return myArray;
    }
    
    private T[] recursiveMergeSort(T[] array){
        if(array.length == 1) return array; //If only one element on the array, the the array is ordered
        T[] myArray = (T[]) Array.newInstance(array[0].getClass(), array.length);
        
        
        //The arrays are divided into 2
        T[] left = (T[]) Array.newInstance(array[0].getClass(), array.length/2);
        System.arraycopy(array, 0, left, 0, left.length);
        T[] right = ((array.length%2 == 0))? 
                (T[]) Array.newInstance(array[0].getClass(), array.length/2):
                (T[]) Array.newInstance(array[0].getClass(), (array.length/2) +1);
        System.arraycopy(array, left.length, right, 0, right.length);
        
        //Now we order both arrays with mergesort and we merge both
        
        left = recursiveMergeSort(left);
        right = recursiveMergeSort(right);
        
        int i = 0, //Controls access to left
            j = 0, //Controls access to right
            k = 0; //Controls access to myArray
        
        while (k < myArray.length){
            if(i>= left.length) 
                myArray[k++] = right[j++];//If left is completely merged into the final array, then we can merge the rest of right
            else if(j>= right.length)
                myArray[k++] = left[i++];//If right is completely merged into the final array, then we can merge the rest of left
            else if(right[j].compareTo(left[i])<0)
                myArray[k++] = right[j++];
            else
                myArray[k++] = left[i++];
            
        }
        return myArray;
    }
    
    public T[] mergeSort(T[] array){
        System.out.println("Merge Sorting Start");
        System.out.println("param |array|: " + Arrays.deepToString(array));
        this.lastExecutedMethod = "MERGE SORT";
        this.lastArrayAccesses = 0;
        this.lastComparisons = 0;
        this.lastExecutionTime = (-System.currentTimeMillis());
        if (array.length < 1) {
            throw new RuntimeException("Cannot sort an empty array");
        }
        T[] myArray = recursiveMergeSort(array);
        
        System.out.println("Merge Sorting End");
        this.lastExecutionTime += System.currentTimeMillis();
        
        this.totalArrayAccesses += this.lastArrayAccesses;
        this.totalComparisons += this.lastComparisons;
        this.totalExecutionTime += this.lastExecutionTime;
        
        return myArray;
    }

    private T[] recursiveQuickSortLR(T[] array){
        if (array.length == 1) return array;
        T[] myArray = (T[]) Array.newInstance(array[0].getClass(), array.length);
        System.arraycopy(array, 0, myArray, 0, array.length);
        //Due to optimizations for array access, the pivot is going to be both the index in the array and the value so we can compare the values without accessing the array
        
        int pivot = myArray.length/2,
            left = 0,
            right = myArray.length -1;
        
        T pivotValue = myArray[pivot];
        
        while (left <= right){
            if (myArray[left].compareTo(pivotValue) < 0) ++left; // If left is lower than pivot, the position respect to pivot is correct
            else if(myArray[right].compareTo(pivotValue) > 0) --right;//If right is greater or equal to pivot, the position respect to pivot is correct 
            else{ // none left nor right are correct respect to pivot so we interchange them
                T aux = myArray[left];
                myArray[left] = myArray[right];
                myArray[right] = aux;
                
                //TODO: move the cursors right and left and, if necessary, pivot
                
                pivot = (left==pivot) ? right : (right == pivot) ? left : pivot ;
                ++left;
                --right;
            }
        }
        //Pivot is correct. Now we apply quickSort to both sides of the pivot
        
        //TODO: Create the sub arrays correctly
        if(right >= 0){//There is something on the left to order
            T[] leftArray = (T[]) Array.newInstance(myArray[0].getClass(), right+1);
            System.arraycopy(myArray, 0, leftArray, 0, leftArray.length);
            leftArray = recursiveQuickSortLR(leftArray);
            for (int i = 0; i < leftArray.length; ++i){
                myArray[i] = leftArray[i];
            }
        }
        
        if(left<myArray.length){
            T[] rightArray = (T[]) Array.newInstance(myArray[0].getClass(), (myArray.length - left));
            System.arraycopy(myArray, left, rightArray, 0, rightArray.length);
            rightArray = recursiveQuickSortLR(rightArray);
            for (int i = 0; i < rightArray.length; ++i){
                myArray[left+i] = rightArray[i];
            }
        }
        
        
        //We put the values of the sub quicksorts in the array we return
        
        
        return myArray;
    }
    
    public T[] quickSortLR(T[] array){
        System.out.println("Quick Sorting LR Start");
        System.out.println("param |array|: " + Arrays.deepToString(array));
        this.lastExecutedMethod = "QUICK SORT LR";
        this.lastArrayAccesses = 0;
        this.lastComparisons = 0;
        this.lastExecutionTime = (-System.currentTimeMillis());
        
        
        T[] myArray = recursiveQuickSortLR(array);
        
        
        this.lastExecutionTime += System.currentTimeMillis();
        
        this.totalArrayAccesses += this.lastArrayAccesses;
        this.totalComparisons += this.lastComparisons;
        this.totalExecutionTime += this.lastExecutionTime;
        
        System.out.println("Quick Sorting LR End");
        return myArray;
    }
    
    private T[] recursiveQuickSortLL(T[] array){
        if (array.length == 1) return array;
        T[] myArray = (T[]) Array.newInstance(array[0].getClass(), array.length);
        System.arraycopy(array, 0, myArray, 0, array.length);
        //Due to optimizations for array access, the pivot is going to be both the index in the array and the value so we can compare the values without accessing the array
        
        int pivot = myArray.length - 1,
            firstGreaterThanPivot = -1,
            iterator = 0;
        
        T pivotValue = myArray[pivot];
        
        while (iterator < myArray.length){
            if(firstGreaterThanPivot == -1 && myArray[iterator].compareTo(pivotValue)>0) firstGreaterThanPivot = iterator;
            if(firstGreaterThanPivot != -1 && myArray[iterator].compareTo(pivotValue)<=0){
                T aux = myArray[firstGreaterThanPivot];
                myArray[firstGreaterThanPivot] = myArray[iterator];
                myArray[iterator] = aux;
                if(iterator == pivot) pivot = firstGreaterThanPivot;
                ++firstGreaterThanPivot;
            }
            ++iterator;
        }
        //Pivot is correct. Now we apply quickSortLL to both sides of the pivot
        
        //TODO: Create the sub arrays correctly
        if(pivot > 0){//There is something on the left to order
            T[] leftArray = (T[]) Array.newInstance(myArray[0].getClass(), pivot);
            System.arraycopy(myArray, 0, leftArray, 0, leftArray.length);
            leftArray = recursiveQuickSortLL(leftArray);
            for (int i = 0; i < leftArray.length; ++i){
                myArray[i] = leftArray[i];
            }
        }
        
        if(pivot<myArray.length - 1){
            T[] rightArray = (T[]) Array.newInstance(myArray[0].getClass(), (myArray.length - pivot - 1));
            System.arraycopy(myArray, pivot+1, rightArray, 0, rightArray.length);
            rightArray = recursiveQuickSortLL(rightArray);
            for (int i = 0; i < rightArray.length; ++i){
                myArray[i+pivot+1] = rightArray[i];
            }
        }
        
        
        //We put the values of the sub quicksorts in the array we return
        
        
        return myArray;
    }
    
    public T[] quickSortLL(T[] array){
        System.out.println("Quick Sorting LL Start");
        System.out.println("param |array|: " + Arrays.deepToString(array));
        this.lastExecutedMethod = "QUICK SORT LL";
        this.lastArrayAccesses = 0;
        this.lastComparisons = 0;
        this.lastExecutionTime = (-System.currentTimeMillis());
        
        
        T[] myArray = recursiveQuickSortLL(array);
        
        
        this.lastExecutionTime += System.currentTimeMillis();
        
        this.totalArrayAccesses += this.lastArrayAccesses;
        this.totalComparisons += this.lastComparisons;
        this.totalExecutionTime += this.lastExecutionTime;
        
        System.out.println("Quick Sorting LR End");
        return myArray;
    }
    
    public String getStats() {
        return "{\n"
                + "\tLast Executed Metod: " + this.lastExecutedMethod + "\n"
                + "\tLast Execution Duration: " + this.lastExecutionTime + "\n"
                + "\tLast Execution Comparisons: " + this.lastComparisons + "\n"
                + "\tLast Execution Array Acceses: " + this.lastArrayAccesses + "\n"
                + "\tTotal Execution Duration: " + this.totalExecutionTime + "\n"
                + "\tTotal Execution Comparisons: " + this.totalComparisons + "\n"
                + "\tTotal Execution Array Acceses: " + this.totalArrayAccesses + "\n"
             + "}";
    }

}
