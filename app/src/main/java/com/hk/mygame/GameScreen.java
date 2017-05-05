package com.hk.mygame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScreen extends Screen
{
	private final Random rand = new Random();
	public static final int NUM_OF_BUILDINGS = 50;
	private Building[] buildings;
	private final List<Meteor> meteors;
	private final List<Shot> shots;
	private int totalTicks = 0, ticks = 5;
	private boolean lost = false;
	private long elapsed = 0, lst = -1, lastShot, lostAt;

	public GameScreen(GameView game)
	{
		super(game);
		buildings = new Building[NUM_OF_BUILDINGS];
		for(int i = 0; i < buildings.length; i++)
		{
			buildings[i] = new Building(rand.nextInt(491) + 10);
		}

		meteors = new ArrayList<>();
		shots = new ArrayList<>();
	}

	@Override
	public void update(int width, int height)
	{
		if(lost) return;
		totalTicks++;
		if(lst == -1)
		{
			lst = System.currentTimeMillis();
		}
		else
		{
			long time = System.currentTimeMillis();
			elapsed += time - lst;
			lst = time;
		}

		ticks--;
		if(ticks == 0)
		{
//			ticks = rand.nextInt(5) + 5;
			int tks = Math.max(50 - totalTicks / 100, 5);
			ticks = rand.nextInt(tks) + tks;
			Meteor meteor = new Meteor(rand.nextInt(width), -200, rand.nextFloat() * (100) + 50);
			meteor.vel.x = rand.nextFloat() * 7F - 3.5F;
			meteors.add(meteor);
		}

		for(int i = 0; i < meteors.size(); i++)
		{
			Meteor m = meteors.get(i);
			m.update(width, height);

			if(m.pos.y > height)
			{
				if(m.hit == 0)
				{
					for(int j = 0; j < buildings.length; j++)
					{
						float x = j * (width / (buildings.length - 1));
						float y = height;
						float dx = m.pos.x - x;
						float dy = m.pos.y - y;

						if(dx * dx + dy * dy <= m.radius * m.radius)
						{
							buildings[j].actualHealth = Math.max(0, buildings[j].actualHealth - m.radius);
						}
					}
				}
				m.hit = 1;
			}

			if(m.hit != 0 && m.ticksHit++ > 10)
			{
				meteors.remove(i);
				i--;
			}
		}

		float h = 0;
		for(int i = 0; i < buildings.length; i++)
		{
			buildings[i].update();
			h += buildings[i].actualHealth;
		}

		if(h <= 0)
		{
			lost = true;
			lostAt = System.currentTimeMillis();
		}

		label1:
		for(int i = 0; i < shots.size(); i++)
		{
			Shot s = shots.get(i);
			s.update();

			for(int j = 0; j < meteors.size(); j++)
			{
				Meteor m = meteors.get(j);
				PointF a = m.pos;
				PointF b = s.pos;
				if(PointF.length(a.x - b.x, a.y - b.y) < 20 + m.radius)
				{
					m.hit = 2;
					//meteors.remove(j);
					shots.remove(i);
					i--;
					continue label1;
				}
			}

			if(s.pos.y > height)
			{
				shots.remove(i);
				i--;
			}
		}
	}

	@Override
	public void paint(Canvas canvas, int width, int height)
	{
		canvas.drawRect(0, 0, width, height, clr(0x00FFFF, true));
		for(int i = 0; i < buildings.length; i++)
		{
			float w = width / (buildings.length - 1);
			float bh = (buildings[i].actualHealth / 1500F) * height;
			float rh = (buildings[i].health / 1500F) * height;
			float x = i * w;
			float by = height - bh;
			float ry = height - rh;

			canvas.drawRect(x, ry, x + w, ry + rh, clr(0xFF0000, true));
			canvas.drawRect(x, by, x + w, by + bh, clr(0x000000, true));
			canvas.drawRect(x, ry, x + w, ry + rh, clr(0x00FFFF, false));
		}

		for(int i = 0; i < meteors.size(); i++)
		{
			Meteor m = meteors.get(i);

			if(m.hit == 0)
			{
				canvas.drawCircle(m.pos.x, m.pos.y, m.radius, clr(0xFFFF00, true));
			}
			else if(m.hit == 1)
			{
				canvas.drawCircle(m.pos.x, m.pos.y, m.radius, clr(0xFF0000, true));
			}
			else if(m.hit == 2)
			{
				canvas.drawCircle(m.pos.x, m.pos.y, m.radius, clr(0x00FF00, true));
			}
		}

		for(int i = 0; i < shots.size(); i++)
		{
			Shot s = shots.get(i);

			canvas.drawCircle(s.pos.x, s.pos.y, 20, clr(0x000000, true));
		}

		if(lost)
		{

			canvas.drawRect(0, 0, width, height, clrA(0xAA000000));
			Paint p = clr(0xFF0000);
			float prev = p.getTextSize();
			p.setTextSize(p.getTextSize() * 20F);
			float w = p.measureText("You Lost!");
			canvas.drawText("You Lost!", width / 2 - w / 2, height * 2 / 4, p);
			p.setTextSize(p.getTextSize() / 5F);
			p.setColor(0xFFFF00AA);
			String s = "Lasted: " + (elapsed / 10 / 100F) + " seconds";
			w = p.measureText(s);
			canvas.drawText(s, width / 2 - w / 2, height * 3 / 5, p);
			w = p.measureText("(click to retry)");
			canvas.drawText("(click to retry)", width / 2 - w / 2, height * 4 / 5, p);
			p.setTextSize(prev);
		}
	}

	@Override
	public boolean touch(float x, float y, boolean pressed)
	{
		if(lost)
		{
			if (System.currentTimeMillis() - lostAt > 500)
			{
				game.setCurrScreen(new GameScreen(game));
			}
		}
		else
		{
			long time = System.currentTimeMillis();
			if(time - lastShot > 200)
			{
				Shot s = new Shot(game.getWidth() / 2, game.getHeight());
				float dx = x - s.pos.x;
				float dy = y - s.pos.y;
				s.vel.x = dx / 30F;
				s.vel.y = dy / 30F;
				lastShot = time;
				shots.add(s);
			}
		}
		return true;
	}
}
