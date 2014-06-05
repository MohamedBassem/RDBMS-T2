package eg.edu.guc.dbms.sql;

import java.util.LinkedList;
import java.util.List;

public abstract class PhysicalPlanTree {

	public enum Operation {
		PROJECT,
		SELECT,
		PRODUCT,
		SCAN,
		INSERT,
		CREATE_TABLE,
		INDEX,
		UPDATE;
	}
	
	List<PhysicalPlanTree> children;
	
	public PhysicalPlanTree() {
		children = new LinkedList<PhysicalPlanTree>();
	}
	
	public abstract Operation getOperation();
	public List<PhysicalPlanTree> getChildren() {
		return children;
	}
	public PhysicalPlanTree addChild(PhysicalPlanTree tree) {
		children.add(tree);
		return this;
	}
	
	public PhysicalPlanTree popLastChild() {
		return children.remove(children.size()-1);
	}
	
}
