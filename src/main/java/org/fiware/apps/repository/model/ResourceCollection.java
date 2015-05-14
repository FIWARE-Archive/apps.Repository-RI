package org.fiware.apps.repository.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

//import org.jboss.resteasy.links.RESTServiceDiscovery;

@XmlRootElement(name = "collection")
public class ResourceCollection extends AbstractResource{

	private List<ResourceCollection> collections= new ArrayList <ResourceCollection>();
	private List<Resource> resources=new ArrayList <Resource>();
	

	//@XmlElementRef
	//private RESTServiceDiscovery rest;
	
	
	@XmlElementWrapper
	public List<ResourceCollection> getCollections() {
		return collections;
	}
	public void setCollections(List<ResourceCollection> collections) {
		this.collections = collections;
	}
	
	@XmlElementWrapper
	public List<Resource> getResources() {
		return resources;
	}
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}



}

