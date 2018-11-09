package ru.neodoc.content.modeller.utils;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import ru.neodoc.jaxb.utils.CData;

/**
 * Implementation which is able to decide to use a CDATA section for a string.
 */
public class CDataXMLStreamWriter extends DelegatingXMLStreamWriter
{
   public CDataXMLStreamWriter( XMLStreamWriter del )
   {
      super( del );
   }

   @Override
   public void writeCharacters( String text ) throws XMLStreamException
   {
      CData cData = CData.create(text);
	   boolean useCData = cData.isCDataNeeded() || CData.is(text);
      if( useCData )
      {
         super.writeCData( cData.getContent() );
      }
      else
      {
         super.writeCharacters( text );
      }
   }
}