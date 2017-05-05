package com.hk.mygame;

import android.graphics.PointF;

public class Meteor
{
	public final PointF pos, vel;
	public final float radius;
	public int hit = 0;
	public int ticksHit;

	public Meteor(float x, float y, float radius)
	{
		pos = new PointF(x, y);
		vel = new PointF();
		this.radius = radius;
	}

	public void update(int width, int height)
	{
		if(hit == 0)
		{
			vel.y += 0.2F;

			pos.x += vel.x;
			pos.y += vel.y;
		}
	}
}
