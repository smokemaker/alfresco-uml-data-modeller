package ru.neodoc.content.codegen.sdoc.wizard.provider;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;

import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;

public class NameLabelProvider extends BaseWrapperLabelProvider {

	public static CellLabelProvider getCellProvider(){
		NameLabelProvider nlp = new NameLabelProvider();
		return new DelegatingStyledCellLabelProvider(nlp);
	}
	
	@Override
	protected StyledString getWrapperStyledText(BaseWrapper wrapper) {
		return new StyledString(wrapper.getName());
	}

}
