package de.voodoosoft.gameroots.frontend.gdx.view.render.batch.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import de.voodoosoft.gameroots.frontend.gdx.view.render.batch.BatchRenderItem;
import de.voodoosoft.gameroots.frontend.gdx.view.render.batch.BlendMode;



/**
 * Batch item for rendering nine patches.
 */
public class NinePatchBatchItem extends AbstractBatchItem implements Pool.Poolable {

	public NinePatchBatchItem() {
		width = -1;
		height = -1;
		rotation = 0;
		xOrigin = -1;
		yOrigin = -1;
		setBlendMode(DefaultBlendMode.DEFAULT);
	}

	public NinePatchBatchItem(int layer) {
		super(layer);
		width = -1;
		height = -1;
		rotation = 0;
		xOrigin = -1;
		yOrigin = -1;
		setBlendMode(DefaultBlendMode.DEFAULT);
	}

	/**
	 * Prepares this item for returning to its item pool.
	 */
	@Override
	public void reset() {
		ninePatch = null;
		color = null;
		width = -1;
		height = -1;
		rotation = 0;
		xOrigin = -1;
		yOrigin = -1;
		setBlendMode(DefaultBlendMode.DEFAULT);
	}

	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void shift(float dx, float dy) {
		this.x += dx;
		this.y += dy;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public void setDimension(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public void setOrigin(float xOrigin, float yOrigin) {
		this.xOrigin = xOrigin;
		this.yOrigin = yOrigin;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public void setNinePatch(NinePatch ninePatch) {
		this.ninePatch = ninePatch;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void render(SpriteBatch batch, long time) {
		if (getLastShaderProgram() != null) {
			batch.setShader(null);
			setLastShaderProgram(null);
		}

		if (isBlending()) {
			batch.enableBlending();
			BlendMode blendMode = getBlendMode();
			if (!blendMode.equals(DefaultBlendMode.NONE)) {
				batch.setBlendFunction(blendMode.getSrcFunction(), blendMode.getDestFunction());
			}
		}
		else {
			batch.disableBlending();
		}
		if (color != null) {
			batch.setColor(color);
		}
		else {
			batch.setColor(Color.WHITE);
		}

		if (rotation != 0) {
			if (xOrigin != -1 && yOrigin != -1) {
				ninePatch.draw(batch, x, y, xOrigin, yOrigin, width, height, 1, 1, rotation);
			} else {
				float w = width != -1 ? width : ninePatch.getTotalWidth();
				float h = height != -1 ? height : ninePatch.getTotalHeight();
				ninePatch.draw(batch, x, y, w / 2f, h / 2f, w, h, 1, 1, rotation);
			}
		}
		else {
			if (width == -1 || height == -1) {
				ninePatch.draw(batch, x, y, ninePatch.getTotalWidth(), ninePatch.getTotalHeight());
			}
			else {
				ninePatch.draw(batch, x, y, width, height);
			}
		}
	}

	@Override
	public int compareTo(BatchRenderItem otherItem) {
		int result = super.compareTo(otherItem);
		if (result == 0) {
			if (otherItem instanceof NinePatchBatchItem) {
				NinePatchBatchItem o = (NinePatchBatchItem)otherItem;
				int thisHandle = this.ninePatch.getTexture().getTextureObjectHandle();
				int otherHandle = o.ninePatch.getTexture().getTextureObjectHandle();
				result = Integer.compare(thisHandle, otherHandle);
			}
		}

		return result;
	}

	private Color color;
	private float x, y;
	private float width, height;
	private float xOrigin, yOrigin;
	private NinePatch ninePatch;
	private float rotation;
}
