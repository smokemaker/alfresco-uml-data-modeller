package ru.neodoc.content.modeller.xml2uml.helper;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;

import ru.neodoc.content.modeller.xml2uml.helper.entity.AspectHelper;
import ru.neodoc.content.modeller.xml2uml.helper.entity.ClassHelper;
import ru.neodoc.content.modeller.xml2uml.helper.entity.DataTypeHelper;
import ru.neodoc.content.modeller.xml2uml.helper.entity.ModelConstraintHelper;
import ru.neodoc.content.modeller.xml2uml.helper.entity.ModelHelper;
import ru.neodoc.content.modeller.xml2uml.helper.entity.NamespaceSubHelper;
import ru.neodoc.content.modeller.xml2uml.helper.entity.PropertyConstraintHelper;
import ru.neodoc.content.modeller.xml2uml.helper.entity.PropertyHelper;
import ru.neodoc.content.modeller.xml2uml.helper.entity.PropertyOverrideConstraintHelper;
import ru.neodoc.content.modeller.xml2uml.helper.entity.PropertyOverrideHelper;
import ru.neodoc.content.modeller.xml2uml.helper.entity.TypeHelper;
import ru.neodoc.content.modeller.xml2uml.helper.relation.AbstractAssociationHelper;
import ru.neodoc.content.modeller.xml2uml.helper.relation.ChildAssociationHelper;
import ru.neodoc.content.modeller.xml2uml.helper.relation.ImportSubHelper;
import ru.neodoc.content.modeller.xml2uml.helper.relation.MandatoryAspectHelper;
import ru.neodoc.content.modeller.xml2uml.helper.relation.ParentHelper;
import ru.neodoc.content.modeller.xml2uml.helper.relation.PeerAccociationHelper;
import ru.neodoc.content.modeller.xml2uml.helper.relation.PropertyConstraintRefHelper;
import ru.neodoc.content.modeller.xml2uml.helper.relation.PropertyOverrideConstraintRefHelper;

public class Preloader {
	
	public static void preload() {
		ClassLoader cl = Preloader.class.getClassLoader();
		
		Bundle bundle = ((BundleReference)cl).getBundle();
		Enumeration<URL> entries = bundle.findEntries("/ru/neodoc/content/modeller", "*", false);
		if (entries!=null)
			while(entries.hasMoreElements())
				System.out.println(entries.nextElement().toString());
		
		List<Class<?>> classes = new ArrayList<>(Arrays.asList(
					new Class<?>[] {
						AbstractHelper.class,
						AbstractSubHelper.class,
						
						AbstractAssociationHelper.class,
						AbstractConstraintHelper.class,
						AbstractEntitySubHelper.class,
						AbstractRelationSubHelper.class,
						AbstractSubHelperWithProxy.class,
						AspectHelper.class,
						ChildAssociationHelper.class,
						ClassHelper.class,
						DataTypeHelper.class,
						ImportSubHelper.class,
						MandatoryAspectHelper.class,
						ModelConstraintHelper.class,
						ModelHelper.class,
						NamespaceSubHelper.class,
						ParentHelper.class,
						PeerAccociationHelper.class,
						PropertyHelper.class,
						PropertyConstraintHelper.class,
						PropertyConstraintRefHelper.class,
						PropertyOverrideHelper.class,
						PropertyOverrideConstraintHelper.class,
						PropertyOverrideConstraintRefHelper.class,
						SubHelper.class,
						TypeHelper.class
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
