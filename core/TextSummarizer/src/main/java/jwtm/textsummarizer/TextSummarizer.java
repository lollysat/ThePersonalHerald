package jwtm.textsummarizer;


import jwtm.stemmer.PorterStemmer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;
import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.RealVectorFormat;
import org.apache.commons.math3.linear.SparseRealVector;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import jwtm.pagerank.PageRank;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kmadanagopal
 */
public class TextSummarizer {
    private InputStream modelIn = null;
    private HashSet<String> stopwords = new HashSet<String>();
    private SentenceModel model;
    private SentenceDetectorME sentenceDetector;
    public TextSummarizer()
    {
        modelIn = TextSummarizer.class.getClassLoader().getResourceAsStream("jwtm/opennlp/model/en-sent.bin");                
        try {
            model = new SentenceModel(modelIn);
        } catch (IOException ex) {
            Logger.getLogger(TextSummarizer.class.getName()).log(Level.SEVERE, null, ex);
        } 
        sentenceDetector = new SentenceDetectorME(model);
        loadStopwords();
        
    }
    
    private final void loadStopwords()
    {
        try
        {
            InputStream stopwordSteam = TextSummarizer.class.getClassLoader().getResourceAsStream("jwtm/stopwords/stopwords.txt");

            //Load the stopwords into a hashset which can be used in stopwords removal
            BufferedReader br = new BufferedReader(new InputStreamReader(stopwordSteam, "UTF-8"));                

            //new FileReader(stopwordsFile));
            String line;
            while ((line = br.readLine()) != null) {
               stopwords.add(line);
            }
            br.close();
            
            stopwordSteam.close();
        }
        catch(IOException ex)
        {
            System.err.println(ex);
        }
    }
    
