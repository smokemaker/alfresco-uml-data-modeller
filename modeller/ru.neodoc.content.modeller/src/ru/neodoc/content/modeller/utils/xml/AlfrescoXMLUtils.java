package ru.neodoc.content.modeller.utils.xml;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.alfresco.model.dictionary._1.Model;
import org.alfresco.model.dictionary._1.ObjectFactory;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.profile.alfresco.AlfrescoProfile;

public class AlfrescoXMLUtils {
	
	public static ObjectFactory of = new ObjectFactory(); 
	
	public static Model emptyModel(){
		return of.createModel();
	}
	
	public static Model emptyModel(Package model) {
		Model result = emptyModel();
		return fillModel(model, result);
	}
	
	public static Model fillModel(Package model, Model xmlModel) {
		Model result = xmlModel;
		@SuppressWarnings("static-access")
		AlfrescoProfile.ForPackage.Model umlModel = AlfrescoProfile.asUntyped(model).get(AlfrescoProfile.ForPackage.Model.class);
		
		if (umlModel!=null) {

			result.setName(model.getName());
			result.setAuthor(umlModel.getAuthor());
			result.setDescription(umlModel.getDesription());
			
			String published = umlModel.getPublished();
			if (published != null) {
				try {
					Date dob=null;
					try {
						DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						dob=df.parse(published);
					} catch (ParseException pe) {
						DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
						dob=df.parse(published);
					}
					GregorianCalendar cal = new GregorianCalendar();
					cal.setTime(dob);
					XMLGregorianCalendar xgc = DatatypeFactory.newInstance()
							.newXMLGregorianCalendarDate(cal.get(Calendar.YEAR), 
										cal.get(Calendar.MONTH)+1, 
										cal.get(Calendar.DAY_OF_MONTH), 
										0);
					result.setPublished(xgc);
				} catch (Exception e) {
					// set "published" to now
					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(new Date());
					try {
						XMLGregorianCalendar xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
						result.setPublished(xgc);
					} catch (Exception e1) {
						// NOOP
					}
				}
			}
			
			result.setVersion(umlModel.getVersion());
		}
		return result;
	}
	
	
}
