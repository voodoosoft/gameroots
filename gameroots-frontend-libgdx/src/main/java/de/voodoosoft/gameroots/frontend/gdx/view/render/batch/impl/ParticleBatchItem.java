package de.voodoosoft.gameroots.frontend.gdx.view.render.batch.impl;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;
import de.voodoosoft.gameroots.frontend.gdx.view.render.batch.BatchRenderItem;
import de.voodoosoft.gameroots.frontend.gdx.view.render.batch.BlendMode;
import de.voodoosoft.gameroots.frontend.gdx.view.render.batch.ParticleDefinition;



/**
 * Batch item for rendering particles.
 * <p/>
 * As batch items are pooled and reused,
 * the concrete particle definition to be used for rendering is assigned during each render cycle on the fly
 * by calling  {@link #setParticleDefinition}.
 * <p/>
 *
 */
public class ParticleBatchItem extends AbstractBatchItem implements Pool.Poolable {
	public ParticleBatchItem() {
	}

	/**
	 * Prepares this item for returning to its item pool.
	 */
	@Override
	public void reset() {
		setBlendMode(BlendMode.DEFAULT);
	}

	public void setParticleDefinition(ParticleDefinition particleDefinition) {
		this.particleDefinition = particleDefinition;
	}

	public ParticleDefinition getParticleDefinition() {
		return particleDefinition;
	}

	public void setOffsets(float xOffset, float yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	@Override
	public void render(SpriteBatch batch, long time) {
		if (getLastShaderProgram() != null) {
			batch.setShader(null);
			setLastShaderProgram(null);
		}

		float ex = particleDefinition.getEmitterX();
		float ey = particleDefinition.getEmitterY();

		ParticleEffect effect = particleDefinition.getEffect();
		float x = ex - xOffset;
		float y = ey - yOffset;
		effect.setPosition(x, y);

		if (particleDefinition.getLastUpateTime() == 0) {
			particleDefinition.setLastUpdateTime(time);
		}

		long dns = time - particleDefinition.getLastUpateTime();
		boolean doUpdate = dns >= particleDefinition.getUpdateInterval();
		if (doUpdate) {
			particleDefinition.setLastUpdateTime(time);
			float ds = (float)dns / (float)SECS_AS_NANO;
			effect.draw(batch, ds);
		}
		else {
			effect.draw(batch);
		}

		if (doUpdate && effect.isComplete()) {
			particleDefinition.setRemove(true);
		}
	}

	@Override
	public int compareTo(BatchRenderItem otherItem) {
		int result = super.compareTo(otherItem);
		if (result == 0) {
			if (otherItem instanceof ParticleBatchItem) {
				ParticleBatchItem o = (ParticleBatchItem)otherItem;
				result = Integer.compare(this.particleDefinition.getSharedTextureHandle(), o.getParticleDefinition().getSharedTextureHandle());
			}
		}

		return result;
	}

	private final static long MILLIS_AS_NANO = 1000000l;
	private final static long SECS_AS_NANO = MILLIS_AS_NANO * 1000l;

	private ParticleDefinition particleDefinition;
	private float xOffset, yOffset;
}
