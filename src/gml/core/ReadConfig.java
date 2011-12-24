package gml.core;

import gml.math.Distribution;

import gml.prior.*;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;

import org.jdom.Document;
import org.jdom.Element;

import org.jdom.input.SAXBuilder;

public class ReadConfig {

	private final static String FILE_SEP = System.getProperty("file.separator");

	public ReadConfig(String configFile) {

		readConfigFile(configFile);
	}

	public HashMap<String, HashMap> readConfigFile(String configFile) {

		String path = configFile.substring(0,
				configFile.lastIndexOf(FILE_SEP) + 1);
		System.out.println("coinfig file:\t" + configFile);
		// ArrayList<double[]> config = new ArrayList<double[]>();
//		HashMap<String, String> config = new HashMap<String, String>();
//		HashMap<String, Distribution> configDist = new HashMap<String, Distribution>();
		HashMap<String, HashMap> allConfig = new HashMap<String, HashMap>();
		try {

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new File(configFile));

			StringBuilder output = new StringBuilder();

			Iterator<Element> itr = doc.getRootElement().getChildren().iterator();

			while (itr.hasNext()) {
				Element elem = itr.next();
				String name = elem.getName();
				System.out.println("name=" + elem.getName() + "=endName");
				// System.out.println(elem.getTextNormalize());
				if (name.equalsIgnoreCase("data")) {
					allConfig.put("data", configData(elem));
				} else if (name.equalsIgnoreCase("distribution")) {
					allConfig.put("prior", configPriorDist(elem));
					
				} else if (name.equalsIgnoreCase("mcmc")) {
					allConfig.put("mcmc", configMCMC(elem));
				}

				// System.out.println(elem.getName());
				// output.append("<li>");
				// output.append(elem.getAttribute("recipe").getValue());
				// output.append(", ");
				// output.append(elem.getAttribute("firstName").getValue());
				// output.append("</li>\n");
			}

			// create the end of the HTML output
		
//			allConfig.put("config", config);
//			allConfig.put("distribution", configDist);
			System.out.println(output.toString());
System.out.println(allConfig.toString());
System.out.println(allConfig.get("mcmc").toString());
System.out.println(allConfig.get("data").toString());
System.out.println(allConfig.get("prior").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allConfig;
	}

	private HashMap<String, String> configMCMC (Element elem) throws XMLException {
		System.out.println("\n=====In MCMC====");
		HashMap<String, String> configMCMC = new HashMap<String, String>();
		// int totalIte = elem.getAttribute("length").getIntValue();
		// int thinning = elem.getAttribute("thinning").getIntValue();
		configMCMC.put("totalIte", elem.getAttributeValue("length"));
		configMCMC.put("thinning", elem.getAttributeValue("thinning"));

		Element logE = getChild(elem, "log");

		if (logE != null) {

			List<Element> it2 = logE.getChildren();
			System.out.println(it2.size());
			System.out.println(logE.getContentSize());

			if (it2.size() != 3) {
				// throw new XMLException("Incorrect number of element");
			}
			for (Element childE : it2) {
				// System.out.println(child.getName());
				String name = childE.getName();
				System.out.println(childE.getContent().toString());
				if (name.equals("local")) {
					addConfig(configMCMC, "localOutput", childE, "fileName");
				} else if (name.equals("global")) {
					addConfig(configMCMC, "globalOutput", childE, "fileName");
				} else if (name.equals("each")) {
					addConfig(configMCMC, "eachOutput", childE, "fileName");
					addConfig(configMCMC, "fileExt", childE, "fileExt");
				}

			}
			// config.put("globalOutput", logE.getChild("global")
			// .getAttributeValue("fileName"));

			// Element eachE = logE.getChild("each");
			// config.put("eachSpotOutput",
			// eachE.getAttributeValue("fileName"));
			// config.put("eachSpotOutputExt", eachE.getAttributeValue("ext"));
		} else {
			System.out.println("Use Default log files");
			configMCMC.put("globalOutput", "SpotGlobal.log");
			configMCMC.put("localOutput", "SpotLocal.log");
			configMCMC.put("eachSpotOutput", "Spot");
			configMCMC.put("eachSpotOutputExt", "log");

		}
		System.out.println(configMCMC.toString());
		System.out.println("======end MCMC======\n");
		return configMCMC;
	}


	private HashMap<String, PriorDist> configPriorDist(Element elem)
			throws XMLException {

		Element priorE = getChild(elem, "prior");
//		Element proposalE = getChild(elem, "proposal");
		
//		Element tempE = getChild(priorE, "muMean");
//	
		HashMap<String, PriorDist> configDistPrior = new HashMap<String, PriorDist>();

//		Iterator<Attribute> ite = child.getAttributes().iterator();
//		System.out.println("ite: "+ite.toString());
//		List<Attribute> ite2 = child.getAttributes();
//		System.out.println("list: "+ite2.toString());
//		
		
		
		addPriorDist(configDistPrior, priorE, "muMean");
		addPriorDist(configDistPrior, priorE, "muSd");
		addPriorDist(configDistPrior, priorE, "delta");
		addPriorDist(configDistPrior, priorE, "phi");
		addPriorDist(configDistPrior, priorE, "probMean");
		addPriorDist(configDistPrior, priorE, "probSd");
		
		return configDistPrior;

//		PriorDist x = (PriorDist) configDistPrior.get("muMean");

	}

	private void addPriorDist(HashMap<String, PriorDist> configDist, Element elem,
			String key) throws XMLException {
		
		Element child = getChild(elem, key);
		PriorDist dist = createPriorDist(child);
		configDist.put(key, dist);
		
	}
	
	private void addProposalDist(HashMap<String, Distribution> configDist, Element elem,
			String key) throws XMLException {

		Element child = getChild(elem, key);
		Distribution dist = createProposalDist(child);
		configDist.put(key, dist);
		
	}

	private Distribution createProposalDist(Element child) {
		// TODO Auto-generated method stub
		return null;
	}

	private PriorDist createPriorDist(Element child) throws XMLException {
		String distName = getAttributeValue(child, "dist");
		PriorDist dist = null;
		System.out.println(distName);
		if(distName.equalsIgnoreCase("normal")){
			double mean = Double.parseDouble(child.getAttributeValue("mean"));
			double sd = Double.parseDouble(child.getAttributeValue("sd"));
			dist = new PriorNormal(mean,sd);
		}
		
		else if(distName.equalsIgnoreCase("uniform")){
			double lower = Double.parseDouble(child.getAttributeValue("lower"));
			double upper = Double.parseDouble(child.getAttributeValue("upper"));
			dist = new PriorUniform(lower,upper);
		}
		
		else if(distName.equalsIgnoreCase("beta")){
			double shape = Double.parseDouble(child.getAttributeValue("shape"));
			double scale = Double.parseDouble(child.getAttributeValue("scale"));
			dist = new PriorBeta(shape, scale);
			
		}

		else if(distName.equalsIgnoreCase("exp")){
			double lambda = Double.parseDouble(child.getAttributeValue("lambda"));
			dist = new PriorExp(lambda);
			
		}

		else if(distName.equalsIgnoreCase("gamma")){
			double shape = Double.parseDouble(child.getAttributeValue("shape"));
			double scale = Double.parseDouble(child.getAttributeValue("scale"));
			dist = new PriorGamma(shape,scale);
			
		}

		else if(distName.equalsIgnoreCase("invgamma")){
			double shape = Double.parseDouble(child.getAttributeValue("shape"));
			double scale = Double.parseDouble(child.getAttributeValue("scale"));
			dist = new PriorInvGamma(shape,scale);
			
		}

		return dist;
		
		
	}

	private HashMap<String, String> configData(Element elem ) throws XMLException {
		System.out.println("\n=====In data====");
		HashMap<String, String> configData = new HashMap<String, String>();
		Element dataE = elem.getChild("gel");

		addConfig(configData, "file", dataE);
		addConfig(configData, "format", dataE);
		
		System.out.println("======end data======\n");
		return configData;
	}

	private Element getChild(Element parent, String name) throws XMLException {
		Element child = parent.getChild(name);

		if (child != null) {
			return child;
		} else {
			throw new XMLException("Element \"" + parent.getName()
					+ "\" with out child: " + name);
		}

	}
	
	private Attribute getAttribute(Element tempE, String attrName) throws XMLException {
		Attribute attr = tempE.getAttribute(attrName);
		if (attr != null) {
			return attr;
		} else {
			throw new XMLException("Element \""
					+ tempE.getParentElement().getName() + "\\"
					+ tempE.getName() + "\" with out attribute: " + attrName);
		}

	}
	
	private String getAttributeValue(Element tempE, String attrName) throws XMLException {
		String attr = tempE.getAttributeValue(attrName);
		if (attr != null) {
			return attr;
		} else {
			throw new XMLException("Element \""
					+ tempE.getParentElement().getName() + "\\"
					+ tempE.getName() + "\" with out attribute: " + attrName);
		}

	}
	
	private void addConfig(HashMap<String, String> config, String key,
			Element tempE, String attrName) throws XMLException {
		String value = tempE.getAttributeValue(attrName);
		if (value != null) {
			config.put(key, value);
		} else {
			throw new XMLException("Element \""
					+ tempE.getParentElement().getName() + "\\"
					+ tempE.getName() + "\" with out attribute: " + attrName);
		}

	}

	private void addConfig(HashMap<String, String> config, String key,
			Element tempE) throws XMLException {
		addConfig(config, key, tempE, key);
	}
}

class XMLException extends Exception {
	public XMLException(String msg) {
		super(msg);
	}
}
