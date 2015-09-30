package de.voodoosoft.gameroots.frontend.gdx.view.render.batch.impl;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Pool;
import de.voodoosoft.gameroots.frontend.gdx.view.render.batch.BlendMode;

import java.util.ArrayList;
import java.util.List;



/**
 * Batch item for rendering vertices.
 */
public class VerticeBatchItem extends AbstractBatchItem implements Pool.Poolable  {
	public static class VerticeRegion {
		public TextureRegion textureRegion;
		public float[] vertices;
	}

	public VerticeBatchItem() {
		vertices = new ArrayList<VerticeRegion>(MAX_REGIONS);
		for (int i = 0; i < MAX_REGIONS; i++) {
			VerticeRegion verticedRegion = new VerticeRegion();
			verticedRegion.vertices = new float[SPRITE_SIZE];
			vertices.add(verticedRegion);
		}
	}

	public VerticeRegion nextVertices() {
		if (verticeIdx >= MAX_REGIONS-1) {
			return null;
		}

		return vertices.get(verticeIdx++);
	}

	/**
	 * Prepares this item for returning to its item pool.
	 */
	@Override
	public void reset() {
		verticeIdx = 0;
	}

	public void setShaderProgram(ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
	}

	public ShaderProgram getShaderProgram() {
		return shaderProgram;
	}

	@Override
	public void render(SpriteBatch batch, long time) {
		if (!nullSafeEquals(getLastShaderProgram(), shaderProgram)) {
			batch.setShader(shaderProgram);
			setLastShaderProgram(shaderProgram);
		}

		if (isBlending()) {
			BlendMode blendMode = getBlendMode();
			if (blendMode == BlendMode.DEFAULT) {
				batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			}
			if (blendMode == BlendMode.ALPHA) {
				batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
			}
			else if (blendMode == BlendMode.ADDITIVE) {
				batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
			}
			batch.enableBlending();
		}
		else {
			batch.disableBlending();
		}

		for (int i = 0; i < verticeIdx; i++) {
			VerticeRegion verticeRegion = vertices.get(i);
			batch.draw(verticeRegion.textureRegion.getTexture(), verticeRegion.vertices, 0, verticeRegion.vertices.length);
		}
	}

	private static final int VERTEX_SIZE = 2 + 1 + 2;
	private static final int SPRITE_SIZE = 4 * VERTEX_SIZE;
	private static final int MAX_REGIONS = 512;

	private int verticeIdx;
	private ShaderProgram shaderProgram;
	private List<VerticeRegion> vertices;
}
