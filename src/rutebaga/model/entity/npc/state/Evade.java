package rutebaga.model.entity.npc.state;

import rutebaga.model.entity.npc.NPCEntity;
import rutebaga.model.entity.npc.NPCState;

/**
 * @author nicholasstamas
 * 
 */
public class Evade extends NPCState
{

	@Override
	public NPCState barter(NPCEntity npc)
	{
		rutebaga.commons.Log.log("I can't barter, I'm running away.");
		return this;
	}

	@Override
	public NPCState makeFriendly(NPCEntity npc)
	{
		return NPCState.wander;
	}

	@Override
	public NPCState makeHostile(NPCEntity npc)
	{
		return this;
	}

	@Override
	public NPCState speak(NPCEntity npc)
	{
		rutebaga.commons.Log.log("I'm scared of you! Ahhh!");
		return this;
	}

	@Override
	public NPCState tick(NPCEntity npc)
	{
		// TODO: evasion logic
		return this;
	}

}
