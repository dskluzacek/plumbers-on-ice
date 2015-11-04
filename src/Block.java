public class Block {
	int row;
	int column;
	Sprite sprite;

	public Block(int r, int c, Sprite s) {
	row = r;
	column = c;
	sprite = s;
	}

	public int getRow() {
	return row;
	}
	public int getColumn() {
	return column;
	}
	public int getSprite() {
	return sprite;
	}
	public void setRow(int r) {
	row = r;
	}
	public void setColumn(int c) {
	column = c;
	}
	public void setSprite(Sprite s) {
	sprite = s;
	}
}
