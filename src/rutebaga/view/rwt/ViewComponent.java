package rutebaga.view.rwt;

import java.awt.AWTEvent;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import rutebaga.view.drawer.Drawer;

/**
 * Provides the base functionality of all components within the Windowing
 * Toolkit. All ViewComponents have many states in common such as screen
 * position(x, y), z-index, bounding boxes, and so on. This abstract class
 * removes these basic functionalities of a component and encapsulates it all
 * within the ViewComponent. Each sub-classing component can focus on how to
 * draw itself rather than the tedium of maintaining its other states.
 * <p>
 * Responsibilities are to
 * <ul>
 * <li>Store screen position (x, y).
 * <li>Store z-index to determine drawing order.
 * <li> Maintain the bounds of the component (which can be used for culling or
 * mouse hit detection).
 * <li>If the bounds have changed, force a dirtyBounds() call up to its parents
 * so they may recalculate their bounds.
 * <li>Maintain the parent ViewComponent of this ViewComponent (if any).
 * <li>Process key, mouse and mouseMotion events that are received from the
 * EventDispatcher.
 * 
 * 
 * @author Ryan
 * 
 */
public abstract class ViewComponent
{

	private ViewComponent parent;
	@SuppressWarnings("unused")
	// TODO
	private boolean isVisible, hasFocus, dirtyBounds;
	private Point screenPosition = new Point(0, 0);

	private Shape bounds = new Rectangle();
	

	/**
	 * Renders this ViewComponent.
	 * 
	 * @param draw
	 *            The {@link Drawer} to use for rendering.
	 */

	public abstract void draw(Drawer draw);


	public void visit(ViewVisitor visitor) {
		visitor.visit(this);
	}
	
	/**
	 * Returns the parent ViewComponent of this ViewComponent, if it exists.
	 * 
	 * @return Parent ViewComponent of this ViewComponent if it exists,
	 *         otherwise returns null.
	 */
	public ViewComponent getParent()
	{
		return parent;
	}

	/**
	 * Returns the Point describing where this ViewComponent is located.
	 * 
	 * @return The Point representing the upper-left corner of this
	 *         ViewComponent.
	 */
	public Point getLocation()
	{
		return (Point) screenPosition.clone();
	}

	/**
	 * Returns the horizontal position of this ViewComponent. The same as
	 * {@link #getLocation()}.x.
	 * 
	 * @return displacement of the left edge of this ViewComponent from the left
	 *         edge of the View.
	 */
	public int getX()
	{
		return screenPosition.x;
	}

	/**
	 * Returns the vertical position of this ViewComponent. The same as
	 * {@link #getLocation()}.y.
	 * 
	 * @return displacement of the top edge of this ViewComponent from the top
	 *         edge of the View.
	 */
	public int getY()
	{
		return screenPosition.y;
	}


	public int getWidth() {
		return bounds.getBounds().width;
	}
	
	public int getHeight() {
		return bounds.getBounds().height;
	}
	
	/**
	 * Moves this ViewComponent to the specified Point.
	 * 
	 * @param p
	 *            The new upper-left corner of this ViewComponent.
	 */
	public void setLocation(Point p)
	{
		this.screenPosition = p;
	}

	/**
	 * Moves this ViewComponent to the specified Point. The same as
	 * setLocation(new Point(x,y)).
	 * 
	 * @param x
	 *            The new displacement of the left edge of this ViewComponent
	 *            from the left edge of the View.
	 * @param y
	 *            The new displacement of the top edge of this ViewComponent
	 *            from the top edge of the View.
	 */
	public void setLocation(int x, int y)
	{
		screenPosition.setLocation(x, y);
	}

	/**
	 * Sets the provided ViewComponent as the parent of this ViewComponent.
	 * 
	 * @param parent
	 *            ViewComponent that will be set as this ViewComponent's parent.
	 */
	public void setParent(ViewComponent parent)
	{
		this.parent = parent;
	}

	/**
	 * Returns the current visibility of this ViewComponent.
	 * 
	 * @return True if this ViewComponent is currently visible.
	 */
	public boolean isVisible()
	{
		return isVisible;
	}

	/**
	 * Sets the visiblity of this ViewComponent.
	 * 
	 * @param visibility
	 */
	public void setVisible(boolean visibility)
	{
		this.isVisible = visibility;
	}

	/**
	 * Determines if this ViewComponent is in focus and able to receive events.
	 * 
	 * @return True if this ViewComponent has focus.
	 */
	public boolean hasFocus()
	{
		return hasFocus;
	}

	/**
	 * Sets the focus of this ViewComponent to the specified boolean.
	 * 
	 * @param hasFocus
	 *            The desired focus.
	 */
	public void setHasFocus(boolean hasFocus)
	{
		this.hasFocus = hasFocus;
	}

	/**
	 * Returns the Bounds of this ViewComponent.
	 * 
	 * @return The Bounds of this ViewComponent.
	 */
	public Shape getBounds() {
		return bounds;
	}

	
	public void setBounds( Shape bounds ) {
		dirtyBounds();
		this.bounds = bounds;
	}
	
	public void setBounds( int x, int y, int width, int height ) {
		setLocation(x, y);
		setBounds(new Rectangle(width, height));
	}
	

	protected void dirtyBounds()
	{
		dirtyBounds = true;
		if(parent != null) 
			parent.dirtyBounds();
	}
	
	protected boolean isDirty() {
		return dirtyBounds;
	}
	
	protected boolean processKeyEvent( KeyEvent event ) { eventReceived(event); return false;}	
	protected boolean processMouseEvent( MouseEvent event ) {eventReceived(event); return false;}
	protected boolean processMouseMotionEvent( MouseEvent event ) {eventReceived(event); return false;}


	private void eventReceived(AWTEvent e)
	{
		rutebaga.commons.Log.log("ViewComponent: " + this + " received " + e);
	}
}
