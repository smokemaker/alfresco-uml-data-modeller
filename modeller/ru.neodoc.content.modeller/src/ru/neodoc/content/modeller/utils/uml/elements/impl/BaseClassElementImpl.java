package ru.neodoc.content.modeller.utils.uml.elements.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Type;

import ru.neodoc.content.modeller.utils.uml.AlfrescoUMLUtils;
import ru.neodoc.content.modeller.utils.uml.elements.Aspect;
import ru.neodoc.content.modeller.utils.uml.elements.BaseClassElement;
import ru.neodoc.content.modeller.utils.uml.elements.BaseElement;
import ru.neodoc.content.modeller.utils.uml.elements.ChildAssociation;
import ru.neodoc.content.modeller.utils.uml.elements.DataTypeElement;
import ru.neodoc.content.modeller.utils.uml.elements.ElementFactory;
import ru.neodoc.content.modeller.utils.uml.elements.MandatoryAspect;
import ru.neodoc.content.modeller.utils.uml.elements.Namespace;
import ru.neodoc.content.modeller.utils.uml.elements.PeerAssociation;
import ru.neodoc.content.modeller.utils.uml.elements.Property;

public class BaseClassElementImpl extends BaseTypeElementImpl implements BaseClassElement {

	protected Namespace parentNamespace;

	protected BaseClassElement parent = null;
	
	protected Class classElement;
	
	protected List<Property> properties;
	
	protected List<PeerAssociation> peerAssociations;
	protected List<ChildAssociation> childAssociations;
	protected List<MandatoryAspect> mandatoryAspects;
	
	protected boolean isContentInited = false;
	
	public BaseClassElementImpl() {
		super();
	}

	public BaseClassElementImpl(Element element) {
		super(element);
	}

	public BaseClassElementImpl(EObject eObject) {
		super(eObject);
	}

	@Override
	protected void init() {
		super.init();
		if (super.isValid())
			if (typeElement instanceof Class)
				classElement = (Class) typeElement;
	}
	
	@Override
	public boolean isValid() {
		return super.isValid() && (this.classElement != null);
	}
	
	protected void initContent(){
		// init properties
		properties = new ArrayList<>();
		EList<org.eclipse.uml2.uml.Property> props = classElement.getOwnedAttributes();
		
		for (org.eclipse.uml2.uml.Property prop: props) {
			PropertyImpl property = new PropertyImpl(prop);
			if (property.isValid())
				properties.add(property);
		}
		
		// init associations
		peerAssociations = new ArrayList<>();
		childAssociations = new ArrayList<>();
		mandatoryAspects = new ArrayList<>();
		
		EList<Association> associations = classElement.getAssociations();
		List<Association> alreadySeen = new ArrayList<>();
		
		for (Association association: associations) {
		
			if (alreadySeen.contains(association))
				continue;
			
			if (AlfrescoUMLUtils.isPeerAssociation(association)) {
				// we collect only outgoing association
				if (association.getEndTypes().get(1) != this.typeElement)
					continue;
				
				PeerAssociationImpl pa = new PeerAssociationImpl(association, this);
				if (pa.isValid())
					peerAssociations.add(pa);
				continue;
			}
			
			if (AlfrescoUMLUtils.isChildAssociation(association)) {
				// we collect only "parent" of child association
				if (!AlfrescoUMLUtils.isParentOfChildAssociation(association, this.typeElement))
					continue;
				
				ChildAssociationImpl ca = new ChildAssociationImpl(association, this);
				if (ca.isValid())
					childAssociations.add(ca);
				continue;
			}
			
			if (AlfrescoUMLUtils.isMandatoryAspect(association)) {
				// we collect only owners of mandatory aspects
				if (!AlfrescoUMLUtils.isOwnerOfMandatoryAspect(association, this.typeElement))
					continue;
				
				MandatoryAspectImpl ma = new MandatoryAspectImpl(association, this);
				if (ma.isValid())
					mandatoryAspects.add(ma);
			}
		}
		
		isContentInited = true;
	}

	@Override
	public BaseClassElement getParent() {
		if (parent==null)
			defineParent();
		return parent;
	}
	
