package siriuslayout;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.AbstractLayoutEditPartProvider;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.sirius.diagram.ui.tools.api.layout.LayoutExtender;
import org.eclipse.sirius.diagram.ui.tools.api.layout.provider.CompoundLayoutProvider;
import org.eclipse.sirius.diagram.ui.tools.api.layout.provider.DefaultLayoutProvider;
import org.eclipse.sirius.diagram.ui.tools.api.layout.provider.ExtendableLayoutProvider;
import org.eclipse.sirius.diagram.ui.tools.api.layout.provider.GridLayoutProvider;

public class RadialLayout extends GridLayoutProvider {



	public RadialLayout() {
		super();
		
	}

	/**
	 * Se encarga de ordenar los nodos de forma circular 
	 * colocando el más pesado a las 3 horas y sucesivamente del más pesado al menos pesado
	 * en el sentido de las manecillas del reloj.
	 * 
	 */
	public Command layoutEditParts(@SuppressWarnings("rawtypes")
	final List selectedObjects, final IAdaptable layoutHint) {

		List<EditPart> objs = selectedObjects;
		List<NodeEditPart> nodes = new ArrayList<NodeEditPart>();
		final CompoundCommand cc = new CompoundCommand();
		for (EditPart editPart : objs) {
			if (editPart.getModel() instanceof NodeImpl) {
				System.out.println(">>>>"+editPart.getModel());
				nodes.add((NodeEditPart) editPart);

			}
		}

		orderbySize((ArrayList<NodeEditPart>) nodes);
		int r = nodes.size() * 30;

		double i = 0;
		double sz = nodes.size();
		System.out.println("--> Nodes:" + nodes.size());
		for (NodeEditPart editPart : nodes) {

			double t = ((2 * Math.PI) * (i / sz));
			int x = (int) Math.round(r * Math.cos(t));
			int y = (int) Math.round(r * Math.sin(t));

			int offX = x - (editPart.getFigure().getSize().width / 2);
			int offY = y - (editPart.getFigure().getSize().height / 2);
			System.out.println("|||x:" + x + " -Y:" + y + " -i:" + (i / sz));
			
			 final Command command = createChangeBoundsCommand((IGraphicalEditPart) editPart, new Point(offX, offY));
			 if (command != null && command.canExecute()) {
			 cc.add(command);
			 }
			
			 //editPart.getFigure().setLocation();

			i++;

		}
		return cc;

	}

	public void orderbySize(ArrayList<NodeEditPart> nodes) {
		for (int i = 0; i < nodes.size(); i++) {
			for (int j = 0; j < nodes.size(); j++) {
				if (nodes.get(i).getFigure().getSize().width > nodes.get(j)
						.getFigure().getSize().width) {
					NodeEditPart tm = nodes.get(i);
					nodes.set(i, nodes.get(j));
					nodes.set(j, tm);
				}
			}
		}
	}

	
}
