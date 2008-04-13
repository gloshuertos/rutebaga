package rutebaga.view.rwt;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

import rutebaga.commons.Vector;

public class EventDispatcher implements KeyListener, MouseListener, MouseMotionListener {

	private Set<ViewComponent> registeredComponents;
	private Set<ViewComponent> containsMouse;
	private Vector vector;
	
	public EventDispatcher() {
		registeredComponents = new HashSet<ViewComponent>();
		containsMouse = new HashSet<ViewComponent>();
		vector = new Vector(2);
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);
		
		for(ViewComponent vc : registeredComponents)
			if(vc.hasFocus())
				vc.processKeyEvent(e);
		
		eventReceivedTest(e);
	}

	public void keyReleased(KeyEvent e) {
		for(ViewComponent vc : registeredComponents)
			if(vc.hasFocus())
				vc.processKeyEvent(e);
		
		eventReceivedTest(e);
	}

	public void keyTyped(KeyEvent e) {
		for(ViewComponent vc : registeredComponents)
			if(vc.hasFocus())
				vc.processKeyEvent(e);
		
		eventReceivedTest(e);
	}

	
	
	public void mouseClicked(MouseEvent e) {
		for(ViewComponent vc : containsMouse)
			vc.processMouseEvent(e);
		
		eventReceivedTest(e);
	}
	
	public void mousePressed(MouseEvent e) {
		for(ViewComponent vc : containsMouse)
			vc.processMouseEvent(e);
		
		eventReceivedTest(e);
	}

	public void mouseReleased(MouseEvent e) {
		for(ViewComponent vc : containsMouse)
			vc.processMouseEvent(e);
		
		eventReceivedTest(e);
	}
	
	

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}



	public void mouseDragged(MouseEvent e) {
		
		for(ViewComponent vc : registeredComponents)
			if(vc.getBounds().contains( vector )) { //TODO change vector to do real mouse click detection once vector is done
				if( !containsMouse.contains( vc )) {
					containsMouse.add(vc);
					mouseEntered(vc, e);
				} else {
					vc.processMouseMotionEvent( e );
				}
			} else {
				if( containsMouse.contains( vc )) {
					containsMouse.remove(vc);
					mouseExited(vc, e);
				}
			}
		
		eventReceivedTest(e);
	}

	public void mouseMoved(MouseEvent e) {
		
		for(ViewComponent vc : registeredComponents)
			if(vc.getBounds().contains( vector )) { //TODO change vector to do real mouse click detection once vector is done
				if( !containsMouse.contains( vc )) {
					containsMouse.add(vc);
					mouseEntered(vc, e);
				} else {
					vc.processMouseMotionEvent( e );
				}
			} else {
				if( containsMouse.contains( vc )) {
					containsMouse.remove(vc);
					mouseExited(vc, e);
				}
			}
		
		eventReceivedTest(e);
	}
	
	private void mouseEntered(ViewComponent vc, MouseEvent e) {
		vc.processMouseMotionEvent( createMouseEvent( MouseEvent.MOUSE_ENTERED, e) );
	}
	
	private void mouseExited(ViewComponent vc, MouseEvent e) {
		vc.processMouseMotionEvent( createMouseEvent( MouseEvent.MOUSE_EXITED, e) );
	}
	
	private MouseEvent createMouseEvent( int type, MouseEvent e ) {
		return new MouseEvent(e.getComponent(), MouseEvent.MOUSE_ENTERED, e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger()	);
	}
	
	private void eventReceivedTest(AWTEvent e) {
		System.out.println("Received: " + e);
	}
}

