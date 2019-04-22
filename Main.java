public class Main {
	

    public static void main(String[] args) 
    {
	BTree<Integer> tree = new BTree<Integer>(2); // A B-Tree with order 4 (2*m)
   
	tree.insert(20);
	tree.insert(10);
	tree.insert(30);
	tree.insert(50);
	tree.insert(40);
	tree.insert(60);
	tree.insert(90);
	tree.insert(70);
	tree.insert(80);

	System.out.println("Structure of the constucted tree is : ");
	tree.print();

	Integer value = 70;
	tree.delete(value);
	System.out.println("Structure of the tree after delete of : " + value);
	tree.print();

	value = 60;
	tree.delete(value);
	System.out.println("Structure of the tree after delete of : " + value);
	tree.print();

	value = 50;
	tree.delete(value);
	System.out.println("Structure of the tree after delete of : " + value);
	tree.print();

	/* Expected Output:
	Structure of the constucted tree is :
	Level 1 [ 40]
	Level 2 [ 20]
	Level 3 [ 10]
	Level 3 [ 30]
	Level 2 [ 60]
	Level 3 [ 50]
	Level 3 [ 70 80 90]

	Structure of the tree after delete of : 70
	Level 1 [ 20 40 60]
	Level 2 [ 10]
	Level 2 [ 30]
	Level 2 [ 50]
	Level 2 [ 80 90]

	Structure of the tree after delete of : 60
	Level 1 [ 20 40 80]
	Level 2 [ 10]
	Level 2 [ 30]
	Level 2 [ 50]
	Level 2 [ 90]

	Structure of the tree after delete of : 50
	Level 1 [ 20 40]
	Level 2 [ 10]
	Level 2 [ 30]
	Level 2 [ 80 90]
	*/
    }


    
}