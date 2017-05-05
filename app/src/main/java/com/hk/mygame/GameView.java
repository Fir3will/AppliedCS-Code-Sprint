package com.hk.mygame;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View
{
	private float x, y;
	private Screen currScreen;

	public GameView(Context context, AttributeSet attributeSet)
	{
		super(context, attributeSet);
		setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					return currScreen.touch(event.getX(), event.getY(), false);
				}
				return false;
			}
		});
		currScreen = new StartScreen(this);
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		currScreen.update(getWidth(), getHeight());
		currScreen.paint(canvas, getWidth(), getHeight());

		invalidate();
	}

	public void setCurrScreen(Screen currScreen)
	{
		this.currScreen = currScreen;
	}
}
