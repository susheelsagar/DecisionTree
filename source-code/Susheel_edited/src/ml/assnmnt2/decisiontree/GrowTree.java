package ml.assnmnt2.decisiontree;


import java.util.ArrayList;
import java.io.Serializable;
import java.lang.Math;
import java.util.Enumeration;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;


public class GrowTree implements Classifier,Serializable {

	/**
	 * The class that builds the Decision tree based on the input data
	 */	
	private static final long serialVersionUID = 1L;
	int num_inst,index,checker,homogeneous_check;
	String prev_value=null,label=null; 
	/*Tree object */
	transient Tree root=null;
	Tree t;
	Instances temp_data,copy_instances;	
	Attribute prev_node=null;	
	ArrayList<String> classes;
	static int n;
	
	/* Code to check whether a function is homogeneous or not
	 * Returns 1 if homogeneous
	 * Returns 0 if the input data is non homogeneous*/
	public void set_Instances(Instances data)
	{
		temp_data=new Instances(data);		
	}
	public int isHomogeneous(Instances data) throws Exception
	{
		int check1=0,check2=0 ;
		/*Enumeration of all the instances*/		
		Enumeration<Instance> instance = data.enumerateInstances();		
		while(instance.hasMoreElements())
		{
			Instance ins = instance.nextElement();
			if((int)ins.value(ins.numAttributes()-1)==0)
			{
				check1++;				
			}
			else
			{
				check2++;
			}			
		}
		if(check1==0||check2==0)
		{		
			return 1; 
			//homogeneous data
		}
		else
		{ 
			return 0;
			// non homogeneous data
		} 		
	}
	
	
	
	/*Function that returns the object of the root node to the calling class(Here it is Begin_DTree.class)*/	
	public Tree returnTree() throws Exception
	{
		return root;
	}
	
	
	/*Function to calculate the bestSplit and returns the Attribute with the least impurity*/
	public Attribute bestSplit(Instances data, Instances copy_instances) throws Exception
	{		
		Attribute best_Attribute=null;		
		Enumeration<Attribute> features = data.enumerateAttributes();		
		double min_impurity = 1;		
		/*For EACH ATTRIBUTE*/
		while(features.hasMoreElements())
		{			
			double calc_impurity=0;
			
			Attribute curr_feature = features.nextElement();
			Enumeration<Object> values = curr_feature.enumerateValues();
					
			if(!curr_feature.equals(data.attribute(data.numAttributes()-1)))				
			{						
				while(values.hasMoreElements())
				{
					
					int n=0;
					Enumeration<Instance> instances = data.enumerateInstances();
					Instances temp_instances;
					temp_instances=copy_instances;
					String current_value = values.nextElement().toString();
					
					/*For each of the instances in the present data sent to this function(bestSplit)*/
					while(instances.hasMoreElements())
					{							
						Instance temp_instance;
						temp_instance=instances.nextElement();
						int value_index=(int)temp_instance.value(curr_feature);
						String value=curr_feature.value(value_index);
						if(value.equals(current_value))
						{							
							if(n==0)
							{								
								int num = copy_instances.numInstances();
								num--;
								
								while(num>-1)
								{
									temp_instances.delete(num);
									num--;
								}
								n++;	
							}
							temp_instances.add(temp_instance);							
						}
					}	
					double numb = (double)temp_instances.numInstances()/(double)data.numInstances();
				
					/*Calculating the entropy for each of the subsets*/
				
					calc_impurity+=numb*(entropy(temp_instances));					
				}
				if(calc_impurity < min_impurity)
				{
					min_impurity = calc_impurity;
					best_Attribute = curr_feature;				
				}		
			}
		}
		
		return best_Attribute;		
	}
	
