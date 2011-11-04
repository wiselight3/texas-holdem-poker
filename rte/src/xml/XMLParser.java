package xml;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class XMLParser {
	
	public static void main(String[] args) {
		EntailementCorpus example = null;
		Serializer serializer = new Persister();
		File source = new File("src/RTE2_dev.xml");
		try {
			 example = serializer.read(EntailementCorpus.class, source);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (Pair pair : example.getProperties()) {
			System.out.println("<pair>");
			System.out.print("<id>" +pair.getId() + "</id> ");
			System.out.print("<entailment>" +pair.getEntailment() + "</entailment> ");
			System.out.print("<task>" +pair.getTask() + "</task> ");
			System.out.println();
			System.out.println("<t>" + pair.getT() + "</t>");
			System.out.println("<h>" + pair.getH() + "</h>");
			System.out.println("</pair>");

		}
	}
}
