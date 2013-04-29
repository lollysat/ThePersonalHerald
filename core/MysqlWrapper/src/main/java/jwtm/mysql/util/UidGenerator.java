/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jwtm.mysql.util;
import java.util.UUID;

/**
 *
 * @author kmadanagopal
 */
public class UidGenerator {
    public static final String generateUID(){    
    UUID uid = UUID.randomUUID();    
    return uid.toString();
  }
}
