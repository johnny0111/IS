/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.netbeans.xml.schema.updateschema.ObjectFactory;
import org.netbeans.xml.schema.updateschema.TMyPlace;

/**
 *
 * @author André & Ricardo
 */
public class MessageManagement {

    public static String createPlaceStateContent(TMyPlace myPlace) throws JAXBException {
        ObjectFactory myPlaceXml = new ObjectFactory();
        JAXBElement<TMyPlace> xml = myPlaceXml.createMyPlace(myPlace);
        
        JAXBContext context = JAXBContext.newInstance(TMyPlace.class);
        
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");    
        
        StringWriter writer = new StringWriter();
        marshaller.marshal(xml, writer);
        
        return writer.toString();
    }

    public static TMyPlace retrievePlaceStateObject(String content) throws JAXBException {
       JAXBContext jaxbContext;
        try{
            
        jaxbContext = JAXBContext.newInstance(TMyPlace.class);              

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        TMyPlace place = (TMyPlace) jaxbUnmarshaller.unmarshal(new StringReader(content));
        
        return place;

        }
        catch (JAXBException e) 
        {
            e.printStackTrace();
        }

                return null;
    
    }
}
