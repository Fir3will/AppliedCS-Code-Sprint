package com.hk.mygame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class StartScreen extends Screen
{
	public StartScreen(GameView game)
	{
		super(game);
	}

	@Override
	public void update(int width, int height)
	{

	}

	@Override
	public void paint(Canvas canvas, int width, int height)
	{
		canvas.drawRect(0, 0, width, height, clr(0x00FFFF, true));

		canvas.drawRect(width * 1 / 3, height * 1 / 3, width * 2 / 3, height * 2 / 3, clr(0x000000, false));

		Paint p = clr(0x000000);
		float prev = p.getTextSize();
		p.setTextSize(p.getTextSize() * 12F);
		Rect r = new Rect();
		p.getTextBounds("Start", 0, 5, r);
		canvas.drawText("Start", width / 2 - r.width() / 2, height / 2 + r.height() / 2, p);
		p.setTextSize(prev);
	}

	public boolean touch(float x, float y, boolean pressed)
	{
		if(x > game.getWidth() * 1 / 3 && x < game.getWidth() * 2 / 3 && y > game.getHeight() * 1 / 3 && y < game.getHeight() * 2 / 3)
		{
			game.setCurrScreen(new GameScreen(game));
			return true;
		}
		return false;
	}
}
