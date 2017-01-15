package de.voodoosoft.gameroots.frontend.gdx.view.render.batch.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Pool;



public class CircleBatchItem extends AbstractBatchItem implements Pool.Poolable {
	public CircleBatchItem(ShapeRenderer shapeRenderer) {
		setBlendMode(DefaultBlendMode.NONE);
		this.shapeRenderer = shapeRenderer;
	}

	@Override
	public void reset() {
		setBlendMode(DefaultBlendMode.NONE);
		color = null;
	}

	public void setShapeRenderer(ShapeRenderer shapeRenderer) {
		this.shapeRenderer = shapeRenderer;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setOffsets(float xOffset, float yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public void setLocation(float x, float y, float radius) {
		this.x1 = x;
		this.y1 = y;
		this.radius = radius;
	}

	@Override
	public void render(SpriteBatch batch, long time) {
		if (color != null) {
			shapeRenderer.setColor(color);
		}
//		shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shapeRenderer.circle(x1 - xOffset, y1 - yOffset, radius);
	}

	private ShapeRenderer shapeRenderer;
	private Color color;
	private float x1, y1, radius;
	private float xOffset, yOffset;
}
