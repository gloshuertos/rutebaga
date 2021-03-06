package rutebaga.test.model.ability;

import rutebaga.model.entity.Ability;
import rutebaga.model.entity.AbilityType;
import rutebaga.model.entity.ability.SpawnAction;
import rutebaga.model.environment.Instance;
import rutebaga.model.environment.InstanceType;
import rutebaga.test.model.effect.RandomEffectType;

public class CheeseArrowAbilityType extends AbilityType
{
	private RandomEffectType effectType;

	public InstanceType getEffectType()
	{
		return effectType;
	}

	public Ability makeAbility()
	{
		Ability<Instance> ability = super.makeAbility();

		SpawnAction spawn = new SpawnAction(effectType);
		
		ability.addAction(spawn);
		
		return ability;
	}

	public void setEffectType(RandomEffectType effectType)
	{
		this.effectType = effectType;
	}
}
