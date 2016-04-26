package siriuslayout;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.AbstractLayoutEditPartProvider;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNode3EditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeContainerEditPart;
import org.eclipse.sirius.diagram.ui.internal.edit.parts.DNodeContainerViewNodeContainerCompartmentEditPart;
import org.eclipse.sirius.diagram.ui.tools.api.layout.provider.CompoundLayoutProvider;
import org.eclipse.sirius.diagram.ui.tools.api.layout.provider.GridLayoutProvider;




public class TwoLineLayout extends GridLayoutProvider {
	private int gridw = 1000;
	
	private int xoffset = 5;
	private int yoffset = 5;
	
	private int xMargin = 5;
	private int yMargin = 5;
	
	public TwoLineLayout(int gridw, int xoffset, int yoffset, int xMargin,
			int yMargin) {
		super();
		this.gridw = gridw;
		this.xoffset = xoffset;
		this.yoffset = yoffset;
		this.xMargin = xMargin;
		this.yMargin = yMargin;
	}

	public Command layoutEditParts(@SuppressWarnings("rawtypes")
	final List selectedObjects, final IAdaptable layoutHint) {
		int wmax = 0;
		int hmax = 0;
		int xact = xMargin;
		int yact = yMargin;
		List<EditPart> objs = selectedObjects;
		
		for (EditPart editPart : objs) {
			if(editPart instanceof NodeEditPart){
				wmax = wmax < ((NodeEditPart) editPart).getFigure().getSize().width? ((NodeEditPart) editPart).getFigure().getSize().width : wmax;
				//hmax = hmax < ((NodeEditPart) editPart).getFigure().getSize().height? ((NodeEditPart) editPart).getFigure().getSize().height : hmax;
				
			}
		}
		final CompoundCommand cc = new CompoundCommand();
		 
		for (EditPart editPart : objs) {
			if(editPart instanceof NodeEditPart){
				//((NodeEditPart) editPart).getFigure().setLocation(new Point(xact,yact));
				final Command command = createChangeBoundsCommand((IGraphicalEditPart) editPart, new Point(xact,yact));
				 if (command != null && command.canExecute()) {
				 cc.add(command);
				 }
				if(xact > gridw){
					xact = xMargin;
					yact += hmax +  yoffset;
					hmax = 0;
				}else{
					hmax = hmax < ((NodeEditPart) editPart).getFigure().getSize().height? ((NodeEditPart) editPart).getFigure().getSize().height : hmax;
					xact +=  ((NodeEditPart) editPart).getFigure().getSize().width + xoffset;
				}
			}
		}
		
		
		return cc;
	}
	
	
	
	
}
