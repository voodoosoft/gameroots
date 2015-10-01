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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import de.voodoosoft.gameroots.client.gdx.view.input.IVirtualJoystickRenderer;
import de.voodoosoft.gameroots.client.gdx.view.input.VirtualJoystick;



/**
 * Simple joystick renderer which paints both the joystick and its knob as circles.
 * <p>
 * The knob can either be drawn at the touch position or at the snap position.
 * 
 * @see #setDrawSnappedKnob(boolean)
 * @see VirtualJoystick#setPositionCount(int)
 */
public class CircleJoystickRenderer implements IVirtualJoystickRenderer {

	/**
	 * Creates new <code>CircleJoystickRenderer</code> for usage with the given <code>ShapeRenderer</code>.
	 * <p>
	 * The <code>ShapeRenderer</code> must be disposed externally.
	 * 
	 * @param shapeRenderer
	 */
	public CircleJoystickRenderer(ShapeRenderer shapeRenderer) {
		this.shapeRenderer = shapeRenderer;
		knobRadius = 10;
		centerRadius = 3;
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
	 * Specifies the knob size by setting its radius.
	 * 
	 * @param knobRadius radius
	 */
	public void setKnobRadius(float knobRadius) {
		this.knobRadius = knobRadius;
	}

	/**
	 * Specifies the radius of the drawn joystick center.
	 * 
	 * @param centerRadius radius
	 */
	public void setCenterRadius(float centerRadius) {
		this.centerRadius = centerRadius;
	}

	/**
	 * Sets color for the joystick circle.
	 * 
	 * @param boundsColor
	 */
	public void setBoundsColor(Color boundsColor) {
		this.boundsColor = boundsColor;
	}

	/**
	 * Sets color of the joystick knob.
	 * 
	 * @param knobColor
	 */
	public void setKnobColor(Color knobColor) {
		this.knobColor = knobColor;
	}

	@Override
	public void paint(Circle circle, Vector2 knobDirection, float knobX, float knobY) {
		shapeRenderer.begin(ShapeType.Line);

		// draw outer circle
		shapeRenderer.setColor(boundsColor);
		shapeRenderer.circle(circle.x, circle.y, circle.radius);

		// draw center
		shapeRenderer.setColor(knobColor);
		if (centerRadius > 0) {
			shapeRenderer.circle(circle.x, circle.y, centerRadius);
		}

		shapeRenderer.end();

		// draw knob
		shapeRenderer.begin(ShapeType.Filled);
		if (drawSnappedKnob) {
			float x = circle.x + knobDirection.x * circle.radius;
			float y = circle.y + knobDirection.y * circle.radius;
			shapeRenderer.circle(x, y, knobRadius);
		}
		else {
			shapeRenderer.circle(knobX, knobY, knobRadius);
		}
		shapeRenderer.end();
	}

	private ShapeRenderer shapeRenderer;
	private boolean drawSnappedKnob;
	private Color boundsColor;
	private Color knobColor;
	private float knobRadius;
	private float centerRadius;
}
