package com.hk.mygame;

import android.graphics.PointF;

public class Shot
{
	public final PointF pos, vel;

	public Shot(float x, float y)
	{
		pos = new PointF(x, y);
		vel = new PointF();
	}

	public void update()
	{
		vel.y += 0.2F;

		pos.x += vel.x;
		pos.y += vel.y;
	}
}
