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
		// Your code goes here
	}

}