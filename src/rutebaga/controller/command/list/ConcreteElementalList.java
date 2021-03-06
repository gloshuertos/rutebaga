package rutebaga.controller.command.list;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Set;

import rutebaga.commons.logic.Rule;
import rutebaga.controller.command.Command;
import rutebaga.controller.command.CommandFactory;
import rutebaga.controller.command.FixedLabelDeterminer;
import rutebaga.controller.command.LabelDeterminer;

/**
 * 
 * ConcreteElementalList is a basic implementation of {@link ElementalList} that
 * composites several named {@link Command}s. The ConcreteElementalList is also
 * labeled, as required by its interface.
 * 
 * ConcreteElementalList offers two add mutators, one which
 * {@link #add(String, Command) adds} a named String-Command pair and one which
 * {@link #add(ElementalList) adds} a set of Commands in the form of an
 * ElementalList. Composited Commands cannot be removed, but can be accessed via
 * the Iterator, as per the interface. The Iterator will return ListElements
 * containing the Commands in the same order they were added to the
 * ConcreteElementalList. Commands added to the ConcreteElementalList via the
 * add(ElementalList) operation will be returned in the same order the
 * ElementalList Iterator returns them. Duplicate Commands are both possible and
 * allowed, so clients of ConcreteElementalList must take care to avoid
 * duplicates if they desire Set semantics.
 * 
 * @author Matthew Chuah
 */
