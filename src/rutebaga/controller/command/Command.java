package rutebaga.controller.command;

import rutebaga.controller.ActionDeterminer;
import rutebaga.controller.command.list.ListElement;

/**
 * 
 * Command objects encapsulate an action and its parameters. A Command is
 * typically produced by the Controller and passed to the View, who can execute
 * it agnostic of its meaning, but Command objects may be used in any context.
 * Clients of Commands should be aware that Commands may have state and change
 * their feasibility arbitrarily without notification.
 * 
 * @author Matthew Chuah
 * @see ActionDeterminer
 * @see ListElement
 */
public interface Command
{
	/**
	 * Returns the feasibility state of this Command. The execute operation
	 * should not be invoked if isFeasible returns false.
	 * 
	 * @return true if it is feasible to execute this Command
	 * @see #isFeasible()
	 */
	public boolean isFeasible();

	/**
	 * Executes this Command. The behavior of this operation is undefined when
	 * isFeasible indicates that this Command is not feasible. The execute
	 * operation may be invoked multiple times on a single instance, as long as
	 * isFeasible is true each time. An implementation can require that execute
	 * not be invoked more than once by causing isFeasible to become false after
	 * execute is called.
	 * 
	 * @see #isFeasible()
	 */
	public void execute();
}
