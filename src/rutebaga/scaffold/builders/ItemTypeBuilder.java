package rutebaga.scaffold.builders;

import rutebaga.model.item.ItemType;
import rutebaga.model.item.SlotType;
import rutebaga.scaffold.MasterScaffold;

public class ItemTypeBuilder extends InstanceBuilder
{

	@Override
	protected String getDefaultFileName()
	{
		return "config/items";
	}

	public Object create(String id)
	{
		ItemType itemType = new ItemType();
		return itemType;
	}

	@Override
	public void initialize(String id, Object object, MasterScaffold scaffold)
	{
		super.initialize(id, object, scaffold);
		ItemType itemType = (ItemType) object;

		// BASIC PROPERTIES
		Boolean equippable = getBoolean(id, "equippable");
		itemType.setEquippable(equippable == null ? false : equippable);
		
		Boolean useable = getBoolean(id, "usable");
		itemType.setUsable(useable == null ? false : useable);
		
		// ALLOCATIONS
		// FORMAT: idx*2 -> slot name; idx*2+1 -> qty
		String[] slots = getStringArray(id, "allocation", "[\\s\\t]");
		for(int i=0; i<slots.length; i+=2)
		{
			SlotType type = (SlotType) scaffold.get(slots[i]);
			int qty = Integer.parseInt(slots[i+1]);
			//XXX LOD
			itemType.getAllocation().add(type, qty);
		}
		
		// Usable Entity EFFECTS
		for(Object obj : getObjectArray(id, "useeffects", "[\\s\\t]", scaffold))
		{
			itemType.getUsableEffects().add(obj);
		}
		
		// REVERSIBLE EFFECTS
		for(Object obj : getObjectArray(id, "reffects", "[\\s\\t]", scaffold))
		{
			itemType.getReversibleEffects().add(obj);
		}
		
		// REVERSIBLE EFFECTS
		for(Object obj : getObjectArray(id, "effects", "[\\s\\t]", scaffold))
		{
			itemType.getPermanentEffects().add(obj);
		}
		

		
	}

}
