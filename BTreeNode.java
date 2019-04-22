@SuppressWarnings("unchecked")
class BTreeNode<T extends Comparable<T>> {
	boolean leaf;
	int keyTally;
	Comparable<T> keys[];
	BTreeNode<T> references[];
	int m;
	static int level = 0;

	// Constructor for BTreeNode class
	public BTreeNode(int order, boolean leaf1)
	{
    		// Copy the given order and leaf property
		m = order;
    		leaf = leaf1;
 
    		// Allocate memory for maximum number of possible keys
    		// and child pointers
    		keys =  new Comparable[2*m-1];
    		references = new BTreeNode[2*m];
 
    		// Initialize the number of keys as 0
    		keyTally = 0;
	}

	// Function to print all nodes in a subtree rooted with this node
	public void print(BTreeNode<T> node)
	{
		level++;
		if (node != null) {
			System.out.print("Level " + level + " ");
			node.printKeys();
			System.out.println();

			// If this node is not leaf, then 
        		// print all the subtrees rooted with this node.
        		if (!node.leaf)
			{	
				for (int j = 0; j < 2*m; j++)
    				{
        				this.print(node.references[j]);
    				}
			}
		}
		level--;
	}

	// A utility function to print all the keys in this node
	private void printKeys()
	{
		System.out.print("[");
    		for (int i = 0; i < this.keyTally; i++)
    		{
        		System.out.print(" " + this.keys[i]);
    		}
 		System.out.print("]");
	}

	// A utility function to give a string representation of this node
	public String toString() {
		String out = "[";
		for (int i = 1; i < (this.keyTally-1); i++)
			out += keys[i-1] + ",";
		out += keys[keyTally-1] + "] ";
		return out;
	}

	// Function to insert the given key in tree rooted with this node
	public BTreeNode<T> insert(T key)
	{
        	// If root is full, then tree grows in height
        	if (this.keyTally == 2*m-1)
        	{
            		// Create a new root
            		BTreeNode<T> s = new BTreeNode<T>(m, false);
 
			// Make the current root a child of the new root
            		s.references[0] = this;
 
            		// Split the current root and move 1 key to the new root
            		s.split(0, this);
 
            		// Decide which of the two children is going to have new key
            		int i = 0;
            		if (s.keys[0].compareTo(key) < 0)
                		i++;
            		s.references[i].insertNotFull(key);
 
            		// Change root
			return s;
        	}
        	else  // If root is not full, call insertNotFull for root
		{
            		this.insertNotFull(key);
			return this;
		}
	}

	// A utility function to insert a new key in this node
	// The node must be non-full when this function is called
	private void insertNotFull(T key)
	{
    		// Initialize index as index of rightmost element
    		int i = this.keyTally-1;
 
    		// If this is a leaf node
    		if (this.leaf)
    		{
        		// The following loop does two things
        		// a) Finds the location of new key to be inserted
        		// b) Moves all greater keys to one place ahead
        		while (i >= 0 && this.keys[i].compareTo(key) > 0)
        		{
            			this.keys[i+1] = this.keys[i];
            			i--;
        		}
 
        		// Insert the new key at found location
        		this.keys[i+1] = key;
        		this.keyTally = this.keyTally+1;
    		}
    		else // If this node is not leaf
    		{
        		// Find the child which is going to have the new key
        		while (i >= 0 && this.keys[i].compareTo(key) > 0)
            		i--;
 
        		// See if the found child is full
        		if (references[i+1].keyTally == 2*m-1)
        		{
            			// If the child is full, then split it
            			this.split(i+1, this.references[i+1]);
 
            			// After split, the middle key of references[i] goes up and
            			// references[i] is split into two. Check which of the two
            			// is going to have the new key.
            			if (this.keys[i+1].compareTo(key) < 0)
                			i++;
        		}
        		this.references[i+1].insertNotFull(key);
    		}
	}

	// A utility function to split the child y of this node
	// Node y must be full when this function is called
	private void split(int i, BTreeNode<T> y)
	{
    		// Create a new node which is going to store m-1 keys
    		// of y
    		BTreeNode<T> z = new BTreeNode(y.m, y.leaf);
    		z.keyTally = m - 1;
 
    		// Copy the last (m-1) keys of y to z
    		for (int j = 0; j < m-1; j++)
        		z.keys[j] = y.keys[j+m];
 
    		// Copy the last m children of y to z
    		if (!y.leaf)
    		{
        		for (int j = 0; j < m; j++)
			{
            			z.references[j] = y.references[j+m];
				y.references[j+m] = null;
			}
    		}
 
    		// Reduce the number of keys in y
    		y.keyTally = m - 1;
 
    		// Since this node is going to have a new child,
    		// create space of new child
    		for (int j = this.keyTally; j >= i+1; j--)
        		this.references[j+1] = this.references[j];
 
    		// Link the new child to this node
    		this.references[i+1] = z;
 
    		// A key of y will move to this node. Find location of
    		// new key and move all greater keys one space ahead
    		for (int j = this.keyTally-1; j >= i; j--)
        		this.keys[j+1] = this.keys[j];
 
    		// Copy the middle key of y to this node
    		this.keys[i] = y.keys[m-1];
 
    		// Increment count of keys in this node
    		this.keyTally = this.keyTally + 1;
	}

