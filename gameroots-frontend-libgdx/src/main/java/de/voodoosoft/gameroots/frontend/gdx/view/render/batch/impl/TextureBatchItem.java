package de.voodoosoft.gameroots.frontend.gdx.view.render.batch.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import de.voodoosoft.gameroots.frontend.gdx.view.render.batch.BatchRenderItem;
import de.voodoosoft.gameroots.frontend.gdx.view.render.batch.BlendMode;



/**
 * Batch item for rendering textures.
 */
public class TextureBatchItem extends AbstractBatchItem implements Pool.Poolable {

	public TextureBatchItem() {
		width = -1;
		height = -1;
		rotation = 0;
		setBlendMode(DefaultBlendMode.DEFAULT);
	}

	public TextureBatchItem(int layer) {
		super(layer);
		width = -1;
		height = -1;
		rotation = 0;
		setBlendMode(DefaultBlendMode.DEFAULT);
	}

	/**
	 * Prepares this item for returning to its item pool.
	 */
	@Override
	public void reset() {
		color = null;
		width = -1;
		height = -1;
		rotation = 0;
		setBlendMode(DefaultBlendMode.DEFAULT);
	}

	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setDimension(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}

	public TextureRegion getTextureRegion() {
		return textureRegion;
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
			batch.draw(textureRegion, x, y, width/2, height/2, width, height, 1, 1, rotation);
		}
		else {
			if (width == -1 || height == -1) {
				batch.draw(textureRegion, x, y);
			}
			else {
				batch.draw(textureRegion, x, y, width, height);
			}
		}
	}

	@Override
	public int compareTo(BatchRenderItem otherItem) {
		int result = super.compareTo(otherItem);
		if (result == 0) {
			if (otherItem instanceof TextureBatchItem) {
				TextureBatchItem o = (TextureBatchItem)otherItem;
				int thisHandle = this.getTextureRegion().getTexture().getTextureObjectHandle();
				int otherHandle = o.getTextureRegion().getTexture().getTextureObjectHandle();
				result = Integer.compare(thisHandle, otherHandle);
			}
		}

		return result;
	}

	private Color color;
	private float x, y;
	private float width, height;
	private TextureRegion textureRegion;
	private float rotation;
}
