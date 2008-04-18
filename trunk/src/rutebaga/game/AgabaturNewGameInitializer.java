package rutebaga.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;

import rutebaga.commons.math.ConstantValueProvider;
import rutebaga.commons.math.Vector2D;
import rutebaga.controller.GameInitializer;
import rutebaga.model.entity.CharEntity;
import rutebaga.model.entity.Entity;
import rutebaga.model.entity.npc.NPCEntity;
import rutebaga.model.environment.Appearance;
import rutebaga.model.environment.Environment;
import rutebaga.model.environment.Hex2DTileConvertor;
import rutebaga.model.environment.Instance;
import rutebaga.model.environment.World;
import rutebaga.model.map.Tile;
import rutebaga.scaffold.MasterScaffold;
import rutebaga.test.view.TemporaryMover;
import rutebaga.view.game.FPSTextComponent;
import rutebaga.view.game.MapComponent;
import rutebaga.view.rwt.ButtonComponent;
import rutebaga.view.rwt.ScrollDecorator;
import rutebaga.view.rwt.ViewCompositeComponent;
import temporary.Bumper;
import temporary.WindTunnel;

public class AgabaturNewGameInitializer implements GameInitializer
{
	private CharEntity avatar;
	private World world;
	private MasterScaffold scaffold;

	public AgabaturNewGameInitializer(MasterScaffold scaffold)
	{
		this.scaffold = scaffold;
	}

	public void build()
	{
		Environment environment = new Environment(new Hex2DTileConvertor());

		Properties config = (Properties) scaffold.get("confGame");

		int size = Integer.parseInt(config.getProperty("size"));
		int mapBounds[] =
		{ 0, size, 0, size };

		double tileProb = Double.parseDouble(config.getProperty("tileProb"));

		boolean showTreasure = Boolean.parseBoolean(config
				.getProperty("showTreasure"));

		int numNpcs = Integer.parseInt(config.getProperty("npcQty"));

		Image cheese = (Image) scaffold.get("imgCheese");
		Image grass = (Image) scaffold.get("imgHextile");
		Image treasure = (Image) scaffold.get("imgTreasure");

		Random random = new Random();

		for (int x = mapBounds[0]; x < mapBounds[1]; x++)
		{
			for (int y = mapBounds[2]; y < mapBounds[3]; y++)
			{
				if (random.nextDouble() < tileProb)
				{
					Vector2D location = new Vector2D(x, y);
					Tile tile = new Tile();
					Appearance water = new Appearance(tile);
					water.setImage(grass);
					tile.setAppearance(water);
					environment.add(tile, location);
				}
			}
		}

		if (showTreasure)
		{
			for (int x = mapBounds[0] + 4; x < mapBounds[1] - 4; x++)
			{
				for (int y = mapBounds[2] + 4; y < mapBounds[3] / 2 - 4; y++)
				{
					Vector2D location = new Vector2D(x, y);
					if (x % 8 == 0 && y % 8 == 0)
					{
						WindTunnel tunnel = new WindTunnel();
						Appearance chest = new Appearance(tunnel);
						chest.setImage(treasure);
						tunnel.setAppearance(chest);
						environment.add(tunnel, location);
					}
				}
			}

			for (int x = mapBounds[0] + 4; x < mapBounds[1] - 4; x++)
			{
				for (int y = mapBounds[3] / 2 + 4; y < mapBounds[3] - 4; y++)
				{
					Vector2D location = new Vector2D(x, y);
					if (x % 8 == 0 && y % 8 == 0)
					{
						Instance instance = new Bumper();
						Appearance chest = new Appearance(instance);
						chest.setImage(treasure);
						instance.setAppearance(chest);
						environment.add(instance, location);
					}
				}
			}
		}

		avatar = new CharEntity();
		avatar.setMovementSpeedStrat(new ConstantValueProvider<Entity>(.09));

		// npc = new NPCEntity();
		// Appearance npcAppearance = new Appearance(npc);
		// npcAppearance.setImage(treasure);
		// npc.setAppearance(npcAppearance);
		//		
		// environment.add(npc, new Vector2D(20, 20));

		Appearance appearance = new Appearance(avatar);
		appearance.setImage(cheese);
		avatar.setAppearance(appearance);

		environment.add(avatar, new Vector2D(0, 0));

		int xRng = mapBounds[1] - mapBounds[0];
		int yRng = mapBounds[3] - mapBounds[2];
		int xMin = mapBounds[0];
		int yMin = mapBounds[2];

		for (int i = 0; i < numNpcs; i++)
		{
			NPCEntity npc1 = new NPCEntity();
			npc1.setMovementSpeedStrat(new ConstantValueProvider<Entity>(.09));
			Appearance npcAppearance1 = new Appearance(npc1);
			npcAppearance1.setImage(cheese);
			npc1.setAppearance(npcAppearance1);

			npc1.setTarget(avatar);

			Vector2D location = new Vector2D(random.nextInt(xRng) + xMin,
					random.nextInt(yRng) + yMin);
			System.out.println("placing npc at " + location);
			environment.add(npc1, location);
		}
		
		world = new World();
		world.add(environment);

	}

	public CharEntity getAvatar()
	{
		return avatar;
	}

	public World getWorld()
	{
		return world;
	}

}
