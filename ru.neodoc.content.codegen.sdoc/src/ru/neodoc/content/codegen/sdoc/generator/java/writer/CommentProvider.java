package ru.neodoc.content.codegen.sdoc.generator.java.writer;

import ru.neodoc.content.codegen.sdoc.wrap.BaseWrapper;

public interface CommentProvider {
	
	public String getComment(BaseWrapper baseWrapper);
	
}
