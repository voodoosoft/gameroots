package de.voodoosoft.gameroots.frontend.gdx.view.render.batch;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;



/**
 * Defines common methods for all batch item types.
 */
public interface BatchRenderItem extends Comparable<BatchRenderItem> {
	/**
	 * Returns the z-order of this item.
	 *
	 * @return
	 */
	int getLayer();

	/**
	 * Draws this batch item.
	 *
	 * @param batch sprite batch to use for drawing
	 * @param time current time in ns
	 */
	void render(SpriteBatch batch, long time);
}