    public String summarize(String content, int numSent)
    {
        //hashtable to store the sentenceid and their respective sentences
        HashMap<Integer, String> sentIndex = new HashMap<Integer, String>();
        try {
                //Sentence Boundary Detection                
                String sentences[] = sentenceDetector.sentDetect(content);
                
                //Create a inmemory lucene index
                Directory dir = new RAMDirectory();
                IndexWriter iwriter = new IndexWriter(dir,new SimpleAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);
        
                //variable to generate sentenceIds
                int sentCount = 0;        
                for(String sentence : sentences)
                {
                    if(sentence.split(" ").length <= 3) continue;
                    
                    String[] subsentences = sentence.split("\n");
                    
                    for(String subSent : subsentences)
                    {
                        if(subSent.split(" ").length <= 4 || !subSent.trim().endsWith(".")) continue;
                        
                        //add the sentenceid and sentence to the hash for later retrieval
                        sentIndex.put(sentCount, subSent);

                        //remove stopwords and perform stemming
                        String refinedSentence = performStemming(subSent,stopwords);

                        //add the refined sentence
                        addDocument(iwriter, refinedSentence, sentCount);
                        sentCount++;
                    }
                    
                    
                } 
           
                //close the index writer
                iwriter.close();
                
                //open the index reader
                IndexReader reader = IndexReader.open(dir);        
                
                //create a map for all the terms in the index
                Map<String,Integer> terms = new HashMap<String,Integer>();
                TermEnum termEnum = reader.terms(new Term("content"));
                int pos = 0;
                while (termEnum.next()) {
                  Term term = termEnum.term();
                  if (! "content".equals(term.field())) 
                    break;
                  terms.put(term.text(), pos++);
                }
                
                //Create termfrequence vector for each document
                DocVector[] docs = new DocVector[reader.numDocs()];
                int i = 0;
                for (int docId= 0; docId<reader.numDocs();docId++) {          
                  docs[i] = new DocVector(terms); 
                  TermFreqVector[] tfvs = reader.getTermFreqVectors(docId);
                  for (TermFreqVector tfv : reader.getTermFreqVectors(docId)) {
                    String[] termTexts = tfv.getTerms();
                    int[] termFreqs = tfv.getTermFrequencies();            
                    for (int j = 0; j < termTexts.length; j++) {
                      docs[i].setEntry(termTexts[j], termFreqs[j]);
                    }
                  }
                  docs[i].normalize();
                  i++;
                }
                
                //Initialize the pagerank
                PageRank pr = new PageRank();
                
                //Construct the webgraph (undirected and weighted)
                for (int x= 0; x<reader.numDocs();x++) {          
                   for (int y= 0; y<reader.numDocs();y++) { 
                       if(x == y || x > y) continue;
                       double cosim01 = getCosineSimilarity(docs[x], docs[y]);
                       pr.addEntry(String.valueOf(x), String.valueOf(y), cosim01);
                       pr.addEntry(String.valueOf(y), String.valueOf(x), cosim01);
                       //System.out.println("cosim("+x+","+y+")=" + cosim01);
                   }
               }
                
                //Calculate the pagerank with 10 iterations and threshold being 0.95
                pr.rank(10, 0.99);
                
                //get the top k results
                String[] ids = pr.showResults(numSent).toArray(new String[0]);
                                                
                Arrays.sort(ids, new Comparator<String>()
                {
                  public int compare(String s1, String s2)
                  {
                    return Integer.valueOf(s1).compareTo(Integer.valueOf(s2));
                  }
                });
                
                //Sort the ids 
                //Collections.sort(ids);  
                
                StringBuilder summary = new StringBuilder();
                for(String id : ids)
                {
                    summary.append(sentIndex.get(Integer.parseInt(id))).append(" ");
                }
                
//                Integer[] keys = (Integer[]) sentIndex.keySet().toArray(new Integer[0]);  
//                Arrays.sort(keys);  
//                for(Integer key : keys) {  
//                    summary.append(sentIndex.get(key));  
//                }  
                
                
               reader.close();        
               dir.close();                          
               return summary.toString();
        }
        catch (IOException e) {
          e.printStackTrace();
          return "";
        }        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InvalidFormatException, IOException {   
        
        String text = "Doug Mills/The New York Times\n" +
"\n" +
"President Obama with Speaker of the House Nancy Pelosi and Representative Steney Hoyer after he spoke at the White House on Monday.\n" +
"By DAVID STOUT\n" +
"Published: February 23, 2009\n" +
"\n" +
"WASHINGTON  Despite the huge sums the federal government is spending to aid banks and stimulate the economy, President Obama said on Monday that his administration will slash the federal budget deficit, in part by ending the “casual dishonesty” that has accompanied Washington budgets of late.\n" +
"\n" +
"    More Politics News\n" +
"\n" +
"The president said that the bank-rescue plan and the broader economic stimulus program are necessary not merely to jolt the economy but because the country has “long-term challenges  health care, energy, education and others  that we can no longer afford to ignore.”\n" +
"\n" +
"“But I want to be very clear,” he said at a White House economic conference involving legislators, business and labor leaders and others. “We cannot and will not sustain deficits like these without end. Contrary to the prevailing wisdom in Washington these past few years, we cannot simply spend as we please and defer the consequences to the next budget, the next administration or the next generation.”\n" +
"\n" +
"The president promised, as expected, to halve the deficit that he inherited, estimated at $1.3 trillion or more for 2009, by the end of his first term, partly by having his Cabinet members and other top aides go over the budget “line by line to root out waste and inefficiency,” a process he said was already under way.\n" +
"\n" +
"More specific clues to the president’s thinking may be discernible later this week, when he proposes his first budget, for the federal fiscal year that will begin on Oct. 1. “We’ll start by being honest with ourselves about the magnitude of our deficits,” he said, chiding former President George W. Bush, unmistakably if not by name, for “a series of accounting tricks.”\n" +
"\n" +
"“We’re not going to be able to fall back into the same old habits and make the same inexcusable mistakes,” he said. One such bad habit has been “the casual dishonesty of hiding irresponsible spending,” Mr. Obama said, citing the Bush administration’s technique of “budgeting zero dollars for the Iraq war  zero  for future years, even when we knew the war would continue.”\n" +
"\n" +
"Sooner or later, the president said, tough choices will have to be faced in dealing with the spiraling cost of health care, “the single most pressing fiscal challenge we face, by far,” and to assure the long-term solvency of Social Security. But he also sent a signal that no waste should be considered too small, citing a Department of Agriculture move to put some of its training programs on line to save about $1.3 million a year.\n" +
"\n" +
"The deficit is the year-by-year gap between what the federal government spends and the revenue it takes in. So even if the annual deficits are cut, the total national debt will continue to grow. It now stands at just over $10.8 trillion, according to the Department of the Treasury. Of that amount, about $6.5 trillion is owed to individuals, corporations and governments and other lenders both domestic and foreign, while $4.3 trillion is owed for Social Security benefits, military and civil service pensions and other government programs. ";
        
        TextSummarizer summarizer = new TextSummarizer();
        String summary = summarizer.summarize(text , 5);
        System.out.println(summary+"\n\n");
        
        SimpleTextSummarizer simSum = new SimpleTextSummarizer();
        summary = simSum.summarize(text);
        System.out.println(summary.replace("\n",""));
    }
    
     private static double getCosineSimilarity(DocVector d1, DocVector d2) {
        return (d1.vector.dotProduct(d2.vector)) / (d1.vector.getNorm() * d2.vector.getNorm());
     }
     
     
     public static String performStemming(String sentence, HashSet<String> stopwords){
         StringTokenizer tokenizer = new StringTokenizer(sentence);
         String modString = "";
         while(tokenizer.hasMoreTokens())
         {
             String token = tokenizer.nextToken();
             token = token.toLowerCase();
             if(stopwords.contains(token))  continue;
             modString = modString + findWordStem(token)+" ";
         }
         modString = modString + ".";
         return modString;
     }
       public static void addDocument(IndexWriter Iwriter, String content, int docId) throws CorruptIndexException, IOException {
           Document doc = new Document();

           doc.add(new Field("id", String.valueOf(docId),Field.Store.YES, Field.Index.NOT_ANALYZED ));

           doc.add(new Field("content", content, Field.Store.YES, Field.Index.ANALYZED,Field.TermVector.YES));
           Iwriter.addDocument(doc);
       }
    
    	private static String findWordStem(String word) {
            final PorterStemmer stem = new PorterStemmer();
            char[] letters = word.toCharArray();
            for (int i = 0; i < letters.length; i++)
            {
                stem.add(letters[i]);
            }
            stem.stem();

            String stemmed = stem.toString();            
            return stemmed;
        }
        
    private static class DocVector {
    
        public Map<String,Integer> terms;
        public SparseRealVector vector;
    
        public DocVector(Map<String,Integer> terms) {
          this.terms = terms;
          this.vector = new OpenMapRealVector(terms.size());
        }
    
        public void setEntry(String term, int freq) {
          if (terms.containsKey(term)) {
            int pos = terms.get(term);
            vector.setEntry(pos, (double) freq);
          }
    }
    
    public void normalize() {
      double sum = vector.getL1Norm();
      vector = (SparseRealVector) vector.mapDivide(sum);
    }
    
    public String toString() {
      RealVectorFormat formatter = new RealVectorFormat();
      return formatter.format(vector);
    }
  }
}
