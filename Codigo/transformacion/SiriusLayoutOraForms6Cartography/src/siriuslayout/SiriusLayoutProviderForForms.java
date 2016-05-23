package siriuslayout;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.layout.AbstractLayoutEditPartProvider;
import org.eclipse.gmf.runtime.notation.impl.DiagramImpl;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.ui.tools.api.layout.provider.DefaultLayoutProvider;
import org.eclipse.sirius.diagram.ui.tools.api.layout.provider.GridLayoutProvider;
import org.eclipse.sirius.diagram.ui.tools.api.layout.provider.LayoutProvider;

public class SiriusLayoutProviderForForms implements LayoutProvider {

	public final int DEFAULT = 0;
	public final int RADIAL = 1;
	public final int GRID = 2;
	public final int TWO_LINE = 3;

	private int selectedLayout = 0;

	private GridLayout gl;
	
	public SiriusLayoutProviderForForms() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Devuelve el proveedor de layout correspondiente a la selección
	 * realizada por el usuario.
	 */
	@Override
	public AbstractLayoutEditPartProvider getLayoutNodeProvider(
			IGraphicalEditPart arg0) {
		
		switch (selectedLayout) {
		case RADIAL:
			return new RadialLayout();

		case GRID:

			return gl;
		case TWO_LINE:

			return new DefaultLayoutProvider();

		}

		return null;
	}

	@Override
	public boolean isDiagramLayoutProvider() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean provides(IGraphicalEditPart arg0) {

		if (arg0.getModel() instanceof DiagramImpl) {
			String viewName = ((DSemanticDiagram) ((DiagramImpl) arg0
					.getModel()).basicGetElement()).getDescription().getName();

			if (viewName.matches(".*?Radial.*?")) {
				selectedLayout = RADIAL;
				return true;
			} else if (viewName.matches(".*?Ordenada.*?")) {
				selectedLayout = GRID;
				gl = new GridLayout(1300, 5, 5, 5, 5);
				return true;
			} else if (viewName.matches(".*?DosLineas.*?")) {
				selectedLayout = TWO_LINE;
				return false;
			}
		} else if (arg0.getModel() instanceof NodeImpl) {
			selectedLayout = GRID;
			gl = new GridLayout(1000, 5, 5,5,25);
			return true;
		}

		return false;
	}

}
