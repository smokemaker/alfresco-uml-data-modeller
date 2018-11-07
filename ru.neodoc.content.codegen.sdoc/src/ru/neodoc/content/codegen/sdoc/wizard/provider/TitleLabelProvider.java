package ru.neodoc.content.codegen.sdoc.wizard.provider;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;

import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;

public class TitleLabelProvider extends BaseWrapperLabelProvider {

	public static CellLabelProvider getCellProvider(){
		TitleLabelProvider tlp = new TitleLabelProvider();
		return new DelegatingStyledCellLabelProvider(tlp);
	}

	@Override
	protected StyledString getWrapperStyledText(BaseWrapper wrapper) {
		String title = wrapper.getTitle();
		if (title == null)
			title = "";
		return new StyledString(title);
	}

}
