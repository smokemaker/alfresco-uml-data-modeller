package ru.neodoc.content.codegen.sdoc.wizard.provider;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import ru.neodoc.content.codegen.sdoc.CodegenManager;
import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;

public class BaseWrapperTreeContentProvider implements ITreeContentProvider {

	protected CodegenManager codegenManager;
	
	protected List<? extends BaseWrapper> rootElements = null;
	
	public BaseWrapperTreeContentProvider(CodegenManager codegenManager) {
		this.codegenManager = codegenManager;
	}
	
	public List<? extends BaseWrapper> getRootElements() {
		return rootElements;
	}

	public void setRootElements(List<? extends BaseWrapper> rootElements) {
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
		
		List<? extends BaseWrapper> list =
				this.rootElements==null
				?this.codegenManager.getWrappedNamespaceList()
				:this.rootElements;
		return (Object[])list.toArray(new BaseWrapper[list.size()]);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof BaseWrapper) {
			List<BaseWrapper> lbw = ((BaseWrapper)parentElement).getChildren();
			
			return (Object[])lbw.toArray(new BaseWrapper[lbw.size()]);
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof BaseWrapper)
			return ((BaseWrapper)element).getOwner();
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof BaseWrapper)
			return (((BaseWrapper)element).getChildren().size() > 0);
		return false;
	}


}
