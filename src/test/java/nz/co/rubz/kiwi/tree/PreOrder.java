package nz.co.rubz.kiwi.tree;

public class PreOrder {

	public void preOrderTree(Node node) {
		if (node == null) {
			return;
		}
		System.out.print(node.value + " ");
		preOrderTree(node.left);
		preOrderTree(node.right);
	}
	
	
}
