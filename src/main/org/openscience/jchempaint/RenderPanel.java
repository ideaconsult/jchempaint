/*
 *  $RCSfile$
 *  $Author: egonw $
 *  $Date: 2007-01-04 17:26:00 +0000 (Thu, 04 Jan 2007) $
 *  $Revision: 7634 $
 *
 *  Copyright (C) 1997-2008 Stefan Kuhn
 *
 *  Contact: cdk-jchempaint@lists.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *  All we ask is that proper credit is given for our work, which includes
 *  - but is not limited to - adding the above copyright notice to the beginning
 *  of your source code files, and to any copyright notice that you may distribute
 *  with programs based on this work.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.openscience.jchempaint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import org.openscience.cdk.controller.Controller2DHub;
import org.openscience.cdk.controller.Controller2DModel;
import org.openscience.cdk.controller.IViewEventRelay;
import org.openscience.cdk.controller.SwingMouseEventRelay;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.renderer.IntermediateRenderer;

public class RenderPanel extends JPanel implements IViewEventRelay {
	
	private IntermediateRenderer renderer;
	private IChemModel chemModel;
	
	public IChemModel getChemModel() {
		return chemModel;
	}


	private Controller2DHub hub;
	public Controller2DHub getHub() {
		return hub;
	}
	

	private Controller2DModel controllerModel;
	private SwingMouseEventRelay mouseEventRelay;
	
	
	public RenderPanel(IChemModel chemModel) {
		this.chemModel = chemModel;
		this.setupMachinery();
		this.setupPanel();
	}
	
	private void setupMachinery() {
		// setup the Renderer and the controller 'model'
		this.renderer = new IntermediateRenderer();
		this.controllerModel = new Controller2DModel();
		
		// connect the Renderer to the Hub
		this.hub = new Controller2DHub(this.controllerModel, this.renderer, chemModel,this);
		
		// connect mouse events from Panel to the Hub
		this.mouseEventRelay = new SwingMouseEventRelay(this.hub);
		this.addMouseListener(mouseEventRelay);
		this.addMouseMotionListener(mouseEventRelay);
	}
	
	private void setupPanel() {
		this.setBackground(Color.WHITE);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		if (this.chemModel != null) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
			//TODO render the chemodel, not atomcontainer
			if(this.chemModel.getMoleculeSet()!=null && this.chemModel.getMoleculeSet().getAtomContainerCount()>0)
				this.renderer.paintMolecule(this.chemModel.getMoleculeSet().getAtomContainer(0), g2, this.getBounds());
		}
	}

	public void updateView() {
		this.repaint();
	}

}
