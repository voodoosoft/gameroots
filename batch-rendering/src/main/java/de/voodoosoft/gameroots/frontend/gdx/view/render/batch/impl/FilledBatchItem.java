package de.voodoosoft.gameroots.frontend.gdx.view.render.batch.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Pool;



public class FilledBatchItem extends AbstractBatchItem implements Pool.Poolable {
	public FilledBatchItem(ShapeRenderer shapeRenderer) {
		setBlendMode(DefaultBlendMode.DEFAULT);
	}

	@Override
	public void reset() {
		setBlendMode(DefaultBlendMode.DEFAULT);
		color = null;
	}

	public void setShapeRenderer(ShapeRenderer shapeRenderer) {
		this.shapeRenderer = shapeRenderer;
	}

	public void setLocation(float x, float y) {
		this.x1 = x;
		this.y1 = y;
	}

	public void setDimension(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void render(SpriteBatch batch, long time) {
		if (color != null) {
			shapeRenderer.setColor(color);
		}
		shapeRenderer.rect(x1, y1, width, height);
	}

	private ShapeRenderer shapeRenderer;
	private Color color;
	private float x1, y1, width, height;
}