	protected void defineParent(){
		if(!isValid())
			return;
		Generalization parentGeneralization = null;
		for(Generalization g: classElement.getGeneralizations())
			if (g.getGeneral() instanceof org.eclipse.uml2.uml.Class) {
				if (AlfrescoUMLUtils.isType(g.getGeneral())){
					parentGeneralization = g;
					break;
				}
			}
		if (parentGeneralization==null)
			return;
		org.eclipse.uml2.uml.Class parentType = (org.eclipse.uml2.uml.Class)parentGeneralization.getGeneral();
		// parent = new BaseClassElementImpl(parentType);
		parent = (BaseClassElement)ElementFactory.createElement(parentType);
		if (parent.isValid())
			return;
		parent = null;
	}	
	
	@Override
	public Namespace getNamespace() {
		if (parentNamespace == null) {
			Package p = AlfrescoUMLUtils.getNearestNamespace(classElement);
			if (p != null)
				parentNamespace = ElementFactory.createNamespaceElement(p);
		}
		return parentNamespace;
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.elements.BaseClassElement#getProperties()
	 */
	@Override
	public List<Property> getProperties(){
		if (!isContentInited)
			initContent();
		
		return properties; 
	}
	
	@Override
	public List<PeerAssociation> getPeerAssociations() {
		if (!isContentInited)
			initContent();
		
		return peerAssociations;
	}
	
	@Override
	public List<ChildAssociation> getChildAssociations() {
		if (!isContentInited)
			initContent();
		
		return childAssociations;
	}
	
	@Override
	public List<MandatoryAspect> getMandatoryAspects() {
		if (!isContentInited)
			initContent();
		
		return mandatoryAspects;
	}


	@Override
	public List<Aspect> getRequiredAspects() {
		// TODO Auto-generated method stub
		return getRequiredBaseClassElements(Aspect.class);
	}
	
	@Override
	public List<ru.neodoc.content.modeller.utils.uml.elements.Type> getRequiredTypes() {
		return getRequiredBaseClassElements(ru.neodoc.content.modeller.utils.uml.elements.Type.class);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends BaseClassElement> List<T> getRequiredBaseClassElements(java.lang.Class<?> clazz){
		List<T> result = new ArrayList<>();

		for (Generalization g: classElement.getGeneralizations()){
			if (g.getSpecific() != classElement)
				continue;
			BaseElement bei = ElementFactory.createElement(g.getGeneral());
			if (!BaseClassElement.class.isAssignableFrom(bei.getClass()))
				continue;
			if (clazz!=null)
				if (!clazz.isAssignableFrom(bei.getClass()))
					continue;
			result.add((T)bei);
		}
		
		for (Association a: classElement.getAssociations()){
			
			for (Type t: a.getEndTypes()) {
				if (t == this.typeElement)
					continue;
				BaseElement bei = ElementFactory.createElement(t);
				if (clazz!=null)
					if (!clazz.isAssignableFrom(bei.getClass()))
						continue;
				result.add((T)bei);
			}
		}
		
		
		return result;
	}
	
	@Override
	public List<DataTypeElement> getRequiredDataTypes() {
		List<DataTypeElement> result = new ArrayList<>();
		for (Property prop: getProperties()) {
			Type type = ((org.eclipse.uml2.uml.Property)prop.getElement()).getType();
			if (!(type instanceof DataType))
				continue;
			DataTypeElement dte = new DataTypeElementImpl(type);
			if (dte.isValid())
				result.add(dte);
		}
		return result;
	}
	
	@Override
	public List<Namespace> getRequiredNamespaces() {
		List<Namespace> result = new ArrayList<>();
		
		for (Property prop: getProperties()) {
			Type type = ((org.eclipse.uml2.uml.Property)prop.getElement()).getType();
			if (!(type instanceof DataType))
				continue;
			addNamespace(AlfrescoUMLUtils.getNearestNamespace(type), result);
		}
		
		for (Generalization g: classElement.getGeneralizations()){
			if (g.getSpecific() != classElement)
				continue;
			addNamespace(AlfrescoUMLUtils.getNearestNamespace(g.getGeneral()), result);
		}
		
		for (Association a: classElement.getAssociations()){
			
			for (Type t: a.getEndTypes()) {
				addNamespace(AlfrescoUMLUtils.getNearestNamespace(t), result);
			}
		}
		
		return result;
	}
	
	private void addNamespace(Package p, List<Namespace> list) {
		if (p!=null)
			list.add(new NamespaceImpl(p));
	}
}
