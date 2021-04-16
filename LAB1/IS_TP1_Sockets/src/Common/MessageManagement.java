package Common;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.netbeans.xml.schema.updateschema.ObjectFactory;
import org.netbeans.xml.schema.updateschema.TMyPlace;

/**
 *
 * @author André & Ricardo
 */
public class MessageManagement {

    public static String createPlaceStateContent(TMyPlace myPlace) throws JAXBException {
        //TODO Lab 2:
        //Serealize TMyPlace object to String using JAXB
        ObjectFactory myPlaceXml = new ObjectFactory();
        JAXBElement<TMyPlace> xml = myPlaceXml.createMyPlace(myPlace);
        
      
        JAXBContext context = JAXBContext.newInstance(TMyPlace.class);
        
        Marshaller marshaller = context.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
//        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");    
        
        StringWriter writer = new StringWriter();
        marshaller.marshal(xml, writer);
        
        return writer.toString();
    }

    public static TMyPlace retrievePlaceStateObject(String content) throws JAXBException {
            //TODO Lab 2:
            //Deserealize a String to a TMyPlace object using JAXB
            JAXBContext jaxbContext;
           
            
            try{

            //jaxbContext = JAXBContext.newInstance(TMyPlace.class);  
            jaxbContext = JAXBContext.newInstance(TMyPlace.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            StreamSource streamSource = new StreamSource(new StringReader(content));

            JAXBElement<TMyPlace> place = jaxbUnmarshaller.unmarshal(streamSource, TMyPlace.class);

            //JAXBElement<TMyPlace> place = (JAXBElement<TMyPlace>)jaxbUnmarshaller.unmarshal(new StringReader(content));
            TMyPlace myPlace = (TMyPlace) place.getValue();

            return myPlace;

            }
            catch (JAXBException e) {
                return null;
            }
    }
}