package rutebaga.model.entity;

import java.util.ArrayList;
import java.util.List;

import rutebaga.commons.logic.ChainedRule;
import rutebaga.commons.logic.Rule;
import rutebaga.commons.math.Vector2D;
import rutebaga.model.Named;
import rutebaga.model.environment.ConcreteInstanceSet;
import rutebaga.model.environment.Environment;
import rutebaga.model.environment.Instance;
import rutebaga.model.environment.InstanceSet;

public class Ability<T> implements Named
{
	private AbilityCategory category;

	private Entity entity;

	private ChainedRule<Entity> feasibilityRule = new ChainedRule<Entity>(true);
	private ChainedRule<Entity> existenceRule = new ChainedRule<Entity>(true);

	private List<AbilityAction<T>> actions = new ArrayList<AbilityAction<T>>();
	
	private AbilityType type;
	
	private String name;

	public void act(T target)
	{
		if(!isFeasible() || !exists())
			return;
		rutebaga.commons.Log.log("ACTING!!!");
		if (isFeasible())
		{
			entity.resetCooldown();
			for (AbilityAction<T> action : actions)
				if(action != null)
					action.act(this, target);
		}
	}

	public void addAction(AbilityAction<T> action)
	{
		actions.add(action);
	}

	public void addExistenceRule(Rule<Entity> rule)
	{
		existenceRule.add(rule);
	}

	public void addFeasibilityRule(Rule<Entity> rule)
	{
		feasibilityRule.add(rule);
	}

	public boolean canActOn(Instance<?> i)
	{
		if(! (i instanceof Entity))
			return false;
		Entity entity = (Entity) i;
		if(entity.isDead())
			return false;
		return true;
	}

	public boolean exists()
	{
		return existenceRule.determine(entity);
	}
	
	public AbilityCategory getCategory()
	{
		return category;
	}

	public Vector2D getCoordinate()
	{
		return entity.getCoordinate();
	}

	public Entity getEntity()
	{
		return entity;
	}

	public Environment getEnvironment()
	{
		return entity.getEnvironment();
	}

	public String getName()
	{
		return name;
	}

	public AbilityType getType()
	{
		return type;
	}

	public boolean isFeasible()
	{
		if(entity.getCooldown() > 0)
		{
			return false;
		}
		return feasibilityRule.determine(entity);
	}

	public void setCategory(AbilityCategory category)
	{
		this.category = category;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setType(AbilityType type)
	{
		this.type = type;
	}
	
	protected void setEntity(Entity entity)
	{
		this.entity = entity;
	}

}
