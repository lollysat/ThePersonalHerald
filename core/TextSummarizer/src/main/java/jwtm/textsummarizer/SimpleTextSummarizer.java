/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.textsummarizer;

import net.sf.classifier4J.summariser.SimpleSummariser;

/**
 *
 * @author kmadanagopal
 */
public class SimpleTextSummarizer {
    public String summarize(String text)
    {
        SimpleSummariser summariser = new SimpleSummariser();
        return summariser.summarise(text, 5);
    }
}
