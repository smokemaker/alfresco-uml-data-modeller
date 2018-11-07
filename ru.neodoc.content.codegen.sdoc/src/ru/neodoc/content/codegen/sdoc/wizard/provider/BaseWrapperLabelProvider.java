package ru.neodoc.content.codegen.sdoc.wizard.provider;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;

import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;

public abstract class BaseWrapperLabelProvider extends LabelProvider implements IStyledLabelProvider {

	@Override
	public StyledString getStyledText(Object element) {
		if (element instanceof BaseWrapper)
			return getWrapperStyledText((BaseWrapper)element);
		return new StyledString("");
	}
	
	protected abstract StyledString getWrapperStyledText(BaseWrapper wrapper);

}
