package de.voodoosoft.gameroots.frontend.gdx.view.render.batch.impl;

import com.badlogic.gdx.graphics.GL20;
import de.voodoosoft.gameroots.frontend.gdx.view.render.batch.BlendMode;



/**
 * Various typical blend modes.
 */
public enum DefaultBlendMode implements BlendMode<DefaultBlendMode> {
	NONE(0,0),
	DEFAULT(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA),
	ALPHA(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA),
	ADDITIVE(GL20.GL_ONE, GL20.GL_ONE),
	SHADOW(GL20.GL_ZERO, GL20.GL_ONE_MINUS_SRC_ALPHA);

	DefaultBlendMode(int srcFunction, int destFunction) {
		this.srcFunction = srcFunction;
		this.destFunction = destFunction;
	}

	@Override
	public int getDestFunction() {
		return destFunction;
	}

	@Override
	public int getSrcFunction() {
		return srcFunction;
	}

	@Override
	public boolean equals(BlendMode other) {
		return this.srcFunction == other.getSrcFunction() && this.destFunction == other.getDestFunction();
	}

	int srcFunction;
	int destFunction;
}
