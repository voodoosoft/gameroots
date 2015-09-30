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

	/**
	 * Returns true if this item should be rendered after general scene lighting was applied.
	 * @return
	 */
	public boolean isPostLighting() {
		return postLighting;
	}

	public void setPostLighting(boolean postLighting) {
		this.postLighting = postLighting;
	}

	public BlendMode getBlendMode() {
		return blendMode;
	}

	public void setBlendMode(BlendMode blendMode) {
		this.blendMode = blendMode;
		this.blending = blendMode != BlendMode.NONE;
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

	protected boolean nullSafeEquals(Object o1, Object o2) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}

		return o1.equals(o2);
	}

	@Override
	public int compareTo(BatchRenderItem otherItem) {
		int result = 0;
		if (otherItem instanceof AbstractBatchItem) {
			AbstractBatchItem other = (AbstractBatchItem)otherItem;
			result = Boolean.compare(this.isPostLighting(), other.postLighting);
			if (result == 0) {
				result = Integer.compare(this.getLayer(), other.layer);
				if (result == 0) {
					result = this.getBlendMode().compareTo(other.blendMode);
				}
			}
		}

		return result;
	}

	private BlendMode blendMode;
	private int layer;
	private boolean postLighting;
	private boolean blending;
	private static ShaderProgram lastShaderProgram;
}