	////// You should not change any code above this line //////

	////// Implement the functions below this line //////

	// A function to delete the given key from the sub-tree rooted with this node 
	public BTreeNode<T> delete(T key) 
	{ 
		int idx = findKey(key); 
  
		// The key to be removed is present in this node 
		if (idx < keyTally && keys[idx].equals(key)) 
		{ 
	  
			// If the node is a leaf node - removeFromLeaf is called 
			// Otherwise, removeFromNonLeaf function is called 
			if (leaf) 
				removeFromLeaf(idx); 
			else
				removeFromNonLeaf(idx); 
		} 
		else
		{ 
	  
			// If this node is a leaf node, then the key is not present in tree 
			if (leaf) 
			{ 
				return this; 
			} 
	  
			// The key to be removed is present in the sub-tree rooted with this node 
			// The flag indicates whether the key is present in the sub-tree rooted 
			// with the last child of this node 
			Boolean flag = ( (idx==keyTally)? true : false ); 
	  
			// If the child where the key is supposed to exist has less that t keys, 
			// we fill that child 
			if (references[idx].keyTally < m) 
				fill(idx); 
	  
			// If the last child has been merged, it must have merged with the previous 
			// child and so we recurse on the (idx-1)th child. Else, we recurse on the 
			// (idx)th child which now has atleast t keys 
			if (flag && idx > keyTally) 
				references[idx-1].delete(key); 
			else
				references[idx].delete(key); 
		} 
		return this; 
	}




	// A function to remove the idx-th key from this node - which is a leaf node 
	public void removeFromLeaf (int idx) 
	{ 
  
    	// Move all the keys after the idx-th pos one place backward 
    	for (int i=idx+1; i<keyTally; ++i) 
        	keys[i-1] = keys[i]; 
  
	    // Reduce the count of keys 
    	keyTally--; 
  
  	  	return; 
	} 


	// A function to remove the idx-th key from this node - which is a non-leaf node 
	public void removeFromNonLeaf(int idx) 
	{ 
  
		T  k = (T)keys[idx]; 

	
		// If the child that precedes k (C[idx]) has atleast ts keys, 
		// find the predecessor 'pred' of k in the subtree rooted at 
		// C[idx]. Replace k by pred. Recursively delete pred 
		// in C[idx] 
		if (references[idx].keyTally >= m) 
		{ 
			T pred = getPred(idx); 
			keys[idx] = pred; 
			references[idx].delete(pred); 
		} 
	
		// If the child C[idx] has less that t keys, examine C[idx+1]. 
		// If C[idx+1] has atleast t keys, find the successor 'succ' of k in 
		// the subtree rooted at C[idx+1] 
		// Replace k by succ 
		// Recursively delete succ in C[idx+1] 
		else if  (references[idx+1].keyTally >= m) 
		{ 
			T succ = getSucc(idx); 
			keys[idx] = succ; 
			references[idx+1].delete(succ); 
		} 
	
		// If both C[idx] and C[idx+1] has less that t keys,merge k and all of C[idx+1] 
		// into C[idx] 
		// Now C[idx] contains 2t-1 keys 
		// Free C[idx+1] and recursively delete k from C[idx] 
		else
		{ 
			merge(idx); 
			references[idx].delete(k); 
		} 
		return; 
	} 

		// A function to get predecessor of keys[idx] 
	public T getPred(int idx) 
	{ 
		// Keep moving to the right most node until we reach a leaf 
		BTreeNode<T> cur=references[idx]; 
		while (!cur.leaf) 
			cur = cur.references[cur.keyTally]; 
	
		// Return the last key of the leaf 
		return (T)cur.keys[cur.keyTally-1]; 
	} 
	



	public T getSucc(int idx) 
	{ 
	
		// Keep moving the left most node starting from C[idx+1] until we reach a leaf 
		BTreeNode<T> cur = references[idx+1]; 
		while (!cur.leaf) 
			cur = cur.references[0]; 
	
		// Return the first key of the leaf 
		return (T)cur.keys[0]; 
	} 

