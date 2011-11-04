package xml;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class EntailementCorpus {
	
	@ElementList(inline=true)
	private List<Pair> list;
	
	public List<Pair> getProperties() {
		return list;
	}
	
}


