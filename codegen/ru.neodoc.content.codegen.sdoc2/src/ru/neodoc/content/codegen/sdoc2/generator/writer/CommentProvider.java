package ru.neodoc.content.codegen.sdoc2.generator.writer;

import ru.neodoc.content.codegen.sdoc2.wrap.AbstractWrapper;

public interface CommentProvider {
	
	public String getComment(AbstractWrapper baseWrapper);
	
}
