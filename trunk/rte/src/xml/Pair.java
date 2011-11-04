package xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Pair {
	
	@Attribute
	private int id;
	@Attribute
	private String entailment;
	@Attribute
	private String task;
	
	@Element
	private String t;
	
	@Element
	private String h;
	
	
	public int getId() {
		return id;
	}

	public String getEntailment() {
		return entailment;
	}

	public String getTask() {
		return task;
	}

	public String getT() {
		return t;
	}

	public String getH() {
		return h;
	}
	
}


