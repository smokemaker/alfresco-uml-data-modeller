package ru.neodoc.content.modeller.xml2uml.helper;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.model.dictionary._1.Constraint;
import org.alfresco.model.dictionary._1.NamedValue;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.UMLFactory;
import ru.neodoc.content.modeller.xml2uml.structure.ModelObject;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.AlfrescoProfileLibrary.SimpleParameter;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintCustom;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.ForConstraint.ConstraintMain;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile.Internal.ConstraintType;
import ru.neodoc.content.utils.CommonUtils;
import ru.neodoc.content.utils.PrefixedName;
import ru.neodoc.content.utils.uml.profile.descriptor.PropertyDescriptor;
import ru.neodoc.content.utils.uml.profile.descriptor.StereotypeDescriptor;
import ru.neodoc.content.utils.uml.profile.stereotype.StereotypedElement;

public abstract class AbstractConstraintHelper<ContainerType>
		extends AbstractEntitySubHelper<ContainerType, Constraint, org.eclipse.uml2.uml.Constraint> {

	@Override
	protected ModelObject<Constraint> createNewModelObject(Constraint object) {
		return complexRegistry().getObjectSmartFactory().getObject(object.getName());
	}
	
	@Override
	protected void doCustomFillModelObject(ModelObject<Constraint> modelObject,
			Constraint object) {
		super.doCustomFillModelObject(modelObject, object);
		PrefixedName pn = new PrefixedName(object.getName());
		modelObject.name = pn.getName();
		modelObject.pack = pn.getPrefix();
	}
	
	@Override
	protected org.eclipse.uml2.uml.Constraint getElement(ModelObject<Constraint> modelObject, Constraint object) {
		Namespace parentNamespace = getParentNamespaceObject(modelObject);
		if (parentNamespace==null)
			return null;
		return parentNamespace.getOwnedRule(PrefixedName.name(object.getName()));
	}
	
	@Override
	protected final org.eclipse.uml2.uml.Constraint createElement(ModelObject<Constraint> object) {
		Namespace parentNamespace = getParentNamespaceObject(object);
		if (parentNamespace==null)
			return null;
		return parentNamespace.createOwnedRule(object.name, UMLFactory.eINSTANCE.createConstraint().eClass());
	}
	
	protected abstract Namespace getParentNamespaceObject(ModelObject<Constraint> object);

	@Override
	protected boolean processElement(org.eclipse.uml2.uml.Constraint element,
			ModelObject<Constraint> object) {
		if (element.getSpecification()==null) {
			OpaqueExpression opex = UMLFactory.eINSTANCE.createOpaqueExpression();
			element.setSpecification(opex);
		}
		return true;
	}
	
	@Override
	protected boolean processStereotypedElement(StereotypedElement stereotypedElement, ModelObject<Constraint> object) {
		
		ConstraintType ct = null;
		try {
			ct = ConstraintType.valueOf(object.source.getType().toUpperCase());
		} catch (Exception e) {
			
		}
		if (ct==null)
			ct = ConstraintType.CUSTOM;
		ConstraintMain cm = ConstraintMain._FACTORY.getOrCreate(stereotypedElement, ct);
		if (cm==null)
			return false;
		cm.setConstraintType(ct);
		
		StereotypeDescriptor sd = StereotypeDescriptor.find(cm.getClass());
		if (sd==null)
			return true;
		
/*		if (ct.equals(ConstraintType.CUSTOM)) {
			
			ConstraintCustom cc = (ConstraintCustom)cm;
			cc.setClassName(object.source.getType());
			List<SimpleParameter> list = new ArrayList<>();
			if (object.source.getParameter()!=null)
				for (NamedValue nv: object.source.getParameter()) {
					SimpleParameter sp = new SimpleParameter();
					sp.setName(nv.getName());
					sp.setValue(nv.getValue());
					sp.setList(nv.getList()==null?null:nv.getList().getValue());
					list.add(sp);
				}
			cc.setParameters(list);
		} else {
*/		
		if (ConstraintType.CUSTOM.equals(ct)) {
			ConstraintCustom cc = cm.get(ConstraintCustom.class);
			if (cc!=null)
				cc.setClassName(object.source.getType());
		}
		
		List<NamedValue> unSet = new ArrayList<>();
		if (object.source.getParameter()!=null) {
			for (NamedValue nv: object.source.getParameter()) {
				PropertyDescriptor pd = sd.findPropertyDescriptor(nv.getName());
				if (pd==null) {
					unSet.add(nv);
					continue;
				}
				cm.setAttribute(pd.getName(), CommonUtils.isValueable(nv.getValue())
							?nv.getValue()
							:nv.getList()==null
								?null
								:nv.getList().getValue());
			}
			List<SimpleParameter> list = new ArrayList<>();
			for (NamedValue nv: unSet) {
				SimpleParameter sp = new SimpleParameter();
				sp.setName(nv.getName());
				sp.setValue(nv.getValue());
				sp.setList(nv.getList()==null?null:nv.getList().getValue());
				list.add(sp);
			}	
			cm.setParameters(list);
			return true;
		}
//		}
		
		return true;
	}

	public static String generateConstraintName(ModelObject<Constraint> constraint, ModelObject<?> property) {
		ConstraintType ct = ConstraintType.CUSTOM;
		try {
			ct = ConstraintType.valueOf(constraint.source.getType().toUpperCase());
		} catch (Exception e) {
			
		}
		String name = "constraint_" + property.name;
		name += "_" + ct.name().toLowerCase() + "_";
		
		boolean found = true;
		int i = 0;
		String temp = "";
		while (found) {
			temp = name + i;
			found = false;
			for (ModelObject<?> inner: property.inners) {
				if (temp.equalsIgnoreCase(inner.name)) {
					found = true;
					break;
				}
			}
			i++;
		}
		return temp;
	}
}
