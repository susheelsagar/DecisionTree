package ml.assnmnt2.decisiontree;

import java.io.Serializable;
import java.util.ArrayList;

public class print_DTree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public void printTree(Tree t)
	{
		
		System.out.println("\n\n\n******************************   PRINTING THE TREE   *********************************************\n");	
		print(t,0);
		System.out.println("\n\n\n******************************   END OF THE TREE   *********************************************\n");
	}
	public void print(Tree t, int k)
	{
		if(t!=null)
		{
			int i=0;
			{
				while(i<k)
				{
					System.out.print("\t\t");
					i++;
				}
			}
			if(t.literal!=null)
			{
				System.out.print(t.literal+ " : " ) ;
				if(t.leaf_node!=null)
					System.out.print(t.leaf_node +" ");
				else
					System.out.print(t.present_node + " ");
			}			
			else
			{
				System.out.print(t.present_node);
			}
			if(t.children!=null)
			{
				k++;
				ArrayList<Tree> ch = t.children;
				int size=0;
				while(size<ch.size())
				{   System.out.println();
					Tree tr = ch.get(size);
					print(tr,k);
					size++;
					
				}	
				
			}
		}
	}

}
