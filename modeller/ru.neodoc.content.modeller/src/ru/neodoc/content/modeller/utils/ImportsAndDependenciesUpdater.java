package ru.neodoc.content.modeller.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForClass.ClassMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForGeneralization.Inherit;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Model;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackage.Namespace;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPackageImport.ImportNamespace;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public class ImportsAndDependenciesUpdater implements IRunnableWithProgress {
	
	protected Package umlRoot;
	protected Package packageToUpdate;
	
	public ImportsAndDependenciesUpdater(EObject packToUpdate){
		super();
		if (packToUpdate instanceof Package) {
			this.packageToUpdate = (Package)packToUpdate;
			umlRoot = AlfrescoUMLUtils.getUMLRoot(packageToUpdate);
		}
	}
	
	protected IProgressMonitor rootMonitor;
	
	@Override
	public void run(IProgressMonitor mon) throws InvocationTargetException,
			InterruptedException {
		this.rootMonitor = mon;
		StereotypedElement se = AbstractProfile.asUntyped(packageToUpdate);
		if (se.has(AlfrescoProfile.ForModel.Alfresco.class))
			runAlfresco(rootMonitor, packageToUpdate);
		else if (se.has(AlfrescoProfile.ForPackage.Model.class))
			runModel(rootMonitor, packageToUpdate);
		else if (se.has(AlfrescoProfile.ForPackage.Namespace.class))
			runNamespace(rootMonitor, packageToUpdate);
	}

	protected void runAlfresco(IProgressMonitor monitor, Package alfresco){
		List<Package> models = AlfrescoUMLUtils.getModels(alfresco);
		monitor.beginTask("Updating imports and dependencies", models.size());
		
		for (Package model: models) {
			SubMonitor sm = SubMonitor.convert(monitor, 1);
			runModel(sm, model);
			monitor.worked(1);
		}
		
		monitor.done();
	}
	
	protected void runModel(IProgressMonitor monitor, Package model){
		List<Package> namespaces = AlfrescoUMLUtils.getNamespaces(model);
		monitor.beginTask("Processing model " + model.getName(), namespaces.size() + 2);
		monitor.subTask("Collecting imports and dependencies");
		for (Package namespace: namespaces) {
			SubMonitor sm = SubMonitor.convert(monitor, 1);
			runNamespace(sm, namespace);
			monitor.worked(1);
		}
		
		monitor.subTask("Updating imports");
		updateModelImports(model);
		monitor.worked(1);

		monitor.subTask("Updating dependencies");
		updateModelDependencies(model);
		monitor.worked(1);

		monitor.done();
	}
	
	protected void updateModelImports(Package model) {
		
//		ContentModellerPlugin.getDefault().log("updateModelImports: " + model.getName());
		
		List<Package> namespaces = AlfrescoUMLUtils.getNamespaces(model);

//		ContentModellerPlugin.getDefault().log("namespaces size: " + namespaces.size());
		
		Set<Package> namespacesToImport = new HashSet<Package>();
		Model theModel = Model._HELPER.getFor(model);
		
		for (Package namespace: namespaces) {
//			ContentModellerPlugin.getDefault().log("process package: " + namespace.getName());
			for (Dependency dep: namespace.getClientDependencies()) {
				for (NamedElement ne: dep.getSuppliers())
					if (Namespace._HELPER.is(ne) 
							&& !theModel.equals(Model._HELPER.findNearestFor(ne))) {
//						ContentModellerPlugin.getDefault().log("adding package: " + ne.getName());
						namespacesToImport.add((Package)ne);
					}
			}
		}

		Set<PackageImport> importsToRemove = new HashSet<PackageImport>();
		
		for (PackageImport pi: model.getPackageImports()) {
			Package ip = pi.getImportedPackage();
			if (Namespace._HELPER.is(ip)){
				if (namespacesToImport.contains(ip))
					namespacesToImport.remove(ip);
				else
					importsToRemove.add(pi);
			}	
		}
		
		for (Package p: namespacesToImport) {
//			ContentModellerPlugin.getDefault().log("importing namespace: " + p.getName());
			try {
/*				ContentModellerPlugin.getDefault().log(">>> namespace: " + 
						(Namespace._HELPER.getFor(p)==null?"null":Namespace._HELPER.getFor(p).getClass().getName()));
				ContentModellerPlugin.getDefault().log(">>> model: " + 
						(theModel==null?"null":theModel.getClass().getName()));
*/				ImportNamespace in = theModel.importNamespace(Namespace._HELPER.getFor(p));
/*				ContentModellerPlugin.getDefault().log(">>> result: " + 
						(in==null?"null":in.getClass().getName()));
*/			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		for (PackageImport pi: importsToRemove)
			pi.destroy();
		
//		ContentModellerPlugin.getDefault().log("updateModelImports FINISHED");
	}
	
	protected void updateModelDependencies(Package model){
		Set<Package> collectedDependencies = new HashSet<Package>();
		Set<Dependency> dependenciesToRemove = new HashSet<Dependency>();
		Set<PackageImport> importsToRemove = new HashSet<>();
		
		for (PackageImport pi: model.getPackageImports()) {
			Package p = pi.getImportedPackage();
			
			if (p == null) {
				importsToRemove.add(pi);
				continue;
			}
			
			Package m = AlfrescoUMLUtils.getNearestModel(p);
			if (m!=null)
				collectedDependencies.add(m);
		}
		
		for (Dependency d: model.getClientDependencies()){
			NamedElement ne = d.getSuppliers().get(0);
			if (!AlfrescoUMLUtils.isModel(ne))
				continue;
			
			Package supplierModel = (Package)ne;
			
			if (collectedDependencies.contains(supplierModel))
				collectedDependencies.remove(supplierModel);
			else
				dependenciesToRemove.add(d);
		}
		
		for (Package p: collectedDependencies)
			AlfrescoUMLUtils.dependentModel(model, p);
		
		for (Dependency d: dependenciesToRemove)
			d.destroy();
		
		for (PackageImport pi: importsToRemove)
			pi.destroy();
			
	}
	
	protected void runNamespace(IProgressMonitor monitor, Package namespace){
		runNamespace(monitor, AlfrescoProfile.ForPackage.Namespace._HELPER.getFor(namespace));
	}
	
	protected void runNamespace(IProgressMonitor monitor, Namespace namespace){
		if (namespace==null)
			return;
		List<ClassMain> classes = namespace.getAllClasses();
		monitor.beginTask("Processing namespace " + (namespace.<NamedElement>getElement()).getName(), classes.size() + 1);
		
		
		Set<Namespace> dependenciesPackages = new HashSet<Namespace>();
		
		String objectType = "";
		for (ClassMain cl: classes) {
			if (cl.has(AlfrescoProfile.ForClass.Type.class))
				objectType = "type";
			else if (cl.has(AlfrescoProfile.ForClass.Aspect.class))
				objectType = "aspect";
			else {
				monitor.worked(1);
				continue;
			}
			monitor.subTask("Processing " + objectType + ": " + cl.<NamedElement>getElement().getName());
			Set<Namespace> classDependencies = collectDependenciesForClass(cl);
			dependenciesPackages.addAll(classDependencies);
/*			for (Namespace ns: classDependencies)
				dependenciesPackages.add(ns);
*/			
			monitor.worked(1);
		}

		monitor.subTask("Updating dependencies");
		updateNamespaceDependencies(namespace, dependenciesPackages);
		monitor.worked(1);
		
		monitor.done();
		
	}

	protected Set<Namespace> collectDependenciesForClass(ClassMain classMain){
		Set<Namespace> result = new LinkedHashSet<Namespace>();

		Class classElement = classMain.getElementClassified();
		Namespace namespace = Namespace._HELPER.findNearestFor(classElement);
		if (namespace==null)
			return result;
		for (ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForProperty.Property p: classMain.getAllProperties()){
			ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForPrimitiveType.DataType t = p.getDataType();
			if (t==null)
				continue;
			Namespace ns = AlfrescoProfile.ForPackage.Namespace._HELPER.findNearestFor(t);
			if (ns!=null  && !ns.equals(namespace))
				result.add(ns);
			
			for (AlfrescoProfile.ForDependency.Constrainted constrainted: p.getConstraintRefs()) {
				AlfrescoProfile.ForConstraint.ConstraintMain constraintMain = constrainted.getConstraint();
				ns = AlfrescoProfile.ForPackage.Namespace._HELPER.findNearestFor(constraintMain);
				if (ns!=null  && !ns.equals(namespace))
					result.add(ns);
			}
				
		}
		
		for (AlfrescoProfile.ForDependency.PropertyOverride propertyOverride: classMain.getAllPropertyOverrides()) {
			AlfrescoProfile.ForProperty.Property overridenProperty = propertyOverride.getOverridenProperty();
			Namespace ns = AlfrescoProfile.ForPackage.Namespace._HELPER.findNearestFor(overridenProperty);
			if (ns!=null  && !ns.equals(namespace))
				result.add(ns);
			
			for (AlfrescoProfile.ForDependency.Constrainted constrainted: propertyOverride.getConstraintRefs()) {
				AlfrescoProfile.ForConstraint.ConstraintMain constraintMain = constrainted.getConstraint();
				ns = AlfrescoProfile.ForPackage.Namespace._HELPER.findNearestFor(constraintMain);
				if (ns!=null  && !ns.equals(namespace))
					result.add(ns);
			}
		}
		
		for (Inherit inherit: classMain.getInherits()) {
			if (inherit.getSpecific()==null || inherit.getSpecific().getElementClassified() != classElement)
				continue;
			Namespace ns = Namespace._HELPER.findNearestFor(inherit.getGeneral());
			if (ns!=null && !ns.equals(namespace))
				result.add(ns);
		}
		
		for (Association a: classElement.getAssociations()){
			// FIXME надо решить, игнорировать ли ассоциации, которые
			// физически находятся вне обрабатываемого пакета (неймспейса)
			// пока такие ассоциации игнорируются
			// FIXME переделать на работу с интерфейсами профиля
			// Package ns = AlfrescoUMLUtils.getNearestNamespace(a);
			Namespace ns = Namespace._HELPER.findNearestFor(a);
			if (!ns.equals(namespace))
				continue;
			
			for (Type t: a.getEndTypes()) {
				Namespace ns1 = Namespace._HELPER.findNearestFor(t);
				if (ns1!=null && !ns1.equals(namespace))
					result.add(ns1);
			}
		}
		return result;
	}

	protected Set<Package> collectDependenciesForClass(Class classElement){
		Set<Package> result = new LinkedHashSet<Package>();

		Namespace namespace = Namespace._HELPER.findNearestFor(classElement);
		if (namespace==null)
			return result;
		
		for (Property p: classElement.getAllAttributes()){
			Type t = p.getType();
			if (!(t instanceof DataType))
				continue;
			Package ns = AlfrescoUMLUtils.getNearestNamespace(t);
			if (ns!=null  && ns!=namespace)
				result.add(ns);
		}
		
		for (Generalization g: classElement.getGeneralizations()) {
			if (g.getSpecific() != classElement)
				continue;
			Package ns = AlfrescoUMLUtils.getNearestNamespace(g.getGeneral());
			if (ns!=null && ns!=namespace)
				result.add(ns);
		}
		
		for (Association a: classElement.getAssociations()){
			// FIXME надо решить, игнорировать ли ассоциации, которые
			// физически находятся вне обрабатываемого пакета (неймспейса)
			// пока такие ассоциации игнорируются
			Package ns = AlfrescoUMLUtils.getNearestNamespace(a);
			if (ns!=namespace)
				continue;
			
			for (Type t: a.getEndTypes()) {
				Package ns1 = AlfrescoUMLUtils.getNearestNamespace(t);
				if (ns1!=null && ns1!=namespace)
					result.add(ns1);
			}
		}
		
		return result;
	}
	
	protected void updateNamespaceDependencies(Namespace namespace, Set<Namespace> collectedDependencies){
		List<Dependency> dependenciesToRemove = new ArrayList<Dependency>();
		for (Dependency d: namespace.getElementClassified().getClientDependencies()){
			boolean remove = false;
			Namespace ns = null;
			for (NamedElement ne: d.getSuppliers()){
				ns = Namespace._HELPER.findNearestFor(ne);
				if (ns==null)
					continue;
				
				if (collectedDependencies.contains(ns))
					collectedDependencies.remove(ns);
				else
					remove = true;
			}
			if (remove)
				dependenciesToRemove.add(d);
		}
		for (Dependency d: dependenciesToRemove)
			d.destroy();
		for (Namespace p: collectedDependencies)
			namespace.dependent(p);
//			AlfrescoUMLUtils.dependentPackage(namespace, p);
	}
	
}
