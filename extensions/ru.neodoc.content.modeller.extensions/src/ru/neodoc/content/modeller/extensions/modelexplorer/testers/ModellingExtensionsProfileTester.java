package ru.neodoc.content.modeller.extensions.modelexplorer.testers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.extensions.ModellingExtensionsProfile;
import ru.neodoc.content.profile.alfresco.AlfrescoProfile;
import ru.neodoc.content.utils.uml.UMLUtils;
import ru.neodoc.content.utils.uml.profile.AbstractProfile;

public class ModellingExtensionsProfileTester extends PropertyTester {

	protected static final Map<String, LocalTester> testersMap = new HashMap<>();
	
	protected static abstract class LocalTester {
		protected String name;
		
		public LocalTester(String testerName) {
			super();
			setName(testerName);
			ModellingExtensionsProfileTester.testersMap.put(getName(), this);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public abstract boolean test(Package pack, Object[] args, Object expectedValue);
		
	}
	
	static {
		new LocalTester("extensionsEnabled") {
			
			@Override
			public boolean test(Package pack, Object[] args, Object expectedValue) {
				Model model = UMLUtils.getUMLRoot(pack);
				return ModellingExtensionsProfile._INSTANCE.isApplied(model);
			}
		};
		
		new LocalTester("isAlfresco") {
			@Override
			public boolean test(Package pack, Object[] args, Object expectedValue) {
				Model model = UMLUtils.getUMLRoot(pack);
				return AbstractProfile.isType(model, AlfrescoProfile.ForModel.Alfresco.class);
			}
		};
		
	}
	
	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (receiver instanceof Package) {
			Package p = (Package)receiver;
			LocalTester lt = testersMap.get(property);
			if (lt!=null)
				return lt.test(p, args, expectedValue);
		}
		return false;
	}

	
	
}
