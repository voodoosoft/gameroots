package de.voodoosoft.gameroots.frontend.gdx.view.render.batch;

public interface BlendMode<T extends BlendMode> extends Comparable<T> {
	int getSrcFunction();

	int getDestFunction();

	boolean equals(BlendMode<T> other);
}
