/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm;

import java.io.Reader;
import java.net.URL;

/**
 *
 * @author karthic
 */
public interface TextExtractor {
    
    public String extractTitle(String url);
        
    public String extractText(String url);
    
    public String extractText(Reader reader);
    
}
