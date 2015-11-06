package de.voodoosoft.gameroots.frontend.gdx.view.render.batch.impl;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import de.voodoosoft.gameroots.frontend.gdx.view.render.batch.BatchRenderItem;
import de.voodoosoft.gameroots.frontend.gdx.view.render.batch.BlendMode;



/**
 * Default batch item that defines a post lighting flag, a layer and a blend mode.
 * <p/>
 * Draw order is as follows:
 * <br/>1. post/pre lighting
 * <br/>2. layer
 * <br/>3. blend mode
 */
public abstract class AbstractBatchItem implements BatchRenderItem {
	public AbstractBatchItem() {
	}

	public AbstractBatchItem(int layer) {
		this.layer = layer;
	}

	@Override
	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public BlendMode getBlendMode() {
		return blendMode;
	}

	public void setBlendMode(BlendMode blendMode) {
		this.blendMode = blendMode;
		this.blending = blendMode != DefaultBlendMode.NONE;
	}

	public boolean isBlending() {
		return blending;
	}

	protected static ShaderProgram getLastShaderProgram() {
		return lastShaderProgram;
	}

	protected static void setLastShaderProgram(ShaderProgram lastShaderProgram) {
		AbstractBatchItem.lastShaderProgram = lastShaderProgram;
	}

	@Override
	public int compareTo(BatchRenderItem otherItem) {
		int result = 0;
		if (otherItem instanceof AbstractBatchItem) {
			AbstractBatchItem other = (AbstractBatchItem)otherItem;
			result = this.blendMode.compareTo(other.blendMode);
		}

		return result;
	}

	private BlendMode blendMode;
	private int layer;
	private boolean blending;
	private static ShaderProgram lastShaderProgram;
}
