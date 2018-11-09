package ru.neodoc.content.codegen.sdoc2.extension.java.pages.provider;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;

import ru.neodoc.content.codegen.sdoc2.wizard.provider.AbstractWrapperLabelProvider;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;

public class NameLabelProvider extends AbstractWrapperLabelProvider {

	public static CellLabelProvider getCellProvider(){
		NameLabelProvider nlp = new NameLabelProvider();
		return new DelegatingStyledCellLabelProvider(nlp);
	}
	
	@Override
	protected StyledString getWrapperStyledText(AbstractWrapper wrapper) {
		return new StyledString(wrapper.getName());
	}

}
