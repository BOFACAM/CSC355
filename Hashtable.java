@SuppressWarnings("unchecked")
public class Hashtable<K, V> {
    private Pair[] table;
    private int n;//the number of key-value pairs in the table
    private int m;//the size of the table
    private double alphaHigh = 0.5;//resize if n/m exceeds this (1/2)
    private double alphaLow = 0.125;//resize if n/m goes below this (1/8)

    //constructor--default table size is 11
    public Hashtable() {
	n = 0;
	m = 11;
	table = new Pair[m];
    }

    //constructor
    public Hashtable(int m) {
	n = 0;
	this.m = m;
	table = new Pair[m];
    }


    private void resize(int newCapacity) {
        Pair[] newTable = new Pair[newCapacity];
        Pair[] oldTable = table; // Save the old table
        m = newCapacity; // Update the table size
    
        for (Pair<K, V> pair : oldTable) {
            if (pair != null && !pair.isDeleted()) {
                int hashCode = Math.abs(pair.getKey().hashCode());
                int index = hashCode % m;
    
                while (newTable[index] != null) {
                    index = (index + 1) % m;
                }
    
                newTable[index] = pair;
                //n++;
            }
        }
    
        // Update the table reference to the new resized table
        table = newTable;
    }


    //returns the value associated with key <key>
    //return null if key is not in table
    //do not forget that you will have to cast the result to (V)
    public V get(K key) {
        int hashCode = Math.abs(key.hashCode());
        int index = hashCode % m;

       while(table[index] != null){
        if(table[index].getKey().equals(key)){
            return (V) table[index].getValue();

            }
            index = (index + 1) % m;
        }
        return null;

    }

    //puts (key, val) into the table or updates value
    //if the key is already in the table
    //resize to getNextNum(2*m) if (double)n/m exceeds alphaHigh after the insert
    public void put(K key, V val) {
        if (((double) n / m) > alphaHigh) {
            resize(getNextNum(2 * m));
        }
    
        int hashCode = Math.abs(key.hashCode());
        int index = hashCode % m;
    
        while (table[index] != null && (table[index].isDeleted() || !table[index].getKey().equals(key))) {
            index = (index + 1) % m;
        }
    
        table[index] = new Pair<>(key, val);
        n++;
    
        if (m >= 11 && ((double) n / m) > alphaHigh) {
            resize(getNextNum(2 * m));
        }
    }
    

    //removes the (key, value) pair associated with <key>
    //returns the deleted value or null if the element was not in the table
    //resize to getNextNum(m/2) if m/2 >= 11 AND (double)n/m < alphaLow after the delete
    public V delete(K key) {
        int hashCode = Math.abs(key.hashCode());
        int index = hashCode % m;
    
        while (table[index] != null) {
            if (table[index].getKey().equals(key) && !table[index].isDeleted()) {
                V deletedValue = (V) table[index].getValue();
                table[index].setDeleted(true); // Mark as deleted
                n--;
    
                if (m >= 11 && (double) n / m < alphaLow) {
                    resize(getNextNum(m / 2));
                }
    
                // Rehash and insert elements after the deleted pair
                int nextIndex = (index + 1) % m;
                while (table[nextIndex] != null && !table[nextIndex].isDeleted()) {
                    Pair<K, V> pairToRehash = table[nextIndex];
                    table[nextIndex] = null; // Remove the pair temporarily
                    put(pairToRehash.getKey(), pairToRehash.getValue()); // Reinsert with updated hash
                    nextIndex = (nextIndex + 1) % m;
                }
    
                return deletedValue;
            }
            index = (index + 1) % m;
        }
    
        return null;
    }

    //return true if table is empty
    public boolean isEmpty() {
        return n == 0;
    }

    //return the number of (key,value) pairs in the table
    public int size() {
        return n;
    }

    //This method is used for testing only. Do not use this method yourself for any reason
    //other than debugging. Do not change this method.
    public Pair[] getTable() {
	return table;
    }

    //PRIVATE

    
    //gets the next multiple of 6 plus or minus 1,
    //which has a decent probability of being prime.
    //Use this method when resizing the table.
    private int getNextNum(int num) {
	if(num == 2 || num == 3)
	    return num;
	int rem = num % 6;
	switch(rem) {
	case 0: num++; break;
	case 2: num+=3; break;
	case 3: num+=2; break;
	case 4: num++; break;
	}
	return num;
    }
}
      
