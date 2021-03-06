package rutebaga.commons.logic;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import rutebaga.model.entity.Entity;

/**
 * A rule based on a series of other rules.
 * 
 * For a ChainedRule, behavior is determined by the default value of the
 * {@link Rule}: a default value of true implies AND semantics (the rule is
 * true iff all rules are true), whereas a default value of false implies OR
 * semantics (the rule is false iff all rules are false).
 * 
 * @author Gary LosHuertos
 * 
 * @param <T>
 *            the context type
 */
public class ChainedRule<T> implements Rule<T> {
	private final boolean defaultValue;

	private Collection<Rule<T>> rules = new LinkedList<Rule<T>>();

	/**
	 * Constructs a new ChainedRule with the specified default value.
	 * 
	 * @param defaultValue
	 *            A boolean value that determines the default behavior of this
	 *            ChainRule. True implies AND semantics (the rule is true iff
	 *            all rules are true). False implies OR semantics (the rule is
	 *            false iff all rules are false).
	 */
	public ChainedRule(boolean defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @param rule
	 *            A rule to add on to this ChainedRule.
	 */
	public void add(Rule<T> rule) {
		this.rules.add(rule);
	}

	/**
	 * @param rules
	 *            A Collection of Rules to add to this ChainedRule.
	 */
	public void addAll(Collection<Rule<T>> rules) {
		this.rules.addAll(rules);
	}

	/**
	 * Returns the the status of this rule in the specified context. The
	 * semantics of the ChainedRule is specified by the default value.
	 * 
	 * @param context
	 *            the context in which the rule runs.
	 * @return A boolean corresponding to this Rule's agreement with the default
	 *         value in the given context.
	 * @see rutebaga.commons.logic.Rule#determine(Object)
	 */
	public boolean determine(T context) {
		for (Rule<T> rule : rules)
			if (rule.determine(context) != defaultValue)
				return !defaultValue;
		return defaultValue;
	}

	/**
	 * @return The collection of all the Rules in this ChainedRule.
	 */
	public Collection<Rule<T>> getRules() {
		return Collections.unmodifiableCollection(rules);
	}

	/**
	 * @param rule
	 *            The specific Rule to remove from this ChainedRule.
	 */
	public void remove(Rule<Entity> rule) {
		this.rules.remove(rule);
	}

}
