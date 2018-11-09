package ru.neodoc.content.codegen.sdoc2.extension.java.pages.provider;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import ru.neodoc.content.codegen.sdoc2.CodegenManager;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;

public class AbstractWrapperTreeContentProvider implements ITreeContentProvider {

	protected CodegenManager codegenManager;
	
	protected List<? extends AbstractWrapper> rootElements = null;
	
	public AbstractWrapperTreeContentProvider(CodegenManager codegenManager) {
		this.codegenManager = codegenManager;
	}
	
	public List<? extends AbstractWrapper> getRootElements() {
		return rootElements;
	}

	public void setRootElements(List<? extends AbstractWrapper> rootElements) {
		this.rootElements = rootElements;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		
		List<? extends AbstractWrapper> list =
				this.rootElements==null
				?this.codegenManager.getWrappedNamespaceList()
				:this.rootElements;
		return (Object[])list.toArray(new AbstractWrapper[list.size()]);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof AbstractWrapper) {
			List<AbstractWrapper> lbw = ((AbstractWrapper)parentElement).getChildren();
			
			return (Object[])lbw.toArray(new AbstractWrapper[lbw.size()]);
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof AbstractWrapper)
			return ((AbstractWrapper)element).getOwner();
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof AbstractWrapper)
			return (((AbstractWrapper)element).getChildren().size() > 0);
		return false;
	}


}
