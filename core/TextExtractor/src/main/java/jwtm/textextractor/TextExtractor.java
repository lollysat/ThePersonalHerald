/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.textextractor;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author karthic
 */
public class TextExtractor implements jwtm.TextExtractor{

    public String extractTitle(String url) {
        try {
            return TitleExtractor.getPageTitle(url);
        } catch (IOException ex) {
            Logger.getLogger(TextExtractor.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String extractText(Reader reader) {
        
        // NOTE: Use ArticleExtractor unless DefaultExtractor gives better results for you
        String text = "";
        try {            
                text = ArticleExtractor.INSTANCE.getText(reader);            
        } catch (BoilerpipeProcessingException ex) {
            Logger.getLogger(TextExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return text;
    }
    
    public String extractText(String urlString) {
        
        // NOTE: Use ArticleExtractor unless DefaultExtractor gives better results for you
        String text = "";
        try {
                URL url = new URL(urlString);
                text = ArticleExtractor.INSTANCE.getText(url);            
        } catch (Exception ex) {
            Logger.getLogger(TextExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return text;
    }
    
    public static void main(String args[]) throws MalformedURLException, IOException
    {
        TextExtractor extractor = new TextExtractor();
        System.out.println(extractor.extractText("http://online.wsj.com/article/SB10001424127887324474004578446683056009630.html?mod=WSJ_fc_LEFTTopStories"));
        
        
        System.out.println(TitleExtractor.getPageTitle("http://t.co/c719COmJ"));
    }
}
