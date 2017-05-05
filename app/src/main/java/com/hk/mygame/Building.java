package com.hk.mygame;

public class Building
{
	public float health, actualHealth;
	public final float maxHealth;

	public Building(int height)
	{
		this.health = actualHealth = maxHealth = height;
	}

	public void update()
	{
		health = health * 0.9F + actualHealth * 0.1F;
	}
}
