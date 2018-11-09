package ru.neodoc.content.codegen.sdoc2.wrap;

public abstract class AbstractWrapperExtension {

	protected AbstractWrapper extendedWrapper = null;
	protected String id;
	
	public AbstractWrapperExtension(AbstractWrapper extendedWrapper) {
		super();
		this.extendedWrapper = extendedWrapper;
	}

	public AbstractWrapper getExtendedWrapper() {
		return extendedWrapper;
	}

	public String getId() {
		return id;
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends AbstractWrapperExtension> T getOwnerExtension() {
		AbstractWrapper aw = extendedWrapper.getOwner();
		if (aw!=null)
			return (T)aw.getExtension(id);
		return null;
	}
	
	public boolean isFull() {
		return true;
	}
	
	public boolean isValid() {
		return true;
	}
	
	public String getName() {
		return extendedWrapper.getName();
	}
}