	/*Function that calculates the entropy value of the instances*/
	public double entropy(Instances temp_instances) throws Exception
	{
		int class_count=0;
		double emp_prob=0,entropy_val=0;		
		Enumeration<Instance> coll_instances = temp_instances.enumerateInstances();
		
		/*For the set of instances received calculate the entropy*/
		while(coll_instances.hasMoreElements())
		{
			Instance temp_instance = coll_instances.nextElement();
			/*Code to take the last attribute as the class value*/
			if((int)temp_instance.value(temp_instance.numAttributes()-1)==0)
			{
					class_count++;
			}				
		}
		emp_prob=((double)class_count/(double)temp_instances.numInstances());		
		if(emp_prob==(double)1||emp_prob==(double)0)
		{			
			return (double)0;
		}		
		entropy_val = ((-emp_prob)*Math.log(emp_prob))-((1-emp_prob)*Math.log(1-emp_prob));		
		return entropy_val;
	}
	
	
	/* ************ Insertion Start from here *************************** */
	public int insertNode(Tree node) throws Exception
	{
		if(root==null)
		{			
			root = node;
			return 1;
		}
		else
		{			
			int k=0;
			k=fitNode(root,node);
			return k;
		}		
	}
	
	/*Code to traverse through the tree and insert the node at the specific position*/
	public int fitNode(Tree root, Tree node) throws Exception
	{
		int check=0;
		if(root.present_node!=null)
		{
		Attribute temp = root.present_node;		
		Enumeration<Object> values = temp.enumerateValues();
		if(node.prev_node==root.present_node)
		{
			int i=0;			
			while(values.hasMoreElements())
			{
				if(values.nextElement().equals(node.literal))
				{
					root.children.add(i, node);
					return ++check;
				}
				else
				{
					i++;
				}
			}
		}
		else
		{
			int size=0;
			if(root.present_node!=null&&root.leaf_node==null)
			{	
				ArrayList<Tree> ch = root.children;
				while(size<ch.size())
				{
					check = fitNode(ch.get(size),node);
					if(check==1)
					{
						return check;
					}
					size++;
				}								
			}
		}
		}
		return check;		
	}
	
