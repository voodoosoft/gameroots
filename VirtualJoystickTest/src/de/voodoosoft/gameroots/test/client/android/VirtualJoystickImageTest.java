/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
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
 ******************************************************************************/

package de.voodoosoft.gameroots.test.client.android;

import android.os.Bundle;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import de.voodoosoft.gameroots.client.gdx.view.input.VirtualJoystick;
import de.voodoosoft.gameroots.client.gdx.view.input.impl.TextureJoystickRenderer;



public class VirtualJoystickImageTest extends AndroidApplication implements ApplicationListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		initialize(this, cfg);
	}

	@Override
	public void create() {
		int viewWidth = Gdx.graphics.getWidth();
		int viewHeight = Gdx.graphics.getHeight();

		joystickDirection = new Vector2();
		float radius = 128;
		joystick = new VirtualJoystick(Gdx.input, viewWidth / 2, viewHeight / 2, radius);
		joystick.setPositionCount(8);
		joystick.setDeadZone(64);
		joystick.setYUp(viewHeight);

		spriteBatch = new SpriteBatch();
		TextureJoystickRenderer joystickRenderer = new TextureJoystickRenderer(spriteBatch);
		//		joystickRenderer.setDrawSnappedKnob(true);
		joystickRenderer.setKnobRadius(32);
		knobTexture = new Texture(Gdx.files.internal("joystick-knob.png"));
		joystickRenderer.setKnobTexture(knobTexture);
		borderTexture = new Texture(Gdx.files.internal("joystick-border.png"));
		joystickRenderer.setJoystickTexture(borderTexture);

		joystick.setRenderer(joystickRenderer);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		joystick.update(0);
		joystick.getDirection(joystickDirection);

		spriteBatch.begin();
		joystick.paint();
		spriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		knobTexture.dispose();
		borderTexture.dispose();
	}

	private VirtualJoystick joystick;
	private Vector2 joystickDirection;
	private SpriteBatch spriteBatch;
	private Texture knobTexture;
	private Texture borderTexture;
}