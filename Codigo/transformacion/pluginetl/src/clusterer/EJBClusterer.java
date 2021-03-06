package clusterer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import subkdm.kdmObjects.ClassUnit;
import subkdm.kdmObjects.Cluster;
import subkdm.kdmObjects.CodeItem;
import subkdm.kdmObjects.InterfaceUnit;
import subkdm.kdmObjects.KdmObjectsFactory;
import subkdm.kdmRelations.ClassLevelRelation;
import subkdm.kdmRelations.TypeRelation;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class EJBClusterer {
	
	DirectedGraph<CodeItem, ClassLevelRelation> graph;
	KdmObjectsFactory factory;
	
	public EJBClusterer(){
		graph = new DirectedSparseMultigraph<CodeItem, ClassLevelRelation>();
		factory = KdmObjectsFactory.eINSTANCE;
	}
	
	public void constructGraph(Set<CodeItem> classes, Set<ClassLevelRelation> relations){

		for(CodeItem c : classes){
			graph.addVertex(c);
		}
		
		for(ClassLevelRelation clr : relations){
			graph.addEdge(clr, clr.getFrom(), clr.getTo());
		}
	}
	
	
	public Set<Cluster> makeCluster(Set<CodeItem> ejbs, Set<CodeItem> entities){
		
		Set<Cluster> firstclusters = new HashSet<Cluster>();
		
		DijkstraShortestPath<CodeItem, ClassLevelRelation> sp = 
				new DijkstraShortestPath<CodeItem, ClassLevelRelation>(graph, true);
		
		Set<Set<CodeItem>> allEntityHt = constructHeritageTree(entities);
		
		
		for(CodeItem ejb : ejbs){
			if (!(ejb instanceof ClassUnit)) {
				continue;
			}
			Cluster mod = factory.createCluster();
			mod.setName(ejb.getName()); 
			System.out.println("Verificando caminos de " + ejb.getName() + " hacia entities");
			
			Set<CodeItem> elementos = new HashSet<CodeItem>();
			Set<InterfaceUnit> clasesPadre = new  HashSet<InterfaceUnit>();
			
			// por cada EJB detecta todas las clases que hay en un camino hacia una entidad
			for(CodeItem elem : getHeritageTree(ejb)){ 
				if (elem.getName().contains("Base") || elem.getName().contains("Abstract")) {
					continue;
				}
				elementos.add(elem);
				elem.setIsService("true"); // marca al ejb o sus interfaces como servicios
				if (!elem.getName().equals(ejb.getName()) && elem instanceof InterfaceUnit) {
					clasesPadre.add((InterfaceUnit)elem);
				}
				
				for(Set<CodeItem> entityHt : allEntityHt)
				{
					for(CodeItem entHtElem : entityHt)
					{
						if(!sp.getPath(elem, entHtElem).isEmpty()){
							elementos.addAll(entityHt);
							for(ClassLevelRelation clr : sp.getPath(elem, entHtElem)){
								elementos.add(clr.getFrom());
								elementos.add(clr.getTo());
							}
						}
					}
				}
				
			}
						
			List<CodeItem> allNodes = new ArrayList<CodeItem>(graph.getVertices());
			allNodes.removeAll(elementos);
						
			//Ahora por cada clase enocntrada en el camino, se buscan sus clases relacionadas
			Set<CodeItem> extras = new HashSet<CodeItem>();
			for (CodeItem elem : elementos){
				for(CodeItem item : allNodes)
				{

					
					// teniendo la clase relacionada como origen
					for (CodeItem itemH : getHeritageTree(item)) {
						
						for(ClassLevelRelation clr : sp.getPath(elem, itemH)){
							extras.add(clr.getFrom());
							extras.add(clr.getTo());
						}
					}
						
					// teniendo la clase relacionada como destino (solo herencias)
					for (CodeItem itemH : getHeritageTree(item)) {
						boolean todoEsHerencia = true;
						for(ClassLevelRelation clr : sp.getPath(itemH, elem)){
							if (!isExtensionOrImplementation(clr)) {
								todoEsHerencia = false;
							}
						}
						
						if (todoEsHerencia) {
							for(ClassLevelRelation clr : sp.getPath(itemH, elem)){
									extras.add(clr.getFrom());
									extras.add(clr.getTo());								
							}
						}
					}
				}
			}
			
			((ClassUnit)ejb).getClasesHeredadas().addAll(clasesPadre);
			elementos.addAll(extras);			
			mod.getCodeElement().addAll(elementos);
			firstclusters.add(mod);
		}
		
		return firstclusters;
	}
	
	public Set<Set<CodeItem>> constructHeritageTree(Set<CodeItem> ejbs){
		Set<Set<CodeItem>> heritageTrees = new HashSet<Set<CodeItem>>();
		for(CodeItem ejb : ejbs){
			Set<CodeItem> ht = new HashSet<CodeItem>();
			ht = getHeritageTree(ejb);
			heritageTrees.add(ht);
		}
		return heritageTrees;
	}
	
	public Set<CodeItem> getHeritageTree(CodeItem item){
		Set<CodeItem> set = new HashSet<CodeItem>();
		set.add(item);
		Collection<ClassLevelRelation> allclr = graph.getOutEdges(item);
		
		for(ClassLevelRelation clr: allclr){
			if(isExtensionOrImplementation(clr)){				
				set.add(clr.getFrom());
				set.add(clr.getTo());
				set.addAll(getHeritageTree(clr.getTo()));
			}
		}
		return set;
	}
	
	public boolean isExtensionOrImplementation(ClassLevelRelation clr){
		for(TypeRelation tr: clr.getTypeRelations()){
			if(tr.getName().equalsIgnoreCase("Extends") || tr.getName().equalsIgnoreCase("Implements")){
				return true;
			}
		}
		return false;
	}
	
}
