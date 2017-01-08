package de.voodoosoft.gameroots.frontend.gdx.view.render.batch.impl;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;



/**
 * Specifies a particle type.
 */
public class ParticleDef {

	/**
	 * Creates a new <code>ParticleDefinition</code>.
	 *
	 * @param type unique particle identifier
	 * @param effect underlying particle effect
	 * @param emitterX particle x-coordinate
	 * @param emitterY particle y-coordinate
	 * @param updateInterval time span in ns between each particle update
	 */
	public ParticleDef(Enum type, PooledEffect effect, float emitterX, float emitterY, long updateInterval) {
		this.type = type;
		this.effect = effect;
		this.emitterX = emitterX;
		this.emitterY = emitterY;
		this.updateInterval = updateInterval;

		// check if all emitters share the same texture
		sharedTextureHandle = 0;
		Array<ParticleEmitter> emitters = effect.getEmitters();
		for (int i = 0; i < emitters.size; i++) {
			Sprite sprite = emitters.get(i).getSprite();
			if (sprite != null) {
				int tempHandle = sprite.getTexture().getTextureObjectHandle();
				if (sharedTextureHandle == 0) {
					sharedTextureHandle = tempHandle;
				}
				else if (sharedTextureHandle != tempHandle) {
					sharedTextureHandle = 0;
					break;
				}
			}
		}
	}

	/**
	 * Returns the unique identififer of this particle effect.
	 * @return
	 */
	public Enum getType() {
		return type;
	}

	/**
	 * Returns the x-coordindate of this particle effect.
	 * @return
	 */
	public float getEmitterX() {
		return emitterX;
	}

	/**
	 * Returns the y-coordindate of this particle effect.
	 * @return
	 */
	public float getEmitterY() {
		return emitterY;
	}

	/**
	 * Moves the particle origin to a new location.
	 *
	 * @param emitterX
	 * @param emitterY
	 */
	public void setLocation(float emitterX, float emitterY) {
		this.emitterX = emitterX;
		this.emitterY = emitterY;
	}

	public PooledEffect getEffect() {
		return effect;
	}

	/**
	 * Sets the time span between updating this particle effect.
	 * @param updateInterval update interval in ns
	 */
	public void setUpdateInterval(long updateInterval) {
		this.updateInterval = updateInterval;
	}

	/**
	 * Returns the time span between updating this particle effect.
	 * @return update interval in ns
	 */
	public long getUpdateInterval() {
		return updateInterval;
	}

	/**
	 * Sets the time of the last particle update.
	 * @param lastUpdateTime update time in ns
	 */
	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * Returns the time of the last  particle update.
	 * @return update time in ns
	 */
	public long getLastUpateTime() {
		return lastUpdateTime;
	}

	/**
	 * Specifies whether this particle effect will be removed during the next update cycle.
	 *
	 * @param remove true to stop and remove this particle effect
	 */
	public void setRemove(boolean remove) {
		this.remove = remove;
	}

	/**
	 * Returns true if this particle effect has been completed and should be removed before the next update cycle.
	 *
	 * @return true to stop and remove this particle effect
	 */
	public boolean isRemove() {
		return remove;
	}

	/**
	 * Specifies whether this particle effect should be rendered after stage lighting was applied.
	 * @param postLighting
	 */
	public void setPostLighting(boolean postLighting) {
		this.postLighting = postLighting;
	}

	/**
	 * Returns true when this particle effect should be rendered after stage lighting was applied.
	 * @return
	 */
	public boolean isPostLighting() {
		return postLighting;
	}

	public int getSharedTextureHandle() {
		return sharedTextureHandle;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	@Override
	public String toString() {
		return type + " particles @" + hashCode();
	}

	private PooledEffect effect;
	private float emitterX;
	private float emitterY;
	private Vector2 velocity;
	private long lastUpdateTime;
	private long updateInterval;
	private Enum type;
	private boolean remove;
	private boolean postLighting;
	private int sharedTextureHandle;
}