package rutebaga.controller.command.list;

import java.util.Iterator;
import java.util.Observable;

import rutebaga.controller.command.Command;


/**
 * 
 * An ElementalList is an ordered collection of {@link ListElement ListElements}.
 * Clients access ListElements via the provided Iterator. ElementalLists allow
 * the Controller to encapsulate a list of named {@link Command Commands} to
 * pass to the View. Clients of ElementalList should be aware that
 * implementations of ElementalList may return a different label and collection
 * of ListElements each time the list is queried.
 * 
 * @author Matthew Chuah
 * @see Command
 * @see ListElement
 */
public interface ElementalList extends Iterable<ListElement>
{

	/**
	 * Gets the current label for this ElementalList describing the collection
	 * as a whole.
	 * 
	 * @return the label for this ElementalList
	 */
	public String getLabel();

	/**
	 * Gets an Iterator over the {@link ListElement ListElements} of this
	 * ElementalList.
	 * 
	 * @see ListElement
	 * @return an Iterator of ListElements
	 */
	public Iterator<ListElement> iterator();
	
	/**
	 * Returns the size of the size of the set of ListElements that this ElementalList's Iterator returns.
	 * @return
	 */
	public int contentSize();
	
	/**
	 * Indicates whether this ElementalList has changed since the last time hasChanged was accessed
	 * by the specified object.  Returns true the first it is invoked by the object.
	 * @return true if this ElementalList has changed since the last time it was checked
	 */
	public boolean hasChanged(Object object);
}
