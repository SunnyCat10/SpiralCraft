package me.Sunny.SpiralGeneration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Deserialization for LayoutNode trees.
 * @author Daniel Dovgun
 * @version 9/22/2020
 */
public final class Deserialization {
	
	private static final String EMPTY_NODE_IN_FILE_FOUND_ERROR = "ERROR: An empty node was found inside the template. Initiating the node ID as default.";
	private static final String WRONG_FILE_FORMATING_ERROR = "ERROR: The file formating is incorrect.";
	
	// Prevent initiating this class.
	private Deserialization() { }
	
	/**
	 * Deserialize a LayoutNode tree.
	 * @param stringPath The path to the file in a String format.
	 * @return LayoutNode tree;
	 */
	public static LayoutNode Deserialize(String stringPath) {
		String layout = LoadFile(stringPath);
		StringBuilder stringBuilder = new StringBuilder(layout);
		LayoutNode nodeTree = loadNode(stringBuilder, stringPath);	
		return nodeTree;
	}
	
	/**
	 * Loads a String template of LayoutNode tree from a file path.
	 * @param stringPath LayoutNode tree file path.
	 * @return template of LayoutNode tree in a String format.
	 */
	private static String LoadFile(String stringPath) {
		String treeLayout;		
		Path path = Paths.get(stringPath);
		try {
			treeLayout = Files.readString(path, StandardCharsets.US_ASCII);
		} catch (IOException e) {
			// TODO: Add some default map generation to indicate we run into a bug with I/O.
			e.printStackTrace();
			return null;
		}
		
		if (treeLayout == null) {
			// TODO: Add some default map generation to indicate that the file we got was empty.
			return null;
		}	
		return treeLayout;
	}
	
	/**
	 * Reads one token from the StringBuilder and erase it.
	 * @param stringBuilder StringBuilder object.
	 * @return First character of the StringBuilder object.
	 */
	private static char ReadToken(StringBuilder stringBuilder) {
		char token = stringBuilder.charAt(0);
		stringBuilder.delete(0,1);
		return token;
	}
	
	/**
	 * Reads one node value fro the StringBuilder and erase it.
	 * Example for a valid node: (42A)
	 * @param input
	 * @return
	 */
	private static String readNodeID(StringBuilder input) {
		StringBuilder nodeID = new StringBuilder();
		
		if (input.length() == 1) { // Checking for empty node.
			System.out.print(EMPTY_NODE_IN_FILE_FOUND_ERROR);;
			return null;
		}

		for (int i = 0; i < input.length(); i++) {
			char token = input.charAt(i);
			if (token == ')') { 
				input.delete(0, i+1); // Before breaking from the loop, remove the node from the file.
				break;
			}
			else 
				nodeID.append(token);
		}
	
		System.out.println(nodeID.toString());
		return nodeID.toString();
	}
	
	/**
	 * Recursively build out a LayoutNode tree out of a StringBuilder template.
	 * @param stringBuilder StringBuilder template for the LayoutNode.
	 * @param filePath Stores the file path for error message handling.
	 * @return LayoutNode tree.
	 */
	private static LayoutNode loadNode(StringBuilder stringBuilder, String filePath) {
		char token = ReadToken(stringBuilder);
		LayoutNode node = null;
		
		if (token == '<') 
			return null; 
		else if (token == '(') {
			node = new LayoutNode(readNodeID(stringBuilder));
		}
		else {
			System.out.println(WRONG_FILE_FORMATING_ERROR + '\t' + filePath);
			return null;
		}
		/*if (isDigit(token)) { 
			// TODO: Add the ability to read multiple-digit numbers.
			node = new LayoutNode(String.valueOf(token)); }*/
	
		
		while (true) {
			LayoutNode child = loadNode(stringBuilder, filePath);
			if (child == null)
				break;
			node.addNode(child);
			// TODO: Add some kind of debugging.
			//System.out.println(child.toString());
		}
		return node;
	}

}