package ru.neodoc.content.modeller.uml2xml.helper;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;

import ru.neodoc.content.modeller.uml2xml.helper.classes.AspectHelper;
import ru.neodoc.content.modeller.uml2xml.helper.classes.AspectsHelper;
import ru.neodoc.content.modeller.uml2xml.helper.classes.MandatoryAspectHelper;
import ru.neodoc.content.modeller.uml2xml.helper.classes.MandatoryAspectsHelper;
import ru.neodoc.content.modeller.uml2xml.helper.classes.OverridesHelper;
import ru.neodoc.content.modeller.uml2xml.helper.classes.PropertiesHelper;
import ru.neodoc.content.modeller.uml2xml.helper.classes.PropertyHelper;
import ru.neodoc.content.modeller.uml2xml.helper.classes.PropertyOverrideHelper;
import ru.neodoc.content.modeller.uml2xml.helper.classes.TypeHelper;
import ru.neodoc.content.modeller.uml2xml.helper.classes.TypesHelper;
import ru.neodoc.content.modeller.uml2xml.helper.classes.associations.AssociationsHelper;
import ru.neodoc.content.modeller.uml2xml.helper.classes.associations.ChildAssociationHelper;
import ru.neodoc.content.modeller.uml2xml.helper.classes.associations.PeerAssociationHelper;
import ru.neodoc.content.modeller.uml2xml.helper.constraints.ConstraintParameterHelper;
import ru.neodoc.content.modeller.uml2xml.helper.constraints.ConstraintParametersHelper;
import ru.neodoc.content.modeller.uml2xml.helper.constraints.ModelConstraintHelper;
import ru.neodoc.content.modeller.uml2xml.helper.constraints.ModelConstraintsHelper;
import ru.neodoc.content.modeller.uml2xml.helper.constraints.PropertyConstraintHelper;
import ru.neodoc.content.modeller.uml2xml.helper.constraints.PropertyConstraintRefHelper;
import ru.neodoc.content.modeller.uml2xml.helper.constraints.PropertyConstraintsHelper;
import ru.neodoc.content.modeller.uml2xml.helper.constraints.PropertyOverrideConstraintHelper;
import ru.neodoc.content.modeller.uml2xml.helper.constraints.PropertyOverrideConstraintRefHelper;
import ru.neodoc.content.modeller.uml2xml.helper.constraints.PropertyOverrideConstraintsHelper;
import ru.neodoc.content.modeller.uml2xml.helper.model.DataTypeHelper;
import ru.neodoc.content.modeller.uml2xml.helper.model.DataTypesHelper;
import ru.neodoc.content.modeller.uml2xml.helper.model.ImportHelper;
import ru.neodoc.content.modeller.uml2xml.helper.model.ImportsHelper;
import ru.neodoc.content.modeller.uml2xml.helper.model.ModelHelper;
import ru.neodoc.content.modeller.uml2xml.helper.model.NamespaceHelper;
import ru.neodoc.content.modeller.uml2xml.helper.model.NamespacesHelper;

public class Preloader {

	public static void preload() {
		ClassLoader cl = Preloader.class.getClassLoader();
		
		Bundle bundle = ((BundleReference)cl).getBundle();
		Enumeration<URL> entries = bundle.findEntries("/ru/neodoc/content/modeller", "*", false);
		if (entries!=null)
			while(entries.hasMoreElements())
				System.out.println(entries.nextElement().toString());
		
		List<Class<?>> classes = new ArrayList<Class<?>>(Arrays.asList(
					new Class<?>[] {
						AbstractHelper.class,
						AbstractSubHelper.class,
						
						ModelHelper.class,
						
						ImportsHelper.class,
						ImportHelper.class,
						
						NamespacesHelper.class,
						NamespaceHelper.class,
						
						DataTypesHelper.class,
						DataTypeHelper.class,
						
						ModelConstraintsHelper.class,
						ModelConstraintHelper.class,
						
						TypesHelper.class,
						TypeHelper.class,
						AspectsHelper.class,
						AspectHelper.class,
						
						PropertiesHelper.class,
						PropertyHelper.class,
						PropertyConstraintsHelper.class,
						PropertyConstraintHelper.class,
						PropertyConstraintRefHelper.class,
						
						AssociationsHelper.class,
						PeerAssociationHelper.class,
						ChildAssociationHelper.class,
						
						OverridesHelper.class,
						PropertyOverrideHelper.class,
						PropertyOverrideConstraintsHelper.class,
						PropertyOverrideConstraintHelper.class,
						PropertyOverrideConstraintRefHelper.class,
						
						MandatoryAspectsHelper.class,
						MandatoryAspectHelper.class,
						
						ConstraintParametersHelper.class,
						ConstraintParameterHelper.class
					}
				)); 
		
		for (Class<?> clazz: classes)
			try {
				Class.forName(clazz.getName(), true, cl);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	}
	
}
