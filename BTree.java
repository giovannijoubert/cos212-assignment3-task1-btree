class BTree<T extends Comparable<T>> {

	BTreeNode<T> root; // Pointer to root node
    	int m;  // B-Tree order/2

	// Constructor (Initializes tree as empty)
    	BTree(int order)
    	{  
		root = null; 
		m = order; 
	}

	// function to print the tree
    	public void print()
    	{  
		if (root != null) 
		{ 
			root.print(root);
			System.out.println();
		} 
	}

	// function to insert the given key in this B-Tree
	public void insert(T key)
	{
    		// If the tree is empty
    		if (root == null)
    		{
        		root = new BTreeNode<T>(m, true);  // Create new root
        		root.keys[0] = key;  // Insert key
        		root.keyTally = 1;  // Update number of keys in root
    		}
		// If the tree is not empty
    		else 
    		{
        		root = root.insert(key);
    		}
	}
 

	// function to delete the given key in this B-Tree
	public void delete(T key)
	{
		if (root != null) 
		{ 
			root = root.delete(key);
		} 
	} 

}