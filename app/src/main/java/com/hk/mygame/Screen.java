package com.hk.mygame;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Screen
{
	private final Paint paint;
	public final GameView game;

	public Screen(GameView game)
	{
		this.game = game;
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	public abstract void update(int width, int height);

	public abstract void paint(Canvas canvas, int width, int height);

	public boolean touch(float x, float y, boolean pressed)
	{
		return false;
	}

	public final Paint clr(int clr)
	{
		paint.setColor(clr | 0xFF000000);
		return paint;
	}

	public final Paint clr(int clr, boolean filled)
	{
		paint.setColor(clr | 0xFF000000);
		paint.setStyle(filled ? Paint.Style.FILL : Paint.Style.STROKE);
		return paint;
	}

	public final Paint clrA(int clr)
	{
		paint.setColor(clr);
		return paint;
	}

	public final Paint clrA(int clr, boolean filled)
	{
		paint.setColor(clr);
		paint.setStyle(filled ? Paint.Style.FILL : Paint.Style.STROKE);
		return paint;
	}
}
