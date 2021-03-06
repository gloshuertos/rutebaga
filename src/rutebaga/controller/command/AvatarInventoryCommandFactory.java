package rutebaga.controller.command;

import java.util.List;

import rutebaga.controller.command.list.ConcreteElementalList;
import rutebaga.controller.command.list.ElementalList;
import rutebaga.model.entity.Entity;
import rutebaga.model.entity.EntityEffect;
import rutebaga.model.item.Item;
import rutebaga.view.UserInterfaceFacade;

public class AvatarInventoryCommandFactory implements CommandFactory<Item> {
	
	private final Entity<?> avatar;
	private final UserInterfaceFacade facade;
	private CommandQueue queue;
	
	public AvatarInventoryCommandFactory(Entity<?> avatar, UserInterfaceFacade facade, CommandQueue queue) {
		this.avatar = avatar;
		this.facade = facade;
		this.queue = queue;
	}
	
	public ElementalList getCommandListFor(Item item) {
		ConcreteElementalList list = new ConcreteElementalList();
		list.add("Drop", QueueCommand.makeForQueue(new DropCommand(item), queue));
		if (item.isEquippable())
			list.add("Equip", QueueCommand.makeForQueue(new EquipCommand(item), queue));
		if (item.isUsable())
			list.add("Use", QueueCommand.makeForQueue(new UseCommand(item), queue));
		return list;
	}
	
	private class DropCommand implements Command {
		private Item item;
		
		public DropCommand(Item item) {
			this.item = item;
		}
		
		public boolean isFeasible() {
			return true;
		}
		
		public void execute() {
			avatar.getInventory().drop(item);
			facade.clearMenuStack();
		}
	}
	
	private class EquipCommand implements Command {

		private Item item;
		
		public EquipCommand(Item item) {
			this.item = item;
		}
		
		public boolean isFeasible() {
			return true;
		}
		
		public void execute() {
			avatar.getInventory().equip(item);
			facade.clearMenuStack();
		}
	}
	
	private class UseCommand implements Command {
		
		private Item<?> item;
		
		public UseCommand(Item<?> item) {
			this.item = item;
		}

		public void execute() {
			List<EntityEffect> effects = item.getUsableEffects();
			
			for(EntityEffect effect : effects)
				avatar.accept(effect, null);
			
			avatar.getInventory().remove(item);
			facade.clearMenuStack();
		}

		public boolean isFeasible() {
			return true;
		}
		
	}
}
