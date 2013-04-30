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

package de.voodoosoft.gameroots.client.gdx.view.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;



/**
 * A <code>VirtualJoystick</code> is a game controller designed after classic hardware joysticks.
 * </p>
 * By default, a <i>y-down</i> coordinate system is used.
 * </br>The number of possible knob positions is adjustable as needed.
 * </br>The current position of the joystick knob is given as normalized vector pointing from the joystick center.
 * </p>Touch events are ignored when they
 * <ul>
 * <li>are inside the dead zone</li>
 * <li>are too close to the previously touched position (tolerance) </li>
 * <li>are outside the joystick circle</li>
 * </ul>
 * </br>Drawing of the joystick is delegated to the internal <code>IVirtualJoystickRenderer</code>.
 * 
 * @see #setPositionCount(int)
 * @see #setDeadZone(float)
 * @see #setTolerance(float)
 */
public class VirtualJoystick {

	/**
	 * Creates a new <code>VirtualJoystick</code> for the specified input source.
	 * 
	 * @param input input source
	 */
	public VirtualJoystick(Input input) {
		this.input = input;

		knobDirection = new Vector2();
		tmpCheckVector = new Vector2();
		joystickCircle = new Circle();
		epsilon = 0.0001f;
		yDown = true;
	}

	/**
	 * Creates a new <code>VirtualJoystick</code> for the specified input source at the given position.
	 * 
	 * @param input input source
	 * @param x-coordinate of joystick circle center 
	 * @param y-coordinate of joystick circle center 
	 * @param radius joystick circle radius
	 */
	public VirtualJoystick(Input input, float x, float y, float radius) {
		this(input);

		joystickCircle.set(x, y, radius);
	}

	/**
	 * Set the orientation of the coordinate system to y-up.
	 * </p>
	 * Point 0:0 is at the lower left corner instead of the lower left corner.
	 * 
	 * @param viewHeight height of the view
	 */
	public void setYUp(int viewHeight) {
		this.yDown = false;
		this.viewHeight = viewHeight;
	}

	/**
	 * Set the orientation of the coordinate system to y-down.
	 * </p>
	 * Point 0:0 is at the upper left corner instead of the lower left corner.
	 * 
	 * @param viewHeight height of the view
	 */
	public void setYDown() {
		this.yDown = true;
	}

	/**
	 * Sets the renderer delegate used for drawing the joystick.
	 * 
	 * @param renderer joystick renderer
	 * 
	 * @see #paint()
	 */
	public void setRenderer(IVirtualJoystickRenderer renderer) {
		this.renderer = renderer;
	}

	/**
	 * Sets the minimum distance between two joystick positions to be recognized as change.
	 * 
	 * @param tolerance minimum distance in pixels
	 */
	public void setTolerance(float tolerance) {
		this.tolerance = tolerance;
	}

	/**
	 * Defines the number of possible joystick positions.
	 * </p>
	 * The joystick circle is dividable into n equally sized sections.
	 * </br>For example, 8 positions give sections of 45° each (360° / 45° = 8), 
	 * whereas 4 positions result in sections of 90° (360° / 90° = 4).
	 * </p>
	 * With a finite number of positions, the joystick knob gets always snapped to the nearest section border.
	 * </br>To work with an infinite number of positions, set <code>positionCount</code> to zero. 
	 * 
	 * @param positionCount number of joystick positions
	 */
	public void setPositionCount(int positionCount) {
		if (positionCount <= 0) {
			snapAngle = 0;
		}

		snapAngle = Math.toRadians(360.0 / positionCount);
	}

	/**
	 * Sets the radius of the touchable circle.
	 * 
	 * @param radius radius in pixels.
	 */
	public void setRadius(float radius) {
		joystickCircle.radius = radius;
	}

	/**
	 * Sets the threshold to decide whether very small joystick direction angles are considered as zero. 
	 * 
	 * @param epsilon
	 * @see #snapAngle
	 */
	public void setEpsilon(float epsilon) {
		this.epsilon = epsilon;
	}

	/**
	 * Sets the center of the joystick circle.
	 * 
	 * @param x x-coordinate in pixels
	 * @param y y-coordinate in pixels
	 */
	public void setCenter(float x, float y) {
		joystickCircle.x = x;
		joystickCircle.y = y;
	}

	/**
	 * Sets the radius of the inner joystick circle where touch events are ignored.
	 * 
	 * @param deadZone radius in pixels
	 */
	public void setDeadZone(float deadZone) {
		this.deadZone = deadZone;
	}

	/**
	 * Updates the current knob direction according to the specified touch event.
	 * 
	 * @param fingerId pointer id to query
	 * 
	 * @see Input#isTouched(int)
	 */
	public void update(int fingerId) {
		if (fingerId < 0) {
			return;
		}

		// push knob back into center if not touched
		if (!input.isTouched(fingerId)) {
			knobX = joystickCircle.x;
			knobY = joystickCircle.y;
			knobDirection.x = 0;
			knobDirection.y = 0;
			return;
		}

		// get new touch position and check if touch delta is big enough
		float touchY = input.getY(fingerId);
		float touchX = input.getX(fingerId);
		if (Math.abs(touchX - knobX) < tolerance || Math.abs(touchY - knobY) < tolerance) {
			return;
		}

		// check if touched inside joystick circle
		tmpCheckVector.set(touchX - joystickCircle.x, touchY - joystickCircle.y);
		if (tmpCheckVector.len() > joystickCircle.radius) {
			return;
		}

		// store new knob positions
		knobX = touchX;
		knobY = yDown ? touchY : viewHeight - touchY;

		// calculate knob direction as unit vector
		knobDirection.x = knobX - joystickCircle.x;
		knobDirection.y = knobY - joystickCircle.y;
		float length = knobDirection.len();
		if (length < deadZone) {
			knobDirection.x = 0;
			knobDirection.y = 0;
		}
		else {
			knobDirection.nor();
		}

		// optionally, let joystick snap into fixed positions
		if (snapAngle != 0) {
			snapAngle(knobDirection);
		}
	}

	/**
	 * Returns the current joystick position as normalized vector originating from the joystick center.
	 * </p>
	 * In case a finite number of positions has been set, the direction is one of n possible positions. 
	 * 
	 * @param direction vector being set with the normalized joystick knob direction
	 * 
	 * @see #setPositionCount(double)
	 */
	public void getDirection(Vector2 direction) {
		// hand back current knob direction;
		direction.x = knobDirection.x;
		direction.y = knobDirection.y;
	}

	/**
	 * Delegates joystick painting to the previously set renderer.
	 * 
	 *  @see #setRenderer(IVirtualJoystickRenderer)
	 */
	public void paint() {
		if (renderer != null) {
			renderer.paint(joystickCircle, knobDirection, knobX, knobY);
		}
	}

	private void snapAngle(Vector2 vector) {
		double angle = Math.atan2(vector.y, vector.x);
		if (angle % snapAngle != 0) {
			double newAngle = Math.round(angle / snapAngle) * snapAngle;
			vector.x = (float)Math.cos(newAngle);
			vector.y = (float)Math.sin(newAngle);
			if (Math.abs(vector.x) < epsilon) {
				vector.x = 0;
			}
			if (Math.abs(vector.y) < epsilon) {
				vector.y = 0;
			}
		}
	}

	private Input input;
	private int viewHeight;
	private IVirtualJoystickRenderer renderer;

	private Circle joystickCircle;
	private boolean yDown;
	private float tolerance;
	private float epsilon;
	private float deadZone;
	private double snapAngle;

	private float knobX;
	private float knobY;
	private Vector2 knobDirection;
	private Vector2 tmpCheckVector;
}