		// A function to fill child C[idx] which has less than t-1 keys 
	public void fill(int idx) 
	{ 
	
		// If the previous child(C[idx-1]) has more than t-1 keys, borrow a key 
		// from that child 
		if (idx!=0 && references[idx-1].keyTally>=m) 
			borrowFromPrev(idx); 
	
		// If the next child(C[idx+1]) has more than t-1 keys, borrow a key 
		// from that child 
		else if (idx!=keyTally && references[idx+1].keyTally>=m) 
			borrowFromNext(idx); 
	
		// Merge C[idx] with its sibling 
		// If C[idx] is the last child, merge it with with its previous sibling 
		// Otherwise merge it with its next sibling 
		else
		{ 
			if (idx != keyTally) 
				merge(idx); 
			else
				merge(idx-1); 
		} 
		return; 
	} 

		// A function to borrow a key from C[idx-1] and insert it 
	// into C[idx] 
	public void borrowFromPrev(int idx) 
	{ 
	
		BTreeNode<T> child=references[idx]; 
		BTreeNode<T> sibling=references[idx-1]; 
	
		// The last key from C[idx-1] goes up to the parent and key[idx-1] 
		// from parent is inserted as the first key in C[idx]. Thus, the  loses 
		// sibling one key and child gains one key 
	
		// Moving all key in C[idx] one step ahead 
		for (int i=child.keyTally-1; i>=0; --i) 
			child.keys[i+1] = child.keys[i]; 
	
		// If C[idx] is not a leaf, move all its child pointers one step ahead 
		if (!child.leaf) 
		{ 
			for(int i=child.n; i>=0; --i) 
				child.references[i+1] = child.references[i]; 
		} 
	
		// Setting child's first key equal to keys[idx-1] from the current node 
		child.keys[0] = keys[idx-1]; 
	
		// Moving sibling's last child as C[idx]'s first child 
		if(!child.leaf) 
			child.references[0] = sibling.references[sibling.keyTally]; 
	
		// Moving the key from the sibling to the parent 
		// This reduces the number of keys in the sibling 
		keys[idx-1] = sibling.keys[sibling.keyTally-1]; 
	
		child.keyTally += 1; 
		sibling.keyTally -= 1; 
	
		return; 
	} 

	// A function to borrow a key from the C[idx+1] and place 
	// it in C[idx] 
	public void borrowFromNext(int idx) 
	{ 
	
		BTreeNode<T> child=references[idx]; 
		BTreeNode<T> sibling=references[idx+1]; 
	
		// keys[idx] is inserted as the last key in C[idx] 
		child.keys[(child.keyTally)] = keys[idx]; 
	
		// Sibling's first child is inserted as the last child 
		// into C[idx] 
		if (!(child.leaf)) 
			child.references[(child.keyTally)+1] = sibling.references[0]; 
	
		//The first key from sibling is inserted into keys[idx] 
		keys[idx] = sibling.keys[0]; 
	
		// Moving all keys in sibling one step behind 
		for (int i=1; i<sibling.keyTally; ++i) 
			sibling.keys[i-1] = sibling.keys[i]; 
	
		// Moving the child pointers one step behind 
		if (!sibling.leaf) 
		{ 
			for(int i=1; i<=sibling.keyTally; ++i) 
				sibling.references[i-1] = sibling.references[i]; 
		} 
	
		// Increasing and decreasing the key count of C[idx] and C[idx+1] 
		// respectively 
		child.keyTally += 1; 
		sibling.keyTally -= 1; 
	
		return; 
	} 

	// A function to merge C[idx] with C[idx+1] 
	// C[idx+1] is freed after merging 
	public void merge(int idx) 
	{ 
		BTreeNode<T> child = references[idx]; 
		BTreeNode<T> sibling = references[idx+1]; 
	
		// Pulling a key from the current node and inserting it into (t-1)th 
		// position of C[idx] 
		child.keys[m-1] = keys[idx]; 
	
		// Copying the keys from C[idx+1] to C[idx] at the end 
		for (int i=0; i<sibling.keyTally; ++i) 
			child.keys[i+m] = sibling.keys[i]; 
	
		// Copying the child pointers from C[idx+1] to C[idx] 
		if (!child.leaf) 
		{ 
			for(int i=0; i<=sibling.keyTally; ++i) 
				child.references[i+m] = sibling.references[i]; 
		} 
	
		// Moving all keys after idx in the current node one step before - 
		// to fill the gap created by moving keys[idx] to C[idx] 
		for (int i=idx+1; i<keyTally; ++i) 
			keys[i-1] = keys[i]; 
	
		// Moving the child pointers after (idx+1) in the current node one 
		// step before 
		for (int i=idx+2; i<=keyTally; ++i) 
			references[i-1] = references[i]; 
	
		// Updating the key count of child and the current node 
		child.keyTally += sibling.keyTally+1; 
		keyTally--; 

		return; 
	} 

	// A utility function that returns the index of the first key that is 
	// greater than or equal to k 
	public int findKey(T k) 
	{ 
		int idx=0; 
		while (idx<keyTally && keys[idx].compareTo(k) < 0) 
			++idx; 
		return idx; 
	} 






}