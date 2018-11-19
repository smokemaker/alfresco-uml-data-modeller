package ru.neodoc.content.modeller.extensionpoints;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.papyrus.uml.extensionpoints.library.IRegisteredLibrary;
import org.eclipse.papyrus.uml.extensionpoints.library.RegisteredLibrary;
import org.eclipse.swt.graphics.Image;

import ru.neodoc.content.utils.CommonUtils;

public class RegisteredModel implements IRegisteredModel {

	protected IRegisteredLibrary registeredLibrary = new IRegisteredLibrary() {
		
		@Override
		public URI getUri() {
			return null;
		}
		
		@Override
		public String getProvider() {
			return null;
		}
		
		@Override
		public String getPath() {
			return null;
		}
		
		@Override
		public String getName() {
			return null;
		}
		
		@Override
		public Image getImage() {
			return null;
		}
		
		@Override
		public String getDescription() {
			return null;
		}
	};
	
	protected boolean isValid = false;
	
	public RegisteredModel(IConfigurationElement configElement) {
		String extensionPointType = configElement.getAttribute("extensionPointType");
		if (!"org.eclipse.papyrus.uml.extensionpoints.UMLLibrary".equals(extensionPointType))
			return;
		String wrappedExtensionId = configElement.getAttribute("wrappedExtensionId");
		if (!CommonUtils.isValueable(wrappedExtensionId))
			return;
		
		IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(extensionPointType).getExtensions();
		IExtension extension = null;
		for (int i = 0; i < extensions.length; i++) {
			IExtension iExtension = extensions[i];
			if (wrappedExtensionId.equals(iExtension.getUniqueIdentifier())) {
				extension = iExtension;
				break;
			}
		}
		
		if (extension==null)
			return;
		
		IConfigurationElement[] configElements = extension.getConfigurationElements();
		if ((configElements==null) || (configElements.length==0))
			return;
		IConfigurationElement libraryElement = configElements[0];
		this.registeredLibrary = new RegisteredLibrary(libraryElement, 0);
		this.isValid = true;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	@Override
	public String getName() {
		return this.registeredLibrary.getName();
	}

	@Override
	public URI getUri() {
		return this.registeredLibrary.getUri();
	}

	@Override
	public String getPath() {
		return this.registeredLibrary.getPath();
	}

	@Override
	public String getProvider() {
		return this.registeredLibrary.getProvider();
	}

	@Override
	public String getDescription() {
		return this.registeredLibrary.getDescription();
	}

	@Override
	public Image getImage() {
		return this.registeredLibrary.getImage();
	}

}
