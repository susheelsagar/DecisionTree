package ml.assnmnt2.decisiontree;

import java.io.BufferedReader;
import java.util.Random;
import java.io.FileReader;
import java.io.Serializable;


import weka.classifiers.Evaluation;
import weka.core.Instances;

/* Begin_DTree class handles all the decision tree assignment tasks such as
 *  
 * 	1) building the decision tree based on the input data
 * 	2) Printing the decision tree
 *  3) Evaluating the tree model using 10- fold cross validation
 *  4) Printing the predictive accuracy of the model*/

public class Begin_DTree implements Serializable {
	
	/** Begin_DTree class handles all the decision tree assignment tasks such as
 *  
 * 	1) building the decision tree based on the input data
 * 	2) Printing the decision tree
 *  3) Evaluating the tree model using 10- fold cross validation
 *  4) Printing the predictive accuracy of the model
 *  
 *  */
 	 
	private static final long serialVersionUID = 1L;
	static double  accuracy;	
	
	public static void main(String args[]) throws Exception {
		
	/*The location of the input file*/
		
	//String input_file = "/home/s/Desktop/weka-3-7-13/data/weather.nominal.arff";
	//String input_file = "/home/s/Desktop/weka-3-7-13/data/contact-lenses.arff";
	String input_file = "/home/s/Downloads/mushroom2.arff";
	
	/*Random class object to be passed to the evaluation class*/
	
	Random random = new Random(1);//using seed 1
	int folds = 10;
	
	/*Input instances*/
	
	Instances data = new Instances(new BufferedReader(new FileReader(input_file)));
	
	/*Setting the Class index to the last index*/
	
	data.setClassIndex(data.numAttributes()-1);
	
	/*Call to build a decision tree*/
	
	GrowTree g = new GrowTree();
	g.buildClassifier(data);
	
	/*capturing the decision tree in a Tree object*/
	
	Tree t = g.returnTree();
	
	/*PRINTING THE DECISION TREE*/
	
	print_DTree d = new print_DTree();
	d.printTree(t);
	
	/*Invoking the evaluation class*/
	
	Evaluation e = new Evaluation(data);	
	
	/*Invoking the 10-fold crossValidate function*/
	
	e.crossValidateModel(g, data, folds, random);
	
	/*Calculating and printing the accuracy of the model*/
	System.out.println(e.toSummaryString());
	System.out.println(e.toMatrixString());
	System.out.println("The average predictive accuracy of the model is = "+e.pctCorrect());
		
	}
}
