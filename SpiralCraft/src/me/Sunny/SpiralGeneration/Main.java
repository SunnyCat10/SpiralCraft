package me.Sunny.SpiralGeneration;

public class Main {
	
	public static final String TEST_PATH = "C:/Users/Sunny/eclipse-workspace/SpiralGeneration/test.txt";
	
	public static void main(String[] args) {
		
		LayoutBuilder lb = new LayoutBuilder(TEST_PATH);
		RoomNode root = lb.Build();
		DEBUG(root);
		
		
		
		//test();
		
		//Node n = new LayoutBuilder().getLayout();
		//System.out.print(n.toString());
		
		
	}
	
	public static void DEBUG(AbstractNode root) {
		System.out.println(root.toString());
		if (root instanceof LayoutNode) {
			LayoutNode node = (LayoutNode) root;	
			for (int i = 0; i <= 3; i++) {
			if (node.getNode(i) != null) 
				DEBUG(node.getNode(i));
			}
		}
		if (root instanceof RoomNode) {
			RoomNode node = (RoomNode) root;	
			for (int i = 0; i <= 4; i++) {
			if (node.getNode(i) != null) 
				DEBUG(node.getNode(i));
			}
		}
	}
}
