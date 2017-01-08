package de.voodoosoft.gameroots.frontend.gdx.view.render.batch.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Pool;



public class LineBatchItem extends AbstractBatchItem implements Pool.Poolable {
	public LineBatchItem(ShapeRenderer shapeRenderer) {
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

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void render(SpriteBatch batch, long time) {
		if (color != null) {
			shapeRenderer.setColor(color);
		}
		shapeRenderer.line(x1, y1, x2, y2);
	}

	private ShapeRenderer shapeRenderer;
	private Color color;
	private float x1, y1, x2, y2;
}
