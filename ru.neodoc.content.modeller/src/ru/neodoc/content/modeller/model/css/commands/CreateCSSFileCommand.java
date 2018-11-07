package ru.neodoc.content.modeller.model.css.commands;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.papyrus.infra.core.resource.ModelSet;

public class CreateCSSFileCommand extends ModelSetBasedCommand {

	protected IFile file;
	protected InputStream stream;
	
	public CreateCSSFileCommand(ModelSet modelSet, IFile fileToWrite, InputStream source) {
		super(modelSet);
		this.file = fileToWrite;
		this.stream = source;
	}
	
	@Override
	protected void doExecute() {
		if (file==null || stream==null)
			return;
		if (file.exists())
			try {
				file.delete(true, null);
			} catch (Exception e) {
				
			}
		try {
			file.create(stream, true, null);
		} catch (Exception e) {
			
		}
	}

}
