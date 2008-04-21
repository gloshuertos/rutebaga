package rutebaga.model.item;

import java.util.ArrayList;
import java.util.List;

import rutebaga.model.entity.EntityEffect;
import rutebaga.model.entity.ReversibleEntityEffect;
import rutebaga.model.environment.ConcreteInstanceType;
import rutebaga.model.environment.InstanceType;

public class ItemType<T extends Item> extends ConcreteInstanceType<T>
{
	private boolean equippable = false;
	private SlotAllocation allocation = new SlotAllocation();
	private List<EntityEffect> permanentEffects = new ArrayList<EntityEffect>();
	private List<ReversibleEntityEffect> reversibleEffects = new ArrayList<ReversibleEntityEffect>();
	private double mass = 1.0;

	@Override
	public String toString()
	{
		return "ItemType " + getName() + "("
				+ (equippable ? "must be equipped" : "may be equipped")
				+ ") with " + permanentEffects.size() + " perms and "
				+ reversibleEffects.size() + " reversible";
	}

	public SlotAllocation getAllocation()
	{
		return allocation;
	}

	public List<EntityEffect> getPermanentEffects()
	{
		return permanentEffects;
	}

	public List<ReversibleEntityEffect> getReversibleEffects()
	{
		return reversibleEffects;
	}

	public boolean isEquippable()
	{
		return equippable;
	}

	public void setAllocation(SlotAllocation allocation)
	{
		this.allocation = allocation;
	}

	public void setEquippable(boolean equippable)
	{
		this.equippable = equippable;
	}

	public void setPermanentEffects(List<EntityEffect> permanentEffects)
	{
		this.permanentEffects = permanentEffects;
	}

	public void setReversibleEffects(
			List<ReversibleEntityEffect> reversibleEffects)
	{
		this.reversibleEffects = reversibleEffects;
	}

	@Override
	protected T create()
	{
		return (T) new Item(this);
	}

	@Override
	protected void initialize(T instance)
	{
		super.initialize(instance);
		if (equippable)
		{
			EquippableAspect equip = new EquippableAspect();
			equip.getAllocation().add(allocation);
			equip.getPermanentEquipEffects().addAll(permanentEffects);
			equip.getReversibleEquipEffects().addAll(reversibleEffects);
			instance.setEquippableAspect(equip);
			instance.setMass(mass);
		}
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}
}
