package eg.edu.guc.dbms.sql;

import java.util.LinkedList;
import java.util.List;

public class Project extends PhysicalPlanTree {
	
	List<String> projectionColumns;
	
	public Project() {
		super();
		projectionColumns = new LinkedList<String>();
	}

	@Override
	public Operation getOperation() {
		return Operation.PROJECT;
	}

	public List<String> getProjectionColumns() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void addProjectionColumn(String column) {
		projectionColumns.add(column);
	}
}