	/*buildClassifier function builds the decision tree model */
	@Override
	public void buildClassifier(Instances data) throws Exception 
	{	
		int i=-1;
		int check_value=-1;
		if(checker==0)
		{			
			set_Instances(data);
			classes = new ArrayList<String>();
			copy_instances = new Instances(data);
			copy_instances.setClassIndex(copy_instances.numAttributes()-1);
			Attribute a = copy_instances.attribute(copy_instances.numAttributes()-1);
			Enumeration<Object> e = a.enumerateValues();
			while(e.hasMoreElements())
			{
				classes.add(e.nextElement().toString());
			}
			checker++;
		}		
		/*Checking if the data is homogeneous or not*/
		if(isHomogeneous(data)==1)
		{
			//System.out.println("entered homogeneous data");
			homogeneous_check=1;
			Instance ins = data.lastInstance();
			label=ins.toString(ins.numAttributes()-1);
			 t = new Tree(prev_node,prev_value,null,label);
			if(insertNode(t)==1)
				{
				//			System.out.println("Insertion successful");
				//			System.out.println("inserted node is ------------------------->"+ t.attribute);
				}
			else
			{
				System.out.println("Insertion failed");
			}
			return;
		}
		
		/*Calling BestSplit function that returns the Attribute with least impurity*/
		Attribute s = bestSplit(data,copy_instances);
		
		/*The attribute returned must be stored in a tree*/
		if(root==null)
		{
			 t = new Tree(null,prev_value,s,null );
		}
		else
		{
			 t = new Tree(prev_node,prev_value,s,null);
		}		
		int k2 = insertNode(t);		
		if(k2==0)
		{
			System.out.println("insertion failed");
		}
		else 
		{
			//System.out.println("insertion successful");						
		}
		
		Enumeration<Object> values = s.enumerateValues();		
		Enumeration<Attribute> Attr = data.enumerateAttributes();
		/*Checking for the index of the attribute returned by the bestSplit function*/
		while(Attr.hasMoreElements())
		{
			i++;
			Attribute temp = Attr.nextElement();
			if(temp==s)
			{
				break;
			}
		}
		/* Splitting Data into subsets based on the literals in the attribute returned by the bestSplit function*/
		while(values.hasMoreElements()) 
		{		
			check_value++;
			int n=0;
			Instances value_Instances=new Instances(copy_instances);
			String str=(String)values.nextElement();	
			Enumeration<Instance> splits = data.enumerateInstances();
			while(splits.hasMoreElements())
			{
				Instance ins = splits.nextElement();
				String instance_str = ins.toString();
				String [] str_splits = instance_str.split(",");
				if(n==0)
				{								
					int num = copy_instances.numInstances();
					num--;					
					while(num>-1)
					{
						value_Instances.delete(num);
						num--;
					}
						n++;
				}
				if(str_splits[i].equals(str))
				{
					value_Instances.add(ins);
				}
					
			}			
			/* Recusrsive call to the build classifier function based 
			 * on the subsets of instances obtained by splitting the data 
			 * into different datasets based on the values of the attribute 
			 * returned by the bestSplit function */
		
			if(value_Instances.size()>0)
			{
				if(homogeneous_check==1)
				{			
					prev_value = s.value(check_value);
					homogeneous_check=0;
				}
				else
				{
					prev_value = s.value(check_value);
				}
				prev_node=s;
			buildClassifier(value_Instances);
			}
			/* If any value of the attribute if an empty set i.e it does not have any instances 
			 * with that value, then the class with the highest number of  */
			else
			{
				Enumeration<Instance> ins = data.enumerateInstances();
				int class_index=data.classIndex();
				Attribute class_attribute = data.attribute(class_index);
				int num_classes=class_attribute.numValues();
				int classes[] = new int[num_classes];
				int check=0,max_class=0;
				while(check<num_classes)
				{
					while(ins.hasMoreElements())
					{
						Instance k = ins.nextElement(); 
						if((int)k.classValue()==check)
						{
							classes[check]++;
						}
						
					}
					check++;
				}
				check=0;
				int temp=0;
				while(check<num_classes)
				{
					if(temp<classes[check])
					{
						temp=classes[check];
						max_class = check;
					}
					check++;
				}				
				Tree t2 = new Tree(prev_node,prev_value,null,class_attribute.value(max_class).toString());
				insertNode(t2);
			}			
		}			
	}
	
	public String traverse(String str2[], Tree root)
	{
		Enumeration<Attribute> att = temp_data.enumerateAttributes();
		int i=0,j=0,att_index=0;
		String class_value=null;
		if(root.leaf_node!=null)
		{
			class_value = root.leaf_node;
			return class_value;
		}
		Attribute temp_att = root.present_node;
		while(att.hasMoreElements())
		{			
			Attribute k =att.nextElement();
			if(k.equals(root.present_node))
			{				
				att_index=j;
			}
			else
			{
				j++;
			}
		}		
		String value = str2[att_index];
		Enumeration<Object> values = temp_att.enumerateValues();
		while(values.hasMoreElements())
		{
			if(!value.equals(values.nextElement().toString()))
			{
				i++;
			}
			else
			{
				class_value=traverse(str2,root.children.get(i));
			}
			if(class_value!=null)
				return class_value;
		}		
		return class_value;	
	}
	
	@Override
	public double classifyInstance(Instance instance) throws Exception 
	{
		double k =0;
		int i=0;
		String str = instance.toString();
		String [] str2 = str.split(",");
		str= traverse(str2,root);
		i=0;	
		while(i<classes.size())
		{
			if(str.equals(classes.get(i)))
			{				
				return (double)i;
			}
			i++;			
		}		
		return k;
	}
	
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception 
	{
		double l = classifyInstance(instance);
		double k[] = new double[classes.size()];
		k[(int)l] = (double)1;		
		return k;
	}

	@Override
	public Capabilities getCapabilities() 
	{
				return null;
	}
}
