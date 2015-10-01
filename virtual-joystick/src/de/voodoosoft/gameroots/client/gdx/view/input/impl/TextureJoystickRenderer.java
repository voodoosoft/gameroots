/*******************************************************************************
 * Copyright 2013 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 ******************************************************************************/

package de.voodoosoft.gameroots.client.gdx.view.input.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import de.voodoosoft.gameroots.client.gdx.view.input.IVirtualJoystickRenderer;
import de.voodoosoft.gameroots.client.gdx.view.input.VirtualJoystick;



/**
 * Joystick renderer which paints joystick and knob with texture images.
 * <p>
 * The knob can either be drawn at the touch position or at the snap position.
 * </br>Textures must be created and disposed externally.
 * 
 * @see #setDrawSnappedKnob(boolean)
 * @see VirtualJoystick#setPositionCount(int)
 */
public class TextureJoystickRenderer implements IVirtualJoystickRenderer {

	/**
	 * Creates a new renderer for the given <code>SpriteBatch</code>. 
	 * 
	 * @param batch <code>SpriteBatch</code> to use.
	 */
	public TextureJoystickRenderer(SpriteBatch batch) {
		this.batch = batch;
	}

	/**
	 * Creates a new renderer.
	 * 
	 * @param batch  <code>SpriteBatch</code> to use.
	 * @param joystickRegion <code>TextureRegion</code> to draw the joystick
	 * @param knobRegion <code>TextureRegion</code> to draw the knob
	 */
	public TextureJoystickRenderer(SpriteBatch batch, Texture joystickRegion, Texture knobRegion) {
		this.batch = batch;
		this.joystickRegion = joystickRegion;
		this.knobRegion = knobRegion;
	}

	/**
	 * Decides whether to draw the knob at the touch or at the snapped position.
	 * 
	 * @param drawSnappedKnob true to draw snap position
	 */
	public void setDrawSnappedKnob(boolean drawSnappedKnob) {
		this.drawSnappedKnob = drawSnappedKnob;
	}

	/**
	 * Sets <code>TextureRegion</code> for drawing the joystick.
	 * 
	 * @param joystickRegion
	 */
	public void setJoystickTexture(Texture joystickRegion) {
		this.joystickRegion = joystickRegion;
	}

	/**
	 * Sets <code>TextureRegion</code> for drawing the knob.
	 * 
	 * @param knobRegion
	 */
	public void setKnobTexture(Texture knobRegion) {
		this.knobRegion = knobRegion;
	}

	/**
	 * Specifies the knob size by setting its radius.
	 * 
	 * @param knobRadius radius
	 */
	public void setKnobRadius(float knobRadius) {
		this.knobRadius = knobRadius;
	}

	@Override
	public void paint(Circle circle, Vector2 knobDirection, float knobX, float knobY) {
		batch.draw(joystickRegion, circle.x - circle.radius, circle.y - circle.radius);

		if (drawSnappedKnob) {
			float x = circle.x - knobRadius + knobDirection.x * circle.radius;
			float y = circle.y - knobRadius + knobDirection.y * circle.radius;
			batch.draw(knobRegion, x, y);
		}
		else {
			batch.draw(knobRegion, knobX - knobRadius, knobY - knobRadius);
		}
	}

	private Texture joystickRegion;
	private Texture knobRegion;
	private SpriteBatch batch;
	private boolean drawSnappedKnob;
	private float knobRadius;
}
