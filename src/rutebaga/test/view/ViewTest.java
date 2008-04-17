package rutebaga.test.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import rutebaga.commons.math.Vector;
import rutebaga.model.entity.CharEntity;
import rutebaga.model.entity.EntityEffect;
import rutebaga.model.entity.npc.NPCEntity;
import rutebaga.model.environment.Appearance;
import rutebaga.model.environment.Environment;
import rutebaga.model.environment.Rect2DTileConvertor;
import rutebaga.model.map.Tile;
import rutebaga.view.game.FPSTextComponent;
import rutebaga.view.game.MapComponent;
import rutebaga.view.rwt.*;

public class ViewTest
{

	private static Random random = new Random();

	private static int SCREENWIDTH = 800, SCREENHEIGHT = 600;

	private static int N_NPCS = 0;
	private static double TILE_PROB = 1;

	private static int[] MAP_BOUNDS =
	{ 0, 30, 0, 30 };

	private static boolean TICK_ENVIRONMENT = true;
	private static boolean RENDER_MAP = true;
	private static boolean USE_VOLATILE_IMAGES = false;

	public static void main(String args[])
	{
		View view = new View(SCREENWIDTH, SCREENHEIGHT);
		view.setFullscreen();

		CharEntity avatar;
		CharEntity npc;

		Environment environment = new Environment(new Rect2DTileConvertor());

		try
		{
			VolatileImage tmp;

			Image cheese = ImageIO.read(new File("TestImages/cheese.png"));
			tmp = view.makeVolatileImage(cheese.getWidth(null), cheese
					.getHeight(null));
			Graphics g = tmp.getGraphics();
			g.drawImage(cheese, 0, 0, null);
			g.dispose();
			if (USE_VOLATILE_IMAGES)
				cheese = tmp;
			cheese.setAccelerationPriority(1.0f);

			Image grass = ImageIO.read(new File("TestImages/grass.jpg"));
			tmp = view.makeVolatileImage(grass.getWidth(null), grass
					.getHeight(null));
			g = tmp.getGraphics();
			g.drawImage(grass, 0, 0, null);
			g.dispose();
			if (USE_VOLATILE_IMAGES)
				grass = tmp;
			grass.setAccelerationPriority(1.0f);

			Image treasure = ImageIO.read(new File("TestImages/treasure.png"));
			treasure.setAccelerationPriority(1.0f);
			tmp = view.makeVolatileImage(treasure.getWidth(null), treasure
					.getHeight(null));
			g = tmp.getGraphics();
			g.drawImage(treasure, 0, 0, null);
			g.dispose();
			if (USE_VOLATILE_IMAGES)
				treasure = tmp;
			treasure.setAccelerationPriority(1.0f);

			Random random = new Random();

			for (int x = MAP_BOUNDS[0]; x < MAP_BOUNDS[1]; x++)
			{
				for (int y = MAP_BOUNDS[2]; y < MAP_BOUNDS[3]; y++)
				{
					if (random.nextDouble() < TILE_PROB)
					{
						Vector location = new Vector(x, y);
						Tile tile = new Tile()
						{

							@Override
							public double getFriction()
							{
								return 0.1;
							}

						};
						Appearance water = new Appearance(tile);
						water.setImage(grass);
						tile.setAppearance(water);
						environment.add(tile, location);
					}
				}
			}

			// for (int x = 0; x < 30; x++)
			// {
			// for (int y = 0; y < 30; y++)
			// {
			// Vector location = new Vector(x, y);
			// if (x % 8 == 0 && y % 8 == 0)
			// {
			// WindTunnel tunnel = new WindTunnel();
			// Appearance chest = new Appearance(tunnel);
			// chest.setImage(treasure);
			// tunnel.setAppearance(chest);
			// environment.add(tunnel, location);
			// }
			// }
			// }
			//
			// for (int x = 0; x < 30; x++)
			// {
			// for (int y = 40; y < 70; y++)
			// {
			// Vector location = new Vector(x, y);
			// if (x % 8 == 0 && y % 8 == 0)
			// {
			// Instance instance = new Bumper();
			// Appearance chest = new Appearance(instance);
			// chest.setImage(treasure);
			// instance.setAppearance(chest);
			// environment.add(instance, location);
			// }
			// }
			// }

			avatar = new CharEntity()
			{

				@Override
				public double getFriction()
				{
					return 0;
				}

				@Override
				public double getMass()
				{
					return 1;
				}

				@Override
				public Object accept(EntityEffect e)
				{
					return null;
				}

			};

			// npc = new NPCEntity();
			// Appearance npcAppearance = new Appearance(npc);
			// npcAppearance.setImage(treasure);
			// npc.setAppearance(npcAppearance);
			//		
			// environment.add(npc, new Vector(20, 20));

			Appearance appearance = new Appearance(avatar);
			appearance.setImage(cheese);
			avatar.setAppearance(appearance);

			environment.add(avatar, new Vector(0, 0));

			int xRng = MAP_BOUNDS[1] - MAP_BOUNDS[0];
			int yRng = MAP_BOUNDS[3] - MAP_BOUNDS[2];
			int xMin = MAP_BOUNDS[0];
			int yMin = MAP_BOUNDS[2];

			for (int i = 0; i < N_NPCS; i++)
			{
				NPCEntity npc1 = new NPCEntity();
				Appearance npcAppearance1 = new Appearance(npc1);
				npcAppearance1.setImage(cheese);
				npc1.setAppearance(npcAppearance1);

				npc1.setTarget(avatar);

				environment.add(npc1, new Vector(random.nextInt(xRng) + xMin,
						random.nextInt(yRng) + yMin));
			}

			if (RENDER_MAP)
			{
				MapComponent map = new MapComponent(avatar, view.getWidth(),
						view.getHeight());
				view.addViewComponent(map);
			}

			FPSTextComponent fps = new FPSTextComponent();
			fps.setLocation(100, 100);
			fps.setFontColor(Color.RED);

			view.addViewComponent(fps);

			TemporaryMover mover = new TemporaryMover(avatar);
			view.addKeyListener(mover);
			
			ViewCompositeComponent vcc = new ViewCompositeComponent();
			
			for(int i=0; i<20; i++)
				vcc.addChild(new ButtonComponent("test" + i));
			
			ScrollDecorator scroll = new ScrollDecorator(vcc, 600, 50);
			scroll.setLocation(300, 850);
			scroll.setScrollSpeed(10);
			
			view.addViewComponent(scroll);

			long time;
			long envStart;
			
			while (true)
			{
				time = System.currentTimeMillis();
				System.out.println("\n\n");
				mover.tick();
				view.renderFrame();
				envStart = System.currentTimeMillis();
				System.out.println("view render total: " + (envStart-time));
				if (TICK_ENVIRONMENT)
					environment.tick();
				System.out.println("environment total: " + (System.currentTimeMillis()-envStart));
				System.out.println("total: " + (System.currentTimeMillis()-time));

			}

		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Vector createImpulse()
	{
		double xImpulse = 0.1;
		if (random.nextBoolean())
			xImpulse *= -1.0;

		xImpulse *= random.nextInt(4);

		double yImpulse = 0.1;
		if (random.nextBoolean())
			yImpulse *= -1.0;

		yImpulse *= random.nextInt(7);

		return new Vector(xImpulse, yImpulse);
	}
}
