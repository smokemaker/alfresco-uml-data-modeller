package ru.neodoc.content.codegen.sdoc2.extension.java.pages.provider;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;

import ru.neodoc.content.codegen.sdoc2.wizard.provider.AbstractWrapperLabelProvider;
import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;

public class TitleLabelProvider extends AbstractWrapperLabelProvider {

	public static CellLabelProvider getCellProvider(){
		TitleLabelProvider tlp = new TitleLabelProvider();
		return new DelegatingStyledCellLabelProvider(tlp);
	}

	@Override
	protected StyledString getWrapperStyledText(AbstractWrapper wrapper) {
		String title = wrapper.getTitle();
		if (title == null)
			title = "";
		return new StyledString(title);
	}

}
