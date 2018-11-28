package com.mycompany.app;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.lang.model.element.Element;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.io.File;

public class App
{

    
    public static String parse(File file,String input1,String input2) throws ParserConfigurationException,
    SAXException, IOException {
    	String fname="";
    	String lname="";
    	String id="";
    	String person="";
    	  try {

    			File fXmlFile = new File("EEAS.xml");
    			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    			Document doc = (Document) dBuilder.parse(fXmlFile);
    					
    			
    			((org.w3c.dom.Document) doc).getDocumentElement().normalize();

    			System.out.println("Root element :" + ((org.w3c.dom.Document) doc).getDocumentElement().getNodeName());
    					
    			NodeList nList = ((org.w3c.dom.Document) doc).getElementsByTagName("ENTITY");
    					
    			System.out.println("----------------------------");

    			for (int temp = 0; temp < nList.getLength(); temp++) {

    				Node nNode = nList.item(temp);
    						
    				System.out.println("\nCurrent Element :" + nNode.getNodeName());
    						
    				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

    					Element eElement = (Element) nNode;
    					if(!input1.equals("") && input2.equals("")) {
    						id=((org.w3c.dom.Document) eElement).getElementsByTagName("Id").item(0).getTextContent()+" ";
    						fname=((org.w3c.dom.Document) eElement).getElementsByTagName("FIRSTNAME").item(0).getTextContent()+" ";
    						lname=((org.w3c.dom.Document) eElement).getElementsByTagName("LASTNAME").item(0).getTextContent()+" ";
    						if(input1.equals(fname)) {
        						
        						person+=id+" "+fname+" "+lname+" ";
        					}
    					}
    					if(input1.equals("") && !input2.equals("")) {
    						id=((org.w3c.dom.Document) eElement).getElementsByTagName("Id").item(0).getTextContent()+" ";
    						fname=((org.w3c.dom.Document) eElement).getElementsByTagName("FIRSTNAME").item(0).getTextContent()+" ";
    						lname=((org.w3c.dom.Document) eElement).getElementsByTagName("LASTNAME").item(0).getTextContent()+" ";
    						if(input2.equals(lname)) {
        						
        						person+=id+" "+fname+" "+lname+" ";
        					}
    					}
    					if(!input1.equals("") && !input2.equals("")) {
    						id=((org.w3c.dom.Document) eElement).getElementsByTagName("Id").item(0).getTextContent()+" ";
    						fname=((org.w3c.dom.Document) eElement).getElementsByTagName("FIRSTNAME").item(0).getTextContent()+" ";
    						lname=((org.w3c.dom.Document) eElement).getElementsByTagName("LASTNAME").item(0).getTextContent()+" ";
    						if(input1.equals(fname) && input2.equals(lname)) {
        						
        						person+=id+" "+fname+" "+lname+" ";
        					}
    					}
    					
    					
    					
    				

    				}
    			}
    		    } catch (Exception e) {
    			e.printStackTrace();
    		    }
    	  return person;
      
  }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
    	
    	File fXmlFile = new File("EEAS.xml");
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    	Document doc = (Document) dBuilder.parse(fXmlFile);
        port(getHerokuAssignedPort());
       

        get("/", (req, res) -> "Hello, World");

        post("/compute", (req, res) -> {
          //System.out.println(req.queryParams("input1"));
          //System.out.println(req.queryParams("input2"));

          String input1 = req.queryParams("input1");
          String input1Val = input1;
           
          String input2 = req.queryParams("input2");
          String input2Val = input2;
          String result=parse(new File("EEAS.xml"),input1,input2);
         Map map = new HashMap();
          map.put("result", result);
          return new ModelAndView(map, "compute.mustache");
        }, new MustacheTemplateEngine());


        get("/compute",
            (rq, rs) -> {
              Map map = new HashMap();
              map.put("result", "not computed yet!");
              return new ModelAndView(map, "compute.mustache");
            },
            new MustacheTemplateEngine());
    	
    			
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}


