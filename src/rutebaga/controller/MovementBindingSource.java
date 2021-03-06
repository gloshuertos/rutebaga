package rutebaga.controller;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import rutebaga.commons.math.Vector2D;
import rutebaga.controller.command.Command;
import rutebaga.controller.keyboard.ConcreteKeyBinding;
import rutebaga.controller.keyboard.KeyBinding;
import rutebaga.controller.keyboard.KeyBindingList;
import rutebaga.controller.keyboard.KeyCode;
import rutebaga.model.entity.Entity;
import rutebaga.model.environment.TileConverter;

public class MovementBindingSource {

	private KeyBindingList<Command> bindings;
	private Entity entity;
	private Map<Double, KeyBinding<Command>> directions;

	public static MovementBindingSource defaultBindings(Entity entity,
			KeyBindingList<Command> bindings) {
		final Vector2D SOUTH = new Vector2D(0, 1);
		final Vector2D NORTH = new Vector2D(0, -1);
		final Vector2D SOUTHEAST = new Vector2D(1, 1);
		final Vector2D NORTHEAST = new Vector2D(1, -1);
		final Vector2D SOUTHWEST = new Vector2D(-1, 1);
		final Vector2D NORTHWEST = new Vector2D(-1, -1);

		MovementBindingSource manager = new MovementBindingSource(entity,
				bindings);
		manager.setDirectionBinding("Northwest", KeyCode.get(KeyEvent.VK_Q), NORTHWEST);
		manager.setDirectionBinding("North", KeyCode.get(KeyEvent.VK_W), NORTH);
		manager.setDirectionBinding("Northeast", KeyCode.get(KeyEvent.VK_E), NORTHEAST);
		manager.setDirectionBinding("Southwest", KeyCode.get(KeyEvent.VK_A), SOUTHWEST);
		manager.setDirectionBinding("South", KeyCode.get(KeyEvent.VK_S), SOUTH);
		manager.setDirectionBinding("Southeast", KeyCode.get(KeyEvent.VK_D), SOUTHEAST);

		return manager;
	}

	public MovementBindingSource(Entity entity,
			KeyBindingList<Command> bindings) {
		if (entity == null)
			throw new NullPointerException();
		this.entity = entity;
		this.bindings = bindings;
		this.directions = new HashMap<Double, KeyBinding<Command>>();
	}

	private void removeOldKeyBinding(double angle) {
		KeyBinding<Command> binding = directions.remove(angle);
		if (binding != null)
			bindings.remove(binding);
	}

	public void setDirectionBinding(String name, KeyCode code, Vector2D direction) {
		double angle = direction.getAngle();
		removeOldKeyBinding(angle);
		Command command = new MovementCommand(direction);
		bindings.set(name, code, command);
		directions.put(angle, new ConcreteKeyBinding<Command>(name, code, command));
	}

	public Command[] getMovementCommands() {
		Command[] commands = new Command[bindings.size()];
		int pos = 0;
		for (KeyBinding<Command> binding : bindings)
			commands[pos++] = binding.getBinding();
		return commands;
	}

	private class MovementCommand implements Command {
		private Vector2D direction;

		public MovementCommand(Vector2D direction) {
			this.direction = direction;
		}

		public void execute() {
			TileConverter conv = entity.getEnvironment().getTileConvertor();
			Vector2D tileDirection = new Vector2D(conv.fromRect(direction));
			tileDirection = conv.closestDirection(tileDirection);
			entity.walk(tileDirection);
		}

		public boolean isFeasible() {
			return true;
		}
	}
}
