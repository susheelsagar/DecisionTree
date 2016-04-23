package ml.assnmnt2.decisiontree;

import java.io.Serializable;
import java.util.ArrayList;

import weka.core.Attribute;

public class Tree implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Attribute prev_node;
	String literal;
	Attribute present_node;
	String leaf_node;
	ArrayList<Tree> children;
	public Tree(Attribute prev_node,String literal,Attribute present_node,String leaf_node)
	{
		this.prev_node = prev_node;
		this.literal = literal;
		this.present_node = present_node;
		this.leaf_node = leaf_node;
		this.children = new ArrayList<Tree>();
	}
}
	

