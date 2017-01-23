package de.voodoosoft.gameroots.examples.colorgrading;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Scaling;

public class MainGame extends ApplicationAdapter {

	SpriteBatch batch;
	Texture img;
	FrameBuffer frameBuffer;
	OrthographicCamera sceneCamera, screenSizedCamera;
	ShaderProgram basicColorGradeShader;
	Array<Texture> colorGradeTables = new Array<Texture>();
	int colorGradeIndex = 0;
	boolean showTable = true;
	boolean applyColorGrading = true;
	
	@Override
	public void create () {
		Gdx.input.setInputProcessor(inputAdapter);

		batch = new SpriteBatch();
		img = new Texture("colorgrading/field.jpg");
		sceneCamera = new OrthographicCamera();
		screenSizedCamera = new OrthographicCamera();
		basicColorGradeShader = new ShaderProgram(
				Gdx.files.internal("colorgrading/basic.vert").readString(),
				Gdx.files.internal("colorgrading/basicColorGrade.frag").readString()
		);
		basicColorGradeShader.begin();
		basicColorGradeShader.setUniformi("u_table", 1); // The table texture will be bound to unit 1 when used
		basicColorGradeShader.setUniformf("u_tableSize", 16f); // We are using 16 x 16 x 16 color grade table
		basicColorGradeShader.end();

		colorGradeTables.add(new Texture("colorgrading/colorGradeTable.png"));
		colorGradeTables.add(new Texture("colorgrading/colorGradeTable-dusk.png"));
		colorGradeTables.add(new Texture("colorgrading/colorGradeTable-night.png"));
		colorGradeTables.add(new Texture("colorgrading/colorGradeTable-alien.png"));
		colorGradeTables.add(new Texture("colorgrading/colorGradeTable-monochrome.png"));

		//The color grade tables need linear filtering
		for (Texture texture : colorGradeTables)
			texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
	}

	@Override
	public void resize (int width, int height){
		if (frameBuffer != null && (frameBuffer.getWidth() != width || frameBuffer.getHeight() == height)){
			// window was resized, need to generate new frame buffer
			frameBuffer.dispose();
			frameBuffer = null;
		}

		if (frameBuffer == null){
			try {
				frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false); // true if drawing 3D scene
			} catch (GdxRuntimeException e){
				// On some Androids, RGBA8888 is unsupported
				frameBuffer = new FrameBuffer(Pixmap.Format.RGB565, width, height, false);
			}
		}

		screenSizedCamera.setToOrtho(false, width, height);
		screenSizedCamera.position.set(width / 2, height / 2, 0);
		screenSizedCamera.update();

		// fit the img to view (this is for the arbitrary scene and can be ignored)
		Vector2 dimensions = Scaling.fit.apply(width, height, img.getWidth(), img.getHeight());
		sceneCamera.setToOrtho(false, dimensions.x, dimensions.y);
		sceneCamera.position.set(dimensions.x / 2, dimensions.y / 2, 0);
		sceneCamera.update();
	}

	@Override
	public void render () {

		// For box2dlights, you would need to call rayHandler.update() and rayHandler.prepareRender() here, outside
		// the frameBuffer begin() and end().

		// Draw the scene to the frame buffer object instead of directly to the screen's buffer
		frameBuffer.begin();
		drawScene();
		// For box2dlights, you would call rayHandler.renderOnly() here.
		frameBuffer.end(); // This switches back to the screen's buffer
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen's buffer as always

		// Render frame buffer to screen with shader
		batch.disableBlending();
		if (applyColorGrading) {
			batch.setShader(basicColorGradeShader);
		} else {
			batch.setShader(null);
			batch.setColor(1, 1, 1, 1);
		}
		batch.getProjectionMatrix().idt(); // Identity projection simplifies coordinates below
		batch.begin();
		colorGradeTables.get(colorGradeIndex).bind(1); // Bind the table texture to unit 1 (which we assigned to the shader value "u_table" in the create() method)
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0); // Reset texture unit to the default of 0 so SpriteBatch doesn't get confused later
		batch.draw(frameBuffer.getColorBufferTexture(), -1, 1, 2, -2); // flipped full-screen rectangle with no transformation/projection matrix
		batch.end();
		batch.setShader(null);

		if (showTable){
			batch.setProjectionMatrix(screenSizedCamera.combined);
			batch.begin();
			Texture texture = colorGradeTables.get(colorGradeIndex);
			batch.draw(texture, 0, screenSizedCamera.viewportHeight - texture.getHeight());
			batch.end();
		}
	}

	void drawScene (){
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(sceneCamera.combined);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		if (frameBuffer != null) frameBuffer.dispose();
		basicColorGradeShader.dispose();
		for (Texture texture : colorGradeTables) texture.dispose();
	}

	InputAdapter inputAdapter = new InputAdapter(){
		public boolean keyDown (int keycode) {
			switch (keycode){
				case Input.Keys.F1:
					colorGradeIndex = 0;
					return true;
				case Input.Keys.F2:
					colorGradeIndex = 1;
					return true;
				case Input.Keys.F3:
					colorGradeIndex = 2;
					return true;
				case Input.Keys.F4:
					colorGradeIndex = 3;
					return true;
				case Input.Keys.F5:
					colorGradeIndex = 4;
					return true;
				case Input.Keys.F6:
					applyColorGrading = !applyColorGrading;
					Gdx.app.log("Color grading enabled", "" + applyColorGrading);
					return true;
				case Input.Keys.F7:
					showTable = !showTable;
					return true;
			}
			return false;
		}
	};
}
