package XMLLoader;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

import Actions.ActionStack;
import Actions.ActionsInterface;
import Actions.NoAction;
import Constructs.Point;
import FlatLand.Physics.TypeOfEntity;
import FlatLandStructure.ViewableFlatLand;
import FlatLander.BoundingBox;
import FlatLander.FlatLander;
import flatLand.trainingGround.Sprites.Sprites;

public class FlatLanderWrper extends FlatLander implements Terminal {

	protected Sprites sprite = null;
	protected BoundingBox previousflatLanderBB = new BoundingBox();
	private BoundingBox currentflatLanderBB = new BoundingBox();
	private boolean drawBB = false;

	protected ActionsInterface preferedAction;
	protected ActionStack actions;

	public FlatLanderWrper(int x, int y, String name, double startingDir, boolean collidable,
			boolean shouldPhysicsApply, TypeOfEntity entityType, Color myColor) {
		super(x, y, name, startingDir, collidable, shouldPhysicsApply, entityType, myColor);
		this.setPreferedAction(new NoAction(this));
		this.setActionStack(new ActionStack(new ArrayList<ActionsInterface>()));

		// TODO Auto-generated constructor stub
	}

	public Sprites getSprite() {
		return sprite;
	}

	public void setSprite(Sprites sprite) {
		this.sprite = sprite;
		getCurrentflatLanderBB().setBB(new Point(getX(), getY()), new Point(getX() + this.sprite.getWidth(), getY()),
				new Point(getX() + this.sprite.getWidth(), getY() + this.sprite.getHeight()),
				new Point(getX(), getY() + this.sprite.getHeight()));
		previousflatLanderBB = new BoundingBox(getCurrentflatLanderBB());
	}

	@Override
	public void update() {
		int act = actions.act();
		if (act == -1)
			doPreferedActions();
		sprite.updateState();

		previousflatLanderBB = new BoundingBox(getCurrentflatLanderBB());
		currentflatLanderBB.updateBoundingBox(new Point(getX(), getY()),
				new Point(getX() + this.sprite.getWidth(), getY()),
				new Point(getX() + this.sprite.getWidth(), getY() + this.sprite.getHeight()),
				new Point(getX(), getY() + this.sprite.getHeight()));
		super.update();
	}

	public void updatecurrentBB() {
		currentflatLanderBB.updateBoundingBox(new Point(getX() + moveX, getY() + moveY),
				new Point(getX() + this.sprite.getWidth() + moveX, getY() + moveY),
				new Point(getX() + this.sprite.getWidth() + moveX, getY() + this.sprite.getHeight() + moveY),
				new Point(getX() + moveX, getY() + this.sprite.getHeight() + moveY));

	}

	public boolean above(FlatLander flatLanderToCheckForCollisions) {
		if (currentflatLanderBB.getBottemLeft().getY() < flatLanderToCheckForCollisions.getCurrentflatLanderBB()
				.getTopLeft().getY()
				&& flatLanderToCheckForCollisions.getCurrentflatLanderBB().getTopLeft().getY()
						- currentflatLanderBB.getBottemLeft().getY() == 1
				&& ((currentflatLanderBB.getBottemLeft().getX() >= flatLanderToCheckForCollisions
						.getCurrentflatLanderBB().getTopLeft().getX()
						&& currentflatLanderBB.getBottemLeft().getX() <= flatLanderToCheckForCollisions
								.getCurrentflatLanderBB().getTopRight().getX())
						|| (currentflatLanderBB.getBottemRight().getX() >= flatLanderToCheckForCollisions
								.getCurrentflatLanderBB().getTopLeft().getX()
								&& currentflatLanderBB.getBottemRight().getX() <= flatLanderToCheckForCollisions
										.getCurrentflatLanderBB().getTopRight().getX()))) {
			return true;
		}
		return false;
	}

	public BoundingBox getCurrentBoundingBox() {

		return currentflatLanderBB;
	}

	private BoundingBox getPreviousBoundingBox() {

		return this.previousflatLanderBB;
	}

	public int collidesFrom(FlatLander flatLanderToCheckForCollisions) {

		int xdirection = previousX - (x + moveX);
		int ydirection = previousY - (y + moveY);
		if (xdirection < 0) {
			// moving right
			if (ydirection < 0) {
				// falling
				return 1;
			} else if (ydirection > 0) {
				// rising
				return 3;
			} else {
				// notMoving
				return 4;
			}

		} else if (xdirection > 0) {
			if (ydirection < 0) {
				// falling
				return 1;
			} else if (ydirection > 0) {
				// rising
				return 3;
			} else {
				// notMoving
				return 2;
			}
		} else {
			if (ydirection < 0) {
				// falling
				return 1;
			} else if (ydirection > 0) {
				// rising
				return 3;
			} else {
				// notMoving
				return 0;
			}
		}
	}

	public int passesThroughSide(FlatLander flatLanderToCheckForCollisions) {
		int predictedX = flatLanderToCheckForCollisions.getX();
		int predictedY = flatLanderToCheckForCollisions.getY();

		return 0;
	}

	public boolean passesThrough(FlatLanderWrper flatLanderToCheckForCollisions) {
		return previousflatLanderBB.passesThrough(getCurrentflatLanderBB(),
				flatLanderToCheckForCollisions.getCurrentBoundingBox());
	}

	public boolean passesThrough(PlayerWrper flatLanderToCheckForCollisions) {
		return previousflatLanderBB.passesThrough(getCurrentflatLanderBB(),
				flatLanderToCheckForCollisions.getCurrentBoundingBox());
	}

	public boolean collidesWith(FlatLanderWrper flatLandercollide) {
		if (collidable && flatLandercollide.collidable) {
			return currentflatLanderBB.collidesWith(flatLandercollide.getCurrentBoundingBox());

		}
		return false;
	}

	public Boolean collidesWith(int x, int y) {

		return currentflatLanderBB.collidesWith(x, y);
	}

	public BoundingBox getCurrentflatLanderBB() {
		return currentflatLanderBB;
	}

	public void setCurrentflatLanderBB(BoundingBox currentflatLanderBB) {
		this.currentflatLanderBB = currentflatLanderBB;
	}

	public boolean isDrawBB() {
		return drawBB;
	}

	public void setDrawBB(boolean drawBB) {
		this.drawBB = drawBB;
	}

	protected void doPreferedActions() {
		preferedAction.act();
	}

	public void setPreferedAction(ActionsInterface preferedAction) {
		this.preferedAction = preferedAction;
	}

	public void setActionStack(ActionStack actions) {
		this.actions = actions;
	}

	public void addToActionStack(ActionsInterface itemToAddToBeginning) {
		this.actions.addActiontoBeginning(itemToAddToBeginning);
	}

	public ActionStack getActionStack() {
		// TODO Auto-generated method stub
		return actions;
	}

	public boolean collidesWith(PlayerWrper flatLandercollide) {
		if (collidable && flatLandercollide.collidable) {
			return this.getCurrentflatLanderBB().collidesWith(flatLandercollide.getCurrentBoundingBox());

		}
		return false;
	}

	@Override
	public void update(String key, boolean gameMode) {
		sprite.update(key, gameMode,false);
	}

	@Override
	public void setKeyBindingsForTerminal(JPanel panel) {
		// TODO terminalAuto-generated method stub

	}


}
