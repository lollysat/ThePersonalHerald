/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.integrationtest;

import jwtm.textextractor.Extractor;

/**
 *
 * @author kmadanagopal
 */
public class ExtractorTest {
    
    public static void main(String args[])
    {
        Extractor extractor = new Extractor("localhost", "3307", "PersonalHerald", "root", "helloworld2");
        extractor.extract();      
    }        
}
