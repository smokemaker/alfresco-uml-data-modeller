package ru.neodoc.content.codegen.sdoc2.wizard.provider;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;

import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;

public abstract class AbstractWrapperLabelProvider extends LabelProvider implements IStyledLabelProvider {

	@Override
	public StyledString getStyledText(Object element) {
		if (element instanceof AbstractWrapper)
			return getWrapperStyledText((AbstractWrapper)element);
		return new StyledString("");
	}
	
	protected abstract StyledString getWrapperStyledText(AbstractWrapper wrapper);

}
