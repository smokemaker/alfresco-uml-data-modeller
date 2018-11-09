package ru.neodoc.content.modeller.tasks;

public interface ExecutionCallback {
	
	public static final ExecutionCallback EMPTY_CALLBACK = new ExecutionCallback() {
		
		@Override
		public void worked(int amount) {
			
		}
		
		@Override
		public void done() {
			
		}
	};		
	
	public void worked(int amount);
	public void done();
	
}
