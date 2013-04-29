package jwtm.clusteringengine.model.stemmer;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.*;
import java.io.Reader;
import java.util.Set;
import org.apache.lucene.util.Version;

public class PorterAnalyzer extends Analyzer{
	
	private Set stopWords;
	
	public PorterAnalyzer(){
		stopWords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
	}
	
	public TokenStream tokenStream(String fieldName, Reader reader){
                TokenStream result = new StandardTokenizer(Version.LUCENE_30,reader);   
                result = new StopFilter(true, result, stopWords);
                return result;
		//return new PorterStemFilter(new StandardFilter(new StopFilter(new LowerCaseFilter(new StandardTokenizer(reader)),stopWords)));
	}

}