public class ConcreteElementalList implements ElementalList,
		CommandFactory<Object> {
	/**
	 * The label of this ConcreteElementalList.
	 */
	private String label;

	/**
	 * The list of ElementalLists for this ConcreteElementalList. The Iterator
	 * iterates over this list and then over the ListElements within each
	 * ElementalList.
	 */
	private LinkedList<ElementalList> list = new LinkedList<ElementalList>();

	/**
	 * Indicates whether this ConcreteElementalList has been changed wrt the object.  
	 */
	private Set<Object> changed = new HashSet<Object>();

	private Rule<Object> changedDeterminer;
	
	private void change() {
		changed.clear();
	}
	
	/**
	 * Returns an ElementalList of Commands that just returns this
	 * ConcreteElementalList.
	 * 
	 * @see rutebaga.controller.command.CommandFactory#getCommandListFor(java.lang.Object)
	 */
	public ElementalList getCommandListFor(Object object) {
		return this;
	}

	/**
	 * Returns the size of this ElementalList. Runs in O(n) time, where n is the
	 * number of elements in the ElementalList.
	 * 
	 * @see rutebaga.controller.command.list.ElementalList#contentSize()
	 */
	public int contentSize() {
		int size = 0;
		for (ElementalList element : list)
			size += element.contentSize();
		return size;
	}

	/**
	 * Add a String to this ConcreteElementalList without a Command.
	 * 
	 * @param label
	 *            the String element to add to this list
	 */
	public void add(String label) {
		add(label, null);
		change();
	}

	/**
	 * Add a named Command to this ConcreteElementalList. It is admissible for
	 * the label to be null, in which case it will be replaced by the empty
	 * String.
	 * 
	 * @param label
	 *            the String element to add to this list
	 * @param command
	 *            the Command to associate with this list element
	 */
	public void add(String label, Command command) {
		// To hell with efficiency!
		list.add(new SingleCommandElementalList(label, command));
		change();
	}

	/**
	 * Add a named Command to this ConcreteElementalList with the given
	 * LabelDeterminer.
	 * 
	 * @param label
	 *            the String element to add to this list
	 * @param command
	 *            the Command to associate with this list element
	 */
	public void add(LabelDeterminer label, Command command) {
		// To hell with efficiency!
		list.add(new SingleCommandElementalList(label, command));
		change();
	}

	
	/**
	 * Add an entire ElementalList as a component of this ConcreteElementalList.
	 * When this ConcreteElementalList's iterator runs, it will visit each of
	 * the specified ElementalList's ListElements in the order its Iterator
	 * returns. No-op if <code>list</code> is null.
	 * 
	 * @param list
	 *            the ElementalList to add as a child to this
	 *            ConcreteElementalList
	 */
	public void add(ElementalList list) {
		if (list != null) {
			this.list.add(list);
			change();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see rutebaga.controller.ElementalList#getLabel()
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the label of this ConcreteElementalList.
	 * 
	 * @param label
	 *            the new label
	 */
	public void setLabel(String label) {
		this.label = (label != null ? label : "");
		change();
	}

	public void setChangeDeterminer(Rule<Object> determiner) {
		changedDeterminer = determiner;
	}
	
	public Rule<Object> getChangeDeterminer() {
		return changedDeterminer;
	}
	
	/**
	 * Returns an Iterator over all the ListElements contained by this
	 * ConcreteElementalList. If an entire ElementalList has been added as a
	 * child to this ConcreteElementalList, each of the child's ListElements
	 * will be returned by this Iterator.
	 * 
	 * @see ElementalList#iterator()
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<ListElement> iterator() {
		return new ConcreteElementalListIterator();
	}

	/**
	 * @author may
	 * 
	 * A private Iterator that iterates through all the ElementalLists contained
	 * by the parent ConcreteElementalList, visiting each of their ListElements
	 * in turn.
	 */
	private class ConcreteElementalListIterator implements
			Iterator<ListElement> {
		private Iterator<ElementalList> lists = ConcreteElementalList.this.list.iterator();

		private Iterator<ListElement> currentList;

		/**
		 * Determines if there is another unvisited ListElement in the current
		 * ElementalList visited or an unvisited ElementalList contained by this
		 * ConcreteElementalList.
		 */
		public boolean hasNext() {
			return lists.hasNext() || (currentList != null && currentList.hasNext());
		}

		/**
		 * Returns the next unvisited ListElement.
		 * 
		 * @return an unvisited ListElement
		 */
		public ListElement next() {
			// currentList is spent, go to next
			while (currentList == null || !currentList.hasNext()) {
				// lists are spent...the end
				if (!lists.hasNext())
					throw new NoSuchElementException("No further ListElements");
				currentList = lists.next().iterator();
			}
			return currentList.next();
		}

		/**
		 * Don't even think about it.
		 */
		public void remove() throws UnsupportedOperationException {
			throw new UnsupportedOperationException("Bad! Bad!");
		}
	}

	/**
	 * @author may
	 * 
	 * A wrapper for a String-Command list element. This ElementalList contains
	 * a single ListElement, which is accessed via its Iterator, that contains
	 * the label-command pair the SingleCommandElementalList was constructed
	 * with.
	 */
	private static final LabelDeterminer defaultLabelDeterminer = new FixedLabelDeterminer("");
	private class SingleCommandElementalList implements ElementalList {
		
		/**
		 * The label of the ListElement "contained" by this
		 * SingleCommandElementalList.
		 */
		private LabelDeterminer labelDeterminer;
		private String cachedLabel;

		/**
		 * The command of the ListElement "contained" by this
		 * SingleCommandElementalList.
		 */
		private Command command;

		/**
		 * Create a new SingleCommandElementalList with the specified label and
		 * command. If label is null, it will be replaced by the empty string.
		 * 
		 * @param label
		 *            the label of the ListElement to be contained by this
		 *            SingleCommandElementalList
		 * @param command
		 *            the command of the ListElement to be contained by this
		 *            SingleCommandElementalList
		 */
		public SingleCommandElementalList(String label, Command command) {
			this(label == null ? null : new FixedLabelDeterminer(label), command);
		}

		/**
		 * Create a new SingleCommandElementalList with the specified LabelDeterminer
		 * and command. If label is null, it will be replaced by the empty string.
		 * 
		 * @param label
		 *            the label of the ListElement to be contained by this
		 *            SingleCommandElementalList
		 * @param command
		 *            the command of the ListElement to be contained by this
		 *            SingleCommandElementalList
		 */
		public SingleCommandElementalList(LabelDeterminer label, Command command) {
			this.labelDeterminer = (label == null ? defaultLabelDeterminer : label);
			this.command = command;
		}

		public String getLabel() {
			return labelDeterminer.getLabel();
		}

		public int contentSize() {
			return 1;
		}

		/**
		 * Returns an Iterator over a single ListElement defined by the label
		 * and command this SingleCommandElementalList was created with.
		 * 
		 * @see java.lang.Iterable#iterator()
		 */
		public Iterator<ListElement> iterator() {
			return new Iterator<ListElement>() {
				private boolean visited = false;

				public boolean hasNext() {
					return !visited;
				}

				public ListElement next() {
					if (visited)
						throw new NoSuchElementException("Only one ListElement!");
					else {
						visited = true;
						return new ListElement() {
							public String getLabel() {
								return SingleCommandElementalList.this.getLabel();
							}

							public Command getCommand() {
								return SingleCommandElementalList.this.command;
							}
						};
					}
				}

				public void remove() throws UnsupportedOperationException {
					throw new UnsupportedOperationException("Mommy, make the bad man stop ><");
				}
			};
		}

		public boolean hasChanged(Object object) {
			boolean changed = !labelDeterminer.getLabel().equals(cachedLabel);
			cachedLabel = labelDeterminer.getLabel();
			return changed;
		}
	}

	public boolean hasChanged(Object object) {
		if (changedDeterminer != null)
			return changedDeterminer.determine(object);
	
		boolean hasChanged = !changed.contains(object);
		changed.add(object);
		
		for (ElementalList e: list)
			hasChanged |= e.hasChanged(object);
		
		return hasChanged;
	}
}
