package ru.neodoc.content.modeller.utils.uml.elements.impl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;

import ru.neodoc.content.modeller.utils.uml.elements.BasePackageElement;

public abstract class BasePackageElementImpl extends BaseNamedElementImpl implements BasePackageElement {
	
	protected Package packageElement;
	
	public BasePackageElementImpl(){
		super();
	}
	
	public BasePackageElementImpl(Element element){
		super(element);
	}
	
	public BasePackageElementImpl(EObject eObject){
		super(eObject);
	}
	
	@Override
	public boolean isValid() {
		return super.isValid() && (packageElement != null);
	}
	
	@Override
	protected void init() {
		super.init();
		setPackage();
	}
	
	private void setPackage(){
		if (element != null)
			if (element instanceof Package)
				this.packageElement = (Package)element;
	}
	
	/* (non-Javadoc)
	 * @see ru.neodoc.content.modeller.utils.uml.elements.BasePackageElement#getUri()
	 */
	@Override
	public String getUri(){
		return packageElement.getURI();
	}
}
