package main.javafx.shapes;

import javafx.scene.Parent;
import javafx.scene.shape.Shape;

/**
 * A class to help represent an arrow. You are given the flexibility to
 * customize your arrow by selecting the parts you want and modifying them
 * directly.
 * <p>
 * You can create your own custom arrow simply by extending this class. This way
 * you can manage the arrow yourself if you want a different arrow.
 * 
 * You will need to manaully control the values you want since it's uncertain
 * what is desired.
 * 
 * Through this implementation, you can simply rotate the arrow and not the
 * components to get the results you want.
 * 
 * @author Ethan
 * 
 */
public abstract class Arrow<T extends Shape, U extends Shape> extends Parent {
	protected T body;
	protected U head;

	public Arrow(T body, U head) {
		this.body = body;
		this.head = head;
		this.getChildren().addAll(body, head);
	}

	public T getBody() {
		return body;
	}

	public U getHead() {
		return head;
	}
}
