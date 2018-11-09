package org.alfresco.model.dictionary._1;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ru.neodoc.content.utils.PrefixedName;

public class AlfrescoModelUtils {

	public static PrefixedName getName(Object object) {

		if (object==null)
			return new PrefixedName("");
		
		if (object instanceof NamespaceDefinition) {
			/*
			 Namespace
			 Import 
			 */
			return new PrefixedName(((NamespaceDefinition)object).getPrefix());
		}
		
		if (object instanceof Name) {
			/*
			Association (ChildAssociation)
			Class(Aspect, Type)
			Model
			NamedValue
			Property
			PropertyOverride
			 */
			return new PrefixedName(((Name)object).getName());
		}
		try {
			if (object.getClass().getDeclaredMethod("getName")!=null) {
				/*
				Constraint
				 */
				Method method = object.getClass().getDeclaredMethod("getName");
				Object result = method.invoke(object);
				if (result != null)
					return new PrefixedName(result.toString());
			}
		} catch (Exception e) {
			
		}
		
		return new PrefixedName("");
	}
	
	public static List<PrefixedName> getHierarchyNames(List<Object> hierarchy) {
		List<PrefixedName> result = new ArrayList<>();
		for (Object object: hierarchy)
			result.add(getName(object));
		return result;
	}
	
}
