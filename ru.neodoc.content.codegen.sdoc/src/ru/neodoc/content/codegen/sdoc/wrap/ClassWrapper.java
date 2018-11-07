package ru.neodoc.content.codegen.sdoc.wrap;

import java.util.ArrayList;
import java.util.List;

import ru.neodoc.content.modeller.utils.uml.elements.BaseClassElement;
import ru.neodoc.content.modeller.utils.uml.elements.ChildAssociation;
import ru.neodoc.content.modeller.utils.uml.elements.MandatoryAspect;
import ru.neodoc.content.modeller.utils.uml.elements.Namespace;
import ru.neodoc.content.modeller.utils.uml.elements.PeerAssociation;
import ru.neodoc.content.modeller.utils.uml.elements.Property;

public abstract class ClassWrapper extends NamedElementWrapper {

	protected BaseClassElement classElement;
	
	protected List<PropertyWrapper> properties = new ArrayList<>();

	protected List<PeerAssociationWrapper> peerAssociations = new ArrayList<>();
	protected List<ChildAssociationWrapper> childAssociations = new ArrayList<>();
	protected List<MandatoryAspectWrapper> mandatoryAspects = new ArrayList<>();
	
	
	ClassWrapper(BaseClassElement wrappedElement) {
		super(wrappedElement);
		classElement = wrappedElement;
	}

	public BaseClassElement getClassElement(){
		return this.classElement;
	}
	
	@Override
	protected String getPrefix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKey() {
		String parentPrefix = "";
		if (this.owner != null) {
			parentPrefix = this.owner.getKey();
			if (parentPrefix == null)
				parentPrefix = "";
			parentPrefix += "$";
		}
		
		if (this.classElement != null)
			return parentPrefix + this.classElement.getName();
			
		return super.getKey();
	}
	
	public void fill(){
		for (Property property: classElement.getProperties()){
//			PropertyWrapper pw = new PropertyWrapper(property);
			PropertyWrapper pw = WrapperFactory.get(property);
			if (hasChild(pw))
				continue;
			
			if (property.getDataType() != null) {
//				DataTypeWrapper dtw = new DataTypeWrapper(property.getDataType());
				DataTypeWrapper dtw = WrapperFactory.get(property.getDataType());
				pw.setDataTypeWrapper(dtw);
				Namespace parentNamespace = property.getDataType().getNamespace();
				NamespaceWrapper nw = WrapperFactory.get(parentNamespace);
				dtw.setOwner(nw);
			}
			properties.add(pw);
			addChild(pw);
		}
			
		for (PeerAssociation pa: classElement.getPeerAssociations()) {
//			PeerAssociationWrapper pw = new PeerAssociationWrapper(pa);
			PeerAssociationWrapper pw = WrapperFactory.get(pa);
			if (hasChild(pw))
				continue;
			
			peerAssociations.add(pw);
			addChild(pw);
		}
		
		for (ChildAssociation ca: classElement.getChildAssociations()) {
//			ChildAssociationWrapper cw = new ChildAssociationWrapper(ca);
			ChildAssociationWrapper cw = WrapperFactory.get(ca);
			if (hasChild(cw))
				continue;
			
			childAssociations.add(cw);
			addChild(cw);
		}
		
		for (MandatoryAspect ma: classElement.getMandatoryAspects()) {
//			MandatoryAspectWrapper mw = new MandatoryAspectWrapper(ma);
			MandatoryAspectWrapper mw = WrapperFactory.get(ma);
			if (hasChild(mw))
				continue;
				
			mandatoryAspects.add(mw);
			addChild(mw);
		}
	}
	
}
