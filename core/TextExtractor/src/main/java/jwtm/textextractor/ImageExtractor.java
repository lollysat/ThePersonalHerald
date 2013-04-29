/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.textextractor;

import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.apache.html.dom.HTMLDocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.tidy.Tidy;
/**
 *
 * @author kmadanagopal
 */
public class ImageExtractor {
    private Tidy tidy;
    public ImageExtractor()
    {
        tidy = new Tidy();                       
        tidy.setErrout(new PrintWriter(new StringWriter()));
    }
    
    public String extractImage(String url) {
        try
        {        
            InputStream input = new URL(url).openStream();            
            Document document = tidy.parseDOM(input, null);
            NodeList imgs = document.getElementsByTagName("img");
            for (int i = 0; i < imgs.getLength(); i++) {
                String img = imgs.item(i).getAttributes().getNamedItem("src").getNodeValue(); 
//                System.out.println(img);
                if(!img.contains("logo"))
                    return img;
        }     
            return "";
        }catch(Exception ex)
        {
            return "";
        }
    }
    
    public static void main(String args[])
    {
        ImageExtractor im = new ImageExtractor();
        im.extractImage("http://techcrunch.com/2013/04/25/baidus-1q-earnings-of-328-9m-misses-analysts-estimates-as-its-rd-costs-soar/");
    }
}
