/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.articleranker;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.types.Instance;
import cc.mallet.types.Labeling;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 *
 * @author kmadanagopal
 */
public class MalletTest {
    
    public String classify(String text) throws IOException, ClassNotFoundException
    {
        Classifier classifier;
        InputStream modelIn = ClassifyCategory.class.getClassLoader().getResourceAsStream("jwtm/classifier/news-group.classifier");
        ObjectInputStream ois = new ObjectInputStream (modelIn);
        classifier = (Classifier) ois.readObject();
        ois.close();
        
        Classification cf =  classifier.classify(text);
        return cf.getLabeling().getBestLabel().toString();
    }
    
    public static void main(String args[]) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        String clsPath = "C:\\Users\\kmadanagopal\\Desktop\\mallet-2.0.7\\bin\\news-group.classifier";
        Classifier clas = loadClassifier(new File(clsPath));
        printLabelings(clas);
        
    }
    public static Classifier loadClassifier(File serializedFile)
        throws FileNotFoundException, IOException, ClassNotFoundException {

        // The standard way to save classifiers and Mallet data                                            
        //  for repeated use is through Java serialization.                                                
        // Here we load a serialized classifier from a file.                                               

        Classifier classifier;

        ObjectInputStream ois =
            new ObjectInputStream (new FileInputStream (serializedFile));
        classifier = (Classifier) ois.readObject();
        ois.close();

        return classifier;
    }
 
    public static void printLabelings(Classifier classifier) throws IOException {

        Classification cf =  classifier.classify("2013 NFL draft -- San Diego Chargers select Manti Te'o with sixth pick in second round - ESPN");
        System.out.println(cf.getLabeling().getBestLabel());
       
        
    }
}
